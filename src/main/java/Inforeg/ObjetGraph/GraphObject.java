package Inforeg.ObjetGraph;

import Inforeg.Draw.Draw;
import java.awt.Graphics2D;

/**
 *
 * @author remir
 */
public interface GraphObject {

    /**
     * Dessine l'objet dans la fenêtre de dessin
     * @param d fenêtre de dessin. Utiliser les fonctions de passage au coordonnées de la fenêtre de dessin.
     * @param g graphique2D. Utiliser les fonctions draw.
     */
    public void paint(Draw d, Graphics2D g);
}
