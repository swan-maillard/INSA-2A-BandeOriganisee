import java.awt.*;

public enum Colors {

    BLACK("Noir", Color.BLACK),
    WHITE("Blanc", Color.WHITE),
    RED("Rouge", Color.RED),
    ORANGE("Orange", Color.ORANGE),
    GREEN("Vert", Color.GREEN),
    BLUE("Bleu", Color.BLUE),
    MAGENTA("Magenta", Color.MAGENTA);

    private String name;
    private Color color;

    Colors(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public static String[] getColors() {
        String[] colors = new String[Colors.values().length];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = Colors.values()[i].getName();
        }

        return colors;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }
}
