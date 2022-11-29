package Inforeg.Algo;

import Inforeg.Draw.Draw;
import Inforeg.ObjetGraph.Node;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

/**
 *
 * @author inky19
 */
public class BFS extends Algorithm implements AlgorithmS {

        public BFS(){
            super(false);
            this.setName("BFS");
    }
    
    
    @Override
    public void process(Draw d, Node src) {
        int start = d.getG().getNodeId(src);
        LinkedList<Integer> stack = new LinkedList<Integer>();
        stack.add(start);
        LinkedList<String> order = new LinkedList<>();
        boolean[] visited = new boolean[d.getNodes().size()];
        ArrayList<LinkedList<Integer>> listAdj = d.getG().getListAdj();
        while (!stack.isEmpty()) {
            int current = stack.remove();
            if(!visited[current]){
                visited[current] = true;
                order.add(d.getNodes().get(current).getLabel());
                for (int i : listAdj.get(current)) {
                    if (!visited[i]){
                        stack.add(i);
                    }
                }
            }
        }
        String res = "Chemin du BFS : ";
        for (String s: order){
            res += s + " | ";
        }
        d.setResultat(res);
        d.algoFinished();
    }
    
    

    @Override
    public void process(Draw d) {
        if (d.isAuto()){
            process(d, d.getNodes().get(0));
        } else {
            d.setStatus(Draw.ALGO_INPUT);
        }
    }

}
