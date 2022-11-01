package Inforeg.Algo;

/*=============================================
Classe PrimMST définissant l'algorithme de PrimMST
Sous classe de la classe Processing
Auteur : Samy AMAL
Date de création : 04/02/2022
Date de dernière modification : 24/03/2022
=============================================*/
import Inforeg.ObjetGraph.Arc;
import Inforeg.Draw.Draw;
import Inforeg.Graph.Graph;
import static Inforeg.Graph.GraphFunction.connected;
import java.awt.Color;

import javax.swing.JOptionPane;

public class PrimMST implements Processing {

    // Function to construct and print MST for a graph represented
    // using adjacency matrix representation
    public void primMST(Draw d) {

        Arc[] arbre;

        Graph G = d.getG();
        G.updateVariable();
        arbre = new Arc[G.getNbsommets()];
        if (connected(G)) {
            // To represent set of vertices included in MST
            boolean vu[] = new boolean[G.getNbsommets()];

            // Initialize all keys as INFINITE
            for (int i = 0; i < G.getNbsommets(); i++) {
                arbre[i] = new Arc(-1, -1, Integer.MAX_VALUE, 0);
                vu[i] = false;
            }

            // Always include first 1st vertex in MST.
            arbre[0].setPoids(0); // Make key 0 so that this vertex is
            // picked as first vertex
            arbre[0].setSrc(-1); // First node is always root of MST

            // The MST will have V vertices
            for (int count = 0; count < G.getNbsommets() - 1; count++) {
                // Pick thd minimum key vertex from the set of vertices
                // not yet included in MST
                int u = findMin(listePoids(arbre), vu, G.getNbsommets());

                // Add the picked vertex to the MST Set
                vu[u] = true;

                // Update key value and parent index of the adjacent
                // vertices of the picked vertex. Consider only those
                // vertices which are not yet included in MST
                for (int v = 0; v < G.getNbsommets(); v++) // graph[u][v] is non zero only for adjacent vertices of m
                // mstSet[v] is false for vertices not yet included in MST
                // Update the key only if graph[u][v] is smaller than key[v]
                {
                    if (G.getAdj()[u][v] != 0 && vu[v] == false && G.getAdj()[u][v] < arbre[v].getPoids()) {
                        arbre[v] = new Arc(v, u, G.getAdj()[u][v], G.findArc(u, v));
                    }
                }
            }
            int p = 0;
            for (int i = 1; i < arbre.length; i++) {
                d.getG().getLines().get(arbre[i].getLine()).setColor(Color.RED);
                p += d.getLines().get(arbre[i].getLine()).getPoids();
            }
            JOptionPane.showMessageDialog(null, "L'arbre couvrant minimal du graphe a un poids de " + p + ".", "Prim MST", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Le graphe n'est pas connexe !", "Prim MST", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Méthode permettant d'obtenir la liste des poids de l'arbre
     *
     * @return liste des poids
     */
    public int[] listePoids(Arc[] arbre) {
        int poids[] = new int[arbre.length];
        for (int i = 0; i < arbre.length; i++) {
            poids[i] = arbre[i].getPoids();
        }
        return poids;
    }

}
