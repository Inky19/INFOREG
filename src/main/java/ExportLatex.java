/*=============================================
Classe Draw permettant d'exporter les graphes en source LaTeX
Auteur : Samy AMAL
Date de création : 18/2022
Date de dernière modification : 30/03/2022
=============================================*/

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class ExportLatex {
    
    /** JFrame permettant d'afficher le source LaTeX */
    protected JFrame frame;
    /** JTabbedPane contenant les différents éléments */
    protected JTabbedPane tabbedPane;
    /**JPanel contenant le source Latex d'export */
    protected JPanel panelExport;
    /** JPanel contenant les boutons modifiant l'épaisseur des arcs */
    protected JPanel panelThicknessArc;
    /** JPanel contenant les boutons modifiant la couleur des arcs */
    protected JPanel panelColorArc;
    
    /** Couleur de remplissage des noeuds */
    private String noeudFill ;
    /** Epaisseur des arcs */
    private String styleLine ;
    /** Tous les arcs en noir */
    private boolean allBlack;
    /** Compteurs de couleurs d'arcs différentes*/
    private ArrayList<Color> arcColors;

    
    /**
     * Constructeur de la classe
     */
    public ExportLatex(){
        noeudFill = "gray!50";
        styleLine = "very thick";
        allBlack = false;
        arcColors = new ArrayList<>();
    }

    /**
     * Méthode permettant de générer le Frame 
     * @param d Draw
     */
    public void frameLatex(Draw d){      
        frame = new JFrame("Export Latex");
        //fermer la fenêtre quand on quitte
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        tabbedPane = new JTabbedPane();
        
        initPanelThicknessArc(d);
        initPanelCouleurArc(d);
        initPanelCouleurNoeud(d);
        //choix d'ajouter les clous
        initPanelClous(d);
        
        //rename Export
        initPanelExport(d);
        frame.add(tabbedPane);
        frame.pack();
        frame.setVisible(true);
        //frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    } 
   
    /**
     * Méthode permettant de créer le JPanel contenant les boutons permettant de 
     * modifier l'épaisseur des arcs
     * @param d Draw
     */
    public void initPanelThicknessArc(Draw d){
        
        panelThicknessArc = new JPanel();
        panelThicknessArc.setBounds(0,0,100,20);
        panelThicknessArc.setBorder (new TitledBorder(new EtchedBorder(),"Epaisseur des arcs"));
        JTextArea displayThicknessArc = new JTextArea(12, 30);
        displayThicknessArc.setSize(new Dimension(12,30));
        displayThicknessArc.setText(
                        "Veuillez sélectionner l'épaisseur des arcs.\n"+
                        "\n"+        
                        "  - [ultra thin] = 0.1 pt"+
                        "\n"+
                        "  - [very thin] = 0.2 pt"+
                        "\n"+
                        "  - [thin] = 0.4 pt"+
                        "\n"+
                        "  - [semithick] = 0.6 pt"+
                        "\n"+
                        "  - [thick] = 0.8 pt"+
                        "\n"+
                        "  - [very thick] = 1.2 pt"+
                        "\n"+
                        "  - [ultra thick] = 1.6 pt"
        );
        displayThicknessArc.setEditable (false);
        displayThicknessArc.setFocusable(false);
        
        //Thickness des arcs
        JRadioButton styleArc1 = new JRadioButton("ultra thick");
        JRadioButton styleArc2 = new JRadioButton("very thick");
        JRadioButton styleArc3 = new JRadioButton("thick");
        JRadioButton styleArc4 = new JRadioButton("semithick");
        JRadioButton styleArc5 = new JRadioButton("thin");
        JRadioButton styleArc6 = new JRadioButton("very thin");
        JRadioButton styleArc7 = new JRadioButton("ultra thin");
        
        //ButtonGroup
        ButtonGroup groupStyleArc = new ButtonGroup();
        groupStyleArc.add(styleArc1);
        groupStyleArc.add(styleArc2);
        groupStyleArc.add(styleArc3);
        groupStyleArc.add(styleArc4);
        groupStyleArc.add(styleArc5);
        groupStyleArc.add(styleArc6);
        groupStyleArc.add(styleArc7);
        
        ActionListener groupStyleArcListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (ae.getSource()==styleArc1) { 
                    setStyleArc("ultra thick");
                } else if (ae.getSource()==styleArc2) {
                    setStyleArc("very thick");
                } else if (ae.getSource()==styleArc3) {
                    setStyleArc("thick");
                } else if (ae.getSource()==styleArc4) {
                    setStyleArc("semithick");
                } else if (ae.getSource()==styleArc5) {
                    setStyleArc("thin");
                } else if (ae.getSource()==styleArc6) {
                    setStyleArc("very thin");
                } else if (ae.getSource()==styleArc7) {
                    setStyleArc("ultra thin");
                }                
            }
        };
        
        styleArc1.addActionListener(groupStyleArcListener);
        styleArc2.addActionListener(groupStyleArcListener);
        styleArc3.addActionListener(groupStyleArcListener);
        styleArc4.addActionListener(groupStyleArcListener);
        styleArc5.addActionListener(groupStyleArcListener);
        styleArc6.addActionListener(groupStyleArcListener);
        styleArc7.addActionListener(groupStyleArcListener);
        //styleArc3 activé au démarrage
        styleArc3.setSelected(true);

        panelThicknessArc.add(styleArc1);
        panelThicknessArc.add(styleArc2);
        panelThicknessArc.add(styleArc3);
        panelThicknessArc.add(styleArc4);
        panelThicknessArc.add(styleArc5);
        panelThicknessArc.add(styleArc6);
        panelThicknessArc.add(styleArc7);
        panelThicknessArc.add(displayThicknessArc);
        tabbedPane.add("Epaisseur Arcs",panelThicknessArc);
    }
    
    /**
     * Méthode permettant de créer le JPanel contenant les boutons permettant
     * de modifier les couleurs des arcs en noir
     * @param d Draw
     */
    public void initPanelCouleurArc(Draw d){
        
        panelColorArc= new JPanel();
        panelColorArc.setBorder (new TitledBorder(new EtchedBorder(),"Couleur des arcs"));
        JTextArea displayCouleurArc = new JTextArea(12, 30);
        displayCouleurArc.setSize(new Dimension(12,30));
        displayCouleurArc.setText(
                        "Veuillez sélectionner la couleur des arcs :\n"+
                        "\n"+
                        "  - préserver les couleurs du graphe\n"+
                        "  - dessiner tous les arcs en noir"
        );
        displayCouleurArc.setEditable (false);
        displayCouleurArc.setFocusable(false);
        
        JRadioButton colorArc1 = new JRadioButton("Couleurs du graphe");
        JRadioButton colorArc2 = new JRadioButton("Tout en noir");
        
        ButtonGroup groupColorArc = new ButtonGroup();
        groupColorArc.add(colorArc1);
        groupColorArc.add(colorArc2);
        
        ActionListener groupColorArcListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (ae.getSource()==colorArc1) {
                    setAllBlack(false);
                    System.out.println("false");
                } else if (ae.getSource()==colorArc2) {
                    setAllBlack(true);
                    System.out.println("true");
                }
            }
        };
        colorArc1.addActionListener(groupColorArcListener);
        colorArc2.addActionListener(groupColorArcListener);
        colorArc1.setSelected(true);
        
        panelColorArc.add(colorArc1);
        panelColorArc.add(colorArc2);
        panelColorArc.add(displayCouleurArc);
        tabbedPane.add("Couleur arcs",panelColorArc);
    }
    
    /**
     * Méthode permettant de créer le JPanel contenant les boutons permettant
     * de modifier la couleur des noeuds
     * @param d Draw
     */
    public void initPanelCouleurNoeud(Draw d){
        JPanel panelColNode= new JPanel();
        panelColNode.setBorder (new TitledBorder(new EtchedBorder(),"Couleur des noeuds"));
        JTextArea displayColNode = new JTextArea(12, 30);
        displayColNode.setSize(new Dimension(12,30));
        
        displayColNode.setText(
                        "Veuillez sélectionner la couleur de remplissage des Noeuds :\n"
        );
        
        displayColNode.setEditable (false);
        displayColNode.setFocusable(false);
        
        JRadioButton colorNode1 = new JRadioButton("Blanc");
        JRadioButton colorNode2 = new JRadioButton("Gris");
        JRadioButton colorNode3 = new JRadioButton("Rouge");
        JRadioButton colorNode4 = new JRadioButton("Vert");
        JRadioButton colorNode5 = new JRadioButton("Bleu");
        JRadioButton colorNode6 = new JRadioButton("Cyan");
        JRadioButton colorNode7 = new JRadioButton("Magenta");
        JRadioButton colorNode8 = new JRadioButton("Jaune");
        
        ButtonGroup groupColorNode = new ButtonGroup();
        groupColorNode.add(colorNode1);
        groupColorNode.add(colorNode2);
        groupColorNode.add(colorNode3);
        groupColorNode.add(colorNode4);
        groupColorNode.add(colorNode5);
        groupColorNode.add(colorNode6);        
        groupColorNode.add(colorNode7);
        groupColorNode.add(colorNode8);        
        colorNode2.setSelected(true);
        
        ActionListener groupColorNodeListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (ae.getSource()==colorNode1) {
                    setNoeudFill("white");
                } else if (ae.getSource()==colorNode2) {
                    setNoeudFill("gray!50");
                } else if (ae.getSource()==colorNode3) {
                    setNoeudFill("red!50");
                } else if (ae.getSource()==colorNode4) {
                    setNoeudFill("green!50");
                } else if (ae.getSource()==colorNode5) {
                    setNoeudFill("blue!50");
                } else if (ae.getSource()==colorNode6) {
                    setNoeudFill("cyan!50");
                } else if (ae.getSource()==colorNode7) {
                    setNoeudFill("magenta!50");   
                } else if (ae.getSource()==colorNode8) {
                    setNoeudFill("yellow!50");              
                }
            }
        };

        colorNode1.addActionListener(groupColorNodeListener);
        colorNode2.addActionListener(groupColorNodeListener);
        colorNode3.addActionListener(groupColorNodeListener);
        colorNode4.addActionListener(groupColorNodeListener);
        colorNode5.addActionListener(groupColorNodeListener);
        colorNode6.addActionListener(groupColorNodeListener);
        colorNode7.addActionListener(groupColorNodeListener);
        colorNode8.addActionListener(groupColorNodeListener);
        
        panelColNode.add(colorNode1);
        panelColNode.add(colorNode2);
        panelColNode.add(colorNode3);
        panelColNode.add(colorNode4);
        panelColNode.add(colorNode5);
        panelColNode.add(colorNode6);
        panelColNode.add(colorNode7);
        panelColNode.add(colorNode8);
        panelColNode.add(displayColNode);
        tabbedPane.add("Couleur des noeuds",panelColNode);
    }
       
    
     /**
     * Méthode permettant de créer le JPanel contenant les boutons permettant
     * de modifier la couleur des noeuds
     * @param d Draw
     */   
    public void initPanelClous(Draw d){
        JPanel panelClous= new JPanel();
        
        JTextArea displayClous = new JTextArea(12, 30);
        displayClous.setSize(new Dimension(12,30));
        displayClous.setText( 
                "PAS D'IMPLEMENTATION\n"+
                "Clous non présents dans la source LaTeX"
        );
        panelClous.add(displayClous);
        tabbedPane.add("Clous",panelClous);
    }
    
    
    
    /**
     * Méthode permettant de créer le JPanel contenant le source Latex de l'export
     * @param d Draw
     */
    public void initPanelExport(Draw d){
        panelExport = new JPanel();
        panelExport.setBorder (new TitledBorder(new EtchedBorder(),"Source LaTeX"));
        JTextArea display = new JTextArea(16, 58);
        display.setEditable (false); // set textArea non-editable
        JScrollPane scroll = new JScrollPane(display);
        scroll.setVerticalScrollBarPolicy (ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        
        JButton exportLatexButton = new JButton("Exporter");
        exportLatexButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                display.setText("");
                display.append(getSourceLatex(d));  
            } 
        } );
        panelExport.add(exportLatexButton);
        panelExport.add(scroll);
        
        tabbedPane.add("Export",panelExport);
    }
    
    /**
     * Méthode permettant de générer le source LaTeX à partir du Draw
     * @param d Draw
     * @return String
     */
    public String getSourceLatex(Draw d){  
        //premiere partie
        String latex1 = "\\documentclass{article}\n"+
                       "\\usepackage[utf8]{inputenc}\n"+
                       "\\usepackage{tikz}\n"+
                       "\\usetikzlibrary{positioning}\n"+  
                       "\\usepackage{pgfplots}\n"+
                       " \n"+
                       "\\begin{document}\n"+
                       " \n"+
                       "\\begin{tikzpicture}[scale=0.04]\n";
        //deuxieme partie
        String latex2 = writeCoordinates(d);
        if(d.getOriente()==0){//orienté
            latex2 += "\\tikzstyle{style}=[->,"+styleLine +"]\n";
            if(d.getPondere()){//pondéré
                latex2 += writeLinesPond(d);
            }else{
                latex2 += writeLinesNonPond(d);
            }
        }else{
            latex2 += "\\tikzstyle{style}=[->,"+styleLine +"]\n";
            if(d.getPondere()){//non orienté + pondéré
                latex2 += writeLinesPond(d);
            }else{//non orienté + non pondéré
                latex2 += writeLinesNonPond(d);
            }
        }
        latex2 += "\\end{tikzpicture}\n"+
                 "\n"+
                 "\\end{document}\n";
        
        if (!isAllBlack()){
            int i = 0;
            for (Color c : this.arcColors){
                latex1 += writeColorRGB(c,i);
                i += 1;
            }         
        }
        return latex1+latex2;
    }
    
    /**
     * Méthode permettant de récupérer les coordonées des noeuds
     * @param d Draw
     * @return String
     */
    public String writeCoordinates(Draw d){
        
        Ellipse2D.Double[] circ = d.getCirc();
        String[] lbl = d.getCircLbl() ;
        String coord = "";
        
        for (int i =0;i<d.getNumOfCircles();i++){
            int x = (int) circ[i].getCenterX() ;
            int y = (int) circ[i].getCenterY() ;
            coord = coord + "\\node[draw,circle,fill="+noeudFill+"] ("+ String.valueOf(i)+") at ("
                    + String.valueOf(x)+",-"+ String.valueOf(y)+") {"
                    + lbl[i] + "}; \n";
        }
        return coord;
    }
    
    /**
     * Méthode permettant de générer le source Latex pour les arcs
     * Cas Pondéré Orienté
     * @param d Draw
     * @return String
     */
    public String writeLinesPond(Draw d){
        String arcs = "";
        ArrayList<MyLine> lines = d.getLines();
        for (int i=0;i<d.getNumOfLines();i++){
            MyLine l = lines.get(i);
            int src = d.findEllipse(l.getFromPoint().x,l.getFromPoint().y);
            int dest = d.findEllipse(l.getToPoint().x,l.getToPoint().y);
            int poids = l.getPoids();
            Color c = l.getC();
            arcs += "\\draw["+writeArcColor(c)+"style] (" + String.valueOf(src);
            if(src == dest){//boucle sur le même noeud
                arcs += ") to [loop] (" + String.valueOf(dest)+") {};\n";
            }else{
                arcs += ")--(" + String.valueOf(dest) + ") node[midway,label="+ String.valueOf(poids)  +"] {};\n" ;
            }
        }
        return arcs;
    }
    
    /**
     * Méthode permettant de générer le source Latex pour les arcs
     * Cas non Pondéré Orienté
     * @param d Draw
     * @return String
     */    
    public String writeLinesNonPond(Draw d){
        String arcs = "";
        ArrayList<MyLine> lines = d.getLines();
        for (int i=0;i<d.getNumOfLines();i++){
            MyLine l = lines.get(i);
            int src = d.findEllipse(l.getFromPoint().x,l.getFromPoint().y);
            int dest = d.findEllipse(l.getToPoint().x,l.getToPoint().y);
            Color c = l.getC();
            arcs += "\\draw["+writeArcColor(c)+"style] (" + String.valueOf(src);
            if(src == dest){//boucle sur le même noeud
                arcs += ") to [loop] (" + String.valueOf(dest)+") {};\n";
            }else{
                arcs += ")--(" + String.valueOf(dest) +") {};\n" ;
            }
        } 
        return arcs;
    }
        
    /**
     * Méthode permettant de générer le source Latex pour les couleurs des arcs 
     * à partir des niveaux RGB
     * @param color Color
     * @param i int
     */   
    public String writeColorRGB(Color color,int i){
        String red = Integer.toString(color.getRed());
        String green = Integer.toString(color.getGreen());
        String blue = Integer.toString(color.getBlue());
        String istring = Integer.toString(i);
        return "\\definecolor{mycolor"+ Integer.toString(i)+"}{RGB}{"+red+","+green+","+blue+"}\n" ;
    }
    
    /**
     * Méthode permettant de générer le source Latex pour les couleurs des arcs 
     * à intégrer dans le style tikz
     * @param c Color
     */ 
    public String writeArcColor(Color c){
        String color = "";
        if (!isAllBlack()){
            if(this.arcColors.contains(c)){
                color = color + "mycolor"+ Integer.toString(this.arcColors.indexOf(c))+",";
            }else{
                this.arcColors.add(c);
                color = color + "mycolor"+ Integer.toString(this.arcColors.size()-1)+",";
            }
        }
        return color ;
    }
        
    /**
     * Méthode permettant de modifier le style des arcs 
     * @param s String
     */
    public void setStyleArc(String s){
        this.styleLine = s;
    }
    
    /**
     * Méthode permettant d'activer ou désactiver l'option "arcs en noir"
     * @param b boolean
     */
    public void setAllBlack(boolean b){
        this.allBlack = b;
    }
 
    /**
     * Méthode permettant de modifier la couleur des noeuds
     * @param noeudFill String
     */
    public void setNoeudFill(String noeudFill) {
        this.noeudFill = noeudFill;
    }
    
    /**
     * Getter de l'attribut allBlack
     */
    public boolean isAllBlack() {
        return allBlack;
    }
    

    
    
}
