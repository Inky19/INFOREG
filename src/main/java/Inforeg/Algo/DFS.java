package Inforeg.Algo;

import Inforeg.Draw.Draw;
import Inforeg.ObjetGraph.Node;
import java.awt.Color;
import java.util.LinkedList;

/**
 * Depth first search algorithm
 *
 * @author François MARIE
 * @author Rémi RAVELLI
 */
public class DFS extends Algorithm implements AlgorithmS {

    public DFS() {
        super(false);
        this.setName("DFS");
    }

    @Override
    public void process(Draw d, Node src) {
        int start = d.getG().getNodeId(src);
        LinkedList<String> order = new LinkedList<>();
        boolean[] visited = new boolean[d.getNodes().size()];
        dfsRecursive(start, visited, order, d);
        d.stepBysStep.nextStep();
        String res = "Chemin du DFS : ";
        for (String s : order) {
            res += s + " | ";
        }
        d.setResultat(res);
        d.algoFinished();
    }

    private void dfsRecursive(int current, boolean[] visited, LinkedList<String> order, Draw d) {
        visited[current] = true;
        int[][] adj = d.getG().getAdjMatrix();
        // STEP
        d.stepBysStep.colorNode(d.getG().getNode(current), Color.ORANGE, false);
        d.stepBysStep.nextStep();
        d.stepBysStep.colorNode(d.getG().getNode(current), Color.GRAY, false);
        //
        for (int i = 0; i < adj.length; i++) {
            if (adj[current][i] > 0) {
                if (!visited[i]) {
                    dfsRecursive(i, visited, order, d);
                }
            }
        }
        order.add(d.getNodes().get(current).getLabel());
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
