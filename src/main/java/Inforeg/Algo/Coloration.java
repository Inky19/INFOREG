/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Inforeg.Algo;

import Inforeg.Draw.Draw;
import Inforeg.Graph.Graph;
import Inforeg.Graph.GraphO;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author Rémi
 */
public class Coloration {
    private ArrayList<LinkedList<Integer>> listAdj;
    boolean oriente;
    Graph g;
    
    public Coloration(Draw d) {
        this.g = d.getG();
        this.oriente = (g instanceof GraphO);
    }
    
    private int minColorAvailable(LinkedList<Integer> colors) {
        return 0;
    }
    
    private void updateListAdj(int[][] matrix) {
        listAdj.clear();
        for (int i=0; i<matrix.length; i++) {
            listAdj.add(new LinkedList<Integer>());
            for (int j=0; j<matrix.length; j++) {
                if (matrix[i][j] > 0) {
                   listAdj.get(i).add(j);
                }
            }
        }
        
    }
    
    private int availableColor(HashSet<Integer> colors) {
        int minColor = 0;
        for (int e : colors) {
            if (!colors.contains(minColor))
                return minColor;
            minColor++;
        }
        return minColor;
    }
    
    
    
    private int[] colorationGloutonO() {
        int max = g.nbmax;
        int[][] adj = g.getAdj();
        int[] color = new int[max];
        updateListAdj(g.getAdj());
        HashSet<Integer> neighboursColors = new HashSet<>();
        for (int node = 0; node < max; node++) {
            neighboursColors.clear();
            for (Integer neighbour : listAdj.get(node)) {
                if (color[neighbour] >= 0) {
                    neighboursColors.add(color[neighbour]);
                }
                color[node] = availableColor(neighboursColors);
            }
        }
        return color;
    }
    
}
