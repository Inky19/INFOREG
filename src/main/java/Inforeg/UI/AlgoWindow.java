/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package Inforeg.UI;

import Inforeg.Algo.*;
import Inforeg.Draw.Draw;
import java.awt.GridLayout;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author inky19
 */
public class AlgoWindow extends JDialog{
    
    private static final ImageIcon pathIco = new ImageIcon(AlgoWindow.class.getClassLoader().getResource("asset/icons/algo/path.png"));
    private static final ImageIcon scanIco = new ImageIcon(AlgoWindow.class.getClassLoader().getResource("asset/icons/algo/scan.png"));
    private static final ImageIcon colorationIco = new ImageIcon(AlgoWindow.class.getClassLoader().getResource("asset/icons/algo/coloration.png"));
    private static final ImageIcon flowIco = new ImageIcon(AlgoWindow.class.getClassLoader().getResource("asset/icons/algo/flow.png"));
    private static final ImageIcon treeIco = new ImageIcon(AlgoWindow.class.getClassLoader().getResource("asset/icons/algo/tree.png"));
    
    private Draw d;
    
    public AlgoWindow(JFrame frame, Draw d){
        
        super(frame, "Liste des algorithmes");
        this.d = d;
        
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        
        
        AlgoBox path = new AlgoBox("Plus court chemin", this, pathIco);
        path.addAlgo(new Dijkstra());
        
        AlgoBox flow = new AlgoBox("Flot", this, flowIco);
        flow.addAlgo(new FordFulkerson());
        
        AlgoBox tree = new AlgoBox("Arbre", this, treeIco);
        tree.addAlgo(new PrimMST());
        tree.addAlgo(new KruskalMST());
        
        AlgoBox coloration = new AlgoBox("Coloration", this, colorationIco);
        coloration.addAlgo(new Coloration());
        
         
        
        JPanel dialPanel = new JPanel();
        dialPanel.setLayout(new GridLayout(2,2));
        dialPanel.add(path);
        dialPanel.add(flow);
        dialPanel.add(tree);
        dialPanel.add(coloration);
        dialPanel.setVisible(true);
        
        this.add(dialPanel);
        this.setModal(true);
    }
    
    public void selectAlgo(Algorithm algo){
        d.setAlgo(algo);
        this.dispose();
    }
}
