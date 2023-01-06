package Inforeg.UI;

import Inforeg.AssetLoader;
import Inforeg.Draw.Draw;
import Inforeg.Save.Utils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * Fenêtre de l'export de la matrice
 *
 * @author François MARIE
 * @auhtor Rémi RAVELLI
 */
public class MatrixWindow extends JDialog {
    CustomButton copy;

    public MatrixWindow(JFrame frame, Draw d) {
        super(frame, "Exporter la matrice d'adjacence");
        // Position
        this.setResizable(false);
        this.setSize(600, 500);
        this.setLocationRelativeTo(null);
        
        int[][] matrix = d.getG().getAdjMatrix();
        JPanel panel = new JPanel();
        BoxLayout bl = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(bl);
        panel.setBorder(new EmptyBorder(10,10,10,10));
        JLabel text;
        if (matrix==null || matrix.length == 0) {
            text = new JLabel("Le graphe est vide. Ajouter des noeuds pour créer le graphe.");
            text.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            panel.add(text);
        } else {
            text = new JLabel("La matrice d'adjacence du graphe est :");
            text.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            text.setBorder(new EmptyBorder(0,0,10,0));
            panel.add(text);

            JScrollPane scroll = new JScrollPane (showMatrix(d));
            scroll.setBorder(new EmptyBorder(0,0,10,0));
            copy = new CustomButton("Copier",AssetLoader.copyIco, AlgoBox.BUTTON_COLOR, AlgoBox.BUTTON_COLOR, Color.decode("#94bc63"));
            copy.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            copy.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    copy.setText("Copié dans le presse papier");
                    copy.select();
                    copy.setIcon(AssetLoader.checkIco);
                    Inforeg.Save.Utils.copyToClipboard(d.getG().afficher());
                }
            });

            panel.add(scroll);
            panel.add(copy);
        }


        this.add(panel);
        
        
    }
    
    private JPanel showMatrix(Draw d) {
        int[][] matrix = d.getG().getAdjMatrix();
        int w = matrix.length, h = matrix[0].length;
        JPanel pane = new JPanel(new GridLayout(w , h));
        
        for (int i=0; i < w; i++) {
            for (int j=0; j < h; j++) {
                var box = new JTextField(String.valueOf(matrix[i][j]));
                box.setEditable(false);
                box.setPreferredSize(new Dimension(20,20));
                box.setHorizontalAlignment(JTextField.CENTER);
                pane.add(box);
            }
        }
        return pane;
    }
    
    

}
