# Bande Organisée

## Description

Le projet **Bande Organisée** est une implémentation d'un algorithme de simulation de comportements de groupes d'animaux, connu sous le nom de "boids", développé par Craig Reynolds en 1986. 
Ce programme permet de simuler un groupe d'agent (boids) qui interagissent entre eux selon des règles simples de cohésion, séparation et alignement, ainsi que d'autres comportements tels que la fuite d'un prédateur et l'évitement des obstacles.

Voir plus sur les boids : https://fr.wikipedia.org/wiki/Boids

## Fonctionnalités

- Création de boids de différentes espèces.
- Gestion des paramètres de chaque espèce.
- Comportements de boids basés sur :
  - **Cohésion** : les boids se dirigent vers le centre de leurs voisins.
  - **Séparation** : les boids évitent de se rapprocher trop les uns des autres.
  - **Alignement** : les boids s'alignent avec la direction moyenne de leurs voisins.
  - **Chasse/Fuite** : les prédateurs poursuivent les proies tandis que celles-ci tentent de fuir.
  - **Évitement d'obstacles** : les boids évitent les obstacles dans leur environnement.
- Affichage des trajets laissés par les boids pour visualiser leurs mouvements.
- Visualisation du champ de vision des boids avec un angle mort.
- Ajout d'obstacles de tailles variables

## Prérequis

- Java Development Kit (JDK) 8 ou supérieur
- Un environnement de développement intégré (IDE) tel qu'Eclipse, IntelliJ IDEA ou NetBeans.

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

## Installation

1. Clonez le dépôt GitHub :

   ```bash
   git clone https://github.com/swan-maillard/BandeOriganisee.git
   ```

2. Accédez au répertoire du projet :

   ```bash
   cd BandeOriganisee
   ```

3. Ouvrez le projet dans votre IDE préféré.

4. Compilez et exécutez le projet via la méthode main de la classe App. Assurez-vous que les bibliothèques nécessaires sont ajoutées à votre classpath.


## Utilisation

1. Lancez l'application. Une fenêtre s'ouvrira montrant la simulation des boids.
2. Vous pouvez créer différentes d'espèces de boids (max. 5 espèces) et modifier leurs nombres (max. 100 boids par espèce), leurs comportements (prédateur ou proie), et d'autres paramètres via l'interface utilisateur.
3. Vous pouvez rajouter des obstacles.


## Auteurs

- Swan Maillard (maillard.swan@gmail.com)

## Licence

Ce projet est sous licence MIT. Veuillez consulter le fichier `LICENSE` pour plus d'informations.
