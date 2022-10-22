package Inforeg.Graph;

/*=============================================
Classe abstraite Graph définissant la structure 
générale d'un graphe
Auteur : Béryl CASSEL
Date de création : 27/01/2022
Date de dernière modification : 29/03/2022
=============================================*/
import Inforeg.ObjetGraph.Arc;
import Inforeg.Draw.Draw;
import Inforeg.ObjetGraph.MyLine;
import Inforeg.ObjetGraph.Node;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public abstract class Graph {

    /**
     * Nombre de Noeuds du Graph
     */
    protected int nbsommets;

    /**
     * Nombre maximal de sommets que peut contenir un graphe
     */
    public final static int nbmax = 1000;

    boolean oriente;
    // Structures de données de dessin
    private ArrayList<Node> nodes = new ArrayList<>();
    private ArrayList<MyLine> lines = new ArrayList<>();
    // Structure de données de traitement
    protected int[][] adj;
    protected ArrayList<Arc> lstArcs;
    // Passage de noeud à int
    private HashMap<Node, Integer> hashNode;

    public Graph(Draw d) {
        this.oriente = d.getOriente();
        this.lines = new ArrayList<>();
        this.nodes = new ArrayList<>();

        this.lstArcs = new ArrayList<Arc>();

        hashNode = new HashMap<>();
    }

    public void updateVariable() {
        //PROVISOIRE
        hashNode.clear();//
        lstArcs.clear();//
        int id = 0;//
        for (Node n : nodes) {//
            hashNode.put(n, id);//
            id++;//
        }//
        // FIN PROVISOIRE
        this.nbsommets = nodes.size();
        this.adj = new int[nbsommets][nbsommets];
        int i = 0;
        for (MyLine l : lines) {
            int p = l.getPoids();
            int src = hashNode.get(l.getFrom());
            int dest = hashNode.get(l.getTo());
            Arc a = new Arc(src, dest, p, i);
            //addArc(a);
            lstArcs.add(a);
            adj[src][dest] = p;
            if (!oriente) {
                adj[dest][src] = p;
            }
            i++;
        }
    }

    public ArrayList<Arc> getLstArcs() {
        return lstArcs;
    }

    public void setLstArcs(ArrayList<Arc> lstArcs) {
        this.lstArcs = lstArcs;
    }

    /**
     * Getter de la matrice d'adjacence du graphe
     *
     * @return un tableau nbmax*nbmax
     */
    public int[][] getAdj() {
        return adj;
    }

    public HashMap<Node, Integer> getHashNode() {
        return hashNode;
    }

    /**
     * Setter de la matrice d'adjacence du graphe
     *
     * @param adj tableau nbmax*nbmax
     */
    public void setAdj(int[][] adj) {
        this.adj = adj;
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
                mat += formatInt(adj[i][j], d) + "|";
            }
            mat += "\n";
        }
        return mat;
    }

    public int findMaxDecimal() {
        int max = 0;
        for (int i = 0; i < nbsommets; i++) {
            for (int j = 0; j < nbsommets; j++) {
                if (String.valueOf(adj[i][j]).length() > max) {
                    max = String.valueOf(adj[i][j]).length();
                }
            }
        }
        return max;
    }

    public String formatInt(int n, int d) {
        String aux = String.valueOf(n);
        int count = d - aux.length();
        while (count > 0) {
            aux = "_" + aux;
            --count;
        }
        return aux;
    }

    /**
     * Test si un Arc est bien présent dans le graphe
     *
     * @param a un Arc
     * @return un booléen (true si l'Arc est dans le graphe, false sinon)
     */
    public boolean estPresent(Arc a) {
        return this.lstArcs.contains(a);
    }
    
    public Node getNode(int value) {
        for (Entry<Node, Integer> entry : hashNode.entrySet()) {
            if (value ==  entry.getValue()) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    
    
    
    public MyLine findLine(int from, int to) {
        for (MyLine l : lines) {
            if ((hashNode.get(l.getFrom()) == from)&&(hashNode.get(l.getTo())== to)) {
                return l;
            }
        }
        return null;
    }
    /**
     * Méthode permettant d'ajouter un Arc passé en paramètre au Graph si cela
     * est possible
     *
     * @param a l'Arc à ajouter
     */
    public abstract void addArc(Arc a);

    public abstract int findArc(int src, int dest);

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<MyLine> getLines() {
        return lines;
    }

}
