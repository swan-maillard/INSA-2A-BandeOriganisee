import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;

/**
 * JPanel du panneau de contrôle
 */
public class ControlsView extends JPanel implements ActionListener, ChangeListener {

    // Liste déroulante des différents menus
    private JComboBox<String> actionComboBox;

    // Menu création d'espèce
    private JTextField createFlockNameField;
    private JSpinner createFlockNumberField;
    private JComboBox<String> createFlockColorComboBox;
    private JComboBox<String> createFlockTypeComboBox;
    private JButton createFlockButton;

    // Menu modification d'une espèce
    private JComboBox<String> flocksComboBox;
    private JSpinner updateFlockNumberField;
    private JComboBox<String> updateFlockColorComboBox;
    private JComboBox<String> updateFlockTypeComboBox;
    private JSlider cohesionSlider;
    private JSlider alignmentSlider;
    private JSlider separationSlider;
    private JSlider intoleranceSlider;
    private JSlider speedSlider;
    private JSlider viewRangeSlider;
    private JCheckBox trailsCheckBox;
    private JCheckBox viewRangeCheckBox;
    private JButton resetFlockButton;
    private JButton deleteFlockButton;

    // Menu ajout d'obstacles
    private JSpinner obstacleSizeField;
    private JComboBox<String> obstacleColorComboBox;
    private JButton deleteObstaclesButton;

    /**
     * Constructeur
     *
     * @param x position x
     * @param y position y
     * @param w largeur
     * @param h hauteur
     */
    public ControlsView(int x, int y, int w, int h) {
        this.setLayout(null);
        this.setBounds(x, y, w, h);
        this.setBackground(Color.WHITE);

        int width = getWidth() - 40;

        JLabel actionLabel = new JLabel("Action");
        actionLabel.setBounds(20, 20, width, 20);
        this.add(actionLabel);

        // On récupert les menus (actions) disponibles
        App.updateControlsMenusDisplays();
        ArrayList<String> actionItems = new ArrayList<>();
        for (int actionIndex : App.controlsActionsDisplayed) {
            actionItems.add(App.CONTROLS_ACTIONS[actionIndex]);
        }

        // Liste déroulante des menus disponibles
        actionComboBox = new JComboBox<>();
        actionComboBox.setModel(new DefaultComboBoxModel<>(actionItems.toArray(new String[0])));
        actionComboBox.setSelectedIndex(Arrays.asList(App.controlsActionsDisplayed).indexOf(App.controlCurrentState));
        actionComboBox.setBounds(20, 40, width, 20);
        actionComboBox.addActionListener(this);
        this.add(actionComboBox);

        // On ajoute le JPanel du menu selectionné
        switch (App.controlCurrentState) {
            case App.CREATE_FLOCK_INDEX:
                this.add(createFlockPanel());
                break;
            case App.UPDATE_FLOCK_INDEX:
                this.add(updateFlockPanel());
                break;
            case App.ADD_OBSTACLE_INDEX:
                this.add(createObstaclePanel());
                break;
        }
    }

