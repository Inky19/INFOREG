/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package Inforeg.Save;

import Inforeg.ObjetGraph.Arc;
import Inforeg.Draw.Draw;
import Inforeg.Interface;
import Inforeg.ObjetGraph.MyLine;
import Inforeg.ObjetGraph.Node;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import java.util.Scanner;
import javax.swing.JOptionPane;
/**
 * Gestion des fichiers de sauvegarde .inforeg
 * @author inky19
 */
public abstract class saveManager {
    
    /**
     * Sauvegarde un graphe en proposant une interface graphique à l'utilisateur pour choisir l'emplacement et le nom.
     * Utilisée lorsque le graphe n'a jamais été enregistré aupravant.
     * @param d Draw à sauvegarder
     */
    public static String save(Draw d){
        JFileChooser fileExplorer = new JFileChooser();
        int res = fileExplorer.showOpenDialog(null);
        if (res == JFileChooser.APPROVE_OPTION){
            
            File file = fileExplorer.getSelectedFile();
            String name = file.getName();
            String path = "";
            if (name.length() < 8 || !name.toLowerCase().substring(name.length()-8).equals(".inforeg")) {
                path = file.getPath() + ".inforeg";
                name += ".inforeg";
            } else {
                path = file.getPath();
            }
            saveToFile(d, path);
            return name.substring(0, name.length()-8);
        }
        return null;
    }
    
    /**
     * Sauvegarde directement un graphe dans un fichier donné.
     * Utilisée directement pour un enregistrement silencieux si le fichier existe déjà.
     * @param d Draw à sauvegarder
     * @param path Emplacement du fichier cible
     */
    public static void saveToFile(Draw d, String path){
        try {
            BufferedWriter fileBuffer = new BufferedWriter(new FileWriter(path));
            String sep = ", "; // Caractère(s) de séparation dans le fichier de sauvegarde
            
            // Ligne contenant des informations sur le type de graphe et sur la version du logiciel avec laquelle le fichier a été généré
            fileBuffer.write("Inforeg " + Interface.VERSION + sep + d.getPondere() + sep + d.getOriente() + sep + d.getNextNodeId());
            fileBuffer.newLine();
            
            // Sauvegarde des nœuds
            // Structure d'une ligne :
            // Node, <id>, <label>, <cx>, <cy>, <r>, <coulor (en hex)>
            fileBuffer.write("########## NODES ##########");
            ArrayList<Node> nodes = d.getNodes();
            for (Node node: nodes){
                fileBuffer.newLine();
                fileBuffer.write("Node" + sep + node.getId() + sep + node.getLabel() + sep + node.getCx() + sep + node.getCy() + sep + node.getR() + sep + node.getColorHex());
            }
            
            // Sauvegarde des arcs
            // Structure d'une ligne :
            // Arc, <lablel nœud1>, <lablel nœud2>, <clouX>, <clouY>, <ponderation>
            fileBuffer.newLine();
            fileBuffer.write("########## ARCS ##########");
            ArrayList<MyLine> arcs = d.getLines();
            for (MyLine arc: arcs){
                fileBuffer.newLine();
                fileBuffer.write("Arc" + sep + arc.getFrom().getId() + sep + arc.getTo().getId() + sep + arc.getClou().getCx() + sep + arc.getClou().getCy() + arc.getPoids());
            }
            fileBuffer.flush();
            fileBuffer.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Une erreur s'est produite lors de l'écriture du fichier.", "Erreur de sauvegarde", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public Draw load(){
        JFileChooser fileExplorer = new JFileChooser();
        int res = fileExplorer.showOpenDialog(null);
        return null;
    }
}
