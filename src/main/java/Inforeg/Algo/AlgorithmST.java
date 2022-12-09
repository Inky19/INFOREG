package Inforeg.Algo;

import Inforeg.Draw.Draw;
import Inforeg.ObjetGraph.Node;

/**
  Algorithmes qui demandent un noeud de départ et d'arrivée
 * @author inky19
 */
public interface AlgorithmST {

    public void process(Draw d, Node src, Node dest);
}
