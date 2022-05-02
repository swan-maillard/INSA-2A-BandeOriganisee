import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * Classe principale
 */
public class App {

    // Délai entre chaque ré-affichage (en millisecondes)
    public static final int TIMER_DELAY = 50;

    // Nombre de flocks maximal et nombre de boids maximal par flock
    public static final int MAX_FLOCKS = 5;
    public static final int MAX_BOIDS_PER_FLOCK = 100;

    // Vitesses min et max des boids
    public static final int BOIDS_MIN_SPEED = 5;
    public static final int BOIDS_MAX_SPEED = 15;

    // Types des flocks (Proie ou Prédateur)
    public static final int TYPE_PREY = 0;
    public static final int TYPE_PREDATOR = 1;

    // Liste des flocks (différentes espèces) et des obstacles
    public static ArrayList<Flock> flocks = new ArrayList<>();
    public static ArrayList<Obstacle> obstacles = new ArrayList<>();

    // Variables du panneau de contrôle
    public static int controlCurrentState = 1;
    public static int controlCurrentFlockIndex = 0;

    // JPanel principal de l'application
    private static AppView appView;

    /**
     * Crée l'IHM et lance la simulation
     */
    private static void run() {
        appView = new AppView();
        runSimulation();
    }

    /**
     * Repaint la simulation toutes les TIMER_DELAY millisecondes
     */
    private static void runSimulation() {
        new Timer(TIMER_DELAY, (ActionEvent e) -> {
            appView.repaintSimulation();
        }).start();
    }

    /**
     * On repaint le JFrame de contrôle
     */
    public static void repaintControls() {
        appView.repaintControls();
    }

    /**
     * Ajoute une espèce à la liste des espèces (flocks)
     *
     * @param name   nom de l'espèce
     * @param number nombre de boids
     * @param colors couleur de l'espèce (parmi les couleurs de Colors)
     * @param type   type de l'espèce (proie ou prédateur)
     */
    public static void addFlock(String name, int number, Colors colors, int type) {
        if (!name.equals("") && flocksSize() < MAX_FLOCKS) {
            number = Math.min(MAX_BOIDS_PER_FLOCK, Math.max(0, number));
            flocks.add(new Flock(name, number, colors, type));

            // On change l'état du panneau de contrôle pour pouvoir modifier cette nouvelle espèce
            controlCurrentState = ControlsView.UPDATE_FLOCK_INDEX;
            // On définit cette espèce comme espèce courante
            controlCurrentFlockIndex = Math.max(0, flocksSize() - 1);
        }
    }

    /**
     * Réinitialise la position et la vitesse des boids de la flock
     *
     * @param flock
     */
    public static void resetFlock(Flock flock) {
        for (Boid boid : flock.getBoids()) {
            boid.setRandomPosition();
            boid.setRandomVelocity();
            boid.clearTrails();
        }
    }

    /**
     * Supprime la flock
     *
     * @param flock
     */
    public static void removeFlock(Flock flock) {
        flocks.remove(flock);

        // La première espèce devient l'espèce courante
        controlCurrentFlockIndex = 0;

        // S'il n'y a plus de d'espèces, on change l'état du panneau de contrôle pour créer une espèce
        if (flocksSize() == 0) {
            controlCurrentState = ControlsView.CREATE_FLOCK_INDEX;
        }
    }

    /**
     * Ajoute un obstacle à la liste d'obtacles
     *
     * @param obstacle
     */
    public static void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

    /**
     * Supprime tous les obstacles
     */
    public static void removeObstacles() {
        obstacles.clear();
    }

    /**
     * Renvoie le nombre de boids de l'espèce
     *
     * @return nombre de boids
     */
    public static int flocksSize() {
        return flocks.size();
    }

    /**
     * Renvoie l'espèce selectionnée dans le panneau de contrôle
     *
     * @return l'espèce selectionnée
     */
    public static Flock getCurrentFlock() {
        return flocks.get(controlCurrentFlockIndex);
    }

    /**
     * Retourne la liste de noms des espèces
     *
     * @return liste de noms des espèces
     */
    public static ArrayList<String> getFlocksNames() {
        ArrayList<String> flocksName = new ArrayList<>(flocksSize());
        for (Flock flock : flocks) {
            flocksName.add(flock.getName());
        }
        return flocksName;
    }

    /**
     * Fonction principale du programme
     *
     * @param args
     */
    public static void main(String[] args) {
        App.run();
    }
}
