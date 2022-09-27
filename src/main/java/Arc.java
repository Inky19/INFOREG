/*=============================================
Classe Objet Arc définissant un Arc orienté
Auteur : Béryl CASSEL
Date de création : 26/01/2022
Date de dernière modification : 08/03/2022
Commentaires Ajoutés
=============================================*/

public class Arc implements Comparable<Arc> {

    /**
     * Indice de la ligne correspondant dans la liste lines de Draw()
     */
    private int line;

    /**
     * Sommet source
     */
    private int src;

    /**
     * Sommet de destination
     */
    private int dest;

    /**
     * Poids de l'arc
     */
    private int poids;

    /**
     * Constructeur d'un Arc orienté
     * @param s = sommet source de l'Arc
     * @param d = sommet de destination de l'Arc
     * @param p = poids de l'Arc
     * @param ind = indice de la ligne correspondante
     */
    Arc(int s, int d, int p, int ind){
        this.src = s;
        this.dest = d;
        this.poids = p;
        this.line = ind;
    }

    /**
     * Getter de l'indice de la ligne représentant cet arc dans la
     * liste lines du Draw de notre interface
     * @return l'indice de l'arc dans la liste lines
     */
    public int getLine() {
		return line;
	}

	/**
     * Getter du sommet source
     * @return la valeur de l'attribut src
     */
    public int getSrc() {
        return src;
    }

    /**
     * Getter du poids de l'Arc
     * @return la valeur de l'attribut poids
     */
    public int getPoids() {
        return poids;
    }

    /**
     * Getter du sommet de destination de l'Arc
     * @return la valeur de l'attribut dest
     */
    public int getDest() {
        return dest;
    }
    
    /**
     * Setter du sommet source
     * @param src
     */
    public void setSrc(int src) {
        this.src = src;
    }

    /**
     * Setter du poids de l'Arc
     * @param poids
     */
    public void setPoids(int poids) {
        this.poids = poids;
    }

    /**
     * Setter du sommet de destination de l'Arc
     * @param dest
     */
    public void setDest(int dest) {
        this.dest = dest;
    }

    /**
     * Méthode permettant de comparer le poids de l'Arc
     * à celui d'un Arc passé en paramètre
     * @param compareEdge = Arc à comparer
     * @return la différence entre les deux poids des arcs
     */
    public int compareTo(Arc compareEdge){
        return this.poids - compareEdge.poids;
    }

    /**
     * Méthode permettant de générer une ligne de représentation
     * de l'arc dans la console
     * @return une ligne décrivant l'arc
     */
    public String toString(){
        return this.src + " -> " + this.dest + " : " + this.poids;
    }
    
}