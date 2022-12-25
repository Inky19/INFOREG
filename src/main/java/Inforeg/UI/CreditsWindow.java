package Inforeg.UI;

import Inforeg.AssetLoader;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Fenêtre des crédits
 *
 * @author François MARIE
 * @auhtor Rémi RAVELLI
 */
public class CreditsWindow extends JDialog {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 550;

    private Color bgColor;

    // Gradient de couleur utilisé pour l'animation de la version 2.0
    private static final Color[] gradient = {new Color(113, 120, 219), new Color(109, 116, 211), new Color(90, 110, 200), new Color(53, 107, 198), new Color(90, 110, 200), new Color(109, 116, 211), new Color(113, 120, 219)};

    private JPanel mainPanel;
    private JPanel logoPanel;
    private JPanel titlePanel;
    private JPanel panel1;
    private JPanel panel2;
    private AnimatedArea panel2Draw;

    // Taille du cadre de l'animation
    private Dimension draw2Dim;

    // Timer pour gérer l'animation de la version 2.0
    private Timer timer;

    private boolean frameFinished; // Indique si la frame graphique est terminée.

    public CreditsWindow(JFrame frame) throws IOException, InterruptedException {
        super(frame, "Crédits");
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (timer != null) {
                    timer.stop(); // Empêche le timer de continuer à tourner en arrière plan, même quand la fenêtre est fermée.
                }
            }
        });
        bgColor = getBackground();

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frameFinished = false;
        this.setResizable(false);
        this.setSize(WIDTH, HEIGHT);
        this.setLocationRelativeTo(null); // Centre la fenêtre à son lancement.

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        logoPanel = new JPanel();
        BufferedImage bannerImg = ImageIO.read(AssetLoader.getURL("asset/logoINFOREG.png"));
        Image bannerImgResized = bannerImg.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        JLabel banner = new JLabel(new ImageIcon(bannerImgResized));
        logoPanel.add(banner);
        logoPanel.setMaximumSize(new Dimension(WIDTH, 200));
        logoPanel.setPreferredSize(new Dimension(WIDTH, 200));

        JLabel logoText = new JLabel("<html><span style='font-size:20px'>Inforeg 2.0</span>");
        logoPanel.add(logoText);

        titlePanel = new JPanel();
        JLabel titleText = new JLabel("<html><span style='font-size:15px'>Crédits :</span>");
        titlePanel.setMaximumSize(new Dimension(WIDTH, 20));
        titlePanel.add(titleText);

        panel1 = new JPanel();

        JLabel panel1Text1 = new JLabel("<html><span style='font-size:12px'>Version 1.0 :</span><br/>"
                + "<i>Projet de PGROUP 2021-2022  </i></html>");
        JLabel source1 = new JLabel("Source", AssetLoader.githubIco, JLabel.CENTER);
        source1.setForeground(Color.GRAY.darker());
        source1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/IstyarConstantine/PGROU-INFOREG"));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });
        source1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        source1.setPreferredSize((new Dimension(WIDTH, source1.getPreferredSize().height)));
        JLabel panel1Text2 = new JLabel("<html><p style=\"text-align:center\">Béryl CASSEL, Samy AMAL,<br/>Cristobal CARRASCO DE RODT,<br/>Jorge QUISPE, Isaías VENEGAS</p></html>");

        panel1Text1.setPreferredSize(new Dimension(WIDTH - 150, panel1Text1.getPreferredSize().height));
        panel1Text1.setMaximumSize(new Dimension(WIDTH - 150, panel1Text1.getPreferredSize().height));
        panel1.add(panel1Text1);
        panel1.add(source1);
        panel1.add(panel1Text2);

        panel2 = new JPanel(new GridBagLayout());
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        JLabel panel2Text = new JLabel("<html><span style='font-size:12px'>Version 2.0 :</span><br/>"
                + "<i>Projet de PAPPL 2022-2023</i></html>"
        );
        panel2Text.setPreferredSize(new Dimension(WIDTH - 150, panel2Text.getPreferredSize().height));
        panel2Text.setMaximumSize(new Dimension(WIDTH - 150, panel2Text.getPreferredSize().height));
        panel2Text.setAlignmentX(CENTER_ALIGNMENT);

        JLabel source2 = new JLabel("Source", AssetLoader.githubIco, JLabel.CENTER);
        source2.setForeground(Color.GRAY.darker());
        source2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/Inky19/Projet-INFOREG"));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });
        source2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        source2.setPreferredSize((new Dimension(WIDTH, source2.getPreferredSize().height)));
        source2.setMaximumSize((new Dimension(WIDTH, source2.getPreferredSize().height)));
        source2.setAlignmentX(CENTER_ALIGNMENT);

        panel2Draw = new AnimatedArea();
        draw2Dim = new Dimension(WIDTH, 90);
        panel2Draw.setPreferredSize(draw2Dim);
        panel2Draw.setMaximumSize(draw2Dim);
        panel2Draw.setAlignmentX(CENTER_ALIGNMENT);
        int order = (int) (Math.random() * 2); // Ordre des noms aléatoire
        panel2Draw.addLine(new AnimatedText("François MARIE", 30 + (order * 35)));
        panel2Draw.addLine(new AnimatedText("Rémi RAVELLI", 30 + (1 - order) * 35));

        panel2.add(panel2Text);
        panel2.add(source2);
        panel2.add(panel2Draw);

        mainPanel.add(logoPanel);
        mainPanel.add(titlePanel);
        mainPanel.add(panel2);
        mainPanel.add(panel1);
        this.add(mainPanel);
        this.setModal(true);
        update();
    }

    /**
     * Fonction pour gérer l'animation de la version 2.0
     *
     * @throws InterruptedException
     */
    private void update() throws InterruptedException {
        int delay = 30; //milliseconds
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (frameFinished) {
                    frameFinished = false;
                    panel2Draw.repaint();
                }
            }
        };
        timer = new Timer(delay, taskPerformer);
        timer.setCoalesce(true);
        timer.start();
    }

    /**
     * JPanel contenant les différents textes animés.
     */
    private class AnimatedArea extends JPanel {

        private ArrayList<AnimatedText> lines;

        public AnimatedArea() {
            super();
            lines = new ArrayList<>();
        }

        public void addLine(AnimatedText text) {
            lines.add(text);
        }

        /**
         * Permet le rafraîchissement de l'animation
         *
         * @param g Graphics associé au panel
         */
        @Override
        public void paintComponent(Graphics g) {
            // On dessine sur une image en buffer pour éviter les problèmes de performances. (L'animation lag si on dessine directement sur g).
            BufferedImage img = new BufferedImage(draw2Dim.width, draw2Dim.height, BufferedImage.TYPE_3BYTE_BGR);
            Graphics gfx = img.getGraphics();

            Graphics2D gfx2D = (Graphics2D) gfx; // Cast en graphics2D pour pouvoir appliquer des méthodes 2D.
            gfx2D.setPaint(bgColor);
            gfx2D.fillRect(0, 0, img.getWidth(), img.getHeight());
            gfx2D.setFont(new Font("Monospace", Font.BOLD, 20)); // Police utilisée
            for (AnimatedText text : lines) {
                text.update(0.01f);
                text.draw(gfx);
            }
            g.drawImage(img, 0, 0, null);
            frameFinished = true; // La frame est terminée
        }
    }

    private class AnimatedText {

        private class Letter {

            public char chr;
            public float x;
            public float y;
            public Color color;

            public Letter(char chr) {
                this.chr = chr;
                x = 0;
                y = 25;
                color = Color.WHITE;
            }

        }

        private Letter[] text;
        private long time;
        private float yLine; // Position y de la ligne du texte
        private int colorIndex;

        public AnimatedText(String text, float y) {
            char[] chrArray = text.toCharArray();
            this.text = new Letter[text.length()];
            int kerning = 0; // Permet de gérer l'espacement des lettres
            for (int i = 0; i < text.length(); i++) {
                this.text[i] = new Letter(chrArray[i]);
                this.text[i].x = i * 15 + kerning;

                switch (Character.toLowerCase(this.text[i].chr)) {
                    case 'm':
                        kerning += 5;
                        break;
                    case 'i':
                        kerning += -2;
                        break;
                }
            }
            yLine = y;
            time = System.currentTimeMillis();
            colorIndex = 0;
        }

        public void update(float factor) {
            Letter l = null;

            // Défilement du gradient de couleur dans le temps
            if (System.currentTimeMillis() - time > 200) {
                time = System.currentTimeMillis();
                colorIndex++;
                if (colorIndex >= gradient.length) {
                    colorIndex = 0;
                }
            }

            int loopColor = colorIndex;
            for (int i = 0; i < text.length; i++) {
                l = text[i];

                // Changement de la couleur en fonction de la lettre.
                loopColor++;
                if (loopColor >= gradient.length) {
                    loopColor = 0;
                }
                l.color = gradient[loopColor];
                l.x += 200 * factor;

                // Retour à gauche du cadre
                if (l.x > draw2Dim.width) {
                    l.x = 0;
                }
                l.y = yLine + (float) (Math.cos(i + 0.005 * System.currentTimeMillis()) * 4);
            }
        }

        /**
         * Affiche les lettres du texte sur la zone graphique.
         *
         * @param gfx Graphics sur lequel dessiner
         */
        public void draw(Graphics gfx) {
            Graphics2D gfx2D = (Graphics2D) gfx;
            for (Letter l : text) {
                gfx2D.setColor(l.color);
                gfx2D.drawString(String.valueOf(l.chr), l.x, l.y);
            }
        }
    }
}
