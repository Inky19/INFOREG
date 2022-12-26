package Inforeg.Graph;

import Inforeg.Draw.Draw;
import Inforeg.ObjetGraph.Arc;
import Inforeg.ObjetGraph.Node;
import static java.lang.Integer.max;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

/**
 * Classe pour représenter un graphe
 *
 * @author Rémi RAVELLI
 * @author François MARIE
 * @author Béryl CASSEL
 */
public class Graph {

    /**
     * Nombre de Noeuds du Graphe
     */
    protected int nbsommets;
    boolean oriente;
    // Structures de données de dessin
    private ArrayList<Node> nodes = new ArrayList<>();
    private ArrayList<Arc> lines = new ArrayList<>();
    private int nextLabel;
    private int nextId;
    // Structure de données de traitement
    protected int[][] adjMatrix;
    private final ArrayList<LinkedList<Integer>> listAdj = new ArrayList<>();
    // Passage de noeud à int
    private final HashMap<Node, Integer> hashNode;
    //private final HashMap<MyLine, Integer> hashArc;

    public Graph(Draw d) {
        this.oriente = d.getOriente();
        this.lines = new ArrayList<>();
        this.nodes = new ArrayList<>();
        this.nextLabel = 0;
        this.nextId = 0;

        hashNode = new HashMap<>();
    }

    public void updateVariable() {
        hashNode.clear();
        listAdj.clear();
        int id = 0;
        for (Node n : nodes) {
            hashNode.put(n, id);
            listAdj.add(new LinkedList<Integer>());
            id++;
        }

        this.nbsommets = nodes.size();
        this.adjMatrix = new int[nbsommets][nbsommets];
        int i = 0;
        // Matrix ans
        for (Arc l : lines) {
            int p = l.getPoids();
            int src = hashNode.get(l.getFrom());
            int dest = hashNode.get(l.getTo());
            adjMatrix[src][dest] = p;
            listAdj.get(src).add(dest);
            if (!oriente) {
                adjMatrix[dest][src] = p;
                listAdj.get(dest).add(src);
            }
            i++;
        }
    }

    public void addNode(Node node) {
        nodes.add(node);
        if (node.getId() >= nextId) {
            nextId = node.getId() + 1;
        }
        nextLabel = getMinAvailableLabel();
    }

    public void addNode(double x, double y, double radius) {
        nextLabel = getMinAvailableLabel();
        nodes.add(new Node(x, y, radius, Integer.toString(nextLabel), nextId));
        nextId++;
    }

    public void removeNode(Node node) {
        try {
            int lbl = Integer.parseInt(node.getLabel());
            nextLabel = lbl;
        } catch (NumberFormatException e) {
        }
        ArrayList<Arc> linesCopy = new ArrayList<>(lines);
        for (Arc arc : linesCopy) {
            if (arc.getFrom() == node || arc.getTo() == node) {
                lines.remove(arc);

            }
        }
        nodes.remove(node);
    }

    public void removeLine(Arc arc) {
        lines.remove(arc);
    }

    public void addLine(Arc arc) {
        lines.add(arc);
    }

    public boolean lineExist(Arc arc) {
        Node from = arc.getFrom();
        Node to = arc.getTo();
        for (Arc line : lines) {
            if (oriente) {
                if (line.getFrom() == from && line.getTo() == to) {
                    return true;
                }
            } else {
                if ((line.getFrom() == from && line.getTo() == to) || line.getFrom() == to && line.getTo() == from) {
                    return true;
                }
            }
        }
        return false;
    }

    private int getMinAvailableLabel() {
        // max label
        int maxLabel = 0;
        for (Node node : nodes) {
            try {
                int lbl = Integer.parseInt(node.getLabel());
                maxLabel = max(maxLabel, lbl);
            } catch (NumberFormatException e) {
            }
        }
        boolean[] taken = new boolean[maxLabel + 1];
        for (int i = 0; i < taken.length; i++) {
            taken[i] = false;
        }
        for (Node node : nodes) {
            try {
                int lbl = Integer.parseInt(node.getLabel());
                taken[lbl] = true;
            } catch (NumberFormatException e) {
            }
        }
        int minLbl = 0;
        while (minLbl < taken.length && taken[minLbl]) {
            minLbl++;
        }
        return minLbl;

    }

    /**
     * Getter de la matrice d'adjacence du graphe
     *
     * @return un tableau nbmax*nbmax
     */
    public int[][] getAdjMatrix() {
        return adjMatrix;
    }

    public ArrayList<LinkedList<Integer>> getListAdj() {
        return listAdj;
    }

    public HashMap<Node, Integer> getHashNode() {
        return hashNode;
    }

    /**
     * Setter de la matrice d'adjacence du graphe
     *
     * @param adjMatrix tableau nbmax*nbmax
     */
    public void setAdjMatrix(int[][] adjMatrix) {
        this.adjMatrix = adjMatrix;
    }

    /**
     * Getter du nombre de sommets du graphe
     *
     * @return un entier
     */
    public int getNbsommets() {
        return nodes.size();
    }

    /**
     * Setter du nombre de sommets du graphe
     *
     * @param nbsommets
     */
    public void setNbsommets(int nbsommets) {
        this.nbsommets = nbsommets;
    }

    /**
     * Méthode permettant de générer un String représentant la matrice
     * d'adjacence du graphe
     *
     * @return un String représentant la matrice
     */
    public String afficher() {
        int d = findMaxDecimal();
        String mat = "";
        for (int i = 0; i < nbsommets; i++) {
            mat += "|";
            for (int j = 0; j < nbsommets; j++) {
                mat += formatInt(adjMatrix[i][j], d) + "|";
            }
            mat += "\n";
        }
        return mat;
    }

    public int findMaxDecimal() {
        int max = 0;
        for (int i = 0; i < nbsommets; i++) {
            for (int j = 0; j < nbsommets; j++) {
                if (String.valueOf(adjMatrix[i][j]).length() > max) {
                    max = String.valueOf(adjMatrix[i][j]).length();
                }
            }
        }
        return max;
    }

    public String formatInt(int n, int d) {
        String formated = String.valueOf(n);
        String sep = " ";
        int count = (d - formated.length());
        for (int i=0; i < count/2; i++) {
            formated = sep + formated + sep;
        }
        if (count%2==1) {
            formated += sep;
        }
        return formated;
    }

    public Node getNode(int value) {
        for (Entry<Node, Integer> entry : hashNode.entrySet()) {
            if (value == entry.getValue()) {
                return entry.getKey();
            }
        }
        return null;
    }

    public Arc findLine(int from, int to) {
        for (Arc l : lines) {
            if (((hashNode.get(l.getFrom()) == from) && (hashNode.get(l.getTo()) == to)) || (!oriente && (hashNode.get(l.getFrom()) == to) && (hashNode.get(l.getTo()) == from))) {
                return l;
            }
        }
        return null;
    }

    public Arc findLine(Node from, Node to) {
        for (Arc l : lines) {
            if ((l.getFrom() == from && l.getTo() == to) || (!oriente && l.getFrom() == from && l.getTo() == to)) {
                return l;
            }
        }
        return null;
    }

    public int getNodeId(Node n) {
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i) == n) {
                return i;
            }
        }
        return -1;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<Arc> getLines() {
        return lines;
    }

    public boolean isOriente() {
        return oriente;
    }

    public int getNextId() {
        return nextId;
    }

    public void setNextId(int nextId) {
        this.nextId = nextId;
    }

}
