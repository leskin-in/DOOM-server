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
        representations = temporaryMap;
    }

    /**
     * Deserialize the packet into a state, filling all variables
     * @param packet a packet to deserialize
     * @param state GameState to modify
     */
    public static void deserializeToField(Packet packet, GameState state) throws IOException {
        if (packet.getType() != PacketType.GAME.getCode()) {
            throw new IOException(
                    "Cannot build field from packet of type " + Integer.toString(packet.getType())
            );
        }
        byte[] dataRaw = packet.getData();

        // Get game state
        for (int y = 0; y < state.fieldHeight; y++) {
            for (int x = 0; x < state.fieldWidth; x++) {
                byte cellCode = dataRaw[y * state.fieldWidth + x];
                Representation r = representations.get(cellCode);
                if (r == null) {
                    throw new IOException(
                            "Found incorrect cell code 0x" + Integer.toHexString(cellCode) + " at (" + Integer.toString(x) + ", " + Integer.toString(y) + ")"
                    );
                }
                state.fieldCells.set(
                        y * state.fieldWidth + x,
                        new Cell(x, y, r)
                );
                if (cellCode == CODE_PLAYER) {
                    state.playerRepresentation = representations.get(cellCode);
                }
            }
        }

        // Get metadata
        String[] metaParameters = new String(
                Arrays.copyOfRange(dataRaw, state.fieldWidth * state.fieldHeight, dataRaw.length),
                "ASCII"
        ).split("\\s+");
        if ((metaParameters.length < 4) || (metaParameters.length > 5)) {
            throw new IOException(
                    "The number of meta-parameters is incorrect. The parameters are " + String.join(" ", metaParameters)
            );
        }
        state.playerHealth = Integer.parseInt(metaParameters[0]);
        state.playerHealthPercent = Integer.parseInt(metaParameters[1]);
        state.weapon = metaParameters[2];
        state.weaponCharge = Integer.parseInt(metaParameters[3]);
        if (metaParameters.length == 4) {
            state.lastServerCommand = null;
        }
        else {
            state.lastServerCommand = metaParameters[4];
        }
    }
}
