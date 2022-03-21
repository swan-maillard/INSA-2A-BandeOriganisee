import java.awt.*;

public enum FlockColor {

    BLUE("Bleu", Color.BLUE), RED("Rouge", Color.RED), ORANGE("Orange", Color.ORANGE);

    private String name;
    private Color color;

    FlockColor(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public static String[] getColors() {
        String[] colors = new String[FlockColor.values().length];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = FlockColor.values()[i].getName();
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
