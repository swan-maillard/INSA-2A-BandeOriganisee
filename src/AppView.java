import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Fenêtre principale de l'application
 */
public class AppView extends JFrame implements MouseListener {

    // Taille des fenêtres
    public static int SIMULATION_PANEL_WIDTH;
    public static int CONTROLS_PANEL_WIDTH;
    public static int HEIGHT;

    // JPanels composants
    private JPanel simulationPanel;
    private JPanel controlsPanel;

    /**
     * Constructeur
     */
    public AppView() {

        // La taille de la fenêtre est la taille de l'écran - 100px
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        SIMULATION_PANEL_WIDTH = (int) (0.75 * (screenSize.width - 50));
        CONTROLS_PANEL_WIDTH = (int) (0.25 * (screenSize.width - 50));
        HEIGHT = screenSize.height - 100;

        // Paramétrage de la fenêtre
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

        // On ajoute le JPanel de simulation
        simulationPanel = new SimulationView(0, 0, SIMULATION_PANEL_WIDTH, HEIGHT);
        simulationPanel.addMouseListener(this);
        contentPane.add(simulationPanel);

        // On ajoute le JPanel de contrôle
        controlsPanel = new ControlsView(SIMULATION_PANEL_WIDTH, 0, CONTROLS_PANEL_WIDTH, HEIGHT);
        contentPane.add(controlsPanel);

        this.setContentPane(contentPane);

        this.setVisible(true);
    }

    /**
     * On repaint la simulation
     */
    public void repaintSimulation() {
        simulationPanel.repaint();
    }

    /**
     * On repaint le panneau de contrôle
     */
    public void repaintControls() {
        JPanel contentPane = (JPanel) this.getContentPane();
        contentPane.remove(controlsPanel);
        controlsPanel = new ControlsView(SIMULATION_PANEL_WIDTH, 0, CONTROLS_PANEL_WIDTH, HEIGHT);
        contentPane.add(controlsPanel);
        this.setContentPane(contentPane);
    }


    @Override
    public void mousePressed(MouseEvent e) {
        // Le joueur a cliqué sur l'écran de simulation
        // Soit on ajoute un boid, soit on ajoute un obstacle
        if (App.controlCurrentState == App.UPDATE_FLOCK_INDEX) {
            Flock currentFlock = App.getCurrentFlock();

            Point location = e.getPoint();
            if (currentFlock.getBoidsNumber() < App.MAX_BOIDS_PER_FLOCK) {
                currentFlock.addBoidAt(location.x, location.y);
            }
            App.repaintControls();
        } else if (App.controlCurrentState == App.ADD_OBSTACLE_INDEX) {
            // doAction va rajouter un obstacle
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
