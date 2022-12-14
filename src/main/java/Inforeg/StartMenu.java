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
import Inforeg.UI.CheckBox;
import Inforeg.Save.SaveManager;
import Inforeg.UI.ToolButton;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Taskbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

/**
 * Fenêtre de démarrage de l'application
 * @author remir
 */
public class StartMenu {
    
    public static void main(String[] args) throws IOException {
        Color selectedColor = Color.decode("#c2c1be");
        Color buttonColor = Color.decode("#dbdbdb");
        
        try {
            //set icon for mac os (and other systems which do support this method)
            final Taskbar taskbar = Taskbar.getTaskbar();
            taskbar.setIconImage(AssetLoader.appIco.getImage());
        } catch (final UnsupportedOperationException e) {
            System.out.println("The os does not support: 'taskbar.setIconImage'");
        } catch (final SecurityException e) {
            System.out.println("There was a security exception for: 'taskbar.setIconImage'");
        }   
        // Création de la fenêtre d'accueil
        JFrame J = new JFrame("INFOREG");
        J.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        J.setSize(600, 400);
        J.setResizable(false);
        J.setLocationRelativeTo(null);
        JPanel mainPanel = (JPanel) J.getContentPane();
        JPanel logoPanel = new JPanel();
        JPanel buttonsPanel = new JPanel();

        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.PAGE_AXIS));
        JPanel create = new JPanel();
        mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        // Icone de l'application
        ImageIcon icon = AssetLoader.appIco;
        J.setIconImage(icon.getImage());

        BufferedImage bannerImg = ImageIO.read(AssetLoader.getURL("asset/logoINFOREG.png"));
        Image bannerImgResized = bannerImg.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        JLabel banner = new JLabel(new ImageIcon(bannerImgResized));
        
        CheckBox graphOriente = new CheckBox("Orienté");
        CheckBox graphPond = new CheckBox("Pondéré");
        graphPond.setFocusPainted(false);

        // Bouton générant un nouveau graphe
        ToolButton nouveau = new ToolButton("Créer",buttonColor,selectedColor, null);
        nouveau.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Draw d = new Draw(graphOriente.isSelected(),graphPond.isSelected());
                if (!graphPond.isSelected()) {
                    d.setPondere(false);
                }
                SwingUtilities.invokeLater(new Interface(d)::createAndShowGui);
                J.dispatchEvent(new WindowEvent(J, WindowEvent.WINDOW_CLOSING));
            }
        });

        // Bouton permettant de charger un graphe existant
        ToolButton charge = new ToolButton("Charger un Graphe existant",buttonColor,selectedColor,null);
        charge.setAlignmentX(Component.CENTER_ALIGNMENT);
        charge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Draw d = SaveManager.load();
                SwingUtilities.invokeLater(new Interface(d)::createAndShowGui);
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
