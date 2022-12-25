package Inforeg.ObjetGraph;

/**
 *
 * @author remir
 */
public interface Clickable {

    /**
     * @param x : coordonnée globale
     * @param y : coordonnée globale
     * @return true si le point (x,y) se trouve dans la hitbox de l'objet.
     */
    public boolean contains(double x, double y);
}
