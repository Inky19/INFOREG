/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package inforeg.Save;

import inforeg.Arc;
import inforeg.Draw;
import inforeg.Interface;
import inforeg.MyLine;
import inforeg.ObjetGraph.Noeud;
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
    public static void save(Draw d){
        JFileChooser fileExplorer = new JFileChooser();
        int res = fileExplorer.showOpenDialog(null);
        if (res == JFileChooser.APPROVE_OPTION){
            
            File file = fileExplorer.getSelectedFile();
            String name = file.getName();
            String path = "";
            if (name.length() < 8 || !name.toLowerCase().substring(name.length()-8).equals(".inforeg")) {
                path = file.getPath() + ".inforeg";
            } else {
                path = file.getPath();
            }
            saveToFile(d, path);
        }
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
            String sep = ", "; // Caractère de séparation dans le fichier de sauvegarde
            
            // Ligne contenant des informations sur le type de graphe et sur la version du logiciel avec laquelle le fichier a été généré
            fileBuffer.write("Inforeg " + Interface.VERSION + sep + d.getPondere() + sep + d.getOriente());
            fileBuffer.newLine();
            
            // Sauvegarde des nœuds
            // Structure d'une ligne :
            // Node, <label>, <cx>, <cy>, <r>, <coulor (en hex)>
            fileBuffer.write("========== NODES ==========");
            ArrayList<Noeud> nodes = d.getNodes();
            for (Noeud node: nodes){
                fileBuffer.newLine();
                fileBuffer.write("Node" + sep + 0 + sep + node.getCx() + sep + node.getCy() + sep + node.getR() + sep + node.getColorHex());
            }
            
            // Sauvegarde des arcs
            // Structure d'une ligne :
            // Arc, <lablel nœud1>, <lablel nœud2>, <clouX>, <clouY>, <ponderation>
            fileBuffer.newLine();
            fileBuffer.write("========== ARCS ==========");
            ArrayList<MyLine> arcs = d.getLines();
            for (MyLine arc: arcs){
                fileBuffer.newLine();
                fileBuffer.write("Arc" + sep + arc.getFrom().getLabel() + sep + arc.getTo().getLabel() + sep + arc.getClou().getCx() + sep + arc.getClou().getCy() + arc.getPoids());
            }
            fileBuffer.flush();
            fileBuffer.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Une erreur s'est produite lors de l'écriture du fichier.", "Erreur de sauvegarde", JOptionPane.ERROR_MESSAGE);
        }
    }
}
