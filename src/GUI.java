import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class GUI extends JFrame implements ActionListener, ChangeListener, MouseListener, KeyListener {

    public static int SIMULATION_PANEL_WIDTH;
    public static int CONFIG_PANEL_WIDTH;
    public static int HEIGHT;

    private final int TIMER_DELAY = 100;

    protected static ArrayList<Flock> flocks = new ArrayList<>();
    protected static ArrayList<Obstacle> obstacles = new ArrayList<>();

    private JComboBox<String> speciesComboBox;
    private JComboBox<String> actionComboBox;
    private JSlider cohesionSlider;
    private JSlider alignmentSlider;
    private JSlider separationSlider;
    private JSlider intoleranceSlider;
    private JSlider speedSlider;
    private JSlider viewRangeSlider;
    private JCheckBox trailsCheckBox;
    private JCheckBox viewRangeCheckBox;

    private JTextField createNameField;
    private JSpinner createNumberBoidsField;
    private JComboBox<String> createColorComboBox;
    private JButton createFlockButton;

    private JSpinner updateNumberBoidsField;
    private JComboBox<String> updateColorComboBox;
    private JButton deleteFlockButton;

    private JPanel simulationPanel;
    private JPanel configPanel;
    private int configState = 1;
    private int configCurrentFlockIndex = 0;


    private static Timer timer;

    public GUI() {
        try {
            UIManager.setLookAndFeel( new FlatLightLaf() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        SIMULATION_PANEL_WIDTH = (int) (0.75*(screenSize.width-100));
        CONFIG_PANEL_WIDTH = (int) (0.25*(screenSize.width-100));
        HEIGHT = screenSize.height - 100;

        this.setTitle("Bande Organisée");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        Insets insets = this.getInsets();
        int totalWidth = SIMULATION_PANEL_WIDTH + CONFIG_PANEL_WIDTH + insets.left + insets.right;
        int totalHeight = HEIGHT + insets.bottom + insets.top;
        this.setPreferredSize(new Dimension(totalWidth, totalHeight));
        this.pack();
        this.setLocationRelativeTo(null);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(null);

        simulationPanel = new simulationView(SIMULATION_PANEL_WIDTH, HEIGHT);
        simulationPanel.addMouseListener(this);
        contentPane.add(simulationPanel);

        flocks.add(new Flock("Espèce 1", 100, FlockColor.RED));
        obstacles.add(new Obstacle(new Vector2D(400, 400), 20));

        contentPane.add(configPanel());

        this.setContentPane(contentPane);

        this.setVisible(true);

        runSimulation();
    }

    private JPanel configPanel() {
        configPanel = new JPanel();
        configPanel.setLayout(null);
        configPanel.setBounds(SIMULATION_PANEL_WIDTH, 0, CONFIG_PANEL_WIDTH, HEIGHT);
        configPanel.setBackground(Color.WHITE);

        int settingsWidth = CONFIG_PANEL_WIDTH - 40;

        JLabel actionLabel = new JLabel("Action");
        actionLabel.setBounds(20, 20, settingsWidth, 20);
        configPanel.add(actionLabel);


        ArrayList<String> actionItems = new ArrayList<>();
        if (flocks.isEmpty()) {
            actionItems.addAll(List.of("Créer une espèce"));
        } else if (flocks.size() == 5) {
            actionItems.addAll(List.of("Modifier une espèce"));
        } else {
            actionItems.addAll(List.of("Créer une espèce", "Modifier une espèce"));
        }

        actionComboBox = new JComboBox<>();
        actionComboBox.setModel(new DefaultComboBoxModel<>(actionItems.toArray(new String[0])));
        actionComboBox.setSelectedIndex(configState);
        actionComboBox.setBounds(20, 40, settingsWidth, 20);
        actionComboBox.addActionListener(this);
        configPanel.add(actionComboBox);

        switch (flocks.size() < 5 ? configState : configState + 1) {
            case 0:
                configPanel.add(createSpeciesPanel());
                break;
            case 1:
                configPanel.add(updateSpeciesPanel());
                break;
        }

        return configPanel;
    }

    private JPanel createSpeciesPanel() {
        int settingsWidth = CONFIG_PANEL_WIDTH - 40;
        JPanel createSpeciesPanel = new JPanel();

        createSpeciesPanel.setLayout(null);
        createSpeciesPanel.setBounds(0, 150, CONFIG_PANEL_WIDTH, HEIGHT-100);
        createSpeciesPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel("Nom");
        nameLabel.setBounds(20, 0, settingsWidth, 20);
        createSpeciesPanel.add(nameLabel);

        createNameField = new JTextField();
        createNameField.setBounds(20, 20, settingsWidth, 20);
        createSpeciesPanel.add(createNameField);

        JLabel numberLabel = new JLabel("Nombre");
        numberLabel.setBounds(20, 40, (settingsWidth-20)/2, 20);
        createSpeciesPanel.add(numberLabel);

        createNumberBoidsField = new JSpinner();
        createNumberBoidsField.setModel(new SpinnerNumberModel(1, 0, 100, 1));
        createNumberBoidsField.setBounds(20, 60, (settingsWidth-20)/2, 20);
        createSpeciesPanel.add(createNumberBoidsField);

        JLabel colorLabel = new JLabel("Couleur");
        colorLabel.setBounds(30 + settingsWidth/2, 40, (settingsWidth-20)/2, 20);
        createSpeciesPanel.add(colorLabel);

        createColorComboBox = new JComboBox<>();
        createColorComboBox.setModel(new DefaultComboBoxModel<>(FlockColor.getColors()));
        createColorComboBox.setBounds(30 + settingsWidth/2, 60, (settingsWidth-20)/2, 20);
        createSpeciesPanel.add(createColorComboBox);

        createFlockButton = new JButton("Créer l'espèce");
        createFlockButton.setBounds(20, 100, settingsWidth, 20);
        createFlockButton.addActionListener(this);
        createSpeciesPanel.add(createFlockButton);

        return createSpeciesPanel;
    }

    private JPanel updateSpeciesPanel() {
        Flock currentFlock = flocks.get(configCurrentFlockIndex);

        int settingsWidth = CONFIG_PANEL_WIDTH - 40;

        JPanel updateSpeciesPanel = new JPanel();
        updateSpeciesPanel.setLayout(null);
        updateSpeciesPanel.setBounds(0, 150, CONFIG_PANEL_WIDTH, HEIGHT-100);
        updateSpeciesPanel.setBackground(Color.WHITE);

        JLabel speciesLabel = new JLabel("Espèce");
        speciesLabel.setBounds(20, 0, settingsWidth, 20);
        updateSpeciesPanel.add(speciesLabel);

        ArrayList<String> speciesName = new ArrayList<>();
        for (Flock flock : flocks) {
            speciesName.add(flock.getName());
        }
        speciesComboBox = new JComboBox<>();
        speciesComboBox.setModel(new DefaultComboBoxModel<>(speciesName.toArray(new String[0])));
        speciesComboBox.setSelectedIndex(configCurrentFlockIndex);
        speciesComboBox.setBounds(20, 20, settingsWidth, 20);
        speciesComboBox.addActionListener(this);
        updateSpeciesPanel.add(speciesComboBox);

        JLabel numberLabel = new JLabel("Nombre");
        numberLabel.setBounds(20, 100, (settingsWidth-20)/2, 20);
        updateSpeciesPanel.add(numberLabel);

        updateNumberBoidsField = new JSpinner();
        updateNumberBoidsField.setModel(new SpinnerNumberModel(currentFlock.getBoidsNumber(), 0, 100, 1));
        updateNumberBoidsField.setBounds(20, 120, (settingsWidth-20)/2, 20);
        updateNumberBoidsField.addChangeListener(this);
        updateSpeciesPanel.add(updateNumberBoidsField);

        JLabel colorLabel = new JLabel("Couleur");
        colorLabel.setBounds(30 + settingsWidth/2, 100, (settingsWidth-20)/2, 20);
        updateSpeciesPanel.add(colorLabel);

        int colorIndex = currentFlock.getColor().ordinal();
        updateColorComboBox = new JComboBox<>();
        updateColorComboBox.setModel(new DefaultComboBoxModel<>(FlockColor.getColors()));
        updateColorComboBox.setSelectedIndex(colorIndex);
        updateColorComboBox.setBounds(30 + settingsWidth/2, 120, (settingsWidth-20)/2, 20);
        updateColorComboBox.addActionListener(this);
        updateSpeciesPanel.add(updateColorComboBox);

        JLabel cohesionLabel = new JLabel("Cohésion", SwingConstants.CENTER);
        cohesionLabel.setBounds(20, 160, settingsWidth/3, 20);
        updateSpeciesPanel.add(cohesionLabel);

        cohesionSlider = new JSlider(0, 10, (int) (currentFlock.getCohesionCoeff()*10));
        cohesionSlider.setBounds(20, 180, settingsWidth/3, 20);
        cohesionSlider.addChangeListener(this);
        updateSpeciesPanel.add(cohesionSlider);

        JLabel alignmentLabel = new JLabel("Alignement", SwingConstants.CENTER);
        alignmentLabel.setBounds(20 + settingsWidth/3, 160, settingsWidth/3, 20);
        updateSpeciesPanel.add(alignmentLabel);

        alignmentSlider = new JSlider(0, 10, (int) (currentFlock.getAlignementCoeff()*10));
        alignmentSlider.setBounds(20 + settingsWidth/3, 180, settingsWidth/3, 20);
        alignmentSlider.addChangeListener(this);
        updateSpeciesPanel.add(alignmentSlider);

        JLabel separationLabel = new JLabel("Séparation", SwingConstants.CENTER);
        separationLabel.setBounds(20 + 2*settingsWidth/3, 160, settingsWidth/3, 20);
        updateSpeciesPanel.add(separationLabel);

        separationSlider = new JSlider(0, 10, (int) (currentFlock.getSeparationCoeff()*10));
        separationSlider.setBounds(20 + 2*settingsWidth/3, 180, settingsWidth/3, 20);
        separationSlider.addChangeListener(this);
        updateSpeciesPanel.add(separationSlider);

        JLabel intoleranceLabel = new JLabel("Intolérence", SwingConstants.CENTER);
        intoleranceLabel.setBounds(20, 200, settingsWidth/3, 20);
        updateSpeciesPanel.add(intoleranceLabel);

        intoleranceSlider = new JSlider(0, 10, (int) (currentFlock.getSegregationCoeff()*10));
        intoleranceSlider.setBounds(20, 220, settingsWidth/3, 20);
        intoleranceSlider.addChangeListener(this);
        updateSpeciesPanel.add(intoleranceSlider);

        JLabel speedLabel = new JLabel("Vitesse", SwingConstants.CENTER);
        speedLabel.setBounds(20 + settingsWidth/3, 200, settingsWidth/3, 20);
        updateSpeciesPanel.add(speedLabel);

        speedSlider = new JSlider(5, 15, (int) currentFlock.getSpeedMax());
        speedSlider.setBounds(20 + settingsWidth/3, 220, settingsWidth/3, 20);
        speedSlider.addChangeListener(this);
        updateSpeciesPanel.add(speedSlider);

        JLabel viewRangeLabel = new JLabel("Vision", SwingConstants.CENTER);
        viewRangeLabel.setBounds(20 + 2*settingsWidth/3, 200, settingsWidth/3, 20);
        updateSpeciesPanel.add(viewRangeLabel);

        viewRangeSlider = new JSlider(0, 200, currentFlock.getViewRange());
        viewRangeSlider.setBounds(20 + 2*settingsWidth/3, 220, settingsWidth/3, 20);
        viewRangeSlider.addChangeListener(this);
        updateSpeciesPanel.add(viewRangeSlider);

        trailsCheckBox = new JCheckBox("Afficher les traces", currentFlock.displayTrails);
        trailsCheckBox.setBounds(20, 260, settingsWidth/2, 20);
        trailsCheckBox.addActionListener(this);
        updateSpeciesPanel.add(trailsCheckBox);

        viewRangeCheckBox = new JCheckBox("Afficher la vision", currentFlock.displayViewRange);
        viewRangeCheckBox.setBounds(20 + settingsWidth/2, 260, settingsWidth/2, 20);
        viewRangeCheckBox.addActionListener(this);
        updateSpeciesPanel.add(viewRangeCheckBox);

        deleteFlockButton = new JButton("Supprimer l'espèce");
        deleteFlockButton.setBounds(20, 300, settingsWidth, 20);
        deleteFlockButton.addActionListener(this);
        updateSpeciesPanel.add(deleteFlockButton);

        return updateSpeciesPanel;
    }

    private void updateConfigPanel() {
        JPanel contentPane = (JPanel) this.getContentPane();
        contentPane.remove(configPanel);
        contentPane.add(configPanel());
        this.setContentPane(contentPane);
        simulationPanel.repaint();
    }

    private void runSimulation() {
        timer = new Timer(TIMER_DELAY, (ActionEvent e) -> {
            simulationPanel.repaint();
        });

        timer.start();
    }

    protected static void stopSimulation() {
        if (timer.isRunning())
            timer.stop();
    }

    public static void main(String[] args) {
        new GUI();
    }

    private void doAction(EventObject e) {
        int panel = (flocks.size() < 5 ? configState : configState + 1);
        Object src = e.getSource();

        if (src == actionComboBox) {
            configState = actionComboBox.getSelectedIndex();
        } else if (panel == 0) {
            if (src == createFlockButton) {
                if (!createNameField.getText().equals("")) {
                    String name = createNameField.getText();
                    int number = Math.min(100, Math.max(0, (int) createNumberBoidsField.getValue()));
                    flocks.add(new Flock(name, number, FlockColor.values()[createColorComboBox.getSelectedIndex()]));
                    configState = (flocks.size() < 5 ? 1 : 0);
                    configCurrentFlockIndex = flocks.size() - 1;
                }
            }
        } else if (panel == 1) {
            Flock currentFlock = flocks.get(configCurrentFlockIndex);

            if (src == speciesComboBox) {
                configCurrentFlockIndex = speciesComboBox.getSelectedIndex();
            } else if (src == updateColorComboBox) {
                currentFlock.setColor(FlockColor.values()[updateColorComboBox.getSelectedIndex()]);
            } else if (src == updateNumberBoidsField) {
                int nbBoids = (int) updateNumberBoidsField.getValue();
                if (nbBoids >= 0 && nbBoids <= 100) {
                    currentFlock.updateBoidNumber(nbBoids);
                }
            } else if (src == viewRangeCheckBox) {
                currentFlock.displayViewRange = viewRangeCheckBox.isSelected();
            } else if (src == trailsCheckBox){
                currentFlock.displayTrails = trailsCheckBox.isSelected();
                for (Boid boid : currentFlock.getBoids()) {
                    boid.clearTracks();
                }
            } else if (src == deleteFlockButton) {
                flocks.remove(currentFlock);
                configCurrentFlockIndex = 0;
                configState = (flocks.size() > 0 ? 1 : 0);
            } else if (src == viewRangeSlider) {
                currentFlock.setViewRange(viewRangeSlider.getValue());
            } else if (src == speedSlider) {
                currentFlock.setSpeedMax(speedSlider.getValue());
            } else if (src == cohesionSlider) {
                currentFlock.setCohesionCoeff(cohesionSlider.getValue()/10.);
            } else if (src == separationSlider) {
                currentFlock.setSeparationCoeff(separationSlider.getValue()/10.);
            } else if (src == alignmentSlider) {
                currentFlock.setAlignementCoeff(alignmentSlider.getValue()/10.);
            } else if (src == intoleranceSlider) {
                currentFlock.setSegregationCoeff(separationSlider.getValue()/10.);
            }

            if (e instanceof MouseEvent) {
                Point location = ((MouseEvent) e).getPoint();
                if (currentFlock.getBoidsNumber() < 100) {
                    currentFlock.addBoidAt(location.x, location.y);
                }
            }
        }

        if (!(src instanceof JSlider)) updateConfigPanel();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        doAction(e);
    }
    @Override
    public void stateChanged(ChangeEvent e) {
        doAction(e);
    }
    @Override
    public void mousePressed(MouseEvent e) {
        doAction(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
