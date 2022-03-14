import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI implements ActionListener, ChangeListener {

    public static int SIMULATION_PANEL_WIDTH;
    public static int CONFIG_PANEL_WIDTH;
    public static int HEIGHT;

    private JFrame frame;
    private JCheckBox viewRangeCheckBox;
    private JComboBox<String> speciesComboBox;
    private JComboBox<String> actionComboBox;
    private JSlider coherenceSlider;
    private JSlider alignmentSlider;
    private JSlider separationSlider;
    private JSlider intoleranceSlider;
    private JSlider speedSlider;
    private JSlider viewRangeSlider;
    private JCheckBox tracksCheckBox;

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

        frame = new JFrame();
        frame.setTitle("Bande Organisée");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        Insets insets = frame.getInsets();
        int totalWidth = SIMULATION_PANEL_WIDTH + CONFIG_PANEL_WIDTH + insets.left + insets.right;
        int totalHeight = HEIGHT + insets.bottom + insets.top;
        frame.setPreferredSize(new Dimension(totalWidth, totalHeight));
        frame.pack();
        frame.setLocationRelativeTo(null);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(null);

        contentPane.add(simulationPanel());
        contentPane.add(configPanel());

        frame.setContentPane(contentPane);
        frame.setVisible(true);
    }

    private JPanel simulationPanel() {
        JPanel simulationPanel = new JPanel();
        simulationPanel.setLayout(null);
        simulationPanel.setBounds(0, 0, SIMULATION_PANEL_WIDTH, HEIGHT);
        simulationPanel.setBackground(Color.BLUE);

        return simulationPanel;
    }

    private JPanel configPanel() {
        JPanel configPanel = new JPanel();
        configPanel.setLayout(null);
        configPanel.setBounds(SIMULATION_PANEL_WIDTH, 0, CONFIG_PANEL_WIDTH, HEIGHT);
        configPanel.setBackground(Color.WHITE);

        int settingsWidth = CONFIG_PANEL_WIDTH - 40;

        JLabel speciesLabel = new JLabel("Espèces");
        speciesLabel.setBounds(20, 20, settingsWidth, 20);
        configPanel.add(speciesLabel);

        speciesComboBox = new JComboBox<>();
        speciesComboBox.setModel(new DefaultComboBoxModel<>());
        speciesComboBox.setBounds(20, 40, settingsWidth, 20);
        speciesComboBox.addActionListener(this);
        configPanel.add(speciesComboBox);

        JLabel actionLabel = new JLabel("Action");
        actionLabel.setBounds(20, 80, settingsWidth, 20);
        configPanel.add(actionLabel);

        actionComboBox = new JComboBox<>();
        actionComboBox.setModel(new DefaultComboBoxModel<>(new String[] {"Ajouter un individu", "Ajouter un obstacle", "Créer une espèce"}));
        actionComboBox.setBounds(20, 100, settingsWidth, 20);
        actionComboBox.addActionListener(this);
        configPanel.add(actionComboBox);

        JLabel coherenceLabel = new JLabel("Cohérence", SwingConstants.CENTER);
        coherenceLabel.setBounds(20, 200, settingsWidth/3, 20);
        configPanel.add(coherenceLabel);

        coherenceSlider = new JSlider(0, 10, 5);
        coherenceSlider.setBounds(20, 220, settingsWidth/3, 20);
        coherenceSlider.addChangeListener(this);
        configPanel.add(coherenceSlider);

        JLabel alignmentLabel = new JLabel("Alignement", SwingConstants.CENTER);
        alignmentLabel.setBounds(20 + settingsWidth/3, 200, settingsWidth/3, 20);
        configPanel.add(alignmentLabel);

        alignmentSlider = new JSlider(0, 10, 5);
        alignmentSlider.setBounds(20 + settingsWidth/3, 220, settingsWidth/3, 20);
        alignmentSlider.addChangeListener(this);
        configPanel.add(alignmentSlider);

        JLabel separationLabel = new JLabel("Séparation", SwingConstants.CENTER);
        separationLabel.setBounds(20 + 2*settingsWidth/3, 200, settingsWidth/3, 20);
        configPanel.add(separationLabel);

        separationSlider = new JSlider(0, 10, 5);
        separationSlider.setBounds(20 + 2*settingsWidth/3, 220, settingsWidth/3, 20);
        separationSlider.addChangeListener(this);
        configPanel.add(separationSlider);

        JLabel intoleranceLabel = new JLabel("Intolérence", SwingConstants.CENTER);
        intoleranceLabel.setBounds(20, 260, settingsWidth/3, 20);
        configPanel.add(intoleranceLabel);

        intoleranceSlider = new JSlider(0, 10, 5);
        intoleranceSlider.setBounds(20, 280, settingsWidth/3, 20);
        intoleranceSlider.addChangeListener(this);
        configPanel.add(intoleranceSlider);

        JLabel speedLabel = new JLabel("Vitesse", SwingConstants.CENTER);
        speedLabel.setBounds(20 + settingsWidth/3, 260, settingsWidth/3, 20);
        configPanel.add(speedLabel);

        speedSlider = new JSlider(7, 15, 10);
        speedSlider.setBounds(20 + settingsWidth/3, 280, settingsWidth/3, 20);
        speedSlider.addChangeListener(this);
        configPanel.add(speedSlider);

        JLabel viewRangeLabel = new JLabel("Vision", SwingConstants.CENTER);
        viewRangeLabel.setBounds(20 + 2*settingsWidth/3, 260, settingsWidth/3, 20);
        configPanel.add(viewRangeLabel);

        viewRangeSlider = new JSlider(0, 200, 100);
        viewRangeSlider.setBounds(20 + 2*settingsWidth/3, 280, settingsWidth/3, 20);
        viewRangeSlider.addChangeListener(this);
        configPanel.add(viewRangeSlider);

        tracksCheckBox = new JCheckBox("Afficher les traces", false);
        tracksCheckBox.setBounds(20, 360, settingsWidth/2, 20);
        tracksCheckBox.addActionListener(this);
        configPanel.add(tracksCheckBox);

        viewRangeCheckBox = new JCheckBox("Afficher la vision", false);
        viewRangeCheckBox.setBounds(20 + settingsWidth/2, 360, settingsWidth/2, 20);
        viewRangeCheckBox.addActionListener(this);
        configPanel.add(viewRangeCheckBox);

        return configPanel;
    }

    public static void main(String[] args) {
        new GUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void stateChanged(ChangeEvent e) {

    }
}
