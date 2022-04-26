import java.awt.*;

public enum Colors {

    BLACK("Noir", new Color(51, 51, 51), new Color(28, 28, 28)),
    RED("Rouge", new Color(239, 67, 67), new Color(208, 28, 11)),
    ORANGE("Orange", new Color(255, 124, 46), new Color(190, 95, 1)),
    GREEN("Vert", new Color(60, 220, 67), new Color(9, 168, 17)),
    BLUE("Bleu", new Color(60, 90, 239), new Color(12, 41, 157)),
    MAGENTA("Magenta", new Color(214, 34, 241), new Color(142, 8, 161));

    private String name;
    private Color primaryColor;
    private Color secondaryColor;

    Colors(String name, Color primaryColor, Color secondaryColor) {
        this.name = name;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
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

    public Color getPrimaryColor() {
        return primaryColor;
    }

    public Color getSecondaryColor() {
        return secondaryColor;
    }
}
