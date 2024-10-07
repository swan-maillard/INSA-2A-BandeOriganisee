# Bande Organisée

## Description

The **Bande Organisée** project is an implementation of a simulation algorithm for group behaviors of animals, known as "boids," developed by Craig Reynolds in 1986. This program simulates a group of agents (boids) that interact with each other according to simple rules of cohesion, separation, and alignment, as well as other behaviors such as fleeing from a predator and avoiding obstacles.

Learn more about boids: https://en.wikipedia.org/wiki/Boids

## Features

- Creation of boids of different species.
- Management of parameters for each species.
- Boid behaviors based on:
  - **Cohesion**: boids move towards the center of their neighbors.
  - **Separation**: boids avoid getting too close to each other.
  - **Alignment**: boids align with the average direction of their neighbors.
  - **Hunting/Fleeing**: predators chase prey while the prey attempts to escape.
  - **Obstacle avoidance**: boids avoid obstacles in their environment.
- Visualization of the trails left by the boids to show their movements.
- Display of the boids' field of vision with a blind spot.
- Addition of obstacles of varying sizes.

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- An Integrated Development Environment (IDE) such as Eclipse, IntelliJ IDEA, or NetBeans.

## Files

- **App.java**
	> Main class.
- **AppView.java**
	> JFrame. Main window of the application.
- **Boid.java**
	> A boid represents an individual of a species (flock). It moves according to rules that apply forces based on other boids.
- **Colors.java**
	> Enumeration. Contains the different colors usable for the boids and obstacles.
- **ControlsView.java**
	> JPanel. Display of the control panel.
- **Flock.java**
	> Flock. Represents a unique species consisting of boids.
- **Obstacle.java**
	> Circular obstacle that can block the movement of boids.
- **SimulationView.java**
	> JPanel. Display of the simulation.
- **Vector2D.java**
	> Class for manipulating 2D vectors.

## Installation

1. Clone the GitHub repository:

   ```bash
   git clone https://github.com/swan-maillard/INSA-2A-BandeOriganisee.git
   ```

2. Navigate to the project directory:

   ```bash
   cd INSA-2A-BandeOriganisee
   ```

3. Open the project in your preferred IDE.

4. Compile and run the project via the main method of the App class. Ensure that the necessary libraries are added to your classpath.

## Usage

1. Launch the application. A window will open showing the simulation of the boids.
2. You can create different species of boids (max. 5 species) and modify their numbers (max. 100 boids per species), their behaviors (predator or prey), and other parameters through the user interface.
3. You can add obstacles.

## Authors

- Swan Maillard (maillard.swan@gmail.com)
- Adam Laalouj (adam.laalouj@insa-lyon.fr)
- Vincent Sename (vincent.sename@insa-lyon.fr)
- Imad Touil (imad.touil@insa-lyon.fr)

## License

This project is licensed under the MIT License. Please consult the `LICENSE` file for more information.
