package D592Client.GameObjects;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * A factory of {@link Representation} objects.
 */
public class RepresentationFactory {
    public static final int REPR_RECT = 1;
    public static final int REPR_OVAL = 2;
    public static final int REPR_IMG = 3;
    public static final int REPR_RREC = 4;

    public RepresentationFactory() {
        this.map = new HashMap<>();
    }

    /**
     * Get a concrete {@link Representation} using the provided parameters
     * @param type type of graphical object. Must be one of pre-defined constants of {@link RepresentationFactory} ('REPR_...')
     * @param size size of graphical object
     * @param properties properties of graphical object
     * @return {@link Representation} with the required properties. If the given representation exists, new one will not be created
     * @throws IOException if the image URL is unavailable
     * @throws IllegalArgumentException if the provided type is incorrect
     *
     * <br>
     * <h3>The 'properties' parameter supported:</h2>
     * <ul>
     *     <li><b>All types</b>: null</li>
     *     <li><b>REPR_RECT</b>, <b>REPR_RREC</b>, <b>REPR_OVAL</b>: 1 or 2 of type {@link Color} (fill and border ones, respectively)</li>
     *     <li><b>REPR_IMG</b>: 1st of type {@link String} - image location (can be a URL or a file); 2nd (optional) of type {@link Color} - background color</li>
     * </ul>
     */
    public Representation get(int type, Dimension size, Object... properties) throws IOException {
        switch (type) {
            case REPR_RECT:
            case REPR_RREC:
            case REPR_OVAL:
            {
                Color fillColor = null;
                Color borderColor = null;
                if (properties != null) {
                    if (properties[0] instanceof Color) {
                        fillColor = (Color)properties[0];
                    }
                    else {
                        throw new IOException("The type of a required parameter of Representation is incorrect");
                    }
                    if (properties[1] instanceof Color) {
                        borderColor = (Color)properties[1];
                    }
                }
                else {
                    throw new IOException("Not all parameters of Representation were provided");
                }
                String key = Integer.toString(type)
                        + ":"
                        + size.width
                        + "."
                        + size.height
                        + ":"
                        + Integer.toString(fillColor.getRGB())
                        + "."
                        + (borderColor == null ? "" : Integer.toString(borderColor.getRGB()));
                if (map.containsKey(key)) {
                    return map.get(key);
                }
                // If these calls rewrite some object, nothing bad happens, just the efficiency decreases
                if ((type == REPR_RECT) || (type == REPR_RREC)) {
                    map.put(
                            key,
                            new RectRepresentation(
                                    size,
                                    fillColor,
                                    borderColor,
                                    (type == REPR_RREC)
                            )
                    );
                }
                else {
                    map.put(
                            key,
                            new OvalRepresentation(
                                    size,
                                    fillColor,
                                    borderColor
                            )
                    );
                }
                return map.get(key);
            }
            case REPR_IMG:
            {
                Color bgColor = null;
                String location = null;
                if (properties != null) {
                    if (properties[0] instanceof String) {
                        location = (String)properties[0];
                    }
                    else {
                        throw new IOException("The type of parameter #1 of Representation is incorrect");
                    }
                    if (properties[1] instanceof Color) {
                        bgColor = (Color)properties[1];
                    }
                }
                else {
                    throw new IOException("Not all parameters of Representation were provided");
                }
                String key = Integer.toString(type)
                        + ":"
                        + size.width
                        + "."
                        + size.height
                        + ":'"
                        + location
                        + "'."
                        + (bgColor == null ? "" : Integer.toString(bgColor.getRGB()));
                if (map.containsKey(key)) {
                    return map.get(key);
                }
                URL url = null;
                File file = null;
                try {
                    url = new URL(location);
                }
                catch (MalformedURLException ex) {
                    file = new File(location);
                }
                Image image;
                if (url != null) {
                    image = ImageIO.read(url);
                }
                else {
                    image = ImageIO.read(file);
                }
                map.put(
                        key,
                        new ImageRepresentation(size, image, bgColor)
                );
                return map.get(key);
            }
            default:
            {
                throw new IllegalArgumentException("Invalid type of graphical object Representation");
            }
        }
    }

    private Map<String, Representation> map;
}


class RectRepresentation implements Representation {
    RectRepresentation(Dimension size, Color color) {
        this(size, color, null, false);
    }

    RectRepresentation(Dimension size, Color fillColor, Color borderColor) {
        this(size, fillColor, borderColor, false);
    }

    RectRepresentation(Dimension size, Color fillColor, Color borderColor, boolean isRounded) {
        this.size = size;
        this.fillColor = fillColor;
        this.borderColor = borderColor;
        this.isRounded = isRounded;
    }

    public void draw(Graphics g, Point2D location) {
        Color temp = g.getColor();
        g.setColor(fillColor);
        if (this.isRounded) {
            g.fillRoundRect(
                    (int)location.getX(), (int)location.getY(),
                    size.width, size.height,
                    size.width / 2, size.height / 2
            );
        }
        else {
            g.fillRect(
                    (int)location.getX(), (int)location.getY(),
                    size.width, size.height
            );
        }
        if (borderColor != null) {
            g.setColor(borderColor);
            if (this.isRounded) {
                g.drawRoundRect(
                        (int)location.getX(), (int)location.getY(),
                        size.width, size.height,
                        size.width / 2, size.height / 2
                );
            }
            else {
                g.drawRect(
                        (int)location.getX(), (int)location.getY(),
                        size.width, size.height
                );
            }
        }
        g.setColor(temp);
    }

    private Dimension size;
    private Color fillColor;
    private Color borderColor;
    private boolean isRounded;
}


class OvalRepresentation implements Representation {
    OvalRepresentation(Dimension size, Color color) {
        this(size, color, null);
    }

    OvalRepresentation(Dimension size, Color fillColor, Color borderColor) {
        this.size = size;
        this.fillColor = fillColor;
        this.borderColor = borderColor;
    }

    public void draw(Graphics g, Point2D location) {
        Color temp = g.getColor();
        g.setColor(fillColor);
        g.fillOval(
                (int)location.getX(), (int)location.getY(),
                size.width, size.height
        );
        if (borderColor != null) {
            g.setColor(borderColor);
            g.drawOval(
                    (int)location.getX(), (int)location.getY(),
                    size.width, size.height
            );
        }
        g.setColor(temp);
    }

    private Dimension size;
    private Color fillColor;
    private Color borderColor;
}


class ImageRepresentation implements Representation {
    ImageRepresentation(Dimension size, Image image) {
        this(size, image, null);
    }

    ImageRepresentation(Dimension size, Image image, Color bgColor) {
        this.size = size;
        this.image = image;
        this.bgColor = bgColor;
    }

    public void draw(Graphics g, Point2D location) {
        if (bgColor != null) {
            g.drawImage(
                    image,
                    (int)location.getX(), (int)location.getY(),
                    size.width, size.height,
                    bgColor,
                    null
            );
        }
        else {
            g.drawImage(
                    image,
                    (int)location.getX(), (int)location.getY(),
                    size.width, size.height,
                    null
            );
        }
    }

    private Dimension size;
    private Image image;
    private Color bgColor;
}
