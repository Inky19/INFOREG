/*=============================================
Classe Transitions permettant de sauvegarder 
les modifications et de les rétablir à l'aide 
des boutons
Auteur : Isaias VENEGAS
Date de création : 24/03/2022
Date de dernière modification : 30/03/2022
=============================================*/
import java.awt.geom.Ellipse2D;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

class Enregistrement {
    String action; // ajouter/supprimer des noeud/arc, modifier l'étiquette
    Ellipse2D.Double noeud; // noeud
    double x; // position
    double y; // position
    MyLine arc; // arc crée/par supprimer
    Ellipse2D.Double noeud2; // noeud supplémentaire
    String lastLbl; // lbl/poid initial(e)
    String newLbl; // lbl/poid actualisé(e)
    double x2; // position supplémentaire
    double y2; // position supplémentaire
   
    // Constructor pour les actions sur les nœuds
    public Enregistrement(String action, Ellipse2D.Double circ) {
        this.action = action;
        this.noeud = circ;
        this.x = circ.x;
        this.y = circ.y;
    }
    
    // Constructor pour les actions sur les arcs
    public Enregistrement(String action, MyLine line) {
        this.action = action;
        this.arc = line;
        this.noeud = line.getFrom();
        this.noeud2 = line.getTo();
    }

    // Constructor pour les actions de modification des étiquettes
    public Enregistrement(String action, Ellipse2D.Double circ, String currentLbl, String newLbl) {
        this.action = action;
        this.lastLbl = currentLbl;
        this.newLbl = newLbl;
        this.noeud = circ;
    }
    
    // Constructor pour les actions de modification des poids
    public Enregistrement(String action, MyLine line, String currentLbl, String newLbl) {
        this.action = action;
        this.lastLbl = currentLbl;
        this.newLbl = newLbl;
        this.arc = line;
    }
    
    // Constructor pour les actions de mouvement
    public Enregistrement(String action, Ellipse2D.Double circ, double x, double y, double x2, double y2) {
        this.action = action;
        this.noeud = circ;
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
    }
    
    // Constructor pour les actions de mouvement
    public Enregistrement(String action, MyLine line, Ellipse2D.Double fromClou, Ellipse2D.Double toClou) {
        this.action = action;
        this.arc = line;
        this.noeud = fromClou;
        this.noeud2 = toClou;
    }
}
public class Transitions{
    // piles Ctrl+Z et Ctrl+Y
    protected ConcurrentHashMap<Integer, Enregistrement> previousStates;
    protected ConcurrentHashMap<Integer, Enregistrement> nextStates;

    public Transitions(){
       this.previousStates = new ConcurrentHashMap<>();
       this.nextStates = new ConcurrentHashMap<>();
    }
    
    public void createLog(String action, Ellipse2D.Double circ) {
        this.addPreviousState(new Enregistrement(action,circ));
        this.clearNextStates();
    }
    
    public void createLog(String action, MyLine line) {
        this.addPreviousState(new Enregistrement(action,line));
        this.clearNextStates();
    }
    
    public void createLog(String action, Ellipse2D.Double circ, String currentLbl, String newLbl) {
        this.addPreviousState(new Enregistrement(action,circ,currentLbl,newLbl));
        this.clearNextStates();
    }
    
    public void createLog(String action, MyLine line, String currentLbl, String newLbl) {
        this.addPreviousState(new Enregistrement(action,line,currentLbl,newLbl));
        this.clearNextStates();
    }
    
    public void createLog(String action, Ellipse2D.Double circ, double x, double y, double x2, double y2) {
        this.addPreviousState(new Enregistrement(action,circ,x,y,x2,y2));
        this.clearNextStates();
    }
    
    public void reCreateLog(String action, Ellipse2D.Double circ, double x, double y, double x2, double y2) {
        this.addPreviousState(new Enregistrement(action,circ,x,y,x2,y2));
    }
    
    public void createLog(String action, MyLine line, Ellipse2D.Double fromClou, Ellipse2D.Double toClou) {
        this.addPreviousState(new Enregistrement(action,line,fromClou,toClou));
        this.clearNextStates();
    }

    public Enregistrement getPreviousState(){
        Enregistrement pS = previousStates.get(previousStates.size()-1);
        previousStates.remove(previousStates.size()-1);
        return pS;
    }

    public Enregistrement getNextState(){
        Enregistrement pN = nextStates.get(nextStates.size()-1);
        nextStates.remove(nextStates.size()-1);
        return pN;
    }

    public Collection<Enregistrement> getPreviousStates(){
        return previousStates.values();
    }

    public Collection<Enregistrement> getNextStates(){
        return nextStates.values();
    }

    public boolean addPreviousState(Enregistrement log){
        Enregistrement put = previousStates.put(previousStates.size(), log);
        return put!=null;
    }

    public void clearNextStates(){
        nextStates.clear(); // On élimine les actions futures
    }

    public boolean addNextState(Enregistrement log){
        Enregistrement put = nextStates.put(nextStates.size(), log);
        return put!=null;
    }
}
