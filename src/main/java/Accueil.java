/*=============================================
Classe Accueil qui défini le lancement de
l'application
Auteur : Béryl CASSEL
Date de création : 08/03/2022
Date de dernière modification : 18/03/2022
Commentaires ajoutés
=============================================*/

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Accueil {  

    public static void main(String[] args) {

        // Création de la fenêtre d'accueil
        JFrame J = new JFrame("INFOREG");
        J.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        J.setSize(200,200);
        J.setLocationRelativeTo(null);
        JPanel contentPane = (JPanel) J.getContentPane();
        contentPane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));

        // Bouton permettant d'ouvrir un nouveau graphe orienté pondéré
        JButton orientepond = new JButton("Graphe Orienté Pondéré");
        orientepond.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Draw d = new Draw();
                d.setOriente(Draw.ORIENTE);
                SwingUtilities.invokeLater(new InterfaceO(d)::createAndShowGui);
            }
        });

        // Bouton permettant d'ouvrir un nouveau graphe non orienté pondéré
        JButton nonorientepond = new JButton("Graphe Non Orienté Pondéré");
        nonorientepond.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Draw d = new Draw();
                d.setOriente(Draw.NONORIENTE);
                SwingUtilities.invokeLater(new InterfaceNO(d)::createAndShowGui);
            }
        });

        // Bouton permettant d'ouvrir un nouveau graphe orienté non pondéré
        JButton oriente = new JButton("Graphe Orienté");
        oriente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Draw d = new Draw();
                d.setOriente(Draw.ORIENTE);
                d.setPondere(false);
                SwingUtilities.invokeLater(new InterfaceO(d)::createAndShowGui);
            }
        });

        // Bouton permettant d'ouvrir un nouveau graphe non orienté non pondéré
        JButton nonoriente = new JButton("Graphe Non Orienté");
        nonoriente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Draw d = new Draw();
                d.setOriente(Draw.NONORIENTE);
                d.setPondere(false);
                SwingUtilities.invokeLater(new InterfaceNO(d)::createAndShowGui);
            }
        });

        // Bouton permettant de charger un graphe existant
        JButton charge = new JButton("Charger un Graphe existant");
        charge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                //On parcours le répertoire de fichiers
                JFileChooser dialogue = new JFileChooser(".");
                if (dialogue.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
                    File fichier = dialogue.getSelectedFile();
                    String source = fichier.getName();

                    //Si le fichier n'a pas pour extension ".inforeg" on ne l'accepte pas
                    if (source.length() < 8 || !source.toLowerCase().substring(source.length()-8).equals(".inforeg")) {
                        JOptionPane.showMessageDialog(null, "Seuls les fichiers inforeg sont acceptés !", "Mauvaise extension !", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        try {
                            Draw d = (new ChargeDraw(fichier)).chargerDraw();
                            switch (d.getOriente()){
                                case Draw.ORIENTE:
                                    SwingUtilities.invokeLater(new InterfaceO(d)::createAndShowGui);
                                    break;
                                case Draw.NONORIENTE:
                                    SwingUtilities.invokeLater(new InterfaceNO(d)::createAndShowGui);
                                    break;
                                default:
                                    JOptionPane.showMessageDialog(null, "Sauvegarde inexistante", "No Save !!", JOptionPane.INFORMATION_MESSAGE);
                                    break;
                            }

                        // Si le fichier est mal écrit:
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Le fichier sélectionné ne peut pas être utilisé", "Fichier corrompu !", JOptionPane.INFORMATION_MESSAGE);
                        }
                        
                    }
                }
            }
        });

        contentPane.add(orientepond);
        contentPane.add(oriente);
        contentPane.add(nonorientepond);
        contentPane.add(nonoriente);
        contentPane.add(charge);
        J.setVisible(true);
    }

}