package Inforeg.Algo;

/*=============================================
Classe KruskalMST
Auteur : Jorge QUISPE CCAMA
Date de création : 04/02/2022
Date de dernière modification : 25/03/2022
=============================================*/
import Inforeg.ObjetGraph.Arc;
import Inforeg.Draw.Draw;
import Inforeg.Graph.Graph;
import static Inforeg.Graph.GraphFunction.connected;
import java.awt.Color;
import java.util.Arrays;

import javax.swing.JOptionPane;

public class KruskalMST implements Processing {

    public void kruskalMST(Draw d) {
        
        Arc[] arbre;
        Graph G = d.getG();
        G.updateVariable();
        arbre = new Arc[G.getNbsommets()];
        if (connected(G)) {
            // Tnis will store the resultant MST
            //Edge result[] = new Edge[V];

            // An index variable, used for result[]
            int e = 0;

            // An index variable, used for sorted edges
            for (int i = 0; i < G.getNbsommets(); i++) {
                arbre[i] = new Arc(-1, -1, Integer.MAX_VALUE, -1);
            }
            // Step 1:  Sort all the edges in non-decreasing
            // order of their weight.  If we are not allowed to
            // change the given graph, we can create a copy of
            // array of edges

            Arc[] sortedArcs = new Arc[G.getLstArcs().size()];
            for (int j = 0; j < G.getLstArcs().size(); j++) {
                sortedArcs[j] = G.getLstArcs().get(j);
            }
            Arrays.sort(sortedArcs);
            // Allocate memory for creating V subsets
            subset subsets[] = new subset[G.getNbsommets()];
            for (int i = 0; i < G.getNbsommets(); ++i) {
                subsets[i] = new subset();
            }
            // Create V subsets with single elements
            for (int v = 0; v < G.getNbsommets(); ++v) {
                subsets[v].parent = v;
                subsets[v].rank = 0;
            }

            int i = 0; // Index used to pick next edge
            // Number of edges to be taken is equal to V-1
            while (e < G.getNbsommets() - 1) {
                // Step 2: Pick the smallest edge. And increment
                // the index for next iteration
                Arc next_edge = sortedArcs[i++];

                int x = find(subsets, next_edge.getSrc());
                int y = find(subsets, next_edge.getDest());

                // If including this edge does't cause cycle,
                // include it in result and increment the index
                // of result for next edge
                if (x != y) {
                    arbre[e++] = next_edge;
                    union(subsets, x, y);
                }
                // Else discard the next_edge
            }
            int p = 0;
            System.out.println(arbre.length);
            for (int j = 0; j < arbre.length; j++) {
                if (arbre[j].getLine() >= 0) {
                    d.getLines().get(arbre[j].getLine()).setColor(Color.RED);
                    p += d.getLines().get(arbre[j].getLine()).getPoids();
                }
            }
            JOptionPane.showMessageDialog(null, "L'arbre couvrant minimal du graphe a un poids de " + p + ".", "Kruskal MST", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Le graphe n'est pas connexe !", "Kruskal MST", JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
