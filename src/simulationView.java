import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class simulationView extends JPanel {

    private int width;
    private int height;
    private BufferedImage bufferedImage;

    public simulationView(int width, int height) {
        this.width = width;
        this.height = height;

        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        setLayout(null);
        setBounds(0, 0, width, height);
        setBackground(new Color(174, 204, 234));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // AVEC DOUBLE BUFFERING

//        Graphics2D offScreenGraphics = bufferedImage.createGraphics();
//        offScreenGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        offScreenGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
//
//        offScreenGraphics.setColor(new Color(174, 204, 234));
//        offScreenGraphics.fillRect(0, 0, width, height);
//
//        for (Flock flock : GUI.flocks) {
//            flock.drawBoids(offScreenGraphics);
//        }
//
//        g.drawImage(bufferedImage, 0, 0, null);


        // SANS DOUBLE BUFFERING

        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

        for (Obstacle obstacle : GUI.obstacles) {
            obstacle.draw(g2D);
        }

        for (Flock flock : GUI.flocks) {
            flock.drawBoids(g2D);
        }


    }
}
