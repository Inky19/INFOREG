package Inforeg.Algo;

import Inforeg.Draw.Draw;
import Inforeg.Graph.Graph;
import Inforeg.ObjetGraph.Node;
import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Breadth first search algorithm
 *
 * @author François MARIE
 * @author Rémi RAVELLI
 */
public class BFS extends Algorithm implements AlgorithmS {

    public BFS() {
        super(false);
        this.setName("BFS");
    }

    @Override
    public void process(Draw d, Node src) {
        Graph g = d.getG();
        int start = d.getG().getNodeId(src);
        LinkedList<Integer> stack = new LinkedList<>();
        stack.add(start);
        LinkedList<String> order = new LinkedList<>();
        boolean[] visited = new boolean[d.getNodes().size()];
        ArrayList<LinkedList<Integer>> listAdj = d.getG().getListAdj();
        while (!stack.isEmpty()) {
            int current = stack.remove();
            if (!visited[current]) {
                // STEP
                d.stepBysStep.colorNode(g.getNode(current), Color.ORANGE, true);
                d.stepBysStep.setInfoText("Récupération du prochain noeud");
                d.stepBysStep.nextStep();
                d.stepBysStep.colorNode(g.getNode(current), Color.GRAY, true);
                //
                visited[current] = true;
                order.add(d.getNodes().get(current).getLabel());
                boolean neighbour = false;
                for (int i : listAdj.get(current)) {
                    if (!visited[i]) {
                        // STEP
                        neighbour = true;
                        d.stepBysStep.colorNode(g.getNode(i), Color.cyan, true);
                        //
                        stack.add(i);
                    }
                }
                if (neighbour) {
                    d.stepBysStep.setInfoText("Ajout des voisins à la file");
                    d.stepBysStep.nextStep();
                }
            }
        }
        d.stepBysStep.setInfoText("");
        d.stepBysStep.nextStep();
        String res = "Chemin du BFS : ";
        for (String s : order) {
            res += s + " | ";
        }
        d.setResultat(res);
        d.algoFinished();
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
