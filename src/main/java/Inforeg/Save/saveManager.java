/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package Inforeg.Save;

import Inforeg.Draw.Draw;
import Inforeg.Interface;
import Inforeg.ObjetGraph.MyLine;
import Inforeg.ObjetGraph.Nail;
import Inforeg.ObjetGraph.Node;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * Gestion des fichiers de sauvegarde .inforeg
 *
 * @author inky19
 */
public abstract class saveManager {

    public static final String SEP = ", "; // Caractère(s) de séparation dans le fichier de sauvegarde

    /**
     * Sauvegarde un graphe en proposant une interface graphique à l'utilisateur
     * pour choisir l'emplacement et le nom. Utilisée lorsque le graphe n'a
     * jamais été enregistré aupravant.
     *
     * @param d Draw à sauvegarder
     */
    public static boolean save(Draw d) {
        JFileChooser fileExplorer = new JFileChooser();
        int res = fileExplorer.showOpenDialog(null);
        if (res == JFileChooser.APPROVE_OPTION) {
            File file = fileExplorer.getSelectedFile();
            String[] filePath = formatPath(file);
            String name = filePath[0];
            String path = filePath[1];
            d.setPathSauvegarde(path);
            d.setFileName(name.substring(0, name.length() - 8)); // Nom sans l'extension .inforeg
            return saveToFile(d, path);
            }
        return false;
    }

    /**
     * Sauvegarde directement un graphe dans un fichier donné. Utilisée
     * directement pour un enregistrement silencieux si le fichier existe déjà.
     *
     * @param d Draw à sauvegarder
     * @param path Emplacement du fichier cible
     */
    public static boolean saveToFile(Draw d, String path) {
        try {
            BufferedWriter fileBuffer = new BufferedWriter(new FileWriter(path));

            // Ligne contenant des informations sur le type de graphe et sur la version du logiciel avec laquelle le fichier a été généré
            fileBuffer.write("Inforeg" + SEP + Interface.VERSION + SEP + d.getPondere() + SEP + d.getOriente() + SEP + d.getNextNodeId());
            fileBuffer.newLine();

            // Sauvegarde des nœuds
            // Structure d'une ligne :
            // Node, <id>, <label>, <cx>, <cy>, <r>, <coulor (en hex)>
            fileBuffer.write("########## NODES ##########");
            ArrayList<Node> nodes = d.getNodes();
            for (Node node : nodes) {
                fileBuffer.newLine();
                fileBuffer.write("Node" + SEP + node.getId() + SEP + node.getLabel() + SEP + node.getCx() + SEP + node.getCy() + SEP + node.getR() + SEP + color2Hex(node.getColor()));
            }

            // Sauvegarde des arcs
            // Structure d'une ligne :
            // Arc, <id nœud1>, <id nœud2>, <clouX>, <clouY>, <ponderation>
            fileBuffer.newLine();
            fileBuffer.write("########## ARCS ##########");
            ArrayList<MyLine> arcs = d.getLines();
            for (MyLine arc : arcs) {
                fileBuffer.newLine();
                fileBuffer.write("Arc" + SEP + arc.getFrom().getId() + SEP + arc.getTo().getId() + SEP + arc.getClou().getCx() + SEP + arc.getClou().getCy() + SEP + arc.getClou().getR() + SEP + arc.getPoids() + SEP + color2Hex(arc.getColor()));
            }
            fileBuffer.flush();
            fileBuffer.close();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Une erreur s'est produite lors de l'écriture du fichier.", "Erreur de sauvegarde", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    public static Draw load() {
        JFileChooser fileExplorer = new JFileChooser();
        int res = fileExplorer.showOpenDialog(null);

        if (res == JFileChooser.APPROVE_OPTION) {
            File file = fileExplorer.getSelectedFile();
            String[] filePath = formatPath(file);
            String name = filePath[0];
            String path = filePath[1];

            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                // Recupération des propriétés de Draw = Première ligne
                String line = reader.readLine();
                String[] data = line.split(SEP);
                Boolean pondere = Boolean.parseBoolean(data[2]);
                Boolean oriente = Boolean.parseBoolean(data[3]);
                
                Draw d = new Draw(oriente,pondere);
                d.setNextNodeId(Integer.parseInt(data[4]));
                d.setFileName(name.substring(0, name.length() - 8)); // Nom sans l'extension .inforeg
                d.setPathSauvegarde(path);
                
                line = reader.readLine();
                while (line != null) {
                    data = line.split(SEP);
                    String colorHex = "";
                    Color color = Color.BLACK;
                    switch (data[0]) {
                        case "Node":
                            int id = Integer.parseInt(data[1]);
                            String label = data[2];
                            double cx = Double.parseDouble(data[3]);
                            double cy = Double.parseDouble(data[4]);
                            double r = Double.parseDouble(data[5]);
                            colorHex = data[6];
                            color = hex2Color(colorHex);

                            Node node = new Node(cx, cy, r, color, label, id);
                            d.getNodes().add(node);
                            break;
                        case "Arc":
                            int id1 = Integer.parseInt(data[1]);
                            int id2 = Integer.parseInt(data[2]);
                            double nailx = Double.parseDouble(data[3]);
                            double naily = Double.parseDouble(data[4]);
                            double radius = Double.parseDouble(data[5]);
                            int pond = Integer.parseInt(data[6]);
                            colorHex = data[7];
                            
                            // L'intégralité des nœuds doivent être chargés pour pouvoir trouver leur id.
                            // Il est donc nécessaire que le fichier de sauvegarde ne comporte pas des lignes "Arc" avant des "Node" pour être sûr que cela marche.
                            Node n1 = d.getNodeFromId(id1);
                            Node n2 = d.getNodeFromId(id2);
                            Nail nail = new Nail(nailx, naily, radius);
                            color = hex2Color(colorHex);
                            MyLine arc = new MyLine(n1, n2, pond, color, nail);
                            d.getLines().add(arc);

                            break;
                        case "Inforeg":
                            d.setPondere(Boolean.parseBoolean(data[2]));
                            d.setOriente(Boolean.parseBoolean(data[3]));
                            d.setNextNodeId(Integer.parseInt(data[4]));
                            break;
                    }
                    line = reader.readLine();
                }
                reader.close();
                return d;

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Une erreur s'est produite lors de la lecture du fichier.", "Erreur de chargement", JOptionPane.ERROR_MESSAGE);
            }

        }

        return null;
    }

