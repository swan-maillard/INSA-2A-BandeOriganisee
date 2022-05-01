import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Obstacle circulaire
 */
public class Obstacle {

    // Position du centre de l'obstacle
    public Vector2D position;
    // Rayon de l'obstacle
    public int radius;
    // Rayon de répulsion
    public int avoidanceRadius;
    // Couleur de l'obstacle
    public Color color;

    /**
     * Constructeur
     *
     * @param position position du centre
     * @param radius   rayon
     * @param color    couleur
     */
    public Obstacle(Vector2D position, int radius, Color color) {
        this.position = position;
        this.radius = radius;
        this.color = color;
        avoidanceRadius = 2 * radius;
    }

    /**
     * Dessine l'obstacle
     *
     * @param g Graphics2D
     */
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fill(new Ellipse2D.Double(position.x - radius, position.y - radius, 2 * radius, 2 * radius));
    }
}
