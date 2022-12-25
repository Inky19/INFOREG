package Inforeg.Algo;

import Inforeg.Draw.Draw;
import Inforeg.ObjetGraph.Node;

/**
 * Classe abstraitre pour les algorithmes nécessitant de choisir un nœud de
 * départ (source).
 *
 * @author François MARIE
 * @author Rémi RAVELLI
 */
public interface AlgorithmS {

    public void process(Draw d, Node src);
}
