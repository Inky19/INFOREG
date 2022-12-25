package Inforeg.Algo;

import Inforeg.Draw.Draw;
import Inforeg.Graph.Graph;
import static Inforeg.Graph.GraphFunction.connected;
import Inforeg.ObjetGraph.Arc;
import Inforeg.ObjetGraph.Node;
import java.awt.Color;
import javax.swing.JOptionPane;

/**
 * Algorithme de Prim
 *
 * @author François MARIE
 * @author Rémi RAVELLI
 * @author Samy AMAL
 */
public class PrimMST extends Algorithm implements AlgorithmS, Processing {

    public PrimMST() {
        super(false);
        this.setName("Prim");
    }

    // Function to construct and print MST for a graph represented
    // using adjacency matrix representation
    @Override
    public void process(Draw d, Node src) {

        Graph G = d.getG();

        int start = d.getG().getNodeId(src);

        Arc[] arbre;
        G.updateVariable();
        arbre = new Arc[G.getNbsommets()];
        Arc a = null;
        if (connected(G)) {
            // To represent set of vertices included in MST
            boolean vu[] = new boolean[G.getNbsommets()];

            // Initialize all keys as INFINITE
            for (int i = 0; i < G.getNbsommets(); i++) {
                arbre[i] = new Arc(null, null, Integer.MAX_VALUE, Color.BLUE);
                vu[i] = false;
            }
            // Always include first 1st vertex in MST.
            arbre[start].setPoids(0); // Make key 0 so that this vertex is
            // picked as first vertex
            arbre[start].setFrom(null); // First node is always root of MST

            // The MST will have V vertices
            for (int count = 0; count < G.getNbsommets() - 1; count++) {
                // Pick thd minimum key vertex from the set of vertices
                // not yet included in MST
                int u = findMin(listePoids(arbre), vu, G.getNbsommets());
                // Add the picked vertex to the MST Set
                vu[u] = true;
                //#### STEP ####
                a = G.findLine(arbre[u].getFrom(), arbre[u].getTo());
                if (a == null && !G.isOriente()) {
                    a = G.findLine(arbre[u].getTo(), arbre[u].getFrom());
                }
                if (a != null) {
                    d.stepBysStep.colorArc(a, Color.GREEN);
                    d.stepBysStep.setInfoText("Arc de poids minimal suivant " + arbre[u].getPoids());
                    d.stepBysStep.nextStep();
                }
                //############## 

                // Update key value and parent index of the adjacent
                // vertices of the picked vertex. Consider only those
                // vertices which are not yet included in MST
                for (int v = 0; v < G.getNbsommets(); v++) // graph[u][v] is non zero only for adjacent vertices of m
                // mstSet[v] is false for vertices not yet included in MST
                // Update the key only if graph[u][v] is smaller than key[v]
                {
                    if (G.getAdjMatrix()[u][v] != 0 && vu[v] == false && G.getAdjMatrix()[u][v] < arbre[v].getPoids()) {
                        arbre[v] = new Arc(G.getNode(v), G.getNode(u), G.getAdjMatrix()[u][v], Color.BLUE);
                    }
                }
            }
            int p = 0;

            for (int i = 0; i < arbre.length; i++) {
                if (i != start) {
                    a = G.findLine(arbre[i].getFrom(), arbre[i].getTo());
                    if (a == null && !G.isOriente()) {
                        a = G.findLine(arbre[i].getTo(), arbre[i].getFrom());
                    }
                    if (a != null) {
                        a.setColorDisplayed(Color.RED);
                        p += a.getPoids();
                    }
                }
            }
            d.setResultat("L'arbre couvrant minimal du graphe a un poids de " + p + ".");
            d.algoFinished();
            //JOptionPane.showMessageDialog(null, "L'arbre couvrant minimal du graphe a un poids de " + p + ".", "Prim MST", JOptionPane.INFORMATION_MESSAGE);
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

    @Override
    public void process(Draw d) {
        if (d.isAuto()) {
            process(d, d.getNodes().get(0));
        } else {
            d.setStatus(Draw.ALGO_INPUT);
        }
    }

}
