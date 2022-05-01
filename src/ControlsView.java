import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;

public class ControlsView extends JPanel implements ActionListener, ChangeListener {

    public static final int CREATE_FLOCK_INDEX = 0;
    public static final int UPDATE_FLOCK_INDEX = 1;
    public static final int ADD_OBSTACLE_INDEX = 2;

    private static final String[] controlsActions = new String[]{"Créer une espèce", "Modifier une espèce", "Ajouter un obstacle"};
    private int[] controlsActionsDisplayed;

    private JComboBox<String> flocksComboBox;
    private JComboBox<String> actionComboBox;
    private JSlider cohesionSlider;
    private JSlider alignmentSlider;
    private JSlider separationSlider;
    private JSlider intoleranceSlider;
    private JSlider speedSlider;
    private JSlider viewRangeSlider;
    private JCheckBox trailsCheckBox;
    private JCheckBox viewRangeCheckBox;

    private JTextField createFlockNameField;
    private JSpinner createFlockNumberField;
    private JComboBox<String> createFlockColorComboBox;
    private JComboBox<String> createFlockTypeComboBox;
    private JButton createFlockButton;

    private JSpinner updateFlockNumberField;
    private JComboBox<String> updateFlockColorComboBox;
    private JComboBox<String> updateFlockTypeComboBox;
    private JButton resetFlockButton;
    private JButton deleteFlockButton;

    private JSpinner obstacleSizeField;
    private JComboBox<String> obstacleColorComboBox;
    private JButton deleteObstaclesButton;

    public ControlsView(int x, int y, int width, int height) {
        this.setLayout(null);
        this.setBounds(x, y, width, height);
        this.setBackground(Color.WHITE);

        int settingsWidth = getWidth() - 40;

        JLabel actionLabel = new JLabel("Action");
        actionLabel.setBounds(20, 20, settingsWidth, 20);
        this.add(actionLabel);


        ArrayList<String> actionItems = new ArrayList<>();
        if (App.flocksSize() == 0) {
            controlsActionsDisplayed = new int[]{CREATE_FLOCK_INDEX, ADD_OBSTACLE_INDEX}; // Créer espèce et ajouter obstacle
        } else if (App.flocksSize() == 5) {
            controlsActionsDisplayed = new int[]{CREATE_FLOCK_INDEX, ADD_OBSTACLE_INDEX}; // Modifier espèce et ajouter obstacle
        } else {
            controlsActionsDisplayed = new int[]{CREATE_FLOCK_INDEX, UPDATE_FLOCK_INDEX, ADD_OBSTACLE_INDEX}; // Créer espèce, modifier espèce et ajouter obstacle
        }

        for (int actionIndex : controlsActionsDisplayed) {
            actionItems.add(controlsActions[actionIndex]);
        }

        actionComboBox = new JComboBox<>();
        actionComboBox.setModel(new DefaultComboBoxModel<>(actionItems.toArray(new String[0])));
        actionComboBox.setSelectedIndex(App.controlCurrentState);
        actionComboBox.setBounds(20, 40, settingsWidth, 20);
        actionComboBox.addActionListener(this);
        this.add(actionComboBox);

        switch (App.controlCurrentState) {
            case CREATE_FLOCK_INDEX:
                this.add(createFlockPanel());
                break;
            case UPDATE_FLOCK_INDEX:
                this.add(updateFlockPanel());
                break;
            case ADD_OBSTACLE_INDEX:
                this.add(createObstaclePanel());
                break;
        }
    }

