import javax.swing.*;
import java.awt.*;

/**
 * JPanel de la simulation
 */
public class SimulationView extends JPanel {

    /**
     * Constructeur
     *
     * @param x      position x
     * @param y      position y
     * @param width  longueur
     * @param height hauteur
     */
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

        // On récupère un Graphics2D pour pouvoir accéder à d'autres méthodes
        Graphics2D g2D = (Graphics2D) g;

        // Paramètres pour rendre la simulation plus lisse et naturelle
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

        // On dessine les obstacles
        for (Obstacle obstacle : App.obstacles) {
            obstacle.draw(g2D);
        }

        // On rafraichit les boids (on recalcule leur position et on les redessine)
        for (Flock flock : App.flocks) {
            flock.refreshBoids(g2D);
        }
    }
}