    /**
     * Formate le chemin du fichier et son nom avec l'extension .inforeg s'il
     * n'existe pas.
     *
     * @param file Fichier cible
     * @return [name, path] : nom et chemin du fichier sous forme de String dans
     * une array de taille 2.
     */
    public static String[] formatPath(File file) {
        String name = file.getName();
        String path = "";
        if (name.length() < 8 || !name.toLowerCase().substring(name.length() - 8).equals(".inforeg")) {
            path = file.getPath() + ".inforeg";
            name += ".inforeg";
        } else {
            path = file.getPath();
        }
        return new String[]{name, path};
    }

    public static String color2Hex(Color color) {
        String r = Integer.toHexString(color.getRed());
        String g = Integer.toHexString(color.getGreen());
        String b = Integer.toHexString(color.getBlue());
        String[] rgb = new String[] {r,g,b};
        for (int i = 0; i < 3; i++){
            if (rgb[i].length()<2){
                rgb[i] = "0" + rgb[i];
            }
        }
        return (rgb[0] + rgb[1] + rgb[2]);
    }

    public static Color hex2Color(String colorHex) {
        int R = Integer.decode("0x" + colorHex.substring(0, 2));
        int G = Integer.decode("0x" + colorHex.substring(2, 4));
        int B = Integer.decode("0x" + colorHex.substring(4, 6));
        return new Color(R, G, B);
    }
}