    private JPanel createFlockPanel() {
        JPanel createFlockPanel = new JPanel();
        createFlockPanel.setLayout(null);
        createFlockPanel.setBounds(0, 0, getWidth(), getHeight() - 100);
        createFlockPanel.setBackground(Color.WHITE);

        int width = getWidth() - 40;

        JLabel nameLabel = new JLabel("Nom");
        nameLabel.setBounds(20, 100, width, 20);
        createFlockPanel.add(nameLabel);

        createFlockNameField = new JTextField();
        createFlockNameField.setBounds(20, 120, width, 20);
        createFlockPanel.add(createFlockNameField);

        JLabel numberLabel = new JLabel("Nombre");
        numberLabel.setBounds(20, 140, (width - 20) / 3, 20);
        createFlockPanel.add(numberLabel);

        createFlockNumberField = new JSpinner();
        createFlockNumberField.setModel(new SpinnerNumberModel(1, 0, App.MAX_BOIDS_PER_FLOCK, 1));
        createFlockNumberField.setBounds(20, 160, (width - 20) / 3, 20);
        createFlockPanel.add(createFlockNumberField);

        JLabel colorLabel = new JLabel("Couleur");
        colorLabel.setBounds(20 + width / 3, 140, (width - 20) / 3, 20);
        createFlockPanel.add(colorLabel);

        createFlockColorComboBox = new JComboBox<>();
        createFlockColorComboBox.setModel(new DefaultComboBoxModel<>(Colors.getColors()));
        createFlockColorComboBox.setBounds(20 + width / 3, 160, (width - 20) / 3, 20);
        createFlockPanel.add(createFlockColorComboBox);

        JLabel typeLabel = new JLabel("Type");
        typeLabel.setBounds(20 + 2* width /3, 140, (width - 20) / 3, 20);
        createFlockPanel.add(typeLabel);

        createFlockTypeComboBox = new JComboBox<>();
        createFlockTypeComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"Proies", "Prédateurs"}));
        createFlockTypeComboBox.setBounds(20 + 2* width /3, 160, (width - 20) / 3, 20);
        createFlockPanel.add(createFlockTypeComboBox);

        createFlockButton = new JButton("Créer l'espèce");
        createFlockButton.setBounds(20, 200, width, 20);
        createFlockButton.addActionListener(this);
        createFlockPanel.add(createFlockButton);

        return createFlockPanel;
    }

    private JPanel updateFlockPanel() {
        JPanel updateFlockPanel = new JPanel();
        updateFlockPanel.setLayout(null);
        updateFlockPanel.setBounds(0, 0, getWidth(), getHeight() - 100);
        updateFlockPanel.setBackground(Color.WHITE);

        Flock currentFlock = App.getCurrentFlock();
        int width = getWidth() - 40;

        JLabel FlockLabel = new JLabel("Espèce");
        FlockLabel.setBounds(20, 80, width, 20);
        updateFlockPanel.add(FlockLabel);

        ArrayList<String> flocksNames = App.getFlocksNames();
        flocksComboBox = new JComboBox<>();
        flocksComboBox.setModel(new DefaultComboBoxModel<>(flocksNames.toArray(new String[0])));
        flocksComboBox.setSelectedIndex(App.controlCurrentFlockIndex);
        flocksComboBox.setBounds(20, 100, width, 20);
        flocksComboBox.addActionListener(this);
        updateFlockPanel.add(flocksComboBox);

        JLabel numberLabel = new JLabel("Nombre");
        numberLabel.setBounds(20, 150, (width - 20) / 3, 20);
        updateFlockPanel.add(numberLabel);

        updateFlockNumberField = new JSpinner();
        updateFlockNumberField.setModel(new SpinnerNumberModel(currentFlock.getBoidsNumber(), 0, App.MAX_BOIDS_PER_FLOCK, 1));
        updateFlockNumberField.setBounds(20, 170, (width - 20) / 3, 20);
        updateFlockNumberField.addChangeListener(this);
        updateFlockPanel.add(updateFlockNumberField);

        JLabel colorLabel = new JLabel("Couleur");
        colorLabel.setBounds(20 + width / 3, 150, (width - 20) / 3, 20);
        updateFlockPanel.add(colorLabel);

        int colorIndex = currentFlock.getColors().ordinal();
        updateFlockColorComboBox = new JComboBox<>();
        updateFlockColorComboBox.setModel(new DefaultComboBoxModel<>(Colors.getColors()));
        updateFlockColorComboBox.setSelectedIndex(colorIndex);
        updateFlockColorComboBox.setBounds(20 + width / 3, 170, (width - 20) / 3, 20);
        updateFlockColorComboBox.addActionListener(this);
        updateFlockPanel.add(updateFlockColorComboBox);

        JLabel typeLabel = new JLabel("Type");
        typeLabel.setBounds(20 + 2* width /3, 150, (width - 20) / 3, 20);
        updateFlockPanel.add(typeLabel);

        updateFlockTypeComboBox = new JComboBox<>();
        updateFlockTypeComboBox = new JComboBox<>();
        updateFlockTypeComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"Proies", "Prédateurs"}));
        updateFlockTypeComboBox.setSelectedIndex(currentFlock.getType());
        updateFlockTypeComboBox.setBounds(20 + 2* width /3, 170, (width - 20) / 3, 20);
        updateFlockTypeComboBox.addActionListener(this);
        updateFlockPanel.add(updateFlockTypeComboBox);

        JLabel cohesionLabel = new JLabel("Cohésion", SwingConstants.CENTER);
        cohesionLabel.setBounds(20, 210, width / 3, 20);
        updateFlockPanel.add(cohesionLabel);

        cohesionSlider = new JSlider(0, 10, (int) (currentFlock.getCohesionCoeff() * 1000));
        cohesionSlider.setBounds(20, 230, width / 3, 20);
        cohesionSlider.addChangeListener(this);
        updateFlockPanel.add(cohesionSlider);

        JLabel alignmentLabel = new JLabel("Alignement", SwingConstants.CENTER);
        alignmentLabel.setBounds(20 + width / 3, 210, width / 3, 20);
        updateFlockPanel.add(alignmentLabel);

        alignmentSlider = new JSlider(0, 10, (int) (currentFlock.getAlignementCoeff() * 100));
        alignmentSlider.setBounds(20 + width / 3, 230, width / 3, 20);
        alignmentSlider.addChangeListener(this);
        updateFlockPanel.add(alignmentSlider);

        JLabel separationLabel = new JLabel("Séparation", SwingConstants.CENTER);
        separationLabel.setBounds(20 + 2 * width / 3, 210, width / 3, 20);
        updateFlockPanel.add(separationLabel);

        separationSlider = new JSlider(0, 10, (int) (currentFlock.getSeparationCoeff() * 100));
        separationSlider.setBounds(20 + 2 * width / 3, 230, width / 3, 20);
        separationSlider.addChangeListener(this);
        updateFlockPanel.add(separationSlider);

        JLabel intoleranceLabel = new JLabel("Intolérence", SwingConstants.CENTER);
        intoleranceLabel.setBounds(20, 250, width / 3, 20);
        updateFlockPanel.add(intoleranceLabel);

        intoleranceSlider = new JSlider(0, 10, (int) (currentFlock.getIntoleranceCoeff() * 100));
        intoleranceSlider.setBounds(20, 270, width / 3, 20);
        intoleranceSlider.addChangeListener(this);
        updateFlockPanel.add(intoleranceSlider);

        JLabel speedLabel = new JLabel("Vitesse", SwingConstants.CENTER);
        speedLabel.setBounds(20 + width / 3, 250, width / 3, 20);
        updateFlockPanel.add(speedLabel);

        speedSlider = new JSlider(App.BOIDS_MIN_SPEED, App.BOIDS_MAX_SPEED, (int) currentFlock.getSpeedLimit());
        speedSlider.setBounds(20 + width / 3, 270, width / 3, 20);
        speedSlider.addChangeListener(this);
        updateFlockPanel.add(speedSlider);

        JLabel viewRangeLabel = new JLabel("Vision", SwingConstants.CENTER);
        viewRangeLabel.setBounds(20 + 2 * width / 3, 250, width / 3, 20);
        updateFlockPanel.add(viewRangeLabel);

        viewRangeSlider = new JSlider(0, 200, currentFlock.getViewRange());
        viewRangeSlider.setBounds(20 + 2 * width / 3, 270, width / 3, 20);
        viewRangeSlider.addChangeListener(this);
        updateFlockPanel.add(viewRangeSlider);

        trailsCheckBox = new JCheckBox("Afficher les traces", currentFlock.displayTrails);
        trailsCheckBox.setBounds(20, 310, width / 2, 20);
        trailsCheckBox.addActionListener(this);
        updateFlockPanel.add(trailsCheckBox);

        viewRangeCheckBox = new JCheckBox("Afficher la vision", currentFlock.displayViewRange);
        viewRangeCheckBox.setBounds(20 + width / 2, 310, width / 2, 20);
        viewRangeCheckBox.addActionListener(this);
        updateFlockPanel.add(viewRangeCheckBox);

        resetFlockButton = new JButton("Réinitialiser l'espèce");
        resetFlockButton.setBounds(20, 350, width, 20);
        resetFlockButton.addActionListener(this);
        updateFlockPanel.add(resetFlockButton);

        deleteFlockButton = new JButton("Supprimer l'espèce");
        deleteFlockButton.setBounds(20, 390, width, 20);
        deleteFlockButton.addActionListener(this);
        updateFlockPanel.add(deleteFlockButton);

        return updateFlockPanel;
    }

    private JPanel createObstaclePanel() {
        JPanel createObstaclePanel = new JPanel();
        createObstaclePanel.setLayout(null);
        createObstaclePanel.setBounds(0, 0, getWidth(), getHeight() - 100);
        createObstaclePanel.setBackground(Color.WHITE);

        int width = getWidth() - 40;

        JLabel obstacleSizeLabel = new JLabel("Taille");
        obstacleSizeLabel.setBounds(20, 100, width, 20);
        createObstaclePanel.add(obstacleSizeLabel);

        obstacleSizeField = new JSpinner();
        obstacleSizeField.setModel(new SpinnerNumberModel(50, 10, 1000, 1));
        obstacleSizeField.setBounds(20, 120, (width - 20) / 2, 20);
        createObstaclePanel.add(obstacleSizeField);

        JLabel colorLabel = new JLabel("Couleur");
        colorLabel.setBounds(30 + width / 2, 100, (width - 20) / 2, 20);
        createObstaclePanel.add(colorLabel);

        obstacleColorComboBox = new JComboBox<>();
        obstacleColorComboBox.setModel(new DefaultComboBoxModel<>(Colors.getColors()));
        obstacleColorComboBox.setBounds(30 + width / 2, 120, (width - 20) / 2, 20);
        createObstaclePanel.add(obstacleColorComboBox);

        deleteObstaclesButton = new JButton("Supprimer les obstacles");
        deleteObstaclesButton.setBounds(20, 160, width, 20);
        deleteObstaclesButton.addActionListener(this);
        createObstaclePanel.add(deleteObstaclesButton);

        return createObstaclePanel;
    }

    public void doAction(EventObject e) {
        Object src = e.getSource();
        boolean repaint = true;

        if (src == actionComboBox) {
            App.controlCurrentState = actionComboBox.getSelectedIndex();
        } else if (App.controlCurrentState == CREATE_FLOCK_INDEX && src == createFlockButton) {
            String name = createFlockNameField.getText();
            int number = (int) createFlockNumberField.getValue();
            Colors color = Colors.values()[createFlockColorComboBox.getSelectedIndex()];
            int type = createFlockTypeComboBox.getSelectedIndex();
            App.addFlock(name, number, color, type);
        } else if (App.controlCurrentState == UPDATE_FLOCK_INDEX) {
            Flock currentFlock = App.getCurrentFlock();

            if (src == flocksComboBox) {
                App.controlCurrentFlockIndex = flocksComboBox.getSelectedIndex();
            } else if (src == updateFlockColorComboBox) {
                Colors color = Colors.values()[updateFlockColorComboBox.getSelectedIndex()];
                currentFlock.setColors(color);
            } else if (src == updateFlockTypeComboBox) {
                currentFlock.setType(updateFlockTypeComboBox.getSelectedIndex());
            } else if (src == updateFlockNumberField) {
                int nbBoids = (int) updateFlockNumberField.getValue();
                if (nbBoids >= 0 && nbBoids <= App.MAX_BOIDS_PER_FLOCK) {
                    currentFlock.updateBoidNumber(nbBoids);
                }
            } else if (src == viewRangeCheckBox) {
                currentFlock.displayViewRange = viewRangeCheckBox.isSelected();
            } else if (src == trailsCheckBox) {
                currentFlock.displayTrails = trailsCheckBox.isSelected();
                for (Boid boid : currentFlock.getBoids()) {
                    boid.clearTrails();
                }
            } else if (src == resetFlockButton) {
                App.resetFlock(currentFlock);
            } else if (src == deleteFlockButton) {
                App.removeFlock(currentFlock);
            } else if (src == viewRangeSlider) {
                currentFlock.setViewRange(viewRangeSlider.getValue());
            } else if (src == speedSlider) {
                currentFlock.setSpeedLimit(speedSlider.getValue());
            } else if (src == cohesionSlider) {
                currentFlock.setCohesionCoeff(cohesionSlider.getValue() / 1000.);
            } else if (src == separationSlider) {
                currentFlock.setSeparationCoeff(separationSlider.getValue() / 100.);
            } else if (src == alignmentSlider) {
                currentFlock.setAlignementCoeff(alignmentSlider.getValue() / 100.);
            } else if (src == intoleranceSlider) {
                currentFlock.setIntoleranceCoeff(intoleranceSlider.getValue() / 100.);
            }
        } else if (App.controlCurrentState == ADD_OBSTACLE_INDEX) {
            if (e.getSource() == deleteObstaclesButton) {
                App.removeObstacles();
            } else if (e instanceof MouseEvent) {
                Point location = ((MouseEvent) e).getPoint();
                int size = (int) obstacleSizeField.getValue();
                Colors color = Colors.values()[obstacleColorComboBox.getSelectedIndex()];
                App.addObstacle(new Obstacle(new Vector2D(location.x, location.y), size, color));
            }
            repaint = false;
        }

        if (!(src instanceof JSlider) && repaint) App.repaintControls();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        doAction(e);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        doAction(e);
    }
}