    /**
     * Menu de création d'une espèce
     *
     * @return JPanel du menu
     */
    private JPanel createFlockPanel() {
        JPanel createFlockPanel = new JPanel();
        createFlockPanel.setLayout(null);
        createFlockPanel.setBounds(0, 0, getWidth(), getHeight() - 100);
        createFlockPanel.setBackground(Color.WHITE);

        int width = getWidth() - 40;

        // Nom de l'espèce
        JLabel nameLabel = new JLabel("Nom");
        nameLabel.setBounds(20, 100, width, 20);
        createFlockPanel.add(nameLabel);

        createFlockNameField = new JTextField();
        createFlockNameField.setBounds(20, 120, width, 20);
        createFlockPanel.add(createFlockNameField);

        // Nombre de boids
        JLabel numberLabel = new JLabel("Nombre");
        numberLabel.setBounds(20, 140, (width - 20) / 3, 20);
        createFlockPanel.add(numberLabel);

        createFlockNumberField = new JSpinner();
        createFlockNumberField.setModel(new SpinnerNumberModel(1, 0, App.MAX_BOIDS_PER_FLOCK, 1));
        createFlockNumberField.setBounds(20, 160, (width - 20) / 3, 20);
        createFlockPanel.add(createFlockNumberField);

        // Couleur de l'espèce
        JLabel colorLabel = new JLabel("Couleur");
        colorLabel.setBounds(20 + width / 3, 140, (width - 20) / 3, 20);
        createFlockPanel.add(colorLabel);

        createFlockColorComboBox = new JComboBox<>();
        createFlockColorComboBox.setModel(new DefaultComboBoxModel<>(Colors.getColors()));
        createFlockColorComboBox.setBounds(20 + width / 3, 160, (width - 20) / 3, 20);
        createFlockPanel.add(createFlockColorComboBox);

        // Type de l'espèce (Proies ou Prédateurs)
        JLabel typeLabel = new JLabel("Type");
        typeLabel.setBounds(20 + 2 * width / 3, 140, (width - 20) / 3, 20);
        createFlockPanel.add(typeLabel);

        createFlockTypeComboBox = new JComboBox<>();
        createFlockTypeComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"Proies", "Prédateurs"}));
        createFlockTypeComboBox.setBounds(20 + 2 * width / 3, 160, (width - 20) / 3, 20);
        createFlockPanel.add(createFlockTypeComboBox);

        // Bouton de création de l'espèce
        createFlockButton = new JButton("Créer l'espèce");
        createFlockButton.setBounds(20, 200, width, 20);
        createFlockButton.addActionListener(this);
        createFlockPanel.add(createFlockButton);

        return createFlockPanel;
    }

    /**
     * Menu de modification d'une espèce
     *
     * @return JPanel du menu
     */
    private JPanel updateFlockPanel() {
        JPanel updateFlockPanel = new JPanel();
        updateFlockPanel.setLayout(null);
        updateFlockPanel.setBounds(0, 0, getWidth(), getHeight() - 100);
        updateFlockPanel.setBackground(Color.WHITE);

        Flock currentFlock = App.getCurrentFlock();
        int width = getWidth() - 40;

        // Menu déroulant des différentes espèces créées
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

        // Nombre de boids
        JLabel numberLabel = new JLabel("Nombre");
        numberLabel.setBounds(20, 150, (width - 20) / 3, 20);
        updateFlockPanel.add(numberLabel);

        updateFlockNumberField = new JSpinner();
        updateFlockNumberField.setModel(new SpinnerNumberModel(currentFlock.getBoidsNumber(), 0, App.MAX_BOIDS_PER_FLOCK, 1));
        updateFlockNumberField.setBounds(20, 170, (width - 20) / 3, 20);
        updateFlockNumberField.addChangeListener(this);
        updateFlockPanel.add(updateFlockNumberField);

        // Couleur de l'espèce
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

        // Type de l'espèce (Proies ou Prédateurs)
        JLabel typeLabel = new JLabel("Type");
        typeLabel.setBounds(20 + 2 * width / 3, 150, (width - 20) / 3, 20);
        updateFlockPanel.add(typeLabel);

        updateFlockTypeComboBox = new JComboBox<>();
        updateFlockTypeComboBox = new JComboBox<>();
        updateFlockTypeComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"Proies", "Prédateurs"}));
        updateFlockTypeComboBox.setSelectedIndex(currentFlock.getType());
        updateFlockTypeComboBox.setBounds(20 + 2 * width / 3, 170, (width - 20) / 3, 20);
        updateFlockTypeComboBox.addActionListener(this);
        updateFlockPanel.add(updateFlockTypeComboBox);

        // Slider pour le coefficient de cohésion
        JLabel cohesionLabel = new JLabel("Cohésion", SwingConstants.CENTER);
        cohesionLabel.setBounds(20, 210, width / 3, 20);
        updateFlockPanel.add(cohesionLabel);

        cohesionSlider = new JSlider(0, 10, (int) (currentFlock.getCohesionCoeff() * 1000));
        cohesionSlider.setBounds(20, 230, width / 3, 20);
        cohesionSlider.addChangeListener(this);
        updateFlockPanel.add(cohesionSlider);

        // Slider pour le coefficient d'alignement
        JLabel alignmentLabel = new JLabel("Alignement", SwingConstants.CENTER);
        alignmentLabel.setBounds(20 + width / 3, 210, width / 3, 20);
        updateFlockPanel.add(alignmentLabel);

        alignmentSlider = new JSlider(0, 10, (int) (currentFlock.getAlignementCoeff() * 100));
        alignmentSlider.setBounds(20 + width / 3, 230, width / 3, 20);
        alignmentSlider.addChangeListener(this);
        updateFlockPanel.add(alignmentSlider);

        // Slider pour le coefficient de séparation
        JLabel separationLabel = new JLabel("Séparation", SwingConstants.CENTER);
        separationLabel.setBounds(20 + 2 * width / 3, 210, width / 3, 20);
        updateFlockPanel.add(separationLabel);

        separationSlider = new JSlider(0, 10, (int) (currentFlock.getSeparationCoeff() * 100));
        separationSlider.setBounds(20 + 2 * width / 3, 230, width / 3, 20);
        separationSlider.addChangeListener(this);
        updateFlockPanel.add(separationSlider);

        // Slider pour le coefficient d'intolérance
        JLabel intoleranceLabel = new JLabel("Intolérence", SwingConstants.CENTER);
        intoleranceLabel.setBounds(20, 250, width / 3, 20);
        updateFlockPanel.add(intoleranceLabel);

        intoleranceSlider = new JSlider(0, 10, (int) (currentFlock.getIntoleranceCoeff() * 100));
        intoleranceSlider.setBounds(20, 270, width / 3, 20);
        intoleranceSlider.addChangeListener(this);
        updateFlockPanel.add(intoleranceSlider);

        // Slider pour la limite de vitesse
        JLabel speedLabel = new JLabel("Vitesse", SwingConstants.CENTER);
        speedLabel.setBounds(20 + width / 3, 250, width / 3, 20);
        updateFlockPanel.add(speedLabel);

        speedSlider = new JSlider(App.BOIDS_MIN_SPEED, App.BOIDS_MAX_SPEED, (int) currentFlock.getSpeedLimit());
        speedSlider.setBounds(20 + width / 3, 270, width / 3, 20);
        speedSlider.addChangeListener(this);
        updateFlockPanel.add(speedSlider);

        // Slider pour le rayon du champ de vision
        JLabel viewRangeLabel = new JLabel("Vision", SwingConstants.CENTER);
        viewRangeLabel.setBounds(20 + 2 * width / 3, 250, width / 3, 20);
        updateFlockPanel.add(viewRangeLabel);

        viewRangeSlider = new JSlider(0, 200, currentFlock.getViewRange());
        viewRangeSlider.setBounds(20 + 2 * width / 3, 270, width / 3, 20);
        viewRangeSlider.addChangeListener(this);
        updateFlockPanel.add(viewRangeSlider);

        // Checkbox pour afficher les traces des boids
        trailsCheckBox = new JCheckBox("Afficher les traces", currentFlock.displayTrails);
        trailsCheckBox.setBounds(20, 310, width / 2, 20);
        trailsCheckBox.addActionListener(this);
        updateFlockPanel.add(trailsCheckBox);

        // Checkbox pour afficher le champ de vision des boids
        viewRangeCheckBox = new JCheckBox("Afficher la vision", currentFlock.displayViewRange);
        viewRangeCheckBox.setBounds(20 + width / 2, 310, width / 2, 20);
        viewRangeCheckBox.addActionListener(this);
        updateFlockPanel.add(viewRangeCheckBox);

        // Bouton pour réinitialiser les positions et vitesses des boids
        resetFlockButton = new JButton("Réinitialiser l'espèce");
        resetFlockButton.setBounds(20, 350, width, 20);
        resetFlockButton.addActionListener(this);
        updateFlockPanel.add(resetFlockButton);

        // Bouton pour supprimer l'espèce
        deleteFlockButton = new JButton("Supprimer l'espèce");
        deleteFlockButton.setBounds(20, 390, width, 20);
        deleteFlockButton.addActionListener(this);
        updateFlockPanel.add(deleteFlockButton);

        return updateFlockPanel;
    }

    /**
     * Menu de création d'un obstacle
     *
     * @return JPanel du menu
     */
    private JPanel createObstaclePanel() {
        JPanel createObstaclePanel = new JPanel();
        createObstaclePanel.setLayout(null);
        createObstaclePanel.setBounds(0, 0, getWidth(), getHeight() - 100);
        createObstaclePanel.setBackground(Color.WHITE);

        int width = getWidth() - 40;

        // Taille de l'obstacle
        JLabel obstacleSizeLabel = new JLabel("Taille");
        obstacleSizeLabel.setBounds(20, 100, width, 20);
        createObstaclePanel.add(obstacleSizeLabel);

        obstacleSizeField = new JSpinner();
        obstacleSizeField.setModel(new SpinnerNumberModel(50, 10, 1000, 1));
        obstacleSizeField.setBounds(20, 120, (width - 20) / 2, 20);
        createObstaclePanel.add(obstacleSizeField);

        // Couleur de l'obstacle
        JLabel colorLabel = new JLabel("Couleur");
        colorLabel.setBounds(30 + width / 2, 100, (width - 20) / 2, 20);
        createObstaclePanel.add(colorLabel);

        obstacleColorComboBox = new JComboBox<>();
        obstacleColorComboBox.setModel(new DefaultComboBoxModel<>(Colors.getColors()));
        obstacleColorComboBox.setBounds(30 + width / 2, 120, (width - 20) / 2, 20);
        createObstaclePanel.add(obstacleColorComboBox);

        // Bouton pour supprimer tous les obstacles
        deleteObstaclesButton = new JButton("Supprimer les obstacles");
        deleteObstaclesButton.setBounds(20, 160, width, 20);
        deleteObstaclesButton.addActionListener(this);
        createObstaclePanel.add(deleteObstaclesButton);

        return createObstaclePanel;
    }

    /**
     * Cette méthode est appelée quand l'utilisateur fait une action
     *
     * @param e Évenement
     */
    public void doAction(EventObject e) {
        Object src = e.getSource();

        // Si repaint est true le panneau de contrôle sera repaint à la fin de l'action
        boolean repaint = true;

        if (src == actionComboBox) {
            // Change le menu selectionné
            System.out.println(actionComboBox.getSelectedIndex());
            App.controlCurrentState = App.controlsActionsDisplayed[actionComboBox.getSelectedIndex()];
        } else if (App.controlCurrentState == App.CREATE_FLOCK_INDEX && src == createFlockButton) {
            // Crée une espèce
            String name = createFlockNameField.getText();
            int number = (int) createFlockNumberField.getValue();
            Colors color = Colors.values()[createFlockColorComboBox.getSelectedIndex()];
            int type = createFlockTypeComboBox.getSelectedIndex();
            App.addFlock(name, number, color, type);
        } else if (App.controlCurrentState == App.UPDATE_FLOCK_INDEX) {
            // L'utilisateur est sur le menu de modification d'une espèce
            Flock currentFlock = App.getCurrentFlock();

            if (src == flocksComboBox) {
                App.controlCurrentFlockIndex = flocksComboBox.getSelectedIndex(); // Change l'espèce selectionnée
            } else if (src == updateFlockColorComboBox) {
                Colors color = Colors.values()[updateFlockColorComboBox.getSelectedIndex()]; // Modifie la couleur
                currentFlock.setColors(color);
            } else if (src == updateFlockTypeComboBox) {
                currentFlock.setType(updateFlockTypeComboBox.getSelectedIndex()); // Modifie le type
            } else if (src == updateFlockNumberField) {
                int nbBoids = (int) updateFlockNumberField.getValue();
                if (nbBoids >= 0 && nbBoids <= App.MAX_BOIDS_PER_FLOCK) {
                    currentFlock.updateBoidNumber(nbBoids); // Modifie le nombre de boids
                }
            } else if (src == viewRangeCheckBox) {
                // Active/Désactive l'affichage du champ de vision
                currentFlock.displayViewRange = viewRangeCheckBox.isSelected();
            } else if (src == trailsCheckBox) {
                // Active/Désactive l'affichage des traces
                currentFlock.displayTrails = trailsCheckBox.isSelected();
                for (Boid boid : currentFlock.getBoids()) {
                    boid.clearTrails();
                }
            } else if (src == resetFlockButton) {
                App.resetFlock(currentFlock); // Réinitialise la position et vitesse des boids
            } else if (src == deleteFlockButton) {
                App.removeFlock(currentFlock); // Supprome l'espèce
            } else if (src == viewRangeSlider) {
                currentFlock.setViewRange(viewRangeSlider.getValue()); // Modifie le rayon du champ de vision
            } else if (src == speedSlider) {
                currentFlock.setSpeedLimit(speedSlider.getValue()); // Modifie la limite de vitesse
            } else if (src == cohesionSlider) {
                currentFlock.setCohesionCoeff(cohesionSlider.getValue() / 1000.); // Modifie le coefficient de cohésion
            } else if (src == separationSlider) {
                currentFlock.setSeparationCoeff(separationSlider.getValue() / 100.); // Modifie le coefficient de séparation
            } else if (src == alignmentSlider) {
                currentFlock.setAlignementCoeff(alignmentSlider.getValue() / 100.); // Modifie le coefficient d'alignement
            } else if (src == intoleranceSlider) {
                currentFlock.setIntoleranceCoeff(intoleranceSlider.getValue() / 100.); // Modifie le coefficient d'intolérance
            }
        } else if (App.controlCurrentState == App.ADD_OBSTACLE_INDEX) {
            // L'utilisateur est sur le menu de création d'obstacles

            if (e.getSource() == deleteObstaclesButton) {
                App.removeObstacles(); // Supprime tous les obstacles
            } else if (e instanceof MouseEvent) {
                // Si l'utilisateur a cliqué sur le panneau de simulation, on ajoute un obstacle
                Point location = ((MouseEvent) e).getPoint();
                int size = (int) obstacleSizeField.getValue();
                Color color = Colors.values()[obstacleColorComboBox.getSelectedIndex()].getPrimaryColor();
                App.addObstacle(new Obstacle(new Vector2D(location.x, location.y), size, color));
            }
            repaint = false;
        }

        // Si on peut repeindre et que la modification ne vient pas d'un slider, on repaint le panneau de contrôle
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
