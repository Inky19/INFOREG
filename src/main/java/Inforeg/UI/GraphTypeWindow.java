/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Inforeg.UI;

import Inforeg.AssetLoader;
import Inforeg.Draw.Draw;
import Inforeg.Save.SaveManager;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Fenêtre du choix du type de graphe
 *
 * @author François MARIE
 * @auhtor Rémi RAVELLI
 */
public class GraphTypeWindow extends JDialog {

    private Draw d;
    private boolean buttonPressed;

    public GraphTypeWindow() {
        super();
        setTitle("INFOREG");
        d = null;
        buttonPressed = false;
    }

    public Draw chooseGraph() {
        setModal(true);
        Color selectedColor = Color.decode("#c2c1be");
        Color buttonColor = Color.decode("#dbdbdb");
        // Création de la fenêtre d'accueil
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setResizable(false);
        setLocationRelativeTo(null);
        JPanel mainPanel = (JPanel) this.getContentPane();
        JPanel logoPanel = new JPanel();
        JPanel buttonsPanel = new JPanel();

        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.PAGE_AXIS));
        JPanel create = new JPanel();
        mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        // Icone de l'application
        ImageIcon icon = AssetLoader.appIco;
        setIconImage(icon.getImage());

        BufferedImage bannerImg = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
        try {
            bannerImg = ImageIO.read(AssetLoader.getURL("asset/logoINFOREG.png"));
        } catch (IOException ex) {
            System.out.println("An error occured while loading asset/logoINFOREG.png");
            Logger.getLogger(GraphTypeWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        Image bannerImgResized = bannerImg.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        JLabel banner = new JLabel(new ImageIcon(bannerImgResized));

        CheckBox graphOriente = new CheckBox("Orienté");
        CheckBox graphPond = new CheckBox("Pondéré");
        graphPond.setFocusPainted(false);

        JDialog J = this;
        // Bouton générant un nouveau graphe
        CustomButton nouveau = new CustomButton("Créer",buttonColor,selectedColor, null);
        nouveau.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                d = new Draw(graphOriente.isSelected(), graphPond.isSelected());
                if (!graphPond.isSelected()) {
                    d.setPondere(false);
                }
                dispatchEvent(new WindowEvent(J, WindowEvent.WINDOW_CLOSING));
                buttonPressed = true;
                setVisible(false);
            }
        });

        // Bouton permettant de charger un graphe existant
        CustomButton charge = new CustomButton("Charger un Graphe existant",buttonColor,selectedColor,null);
        charge.setAlignmentX(Component.CENTER_ALIGNMENT);
        charge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                d = SaveManager.load();
                buttonPressed = true;
                if (d != null) {
                    dispatchEvent(new WindowEvent(J, WindowEvent.WINDOW_CLOSING));
                    setVisible(false);
                }
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
        setVisible(true);
        if (d == null && buttonPressed) {
            JOptionPane.showMessageDialog(null, "Une erreur s'est produite lors de la création du graphe (Draw is null).", "Erreur critique", JOptionPane.ERROR_MESSAGE);
            throw new Error("Draw is null");
        }
        return d;
    }
}
