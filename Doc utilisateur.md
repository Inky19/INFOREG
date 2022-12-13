# Documentation utilisateur INFOREG <!-- omit from toc -->

## Sommaire <!-- omit from toc -->
---
- [Lancement d'inforeg](#lancement-dinforeg)
- [Fenêtre de démarrage](#fenêtre-de-démarrage)
- [Fenêtre de dessin](#fenêtre-de-dessin)
  - [Elements du graphe](#elements-du-graphe)
  - [Barre d'outils](#barre-doutils)
  - [Sauvegarde](#sauvegarde)
  - [Export](#export)
    - [Export png](#export-png)
    - [Export latex](#export-latex)
    - [Export matric d'adjacence](#export-matric-dadjacence)

## Lancement d'inforeg

Ce logiciel requiert Java 16 ou supérieur. Il est compatible avec linux, windows et mac.
Des différences graphiques peuvent cependant apparaîtres.

Double cliquer sur l'exécutable .jar. pour lancer inforeg. Une fenêtre de démarrage doit apparaître.


## Fenêtre de démarrage
---

## Fenêtre de dessin
---

### Elements du graphe


### Barre d'outils

| Icone   |      Outil      |  Description |
|:----------:|:--------------:|-------|
| ![](src/main/resources/asset/icons/move.png) |  Déplacement | Permet de se déplacer dans la fenêtre de dessin. Avec cet outil, aucun élément du graphe ne peut être modifié. |
| ![](src/main/resources/asset/icons/select.png) |    Sélection  |  Permet d'éditer un arc, un clou ou un noeud. Un maintien du clic sur une zone vide de la zone de dessin permet de faire une sélection multiple des eléments du graphe. |
| ![](src/main/resources/asset/icons/pin.png) | Pin |  Permet d'éditer et ajouter des clous à un arc. Un double clic sur un clou permet de le supprimer. |
| ![](src/main/resources/asset/icons/node.png) | Noeud | Ajoute un noeud en cliquant sur la zone de dessin sur une zone non occupée par un noeud. Un double clic sur un noeud permet de le supprimer  |
| ![](src/main/resources/asset/icons/arc.png) | Arc | Permet d'éditer les arc. Un premier clic permet de sélectionner le premier noeud (il s'affiche en couleur), un clic sur un second noeud permet de créer l'arc. Dans le cas d'un graphe orienté une fenêtre s'affiche, demandant le poids de l'arc. Un double clic sur l'arc permet de le supprimer.  |
| ![](src/main/resources/asset/icons/label.png) | Arc | Permet de modifier les labels du graphe. Un clic sur un noeud ou un arc fait apparaître une fenêtre. Renseigner le nouveau label/poids puis valider.  |
### Sauvegarde

### Export
Aller dans fichier puis exporter. Trois sous-menu s'affichent, 
- png
- latex 
- matrice d'adjacence
#### Export png
#### Export latex
#### Export matric d'adjacence