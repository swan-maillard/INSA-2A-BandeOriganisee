import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SimulationView extends JPanel implements MouseListener {

    public SimulationView(int x, int y, int width, int height) {
        setLayout(null);
        setBounds(x, y, width, height);
        setBackground(new Color(174, 204, 234));
        addMouseListener(this);

        App.addFlock("Merles", 100, Colors.RED);
        App.addPredator("Faucon", 1, Colors.BLACK);
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

    @Override
    public void mousePressed(MouseEvent e) {
        if (App.controlCurrentState == 1) {
            Flock currentFlock = App.getCurrentFlock();

            Point location = e.getPoint();
            if (currentFlock.getBoidsNumber() < App.MAX_BOIDS_PER_FLOCK) {
                currentFlock.addBoidAt(location.x, location.y);
            }
            App.repaintControls();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
