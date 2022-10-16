package Inforeg;

/*=============================================
Classe StartMenu qui défini le lancement de
l'application
Auteur : Béryl CASSEL
Date de création : 08/03/2022
Date de dernière modification : 18/03/2022
Commentaires ajoutés
=============================================*/
import Inforeg.Draw.Draw;
import Inforeg.Save.saveManager;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

public class StartMenu {

    public static void main(String[] args) throws IOException {

        // Création de la fenêtre d'accueil
        JFrame J = new JFrame("INFOREG");
        J.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        J.setSize(600, 400);
        J.setLocationRelativeTo(null);
        JPanel mainPanel = (JPanel) J.getContentPane();
        JPanel logoPanel = new JPanel();
        JPanel buttonsPanel = new JPanel();

        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.PAGE_AXIS));
        JPanel create = new JPanel();
        mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        // Icone de l'application
        ImageIcon icon = new ImageIcon("asset/icon.png");
        J.setIconImage(icon.getImage());

        BufferedImage bannerImg = ImageIO.read(new File("asset/logoINFOREG.png"));
        Image bannerImgResized = bannerImg.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        JLabel banner = new JLabel(new ImageIcon(bannerImgResized));

        JCheckBox graphOriente = new JCheckBox("Orienté");

        JCheckBox graphPond = new JCheckBox("Pondéré");

        // Bouton générant un nouveau graphe
        JButton nouveau = new JButton("Créer");
        nouveau.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Draw d = new Draw();
                if (!graphPond.isSelected()) {
                    d.setPondere(false);
                }

                if (graphOriente.isSelected()) {
                    d.setOriente(Draw.ORIENTE);
                    SwingUtilities.invokeLater(new InterfaceO(d)::createAndShowGui);
                } else {
                    d.setOriente(Draw.NONORIENTE);
                    SwingUtilities.invokeLater(new InterfaceNO(d)::createAndShowGui);
                }
                J.dispatchEvent(new WindowEvent(J, WindowEvent.WINDOW_CLOSING));

            }
        });

        // Bouton permettant de charger un graphe existant
        JButton charge = new JButton("Charger un Graphe existant");
        charge.setAlignmentX(Component.CENTER_ALIGNMENT);
        charge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Draw d = saveManager.load();
                if (d.getOriente()==Draw.ORIENTE){
                    SwingUtilities.invokeLater(new InterfaceO(d)::createAndShowGui);
                } else {
                    SwingUtilities.invokeLater(new InterfaceNO(d)::createAndShowGui);
                }
                J.dispatchEvent(new WindowEvent(J, WindowEvent.WINDOW_CLOSING));
            }
        });

        logoPanel.add(banner);
        create.add(graphOriente);
        create.add(graphPond);
        create.add(nouveau);
        buttonsPanel.add(create);
        buttonsPanel.add(charge);
        mainPanel.add(logoPanel);
        mainPanel.add(buttonsPanel);

        J.setVisible(true);
    }

}
