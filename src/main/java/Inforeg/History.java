package Inforeg;

import Inforeg.Draw.Draw;
import Inforeg.ObjetGraph.Arc;
import Inforeg.ObjetGraph.Nail;
import Inforeg.ObjetGraph.Node;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Classe History permettant de sauvegarder les modifications et de les rétablir
 * à l'aide des boutons
 *
 * @author Rémi RAVELLI
 * @author François MARIE
 * @author Isaias VENEGAS
 */
class Enregistrement {

    int action; // ajouter/supprimer des noeud/arc, modifier l'étiquette
    int index; 
    Node noeud; // noeud
    double x; // position
    double y; // position
    Nail clou;
    Arc arc; // arc crée/par supprimer
    String lastLbl; // lbl/poid initial(e)
    String newLbl; // lbl/poid actualisé(e)
    double x2; // position supplémentaire
    double y2; // position supplémentaire

    // Constructor pour les actions sur les nœuds
    public Enregistrement(int action, Node circ) {
        this.action = action;
        this.noeud = circ;
        this.x = circ.getCx();
        this.y = circ.getCy();
    }

    // Constructor pour les actions sur les arcs
    public Enregistrement(int action, Arc line) {
        this.action = action;
        this.arc = line;
    }

    // Constructeur pour les actions sur les clous
    public Enregistrement(int action, Nail clou) {
        this.action = action;
        this.clou = clou;
    }

    // Constructor pour les actions de modification des étiquettes
    public Enregistrement(int action, Node circ, String currentLbl, String newLbl) {
        this.action = action;
        this.lastLbl = currentLbl;
        this.newLbl = newLbl;
        this.noeud = circ;
    }

    // Constructor pour les actions de modification des poids
    public Enregistrement(int action, Arc line, String currentLbl, String newLbl) {
        this.action = action;
        this.lastLbl = currentLbl;
        this.newLbl = newLbl;
        this.arc = line;
    }
    
    public Enregistrement(int action, Nail clou, Arc arc, int index) {
        this.arc = arc;
        this.clou = clou;
        this.action = action;
        this.index = index;
    }

    // Constructor pour les actions de mouvement
    public Enregistrement(int action, Node circ, double x, double y, double x2, double y2) {
        this.action = action;
        this.noeud = circ;
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
    }

    // Constructor pour les actions de mouvement
    public Enregistrement(int action, Nail clou, double x, double y, double x2, double y2) {
        this.action = action;
        this.clou = clou;
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
    }
}

public class History {

    // actions possibles
    public static final int MOVE_NAIL = 0;
    public static final int MOVE_NODE = 1;
    public static final int ADD_ARC = 2;
    public static final int ADD_NODE = 3;
    public static final int REMOVE_ARC = 4;
    public static final int REMOVE_NODE = 5;
    public static final int LABEL_ARC = 6;
    public static final int LABEL_NODE = 7;
    public static final int REMOVE_NAIL = 8;
    public static final int ADD_NAIL = 9;

    // piles Ctrl+Z et Ctrl+Y
    private final ConcurrentHashMap<Integer, LinkedList<Enregistrement>> previousStates;
    private final ConcurrentHashMap<Integer, LinkedList<Enregistrement>> nextStates;
    private LinkedList<Enregistrement> currentLog;

    public History() {
        this.previousStates = new ConcurrentHashMap<>();
        this.nextStates = new ConcurrentHashMap<>();
        this.currentLog = new LinkedList<>();
    }

    /**
     * Crée un enregistrement pour les actions de suppression d'un Noeud.
     *
     * @param action
     * @param node
     */
    public void createLog(int action, Node node) {
        this.currentLog.add(new Enregistrement(action, node));
    }

    public boolean pileZempty() {
        return previousStates.isEmpty();
    }

    public boolean pileYempty() {
        return nextStates.isEmpty();
    }

    /**
     * Crée un enregistrement pour les actions de suppression d'un Clou.
     *
     * @param action
     * @param clou
     */
    public void createLog(int action, Nail clou) {
        this.currentLog.add(new Enregistrement(action, clou));
    }

    /**
     * Crée un enregistrement pour les actions de suppression d'un arc.
     *
     * @param action
     * @param line
     */
    public void createLog(int action, Arc line) {
        this.currentLog.add(new Enregistrement(action, line));
    }

    /**
     * Crée un enregistrement pour les actions de renommage d'un noeud.
     *
     * @param action
     * @param circ
     * @param currentLbl
     * @param newLbl
     */
    public void createLog(int action, Node circ, String currentLbl, String newLbl) {
        this.currentLog.add(new Enregistrement(action, circ, currentLbl, newLbl));
    }

    /**
     * Crée un enregistrement pour les actions de renommage d'un arc.
     *
     * @param action
     * @param line
     * @param currentLbl
     * @param newLbl
     */
    public void createLog(int action, Arc line, String currentLbl, String newLbl) {
        this.currentLog.add(new Enregistrement(action, line, currentLbl, newLbl));
    }

    /**
     * Crée un enregistrement pour les actions de déplacement d'un noeud.
     *
     * @param action
     * @param circ
     * @param x
     * @param y
     * @param x2
     * @param y2
     */
    public void createLog(int action, Node circ, double x, double y, double x2, double y2) {
        this.currentLog.add(new Enregistrement(action, circ, x, y, x2, y2));
    }

    /**
     * Crée un enregistrement pour les actions de déplacement d'un clou.
     *
     * @param action
     * @param clou
     * @param x
     * @param y
     * @param x2
     * @param y2
     */
    public void createLog(int action, Nail clou, double x, double y, double x2, double y2) {
        this.currentLog.add(new Enregistrement(action, clou, x, y, x2, y2));
    }
    
