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

public abstract class Graph {

    /**
     * Nombre de Noeuds du Graph
     */
    protected int nbsommets;

    /**
     * Nombre maximal de sommets que peut contenir un graphe 
     */
    public final static int nbmax = 1000;

    /**
     * Matrice d'adjacence
     */
    protected int[][] adj;

    private ArrayList<Node> nodes = new ArrayList<>();
    private ArrayList<MyLine> lines = new ArrayList<>(); 
    
    protected ArrayList<Arc> lstArcs;

    public Graph(Draw d){
        this.lines = new ArrayList<>();
        this.nodes = new ArrayList<>();
        
        this.nbsommets = nodes.size();
        this.adj = new int[nbsommets][nbsommets];
        this.lstArcs = new ArrayList<Arc>();
        for (int i=0;i<nbsommets;i++){
            for (int j=0;j<nbsommets;j++){
                this.adj[i][j] = 0;
            }
        }
    }
    
    public void updateArc() {
        for (int i=0;i<lines.size();i++){
            MyLine l = lines.get(i);
            int p = l.getPoids();
            //int src = nodes.get(0);
            //int dest = d.findEllipse(l.getToPoint().x,l.getToPoint().y);
            //Arc a = new Arc(src,dest,p,i);
            //addArc(a);
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
     * @return un tableau nbmax*nbmax
     */
    public int[][] getAdj() {
        return adj;
    }

    /**
     * Setter de la matrice d'adjacence du graphe
     * @param adj tableau nbmax*nbmax
     */
    public void setAdj(int[][] adj) {
        this.adj = adj;
    }

    /**
     * Getter du nombre de sommets du graphe
     * @return un entier
     */
    public int getNbsommets() {
        return nbsommets;
    }

    /**
     * Setter du nombre de sommets du graphe
     * @param nbsommets
     */
    public void setNbsommets(int nbsommets) {
        this.nbsommets = nbsommets;
    }

    /**
     * Méthode permettant de générer un String représentant
     * la matrice d'adjacence du graphe
     * @return un String représentant la matrice
     */
    public String afficher(){
        int d = findMaxDecimal();
        String mat = "";
        for (int i = 0;i<nbsommets;i++){
            mat += "|";
            for (int j = 0; j<nbsommets;j++){
                mat += formatInt(adj[i][j], d) + "|";
            }
            mat += "\n";
        }
        return mat;
    }

    public int findMaxDecimal(){
        int max = 0;
        for (int i=0;i<nbsommets;i++){
            for (int j=0;j<nbsommets;j++){
                if (String.valueOf(adj[i][j]).length()>max){
                    max = String.valueOf(adj[i][j]).length();
                }
            }
        }
        return max;
    }

    public String formatInt(int n, int d){
        String aux = String.valueOf(n);
        int count = d - aux.length();
        while (count>0){
            aux = "_" + aux;
            --count;
        }
        return aux;
    }

    /**
     * Test si un Arc est bien présent dans le graphe
     * @param a un Arc
     * @return un booléen 
     * (true si l'Arc est dans le graphe, false sinon)
     */
    public boolean estPresent(Arc a){
        return this.lstArcs.contains(a);
    }

    /**
     * Méthode permettant d'ajouter un Arc passé en 
 paramètre au Graph si cela est possible
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