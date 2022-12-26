package Inforeg.Algo;

import Inforeg.Draw.Draw;
import Inforeg.Graph.Graph;
import Inforeg.ObjetGraph.Arc;
import Inforeg.ObjetGraph.Node;
import java.awt.Color;
import java.util.LinkedList;

/**
 * Algorithme de Ford-Fulkerson
 *
 * @author François MARIE
 * @author Rémi RAVELLI
 * @author Jorge QUISPE CCAMA
 */
public class FordFulkerson extends Algorithm implements AlgorithmST, Processing {

    public FordFulkerson() {
        super();
        this.setName("Ford-Fulkerson");
    }

    /* Returns true if there is a path from source 's' to
      sink 't' in residual graph. Also fills parent[] to
      store the path */
    private boolean bfs(int rGraph[][], int s, int t, int parent[], int V) {
        // Create a visited array and mark all vertices as
        // not visited
        boolean visited[] = new boolean[V];
        for (int i = 0; i < V; ++i) {
            visited[i] = false;
        }

        // Create a queue, enqueue source vertex and mark
        // source vertex as visited
        LinkedList<Integer> queue
                = new LinkedList<Integer>();
        queue.add(s);
        visited[s] = true;
        parent[s] = -1;

        // Standard BFS Loop
        while (queue.size() != 0) {
            int u = queue.poll();

            for (int v = 0; v < V; v++) {
                if (visited[v] == false
                        && rGraph[u][v] > 0) {
                    // If we find a connection to the sink
                    // node, then there is no point in BFS
                    // anymore We just have to set its parent
                    // and can return true
                    if (v == t) {
                        parent[v] = u;
                        return true;
                    }
                    queue.add(v);
                    parent[v] = u;
                    visited[v] = true;
                }
            }
        }

        // We didn't reach sink in BFS starting from source,
        // so return false
        return false;
    }

    // Returns tne maximum flow from s to t in the given
    // graph
    @Override
    public void process(Draw d) {
        d.setStatus(1);
    }

    @Override
    public void process(Draw d, Node srcNode, Node destNode) {

        int src = d.getG().getNodeId(srcNode);
        int dest = d.getG().getNodeId(destNode);
        Graph g = d.getG();
        g.updateVariable();
        int V = g.getNbsommets();

        int u, v, pred = 0;

        // Create a residual graph and fill the residual
        // graph with given capacities in the original graph
        // as residual capacities in residual graph
        // Residual graph where rGraph[i][j] indicates
        // residual capacity of edge from i to j (if there
        // is an edge. If rGraph[i][j] is 0, then there is
        // not)
        int rGraph[][] = new int[V][V];

        for (u = 0; u < V; u++) {
            for (v = 0; v < V; v++) {
                rGraph[u][v] = g.getAdjMatrix()[u][v];
            }
        }

        // This array is filled by BFS and to store path
        int parent[] = new int[V];

        int max_flow = 0; // There is no flow initially

        // Augment the flow while there is path from source
        // to sink
        while (bfs(rGraph, src, dest, parent, V)) {
            // Find minimum residual capacity of the edhes
            // along the path filled by BFS. Or we can say
            // find the maximum flow through the path found.
            int path_flow = Integer.MAX_VALUE;
            for (v = dest; v != src; v = parent[v]) {
                if (v != dest) {
                    d.stepBysStep.colorNode(d.getNode(u), Color.ORANGE, false);
                }
                pred = u;
                u = parent[v];
                path_flow
                        = Math.min(path_flow, rGraph[u][v]);
            }
            d.stepBysStep.nextStep();
            // update residual capacities of the edges and
            // reverse edges along the path
            for (v = dest; v != src; v = parent[v]) {
                if (v != dest) {
                    d.stepBysStep.colorNode(d.getNode(u), Color.BLUE, false);
                }
                pred = u;
                u = parent[v];
                rGraph[u][v] -= path_flow;
                rGraph[v][u] += path_flow;
            }
            d.stepBysStep.nextStep();
            // Add path flow to overall flow
            max_flow += path_flow;
        }
        Arc l;
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                l = d.findLine(i, j);
                if (l != null) {
                    l.setFlow(Integer.max(0, l.getPoids() - rGraph[i][j]));
                    if (rGraph[i][j] == 0) {
                        l.setColorDisplayed(Color.red);
                    }
                }
            }
        }
        d.setResultat("Le flot maximal du graphe entre les sommets " + d.getNodes().get(src).getLabel() + " et " + d.getNodes().get(dest).getLabel() + " est de " + max_flow + ".");
        d.algoFinished();
    }

}
