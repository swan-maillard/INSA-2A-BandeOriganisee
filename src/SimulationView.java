import javax.swing.*;
import java.awt.*;

public class SimulationView extends JPanel {

    public SimulationView(int x, int y, int width, int height) {
        setLayout(null);
        setBounds(x, y, width, height);
        setBackground(new Color(174, 204, 234));

        App.addFlock("Merles", 100, Colors.RED, App.TYPE_PREY);
        App.addFlock("Faucon", 1, Colors.BLACK, App.TYPE_PREDATOR);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

        for (Obstacle obstacle : App.obstacles) {
            obstacle.draw(g2D);
        }

        for (Flock flock : App.flocks) {
            flock.drawBoids(g2D);
        }
    }
}
