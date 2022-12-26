package Inforeg.Algo;

import Inforeg.Draw.Draw;
import Inforeg.Graph.Graph;
import static Inforeg.Graph.GraphFunction.connected;
import Inforeg.ObjetGraph.Arc;
import java.awt.Color;
import java.util.Arrays;

import javax.swing.JOptionPane;

/**
 * Algorithme de Kruskal
 *
 * @author François MARIE
 * @author Rémi RAVELLI
 * @author Jorge QUISPE CCAMA
 */
public class KruskalMST extends Algorithm implements Processing {

    public KruskalMST() {
        this.setName("Kruskal");
    }

    @Override
    public void process(Draw d) {

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
                arbre[i] = new Arc(null, null, Integer.MAX_VALUE, Color.BLUE);
            }
            // Step 1:  Sort all the edges in non-decreasing
            // order of their weight.  If we are not allowed to
            // change the given graph, we can create a copy of
            // array of edges

            Arc[] sortedArcs = new Arc[G.getLines().size()];
            for (int j = 0; j < G.getLines().size(); j++) {
                sortedArcs[j] = G.getLines().get(j);
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

                int x = find(subsets, G.getNodeId(next_edge.getFrom()));
                int y = find(subsets, G.getNodeId(next_edge.getTo()));

                // If including this edge does't cause cycle,
                // include it in result and increment the index
                // of result for next edge
                if (x != y) {
                    // 
                    d.stepBysStep.colorArc(G.findLine(next_edge.getFrom(), next_edge.getTo()), Color.YELLOW);
                    d.stepBysStep.setInfoText("Prochain arc de poids minimal");
                    d.stepBysStep.nextStep();
                    d.stepBysStep.colorArc(G.findLine(next_edge.getFrom(), next_edge.getTo()), Color.GREEN);
                    //
                    arbre[e++] = next_edge;
                    union(subsets, x, y);
                } else {
                    d.stepBysStep.colorArc(G.findLine(next_edge.getFrom(), next_edge.getTo()), Color.YELLOW);
                    d.stepBysStep.setInfoText("Prochain arc de poids minimal : création d'un cycle");
                    d.stepBysStep.nextStep();
                    d.stepBysStep.colorArc(G.findLine(next_edge.getFrom(), next_edge.getTo()), Color.GRAY);
                }
                // Else discard the next_edge
            }
            d.stepBysStep.setInfoText("");
            d.stepBysStep.nextStep();
            int p = 0;
            System.out.println(arbre.length);
            Arc a = null;
            Arc arc = null;
            for (int j = 0; j < arbre.length; j++) {
                a = arbre[j];
                if (a != null) {
                    arc = G.findLine(a.getFrom(), a.getTo());
                    if (arc == null && !G.isOriente()) {
                        arc = G.findLine(a.getTo(), a.getFrom());
                    }
                    if (arc != null) {
                        arc.setColorDisplayed(Color.RED);
                        p += arc.getPoids();
                    }

                }
            }
            d.setResultat("L'arbre couvrant minimal du graphe a un poids de " + p + ".");
            d.algoFinished();
        } else {
            JOptionPane.showMessageDialog(null, "Le graphe n'est pas connexe !", "Kruskal MST", JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
