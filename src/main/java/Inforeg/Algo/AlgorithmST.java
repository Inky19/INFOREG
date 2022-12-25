package Inforeg.Algo;

import Inforeg.Draw.Draw;
import Inforeg.ObjetGraph.Node;

/**
 * Classe abstraitre pour les algorithmes nécessitant de choisir un nœud de
 * départ et d'arrivée (source & target).
 *
 * @author François MARIE
 * @author Rémi RAVELLI
 */
public interface AlgorithmST {

    public void process(Draw d, Node src, Node dest);
}
