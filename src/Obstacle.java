import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Obstacle {

    public Vector2D position;
    public int radius;
    public int avoidanceRadius;
    public Colors color;

    public Obstacle(Vector2D position, int radius, Colors color) {
        this.position = position;
        this.radius = radius;
        this.color = color;
        avoidanceRadius = 2*radius;
    }

    public void draw(Graphics2D g) {
        g.setColor(color.getPrimaryColor());
        g.fill(new Ellipse2D.Double(position.x - radius, position.y - radius, 2 * radius, 2 * radius));
    }

}
