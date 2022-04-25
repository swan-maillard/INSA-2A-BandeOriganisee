import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class AppView extends JFrame implements MouseListener {

    public static int SIMULATION_PANEL_WIDTH;
    public static int CONTROLS_PANEL_WIDTH;
    public static int HEIGHT;

    private JPanel simulationPanel;
    private JPanel controlsPanel;

    public AppView() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        SIMULATION_PANEL_WIDTH = (int) (0.75 * (screenSize.width - 50));
        CONTROLS_PANEL_WIDTH = (int) (0.25 * (screenSize.width - 50));
        HEIGHT = screenSize.height - 100;

        this.setTitle("Bande Organisée");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        Insets insets = this.getInsets();
        int totalWidth = SIMULATION_PANEL_WIDTH + CONTROLS_PANEL_WIDTH + insets.left + insets.right;
        int totalHeight = HEIGHT + insets.bottom + insets.top;
        this.setPreferredSize(new Dimension(totalWidth, totalHeight));
        this.pack();
        this.setLocationRelativeTo(null);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(null);

        simulationPanel = new SimulationView(0, 0, SIMULATION_PANEL_WIDTH, HEIGHT);
        simulationPanel.addMouseListener(this);
        contentPane.add(simulationPanel);

        controlsPanel = new ControlsView(SIMULATION_PANEL_WIDTH, 0, CONTROLS_PANEL_WIDTH, HEIGHT);
        contentPane.add(controlsPanel);

        this.setContentPane(contentPane);

        this.setVisible(true);
    }

    public void repaintSimulation() {
        simulationPanel.repaint();
    }

    public void repaintControls() {
        JPanel contentPane = (JPanel) this.getContentPane();
        contentPane.remove(controlsPanel);
        controlsPanel = new ControlsView(SIMULATION_PANEL_WIDTH, 0, CONTROLS_PANEL_WIDTH, HEIGHT);
        contentPane.add(controlsPanel);
        this.setContentPane(contentPane);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (App.controlCurrentState == ControlsView.UPDATE_FLOCK_INDEX) {
            Flock currentFlock = App.getCurrentFlock();

            Point location = e.getPoint();
            if (currentFlock.getBoidsNumber() < App.MAX_BOIDS_PER_FLOCK) {
                currentFlock.addBoidAt(location.x, location.y);
            }
            App.repaintControls();
        } else if (App.controlCurrentState == ControlsView.ADD_OBSTACLE_INDEX) {
            ((ControlsView) controlsPanel).doAction(e);
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
