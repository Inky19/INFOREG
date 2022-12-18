package Inforeg.UI;

import Inforeg.AssetLoader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import static java.awt.Font.BOLD;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author inky19
 */
public class CreditsWindow extends JDialog {
    
    private static final int WIDTH = 400;
    private static final int HEIGHT = 700;
    
    private JPanel mainPanel;
    private JPanel logoPanel;
    private JPanel titlePanel;
    private JPanel panel1;
    private JPanel panel2;
    private AnimatedArea panel2Draw; 
    
    public CreditsWindow(JFrame frame) throws IOException{
        super(frame, "Crédits");
        this.setResizable(false);
        this.setSize(WIDTH, HEIGHT);
        this.setLocationRelativeTo(null);
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        logoPanel = new JPanel();
        BufferedImage bannerImg = ImageIO.read(AssetLoader.getURL("asset/logoINFOREG.png"));
        Image bannerImgResized = bannerImg.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        JLabel banner = new JLabel(new ImageIcon(bannerImgResized));
        logoPanel.add(banner);
        
        JLabel logoText = new JLabel("Inforeg 2.0");
        logoPanel.add(logoText);
        
        
        titlePanel = new JPanel();
        JLabel titleText = new JLabel("Crédits :");
        titlePanel.add(titleText);
        
        panel1 = new JPanel();
        JLabel panel1Text = new JLabel("<html>Version 1.0 :<br/>" +
                "Projet de PGROUP 2021-2022<br/>"
                + "Béryl CASSEL, Samy AMAL,<br/>Cristobal CARRASCO DE RODT,<br/>Jorge QUISPE, Isaías VENEGAS</html>");
        panel1.add(panel1Text);
        
        panel2 = new JPanel(new GridBagLayout());
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        JLabel panel2Text = new JLabel("<html>Version 2.0<br/>"+
                "Projet de PAPPL 2022-2023</html>"
                );
        panel2Text.setAlignmentX(CENTER_ALIGNMENT);
        panel2Text.setHorizontalAlignment(JLabel.CENTER);
        panel2Draw = new AnimatedArea();
        Dimension draw2Dim = new Dimension(WIDTH-20, 100);
        panel2Draw.setPreferredSize(draw2Dim);
        panel2Draw.setMaximumSize(draw2Dim);
        panel2Draw.setAlignmentX(CENTER_ALIGNMENT);
        panel2Draw.setBackground(Color.BLUE);
        panel2Draw.addLine(new AnimatedText("Test"));
        
        panel2.add(panel2Text);
        panel2.add(panel2Draw);
        
        mainPanel.add(logoPanel);
        mainPanel.add(titlePanel);
        mainPanel.add(panel2);
        mainPanel.add(panel1);
        this.add(mainPanel);
        this.setModal(true);
        update();
    }
    
    private void update(){
        panel2Draw.repaint();
    }
    
    private class AnimatedArea extends JPanel{
        
        private ArrayList<AnimatedText> lines;
        
        public AnimatedArea(){
            super();
            lines = new ArrayList<>();
        }
        
        public void addLine(AnimatedText text){
            lines.add(text);
        }
        
        public void paint(Graphics gfx){
            super.paint(gfx);
            System.out.println("HERE");
            Graphics2D gfx2D = (Graphics2D) gfx;
            gfx2D.setColor(Color.yellow);
            gfx2D.setFont(new Font("BOLD", BOLD, 25));
            gfx2D.setColor(Color.black);
            gfx2D.drawString("TEST TEST TEST", 0, 0);
            for (AnimatedText text: lines){
                text.update(1);
                text.draw(gfx2D);
            }
        }
    }
    
    private class AnimatedText{
        
        private class Letter{
            
            public char chr;
            public float x;
            public float y;
            
            public Letter(char chr){
                this.chr = chr;
                x = 0;
                y = 0;
            }
            
        }
        
        private Letter[] text;
        private int time;
        
        public AnimatedText(String text){
            char[] chrArray = text.toCharArray();
            this.text = new Letter[text.length()];
            for (int i=0; i<text.length(); i++){
                this.text[i] = new Letter(chrArray[i]);
            }
            time = 0;
        }
        
        public void update(int time){
            this.time += time;
            for (Letter l: text){
                l.x += 2;
                l.y = (float) Math.cos(time);
            }
        }
        
        public void draw(Graphics2D gfx){
            for (Letter l: text){
                gfx.drawString(String.valueOf(l.chr), l.x, l.y);
            }
        }
    }
}
