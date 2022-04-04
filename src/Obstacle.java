import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Obstacle {

    public Vector2D position;
    public int radius;
    public int avoidanceRadius;

    public Obstacle(Vector2D position, int radius) {
        this.position = position;
        this.radius = radius;
        avoidanceRadius = radius+30;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fill(new Ellipse2D.Double(position.x-radius, position.y-radius, 2*radius, 2*radius));
//        g.setColor(Color.RED);
//        g.draw(new Ellipse2D.Double(position.x-avoidanceRadius, position.y-avoidanceRadius, 2*avoidanceRadius, 2*avoidanceRadius));
    }

}
