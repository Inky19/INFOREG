/*=============================================
Classe GOriente définissant un Graphe orienté
Sous classe de la classe abstraite Graphe
Auteur : Béryl CASSEL
Date de création : 27/01/2022
Date de dernière modification : 08/03/2022
=============================================*/

public class GOriente extends Graphe {

    public GOriente(Draw d){
        super(d);
    }
    
    @Override
    /**
     * Méthode d'ajout d'un arc orienté dans le graphe
     * Avec sauvegarde du graphe initial
     * @param a = Arc à ajouter
     */
    public void addArc(Arc a){
        int s = a.getSrc();
        int d = a.getDest();
        if ((s<this.nbsommets) && (d<this.nbsommets)){
            if (this.adj[s][d] == 0){
                /*Si l'arc n'existe pas, on copie l'ancienne version
                du graphe et on modifie la matrice d'adjacence*/
                this.adj[s][d] = a.getPoids();
                this.lstArcs.add(a);
            } else {
                System.out.println("Il existe déjà un arc entre ces deux sommets");
            }
        } else {
            System.out.println("Impossible d'ajouter cet arc");
        }
    }

    @Override
    public int findArc(int src, int dest){
        boolean trouve = false;
        int n = 0;
        while ((n<this.lstArcs.size()) && (!trouve)){
            int s = this.lstArcs.get(n).getSrc();
            int d = this.lstArcs.get(n).getDest();
            if ((src == s) && (dest == d)){
                trouve = true;
            } else {
                n++;
            }
        }
        if (trouve){
            return n;
        } else {
            return -1;
        }
    }

}
