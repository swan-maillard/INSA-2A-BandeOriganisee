import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class GUI extends JFrame implements ActionListener, ChangeListener {

    public static int SIMULATION_PANEL_WIDTH;
    public static int CONFIG_PANEL_WIDTH;
    public static int HEIGHT;

    private ArrayList<Flock> flocks;

    private JComboBox<String> speciesComboBox;
    private JComboBox<String> actionComboBox;
    private JSlider coherenceSlider;
    private JSlider alignmentSlider;
    private JSlider separationSlider;
    private JSlider intoleranceSlider;
    private JSlider speedSlider;
    private JSlider viewRangeSlider;
    private JCheckBox tracksCheckBox;
    private JCheckBox viewRangeCheckBox;

    private JTextField createNameField;
    private JSpinner createNumberBoidsField;
    private JComboBox<String> createColorComboBox;
    private JButton createFlockButton;

    private JPanel configPanel;
    private int configState = 0;
    private JTextField updateNameField;
    private JSpinner updateNumberBoidsField;
    private JComboBox<String> updateColorComboBox;

    public GUI() {
        try {
            UIManager.setLookAndFeel( new FlatLightLaf() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }

        flocks = new ArrayList<>();

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

        contentPane.add(simulationPanel());
        contentPane.add(configPanel());

        this.setContentPane(contentPane);

        this.setVisible(true);
    }

    private JPanel simulationPanel() {
        JPanel simulationPanel = new JPanel();
        simulationPanel.setLayout(null);
        simulationPanel.setBounds(0, 0, SIMULATION_PANEL_WIDTH, HEIGHT);
        simulationPanel.setBackground(Color.BLUE);

        return simulationPanel;
    }

    private JPanel configPanel() {
        configPanel = new JPanel();
        configPanel.setLayout(null);
        configPanel.setBounds(SIMULATION_PANEL_WIDTH, 0, CONFIG_PANEL_WIDTH, HEIGHT);

        int settingsWidth = CONFIG_PANEL_WIDTH - 40;

        JLabel actionLabel = new JLabel("Action");
        actionLabel.setBounds(20, 20, settingsWidth, 20);
        configPanel.add(actionLabel);


        ArrayList<String> actionItems = new ArrayList<>();
        if (flocks.isEmpty()) {
            actionItems.add("Créer une espèce");
        } else {
            actionItems.addAll(List.of("Créer une espèce", "Modifier une espèce", "Ajouter un individu"));
        }

        actionComboBox = new JComboBox<>();
        actionComboBox.setModel(new DefaultComboBoxModel<>(actionItems.toArray(new String[0])));
        actionComboBox.setSelectedIndex(configState);
        actionComboBox.setBounds(20, 40, settingsWidth, 20);
        actionComboBox.addActionListener(this);
        configPanel.add(actionComboBox);

        switch (configState) {
            case 0:
                configPanel.add(createSpeciesPanel());
                break;
            case 1:
                configPanel.add(updateSpeciesPanel());
                break;
            case 2:
                configPanel.add(addBoidPanel());
                break;
        }
        if (configState == 1)
            configPanel.add(updateSpeciesPanel());

        return configPanel;
    }

    private JPanel updateSpeciesPanel() {
        int settingsWidth = CONFIG_PANEL_WIDTH - 40;

        JPanel updateSpeciesPanel = new JPanel();
        updateSpeciesPanel.setLayout(null);
        updateSpeciesPanel.setBounds(0, 150, CONFIG_PANEL_WIDTH, HEIGHT-100);


        JLabel speciesLabel = new JLabel("Espèce");
        speciesLabel.setBounds(20, 0, settingsWidth, 20);
        updateSpeciesPanel.add(speciesLabel);

        speciesComboBox = new JComboBox<>();
        speciesComboBox.setModel(new DefaultComboBoxModel<>());
        speciesComboBox.setBounds(20, 20, settingsWidth, 20);
        speciesComboBox.addActionListener(this);
        updateSpeciesPanel.add(speciesComboBox);

        JLabel nameLabel = new JLabel("Nom");
        nameLabel.setBounds(20, 60, settingsWidth, 20);
        updateSpeciesPanel.add(nameLabel);

        updateNameField = new JTextField();
        updateNameField.setBounds(20, 80, settingsWidth, 20);
        updateSpeciesPanel.add(updateNameField);

        JLabel numberLabel = new JLabel("Nombre");
        numberLabel.setBounds(20, 100, (settingsWidth-20)/2, 20);
        updateSpeciesPanel.add(numberLabel);

        updateNumberBoidsField = new JSpinner();
        updateNumberBoidsField.setModel(new SpinnerNumberModel(1, 1, 100, 1));
        updateNumberBoidsField.setBounds(20, 120, (settingsWidth-20)/2, 20);
        updateSpeciesPanel.add(updateNumberBoidsField);

        JLabel colorLabel = new JLabel("Couleur");
        colorLabel.setBounds(30 + settingsWidth/2, 100, (settingsWidth-20)/2, 20);
        updateSpeciesPanel.add(colorLabel);

        updateColorComboBox = new JComboBox<>();
        updateColorComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"Bleu", "Rouge", "Orange"}));
        updateColorComboBox.setBounds(30 + settingsWidth/2, 120, (settingsWidth-20)/2, 20);
        updateSpeciesPanel.add(updateColorComboBox);

        JLabel coherenceLabel = new JLabel("Cohérence", SwingConstants.CENTER);
        coherenceLabel.setBounds(20, 160, settingsWidth/3, 20);
        updateSpeciesPanel.add(coherenceLabel);

        coherenceSlider = new JSlider(0, 10, 5);
        coherenceSlider.setBounds(20, 180, settingsWidth/3, 20);
        coherenceSlider.addChangeListener(this);
        updateSpeciesPanel.add(coherenceSlider);

        JLabel alignmentLabel = new JLabel("Alignement", SwingConstants.CENTER);
        alignmentLabel.setBounds(20 + settingsWidth/3, 160, settingsWidth/3, 20);
        updateSpeciesPanel.add(alignmentLabel);

        alignmentSlider = new JSlider(0, 10, 5);
        alignmentSlider.setBounds(20 + settingsWidth/3, 180, settingsWidth/3, 20);
        alignmentSlider.addChangeListener(this);
        updateSpeciesPanel.add(alignmentSlider);

        JLabel separationLabel = new JLabel("Séparation", SwingConstants.CENTER);
        separationLabel.setBounds(20 + 2*settingsWidth/3, 160, settingsWidth/3, 20);
        updateSpeciesPanel.add(separationLabel);

        separationSlider = new JSlider(0, 10, 5);
        separationSlider.setBounds(20 + 2*settingsWidth/3, 180, settingsWidth/3, 20);
        separationSlider.addChangeListener(this);
        updateSpeciesPanel.add(separationSlider);

        JLabel intoleranceLabel = new JLabel("Intolérence", SwingConstants.CENTER);
        intoleranceLabel.setBounds(20, 200, settingsWidth/3, 20);
        updateSpeciesPanel.add(intoleranceLabel);

        intoleranceSlider = new JSlider(0, 10, 5);
        intoleranceSlider.setBounds(20, 220, settingsWidth/3, 20);
        intoleranceSlider.addChangeListener(this);
        updateSpeciesPanel.add(intoleranceSlider);

        JLabel speedLabel = new JLabel("Vitesse", SwingConstants.CENTER);
        speedLabel.setBounds(20 + settingsWidth/3, 200, settingsWidth/3, 20);
        updateSpeciesPanel.add(speedLabel);

        speedSlider = new JSlider(7, 15, 10);
        speedSlider.setBounds(20 + settingsWidth/3, 220, settingsWidth/3, 20);
        speedSlider.addChangeListener(this);
        updateSpeciesPanel.add(speedSlider);

        JLabel viewRangeLabel = new JLabel("Vision", SwingConstants.CENTER);
        viewRangeLabel.setBounds(20 + 2*settingsWidth/3, 200, settingsWidth/3, 20);
        updateSpeciesPanel.add(viewRangeLabel);

        viewRangeSlider = new JSlider(0, 200, 100);
        viewRangeSlider.setBounds(20 + 2*settingsWidth/3, 220, settingsWidth/3, 20);
        viewRangeSlider.addChangeListener(this);
        updateSpeciesPanel.add(viewRangeSlider);

        tracksCheckBox = new JCheckBox("Afficher les traces", false);
        tracksCheckBox.setBounds(20, 260, settingsWidth/2, 20);
        tracksCheckBox.addActionListener(this);
        updateSpeciesPanel.add(tracksCheckBox);

        viewRangeCheckBox = new JCheckBox("Afficher la vision", false);
        viewRangeCheckBox.setBounds(20 + settingsWidth/2, 260, settingsWidth/2, 20);
        viewRangeCheckBox.addActionListener(this);
        updateSpeciesPanel.add(viewRangeCheckBox);

        return updateSpeciesPanel;
    }

    private JPanel createSpeciesPanel() {
        int settingsWidth = CONFIG_PANEL_WIDTH - 40;
        JPanel createSpeciesPanel = new JPanel();

        createSpeciesPanel.setLayout(null);
        createSpeciesPanel.setBounds(0, 150, CONFIG_PANEL_WIDTH, HEIGHT-100);

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
        createNumberBoidsField.setModel(new SpinnerNumberModel(1, 1, 100, 1));
        createNumberBoidsField.setBounds(20, 60, (settingsWidth-20)/2, 20);
        createSpeciesPanel.add(createNumberBoidsField);

        JLabel colorLabel = new JLabel("Couleur");
        colorLabel.setBounds(30 + settingsWidth/2, 40, (settingsWidth-20)/2, 20);
        createSpeciesPanel.add(colorLabel);

        createColorComboBox = new JComboBox<>();
        createColorComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"Bleu", "Rouge", "Orange"}));
        createColorComboBox.setBounds(30 + settingsWidth/2, 60, (settingsWidth-20)/2, 20);
        createSpeciesPanel.add(createColorComboBox);

        createFlockButton = new JButton("Créer l'espèce");
        createFlockButton.setBounds(20, 100, settingsWidth, 20);
        createFlockButton.addActionListener(this);
        createSpeciesPanel.add(createFlockButton);

        return createSpeciesPanel;
    }

    private JPanel addBoidPanel() {
        JPanel addBoidPanel = new JPanel();
        int settingsWidth = CONFIG_PANEL_WIDTH - 40;

        addBoidPanel.setLayout(null);
        addBoidPanel.setBounds(0, 150, CONFIG_PANEL_WIDTH, HEIGHT);

        return addBoidPanel;
    }

    private void updateConfigPanel() {
        JPanel contentPane = (JPanel) this.getContentPane();
        contentPane.remove(configPanel);
        contentPane.add(configPanel());
        this.setContentPane(contentPane);
    }

    public static void main(String[] args) {
        new GUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == actionComboBox) {
            configState = actionComboBox.getSelectedIndex();
            updateConfigPanel();
        } else if (e.getSource() == createFlockButton) {
            if (!createNameField.getText().equals("")) {
                String name = createNameField.getText();
                int number = Math.min(100, Math.max(1, (int) createNumberBoidsField.getValue()));
                Color color;
                switch (createColorComboBox.getSelectedIndex()) {
                    case 2:
                        color = Color.RED;
                        break;
                    case 3:
                        color = Color.ORANGE;
                        break;
                    case 1:
                    default:
                        color = Color.BLUE;
                }
                flocks.add(new Flock(name, number, color));
                configState = 1;
                updateConfigPanel();
            }

        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {

    }
}
