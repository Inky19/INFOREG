package Inforeg.Algo;

import Inforeg.Draw.Draw;
import Inforeg.Graph.Graph;
import Inforeg.ObjetGraph.Arc;
import Inforeg.ObjetGraph.Node;
import java.awt.Color;

/**
 * Algorithme de djikstra
 *
 * @author François MARIE
 * @author Rémi RAVELLI
 * @author Samy AMAL
 */
public class Dijkstra extends Algorithm implements AlgorithmST, Processing {

    public Dijkstra() {
        super();
        this.setName("Dijkstra");
    }

    @Override
    public void process(Draw d) {
        d.setStatus(Draw.ALGO_INPUT);
    }

    /**
     * Méthode appliquant l'algorithme de Dijkstra sur le graphe orienté
     * représenté par le Draw d afin de déterminer (si existence) le plus court
     * chemin entre les sommets src et dest
     *
     * @param d : Draw représentant le graphe à étudié
     * @param srcNode : sommet de départ du parcours
     * @param destNode : sommet de destination du parcours
     * @return true si il existe un chemin, false sinon
     */
    @Override
    public void process(Draw d, Node srcNode, Node destNode) {

        int src = d.getG().getNodeId(srcNode);
        int dest = d.getG().getNodeId(destNode);
        int[] dist;
        int[] predecesseur;

        Graph g = d.getG();
        g.updateVariable();

        dist = new int[g.getNbsommets()];
        // The output array. dist[i] will hold
        // the shortest distance from src to i

        predecesseur = new int[g.getNbsommets()];

        // vu[i] will true if vertex i is included in shortest
        // path tree or shortest distance from src to i is finalized
        boolean vu[] = new boolean[g.getNbsommets()];

        // Initialize all distances as INFINITE and stpSet[] as false
        for (int i = 0; i < g.getNbsommets(); i++) {
            dist[i] = Integer.MAX_VALUE;
            vu[i] = false;
        }

        // Distance of source vertex from itself is always 0
        dist[src] = 0;
        // Source has no predecesseur
        predecesseur[src] = -1;
        Node node;
        // Find shortest path for all vertices
        for (int count = 0; count < g.getNbsommets() - 1; count++) {
            // Pick the minimum distance vertex from the set of vertices
            // not yet processed. u is always equal to src in first
            // iteration.
            int u = findMin(dist, vu, g.getNbsommets());
            // ##### STEP #####
            node = d.getNode(u);
            d.stepBysStep.colorNode(node, Color.ORANGE, false);
            d.stepBysStep.setInfoText("Distance la plus petite du noeud " + node.getLabel() + " est " + dist[u]);
            d.stepBysStep.nextStep();
            // ################
            // Mark the picked vertex as processed
            vu[u] = true;

            // Update dist value of the adjacent vertices of the
            // picked vertex.
            for (int v = 0; v < g.getNbsommets(); v++) // Update dist[v] only if is not in sptSet, there is an
            // edge from u to v, and total weight of path from src to
            // v through u is smaller than current value of dist[v]
            {
                if (!vu[v] && g.getAdjMatrix()[u][v] != 0 && dist[u] != Integer.MAX_VALUE && dist[u] + g.getAdjMatrix()[u][v] < dist[v]) {
                    dist[v] = dist[u] + g.getAdjMatrix()[u][v];
                    // ##### STEP #####
                    node = d.getNode(v);
                    d.stepBysStep.colorNode(node, Color.GRAY, false);
                    d.stepBysStep.setInfoText("Mise a jour de " + node.getLabel() + " nouvelle distance " + dist[v]);
                    d.stepBysStep.nextStep();
                    // ################
                    predecesseur[v] = u;
                }
            }
        }
        int s = dest;
        int p = predecesseur[s];
        int count = 0;
        //index dist min and last dist
        while ((s != src) && (count < d.getNodes().size()) && (p != -1)) {
            System.out.println(src + " " + s + " " + p);
            Arc l = d.findLine(p, s);
            if (l != null) {
                l.setColorDisplayed(Color.RED);
                s = p;
                p = predecesseur[p];
                count++;
            } else {
                p = -1;
            }
        }
        if (s != src) {
            d.reinit();
            d.setResultat("Il n'existe pas de chemin entre les sommets "
                    + d.getNodes().get(src).getLabel() + " et " + d.getNodes().get(dest).getLabel() + ".");
        } else {
            d.repaint();
            d.algoFinished();
            d.setResultat("Il existe un plus court chemin entre les sommets "
                    + d.getNodes().get(src).getLabel() + " et " + d.getNodes().get(dest).getLabel()
                    + ", de distance " + dist[dest] + ".");
        }
    }
}
