package Inforeg.Save;

import Inforeg.AssetLoader;
import Inforeg.Draw.Draw;
import Inforeg.Interface;
import Inforeg.ObjetGraph.Arc;
import Inforeg.ObjetGraph.Nail;
import Inforeg.ObjetGraph.Node;
import java.awt.Color;
import java.awt.Component;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Système de sauvegarde et de chargement des fichiers .inforeg
 *
 * @author François MARIE
 * @author Rémi RAVELLI
 */
public abstract class SaveManager {

    private static File previousDir; // Dernière dossier ouvert par l'utilisateur

    public static final String SEP = ", "; // Caractère(s) de séparation dans le fichier de sauvegarde

    /**
     * Sauvegarde un graphe en proposant une interface graphique à l'utilisateur
     * pour choisir l'emplacement et le nom. Utilisée lorsque le graphe n'a
     * jamais été enregistré aupravant.
     *
     * @param d Draw à sauvegarder
     */
    public static boolean save(Draw d) {
        JFileChooser fileExplorer = new JFileChooser() {
            @Override
            protected JDialog createDialog(Component parent) throws HeadlessException {
                JDialog dialog = super.createDialog(parent);
                dialog.setIconImage(AssetLoader.appIco.getImage()); // Changement de l'icone
                return dialog;
            }
        };
        fileExplorer.setApproveButtonText("Enregistrer");
        fileExplorer.setDialogTitle("Enregistrer le graphe");
        fileExplorer.setFileFilter(new FileNameExtensionFilter("Graphe inforeg (.inforeg)", "inforeg"));
        int res = fileExplorer.showOpenDialog(null);
        if (res == JFileChooser.APPROVE_OPTION) {
            File file = fileExplorer.getSelectedFile();
            fileExplorer.setCurrentDirectory(previousDir);
            String[] filePath = Utils.formatPath(file, "inforeg");
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
                fileBuffer.write("Node" + SEP + node.getId() + SEP + node.getLabel() + SEP + node.getCx() + SEP + node.getCy() + SEP + node.getR() + SEP + Utils.color2Hex(node.getColorDisplayed()));
            }

            // Sauvegarde des arcs
            // Structure d'une ligne :
            // Arc, <id nœud1>, <id nœud2>, <ponderation>, <couleur (en hex), <nombre clous>, <clou1_X>, <clou1_Y>, <clou1_R>, ... , <clouN_X>, <clouN_Y>, <clouN_R>
            fileBuffer.newLine();
            fileBuffer.write("########## ARCS ##########");
            ArrayList<Arc> arcs = d.getLines();
            String arcLine = "";
            ArrayList<Nail> nails = null;
            int nbrNails = 0;
            for (Arc arc : arcs) {
                fileBuffer.newLine();
                nails = arc.getNails();
                arcLine = "Arc" + SEP + arc.getFrom().getId() + SEP + arc.getTo().getId() + SEP + arc.getPoids() + SEP + Utils.color2Hex(arc.getColor()) + SEP + nails.size();
                // Sauvegarde des clous
                for (Nail nail : nails) {
                    arcLine += SEP + nail.getCx() + SEP + nail.getCy() + SEP + nail.getR();
                }
                fileBuffer.write(arcLine);
            }
            fileBuffer.flush();
            fileBuffer.close();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Une erreur s'est produite lors de l'écriture du fichier.", "Erreur de sauvegarde", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    /**
     * Charge une instance de Draw depuis un fichier de sauvegarde. Retourn null
     * en cas d'erreur.
     *
     * @return
     */
    public static Draw load() {
        JFileChooser fileExplorer = new JFileChooser() {
            @Override
            protected JDialog createDialog(Component parent) throws HeadlessException {
                JDialog dialog = super.createDialog(parent);
                dialog.setIconImage(AssetLoader.appIco.getImage()); // Changement de l'icone
                return dialog;
            }
        };
        fileExplorer.setApproveButtonText("Ouvrir");
        fileExplorer.setDialogTitle("Ouvrir un graphe existant");
        fileExplorer.setCurrentDirectory(previousDir);
        fileExplorer.setFileFilter(new FileNameExtensionFilter("Graphe inforeg (.inforeg)", "inforeg"));
        int res = fileExplorer.showOpenDialog(null);

        if (res == JFileChooser.APPROVE_OPTION) {
            File file = fileExplorer.getSelectedFile();
            previousDir = file;
            String[] filePath = Utils.formatPath(file, "inforeg");
            String name = filePath[0];
            String path = filePath[1];
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                // Recupération des propriétés de Draw = Première ligne
                String line = reader.readLine();
                String[] data = line.split(SEP);
                Boolean pondere = Boolean.valueOf(data[2]);
                Boolean oriente = Boolean.valueOf(data[3]);

                Draw d = new Draw(oriente, pondere);
                d.setNextNodeId(Integer.parseInt(data[4]));
                d.setFileName(name.substring(0, name.length() - 8)); // Nom sans l'extension .inforeg
                d.setPathSauvegarde(path);

                line = reader.readLine();
                while (line != null) {
                    data = line.split(SEP);
                    Color color = Color.BLACK;
                    switch (data[0]) {
                        case "Node":
                            int id = Integer.parseInt(data[1]);
                            String label = data[2];
                            double cx = Double.parseDouble(data[3]);
                            double cy = Double.parseDouble(data[4]);
                            double r = Double.parseDouble(data[5]);
                            color = Utils.hex2Color(data[6]);

                            Node node = new Node(cx, cy, r, color, label, id);
                            d.getNodes().add(node);
                            break;
                        case "Arc":
                            int id1 = Integer.parseInt(data[1]);
                            int id2 = Integer.parseInt(data[2]);
                            int pond = Integer.parseInt(data[3]);
                            color = Utils.hex2Color(data[4]);

                            // L'intégralité des nœuds doivent être chargés pour pouvoir trouver leur id.
                            // Il est donc nécessaire que le fichier de sauvegarde ne comporte pas des lignes "Arc" avant des "Node" pour être sûr que cela marche.
                            Node n1 = d.getNodeFromId(id1);
                            Node n2 = d.getNodeFromId(id2);
                            Arc arc = new Arc(n1, n2, pond, color);

                            // Chargement des clous
                            int nbrNails = Integer.parseInt(data[5]);
                            int ind = 6; // Indice du permier clou
                            Nail nail = null;
                            double nailx = 0;
                            double naily = 0;
                            double radius = 0;
                            for (int i = 0; i < nbrNails; i++) {
                                nailx = Double.parseDouble(data[ind]);
                                naily = Double.parseDouble(data[ind + 1]);
                                radius = Double.parseDouble(data[ind + 2]);
                                ind += 3;
                                nail = new Nail(nailx, naily, radius, color, arc);
                                arc.addNail(nail);
                            }
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

            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Le fichier n'a pas été trouvé.", "Erreur de chargement", JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Une erreur s'est produite lors de la lecture du fichier.\nLe fichier est peut-être corrompu.", "Erreur de chargement", JOptionPane.ERROR_MESSAGE);
            }

        }

        return null;
    }
}
