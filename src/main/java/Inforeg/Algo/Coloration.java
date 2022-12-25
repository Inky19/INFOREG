package Inforeg.Algo;

import Inforeg.Draw.Draw;
import Inforeg.ObjetGraph.Node;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Coloration gloutonne de graphe.
 *
 * @author Rémi RAVELLI
 * @author François MARIE
 *
 */
public class Coloration extends Algorithm {

    private ArrayList<LinkedList<Integer>> listAdj;
    private HashMap<Node, Integer> hashNode;
    private Draw d;
    private static final String[] COLORS = new String[]{"#e6194B", "#f58231", "#ffe119", "#bfef45", "#3cb44b", "#42d4f4", "#4363d8", "#911eb4", "#f032e6", "#800000"};

    public Coloration() {
        this.setName("Glouton");
        hashNode = new HashMap<>();
        listAdj = new ArrayList<>();
        d = null;
    }

    @Override
    public void process(Draw d) {
        this.d = d;
        hashNode = new HashMap<>();
        listAdj = new ArrayList<>();
        colorationGlouton();
    }

    private int availableColor(HashSet<Integer> colors) {
        int minColor = 0;
        for (int e : colors) {
            if (!colors.contains(minColor)) {
                return minColor;
            }
            minColor++;
        }
        return minColor;
    }

    /**
     * Effectue la coloration d'un graphe avec un algorithme glouton Attention,
     * rien ne garantie que cette coloration est optimale.
     */
    public void colorationGlouton() {
        hashNode = d.getG().getHashNode();
        listAdj = d.getG().getListAdj();
        int max = listAdj.size();
        // Tableau des couleurs à l'issue de la coloration
        int[] color = new int[max];
        for (int i = 0; i < max; i++) {
            color[i] = -1;
        }
        // Ensemble des couleurs des voisins d'un noeud
        HashSet<Integer> neighboursColors = new HashSet<>();
        for (int node = 0; node < max; node++) {
            neighboursColors.clear();
            // Mise à jours des couleurs des voisins de node
            for (Integer neighbour : listAdj.get(node)) {
                if (color[neighbour] >= 0) {
                    neighboursColors.add(color[neighbour]);
                }
            }
            // Recherche de la plus petite couleur disponible
            color[node] = availableColor(neighboursColors);
        }
        d.algoFinished();
        // Coloration effective du graphe

        for (HashMap.Entry<Node, Integer> m : hashNode.entrySet()) {
            int id = m.getValue();
            Color newColor = Color.decode(COLORS[color[id] % COLORS.length]); // % pour éviter un out of bounds -> à mieux gérer
            m.getKey().setColorDisplayed(newColor);
        }
    }

}
