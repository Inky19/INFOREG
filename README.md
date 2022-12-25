# INFOREG
<div align="center">
    <img src="src/main/resources/asset/logoINFOREG.png" width="20%"/>
</div>
Inforeg est un logiciel pédagogique pour la théorie des graphes.  

## Fonctionnalités

### Création de graphe  
Inforeg permet de représenter les nœuds et les arcs d'un graphe :  
- Chaque nœud dispose d'un label qui peut être modifié.
- La couleur de chaque nœud et arc peut être modifiée.
- Un arc peut être découpé en une ligne brisée en plaçant des points (appelés 'clous') pour faciliter la lecture.

### Traitement algorithmique

Une fois un graphe créé, il est possible de tester la connexité et plusieurs algorithmes classiques peuvent être appliqués :
- Prim
- Kruskal
- Ford-Fulkerson
- Dijkstra
- DFS
- BFS
- Coloration glouton

### Sauvegarde et export

Inforeg permet de sauvegarder un graphe au format `.inforeg` pour pouvoir le charger à nouveau plus tard ou en faire une copie.  
Il est également possible d'exporter dans les formats suivants :
- Image PNG (avec fond transparent)
- Code LaTeX avec TikZ

## Installation  
Inforeg est écrit en Java et se base sur Swing.  
Le logiciel a été développé sous Maven pour Java 16 ou supérieur.

## Utilisation  
La documentation utilisateur est disponible dans `doc` au format PDF et Markdown.