    public void createLog(int action, Nail clou, Arc arc, int index) {
        this.currentLog.add(new Enregistrement(action, clou, arc,index));
    }
    
    /**
     * Ajoute les actions créés sur la pile Y
     */
    public void push() {
        if (!currentLog.isEmpty()) {
            this.addPreviousState(currentLog);
            this.clearNextStates();
            this.currentLog = new LinkedList<>();
        }
    }

    /**
     * Efface les actions créées.
     */
    public void clear() {
        this.currentLog = new LinkedList<>();
    }

    private LinkedList<Enregistrement> getPreviousState() {
        LinkedList<Enregistrement> pS = previousStates.get(previousStates.size() - 1);
        previousStates.remove(previousStates.size() - 1);
        return pS;
    }

    private LinkedList<Enregistrement> getNextState() {
        LinkedList<Enregistrement> pN = nextStates.get(nextStates.size() - 1);
        nextStates.remove(nextStates.size() - 1);
        return pN;
    }

    private Collection<LinkedList<Enregistrement>> getPreviousStates() {
        return previousStates.values();
    }

    private Collection<LinkedList<Enregistrement>> getNextStates() {
        return nextStates.values();
    }

    private boolean addPreviousState(LinkedList<Enregistrement> log) {
        LinkedList<Enregistrement> put = previousStates.put(previousStates.size(), log);
        return put != null;
    }

    private void clearNextStates() {
        nextStates.clear(); // On élimine les actions futures
    }

    private boolean addNextState(LinkedList<Enregistrement> log) {
        LinkedList<Enregistrement> put = nextStates.put(nextStates.size(), log);
        return put != null;
    }

    /**
     * Retour en arrière (CTRL+Z)
     *
     * @param d
     */
    public void back(Draw d) {
        Collection<LinkedList<Enregistrement>> pileZ = getPreviousStates();
        if (pileZ.isEmpty()) {
            return;
        }
        LinkedList<Enregistrement> lastRegs = getPreviousState();
        // Pour chaque action, on effectue l'action inverse
        // Ensuite, on déplace l'action sur l'autre pile
        for (int i= lastRegs.size()-1; 0 <= i;  i--) {
            Enregistrement lastReg = lastRegs.get(i);
            switch (lastReg.action) {
                case History.ADD_NODE:
                    d.getNodes().remove(lastReg.noeud);
                    break;
                case History.MOVE_NODE:
                    lastReg.noeud.setCx(lastReg.x);
                    lastReg.noeud.setCy(lastReg.y);
                    break;
                case History.REMOVE_NODE:
                    d.getG().addNode(lastReg.noeud);
                    break;
                case History.ADD_ARC:
                    d.getG().removeLine(lastReg.arc);
                    break;
                case History.MOVE_NAIL:
                    lastReg.clou.setCx(lastReg.x);
                    lastReg.clou.setCy(lastReg.y);
                    break;
                case History.REMOVE_NAIL :
                    lastReg.arc.addNail(lastReg.clou, lastReg.index);
                    break;
                case History.ADD_NAIL:
                    lastReg.clou.delete();
                    break;
                case History.REMOVE_ARC:
                    d.addLine(lastReg.arc);
                    break;
                case History.LABEL_NODE:
                    lastReg.noeud.setLabel(lastReg.lastLbl);
                    break;
                case History.LABEL_ARC:
                    lastReg.arc.setPoids(Integer.parseInt(lastReg.lastLbl));
                    break;
                default:
                    break;
            }
        }
        d.repaint();
        addNextState(lastRegs);
    }

    /**
     * Annule le retour en arrière (CTRL+Y)
     *
     * @param d La zone de dessin
     */
    public void forward(Draw d) {
        Collection<LinkedList<Enregistrement>> pileY = getNextStates();
        if (pileY.isEmpty()) {
            return;
        }
        LinkedList<Enregistrement> nextRegs = getNextState();
        // Pour chaque action, on l'exécute
        // Ensuite, on déplace l'action sur l'autre pile
        for (Enregistrement nextReg : nextRegs) {
            switch (nextReg.action) {
                case History.ADD_NODE :
                    d.getG().addNode(nextReg.noeud);
                    break;
                case History.MOVE_NODE :
                    nextReg.noeud.setCx(nextReg.x2);
                    nextReg.noeud.setCy(nextReg.y2);
                    break;
                case History.REMOVE_NODE :
                    d.getG().getNodes().remove(nextReg.noeud);
                    break;
                case History.ADD_ARC :
                    d.addLine(nextReg.arc);
                    break;
                case History.MOVE_NAIL :
                    nextReg.clou.setCx(nextReg.x2);
                    nextReg.clou.setCy(nextReg.y2);
                    break;
                case History.REMOVE_NAIL :
                    nextReg.clou.delete();
                    break;
                case History.ADD_NAIL :
                    nextReg.arc.addNail(nextReg.clou, nextReg.index);
                    break;
                case History.REMOVE_ARC :
                    d.getG().removeLine(nextReg.arc);
                    break;
                case History.LABEL_NODE :
                    nextReg.noeud.setLabel(nextReg.newLbl);
                    break;
                case History.LABEL_ARC :
                    nextReg.arc.setPoids(Integer.parseInt(nextReg.newLbl));
                    break;
                default:
                    break;
            }
        }
        d.repaint();
        addPreviousState(nextRegs);
    }

}
