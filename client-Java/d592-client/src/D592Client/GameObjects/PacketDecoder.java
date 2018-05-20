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
    public static final byte CODE_EMPTY   = ' ';
    public static final byte CODE_WALL    = '#';
    public static final byte CODE_PLAYER  = 'p';
    public static final byte CODE_ENEMY   = 'e';
    public static final byte CODE_HEAL    = '+';
    public static final byte CODE_POISON  = '-';
    public static final byte CODE_BOMB    = '*';

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
     * @param packet a packet to deserialize
     * @param game a {@link Game} object to modify
     */
    public static void deserializeToField(Packet packet, Game game) throws IOException {
        if (packet.getType() != PacketType.GAME.getCode()) {
            throw new IOException(
                    "Cannot build field from packet of type " + Integer.toString(packet.getType())
            );
        }
        byte[] dataRaw = packet.getData();

        // Get game field
        for (int y = 0; y < game.field.height; y++) {
            for (int x = 0; x < game.field.width; x++) {
                byte cellCode = dataRaw[y * game.field.width + x];
                Representation r = representations.get(cellCode);
                if (r == null) {
                    throw new IOException(
                            "Found incorrect cell code 0x" + Integer.toHexString(cellCode) + " at (" + Integer.toString(x) + ", " + Integer.toString(y) + ")"
                    );
                }
                game.field.cells.set(
                        y * game.field.width + x,
                        new Cell(x, y, r)
                );
                if (cellCode == CODE_PLAYER) {
                    game.player.r = representations.get(cellCode);
                    game.player.x = x;
                    game.player.y = y;
                }
            }
        }

        // Get metadata
        String[] metaParameters = new String(
                Arrays.copyOfRange(dataRaw, game.field.width * game.field.height, dataRaw.length),
                "ASCII"
        ).split("\\s+");
        if ((metaParameters.length < 4) || (metaParameters.length > 5)) {
            throw new IOException(
                    "The number of meta-parameters is incorrect. The parameters are " + String.join(" ", metaParameters)
            );
        }
        game.player.health = Integer.parseInt(metaParameters[0]);
        game.player.healthPercent = Integer.parseInt(metaParameters[1]);
        game.player.weapon.name = metaParameters[2];
        game.player.weapon.charge = Integer.parseInt(metaParameters[3]);
        if (metaParameters.length == 4) {
            game.last_command = null;
        }
        else {
            game.last_command = metaParameters[4];
        }
    }
}
