package D592Client.GameObjects;

import D592Client.NetUtils.Packet;
import D592Client.NetUtils.PacketType;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

/**
 * Decoder for {@link Packet}.
 * Transforms packets into objects
 */
public class PacketDecoder {
    public static byte CODE_EMPTY   = ' ';
    public static byte CODE_WALL    = '#';
    public static byte CODE_PLAYER  = 'p';
    public static byte CODE_ENEMY   = 'e';
    public static byte CODE_HEAL    = '+';
    public static byte CODE_POISON  = '-';
    public static byte CODE_BOMB    = '*';

    private static final Map<Byte, Representation> representations;
    static {
        RepresentationFactory factory = new RepresentationFactory();
        Map<Byte, Representation> temporaryMap = new TreeMap<>();
        try {
            temporaryMap.put(
                    CODE_EMPTY,
                    factory.get(
                            RepresentationFactory.REPR_RECT,
                            Cell.displaySize,
                            new Color(0, 0, 0)
                    )
            );
            temporaryMap.put(
                    CODE_WALL,
                    factory.get(
                            RepresentationFactory.REPR_RECT,
                            Cell.displaySize,
                            new Color(255, 255, 255)
                    )
            );
            temporaryMap.put(
                    CODE_PLAYER,
                    factory.get(
                            RepresentationFactory.REPR_OVAL,
                            Cell.displaySize,
                            new Color(0, 255, 0)
                    )
            );
            temporaryMap.put(
                    CODE_ENEMY,
                    factory.get(
                            RepresentationFactory.REPR_OVAL,
                            Cell.displaySize,
                            new Color(255, 0, 0)
                    )
            );
            temporaryMap.put(
                    CODE_HEAL,
                    factory.get(
                            RepresentationFactory.REPR_RECT,
                            Cell.displaySize,
                            new Color(255, 192, 203)
                    )
            );
            temporaryMap.put(
                    CODE_POISON,
                    factory.get(
                            RepresentationFactory.REPR_RECT,
                            Cell.displaySize,
                            new Color(64, 130, 109)
                    )
            );
            temporaryMap.put(
                    CODE_BOMB,
                    factory.get(
                            RepresentationFactory.REPR_RECT,
                            Cell.displaySize,
                            new Color(128, 107, 42)
                    )
            );
        }
        catch (IOException ex) {
            temporaryMap = null;
        }
        representations = temporaryMap;
    }

    /**
     * Deserialize the packet into a field, filling all variables
     * @param field game field to modify
     * @param player the player to modify
     * @param packet a packet to deserialize
     */
    public static void deserializeToField(Field field, Player player, Packet packet) throws IOException {
        if (packet.getType() != PacketType.GAME.getCode()) {
            throw new IOException(
                    "Cannot build field from packet of type " + Integer.toString(packet.getType())
            );
        }
        byte[] dataRaw = packet.getData();

        // Get game field
        for (int y = 0; y < field.height; y++) {
            for (int x = 0; x < field.width; x++) {
                byte cellCode = dataRaw[y * field.height + x];
                try {
                    Representation r = representations.get(cellCode);
                    field.cells.set(
                            y * field.height + x,
                            new Cell(x, y, r)
                    );
                }
                catch (NullPointerException ex) {
                    throw new IOException(
                            "Found incorrect cell code 0x" + Integer.toHexString(cellCode) + " at ("
                                    + Integer.toString(x) + ", " + Integer.toString(y) + ")"
                    );
                }
            }
        }

        // Get metadata
        String[] metaParameters = new String(
                Arrays.copyOfRange(dataRaw, field.width * field.height, dataRaw.length),
                "ASCII"
        ).split("\\s+");
        if ((metaParameters.length < 4) || (metaParameters.length > 5)) {
            throw new IOException(
                    "The number of meta-parameters is incorrect. The parameters are " + String.join(" ", metaParameters)
            );
        }
        player.health = Integer.parseInt(metaParameters[0]);
        player.healthPercent = Integer.parseInt(metaParameters[1]);

        player.weapon = new Weapon();
    }
}
