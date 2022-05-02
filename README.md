# Bande Organisée

Simulation des comportements grégaires d'individus (*boids*) et notamment de leur déplacement en nuée / banc.

## Fichiers

- **App.java**
	> Classe principale.
- **AppView.java**
	> JFrame. Fenêtre principale de l'application.
- **Boid.java**
	> Un boid représente un individu d'une espèce (flock).
	Il se déplace d'après des règles lui appliquant des forces en fonction des autres boids.
- **Colors.java**
	> Énumération. Contient les différentes couleurs utilisables pour les boids et les obstacles.
- **ControlsView.java**
	> JPanel. Affichage du panneau de contrôle.
- **Flock.java**
	> Nuée. Représente une espèce unique comportant des boids.
- **Obstacle.java**
	> Obstacle circulaire pouvant bloquer le déplacement de boids.
- **SimulationView.java**
	> JPanel. Affichage de la simulation.
- **Vector2D.java**
	> Classe permettant de manipuler des vecteurs à 2 dimensions.

## Lancer la simulation

Pour lancer la simulation il suffit d'exécuter la méthode **main** dans la classe **App**.
Les boids de l'espèce *"Hirondelles"* seront automatiquement ajoutés à la simulation.

## Informations complémentaires

- La simulation peut contenir au maximum **5 espèces**.
- Une espèce peut contenir au maximum **100 boids**.
- Les boids ont du mal à éviter les obstacles s'ils sont proches les uns des autres.