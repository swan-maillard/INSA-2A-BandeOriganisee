import javax.swing.*;
import java.awt.*;

/**
 * JPanel de la simulation
 */
public class SimulationView extends JPanel {

    /**
     * Constructeur
     *
     * @param x position x
     * @param y position y
     * @param w largeur
     * @param h hauteur
     */
    public SimulationView(int x, int y, int w, int h) {
        setLayout(null);
        setBounds(x, y, w, h);
        setBackground(new Color(174, 204, 234));

        App.addFlock("Hirondelles", 50, Colors.BLUE, App.TYPE_PREY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // On récupère un Graphics2D pour pouvoir accéder à d'autres méthodes
        Graphics2D g2D = (Graphics2D) g;

        // Paramètres pour rendre l'affichage plus lisse et fluide
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
