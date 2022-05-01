import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class App {

    public static final int TIMER_DELAY = 50;
    public static final int MAX_BOIDS_PER_FLOCK = 100;
    public static final int BOIDS_MIN_SPEED = 5;
    public static final int BOIDS_MAX_SPEED = 15;

    public static final int TYPE_PREY = 0;
    public static final int TYPE_PREDATOR = 1;

    public static ArrayList<Flock> flocks = new ArrayList<>();
    public static ArrayList<Obstacle> obstacles = new ArrayList<>();

    public static int controlCurrentState = 1;
    public static int controlCurrentFlockIndex = 0;

    private static AppView appView;

    private static void run() {
        appView = new AppView();
        runSimulation();
    }

    private static void runSimulation() {
        new Timer(TIMER_DELAY, (ActionEvent e) -> {
            appView.repaintSimulation();
        }).start();
    }

    public static void repaintControls() {
        appView.repaintControls();
    }

    public static void addFlock(String name, int number, Colors colors, int type) {
        if (!name.equals("")) {
            number = Math.min(MAX_BOIDS_PER_FLOCK, Math.max(0, number));
            flocks.add(new Flock(name, number, colors, type));
            controlCurrentState = (flocksSize() < 5 ? 1 : 0);
            controlCurrentFlockIndex = Math.max(0, flocksSize() - 1);
        }
    }

    public static void resetFlock(Flock flock) {
        for (Boid boid : flock.getBoids()) {
            boid.setRandomLocation();
            boid.setRandomVelocity();
            boid.clearTrails();
        }
    }

    public static void removeFlock(Flock flock) {
        flocks.remove(flock);
        controlCurrentFlockIndex = ControlsView.CREATE_FLOCK_INDEX;
        controlCurrentState = (flocksSize() > 0 ? 1 : 0);
    }

    public static void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

    public static void removeObstacles() {
        obstacles.clear();
    }

    public static int flocksSize() {
        return flocks.size();
    }

    public static Flock getCurrentFlock() {
        return flocks.get(controlCurrentFlockIndex);
    }

    public static ArrayList<String> getFlocksNames() {
        ArrayList<String> flocksName = new ArrayList<>(flocksSize());
        for (Flock flock : flocks) {
            flocksName.add(flock.getName());
        }
        return flocksName;
    }

    public static void main(String[] args) {
        App.run();
    }
}
