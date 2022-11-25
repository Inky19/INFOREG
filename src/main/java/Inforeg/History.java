package Inforeg;

/*=============================================
Classe History permettant de sauvegarder 
les modifications et de les rétablir à l'aide 
des boutons
Auteur : Isaias VENEGAS
Date de création : 24/03/2022
Date de dernière modification : 30/03/2022
=============================================*/
import Inforeg.Draw.Draw;
import Inforeg.ObjetGraph.Arc;
import Inforeg.ObjetGraph.Nail;
import Inforeg.ObjetGraph.Node;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

class Enregistrement {
    int action; // ajouter/supprimer des noeud/arc, modifier l'étiquette
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
    protected ConcurrentHashMap<Integer, Enregistrement> previousStates;
    protected ConcurrentHashMap<Integer, Enregistrement> nextStates;

    public History() {
        this.previousStates = new ConcurrentHashMap<>();
        this.nextStates = new ConcurrentHashMap<>();
    }

    public void createLog(int action, Node circ) {
        this.addPreviousState(new Enregistrement(action, circ));
        this.clearNextStates();
    }

    public void createLog(int action, Nail clou) {
        this.addPreviousState(new Enregistrement(action, clou));
        this.clearNextStates();
    }
    
    public void createLog(int action, Arc line) {
        this.addPreviousState(new Enregistrement(action, line));
        this.clearNextStates();
    }
    
    public void createLog(int action, Node circ, String currentLbl, String newLbl) {
        this.addPreviousState(new Enregistrement(action, circ, currentLbl, newLbl));
        this.clearNextStates();
    }

    public void createLog(int action, Arc line, String currentLbl, String newLbl) {
        this.addPreviousState(new Enregistrement(action, line, currentLbl, newLbl));
        this.clearNextStates();
    }

    public void createLog(int action, Node circ, double x, double y, double x2, double y2) {
        this.addPreviousState(new Enregistrement(action, circ, x, y, x2, y2));
        this.clearNextStates();
    }

    public void reCreateLog(int action, Node circ, double x, double y, double x2, double y2) {
        this.addPreviousState(new Enregistrement(action, circ, x, y, x2, y2));
    }

    public void createLog(int action, Nail clou, double x, double y, double x2, double y2) {
        this.addPreviousState(new Enregistrement(action, clou, x, y, x2, y2));
        this.clearNextStates();
    }

    public Enregistrement getPreviousState() {
        Enregistrement pS = previousStates.get(previousStates.size() - 1);
        previousStates.remove(previousStates.size() - 1);
        return pS;
    }

    public Enregistrement getNextState() {
        Enregistrement pN = nextStates.get(nextStates.size() - 1);
        nextStates.remove(nextStates.size() - 1);
        return pN;
    }

    public Collection<Enregistrement> getPreviousStates() {
        return previousStates.values();
    }

    public Collection<Enregistrement> getNextStates() {
        return nextStates.values();
    }

    public boolean addPreviousState(Enregistrement log) {
        Enregistrement put = previousStates.put(previousStates.size(), log);
        return put != null;
    }

    public void clearNextStates() {
        nextStates.clear(); // On élimine les actions futures
    }

    public boolean addNextState(Enregistrement log) {
        Enregistrement put = nextStates.put(nextStates.size(), log);
        return put != null;
    }
    
    public void back(Draw d) {
        Collection<Enregistrement> pileZ = getPreviousStates();
        if (pileZ.isEmpty()) {
            return;
        }
        Enregistrement lastReg = getPreviousState();
        // Pour chaque action, on effectue l'action inverse
        // Ensuite, on déplace l'action sur l'autre pile
        switch (lastReg.action) {
            case History.ADD_NODE :
                d.getNodes().remove(lastReg.noeud);
                break;
            case History.MOVE_NODE :
                lastReg.noeud.setCx(lastReg.x);
                lastReg.noeud.setCy(lastReg.y);
                break;
            case History.REMOVE_NODE :
                d.getG().addNode(lastReg.noeud);
                break;
            case History.ADD_ARC :
                d.getG().removeLine(lastReg.arc);
                break;
            case History.MOVE_NAIL :
                lastReg.clou.setCx(lastReg.x);
                lastReg.clou.setCy(lastReg.y);
                break;
            case History.REMOVE_NAIL :
                // TO DO
                break;
            case History.ADD_NAIL :
                lastReg.clou.delete();
                break;
            case History.REMOVE_ARC :
                d.addLine(lastReg.arc);
                break;
            case History.LABEL_NODE :
                lastReg.noeud.setLabel(lastReg.lastLbl);
                break;
            case History.LABEL_ARC :
                lastReg.arc.setPoids(Integer.parseInt(lastReg.lastLbl));
                break;
            default:
                break;
        }
        d.repaint();
        addNextState(lastReg);
    }
    
    
    public void forward(Draw d) {
        Collection<Enregistrement> pileY = getNextStates();
            if (pileY.isEmpty()) {
                return;
            }
            Enregistrement nextReg = getNextState();
            // Pour chaque action, on l'exécute
            // Ensuite, on déplace l'action sur l'autre pile
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
                    // TO DO
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
            d.repaint();
            addPreviousState(nextReg);
    }
    
}
