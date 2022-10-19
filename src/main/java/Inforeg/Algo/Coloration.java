/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Inforeg.Algo;

import Inforeg.Draw.Draw;
import Inforeg.Graph.Graph;
import Inforeg.Graph.GraphO;
import java.util.LinkedList;

/**
 *
 * @author Rémi
 */
public class Coloration {
    boolean oriente;
    Graph g;
    
    public Coloration(Draw d) {
        this.g = d.getG();
        this.oriente = (g instanceof GraphO);
    }
    
    private int minColorAvailable(LinkedList<Integer> colors) {
        return 0;
    }
    
    
    private void colorationGloutonO() {
        int max = g.nbmax;
        int[][] adj = g.getAdj();
        boolean[] vu = new boolean[max];
        LinkedList<Integer> neighboursColors = new LinkedList<>();
        for (int i = 0; i < max; i++) {
            neighboursColors.clear();
            
        }
    }
    
}
