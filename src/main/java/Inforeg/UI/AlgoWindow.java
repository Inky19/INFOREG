package Inforeg.UI;

import Inforeg.Algo.*;
import Inforeg.Draw.Draw;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Fenêtre de sélection des algorithmes.
 *
 * @author François MARIE
 * @author Rémi RAVELLI
 */
public class AlgoWindow extends JDialog {

    private static final ImageIcon pathIco = new ImageIcon(AlgoWindow.class.getClassLoader().getResource("asset/icons/algo/path.png"));
    private static final ImageIcon scanIco = new ImageIcon(AlgoWindow.class.getClassLoader().getResource("asset/icons/algo/scan.png"));
    private static final ImageIcon colorationIco = new ImageIcon(AlgoWindow.class.getClassLoader().getResource("asset/icons/algo/coloration.png"));
    private static final ImageIcon flowIco = new ImageIcon(AlgoWindow.class.getClassLoader().getResource("asset/icons/algo/flow.png"));
    private static final ImageIcon treeIco = new ImageIcon(AlgoWindow.class.getClassLoader().getResource("asset/icons/algo/tree.png"));

    private Draw d;

    public AlgoWindow(JFrame frame, Draw d) {

        super(frame, "Liste des algorithmes");
        this.setResizable(false);
        this.d = d;

        this.setSize(500, 500);
        this.setLocationRelativeTo(null);

        AlgoBox path = new AlgoBox("Plus court chemin", this, pathIco);
        path.addAlgo(new Dijkstra());

        AlgoBox flow = new AlgoBox("Flot", this, flowIco);
        flow.addAlgo(new FordFulkerson());

        AlgoBox tree = new AlgoBox("Arbre", this, treeIco);
        tree.addAlgo(new PrimMST());
        tree.addAlgo(new KruskalMST());

        AlgoBox coloration = new AlgoBox("Coloration", this, colorationIco);
        coloration.addAlgo(new Coloration());

        AlgoBox parcours = new AlgoBox("Parcours", this, scanIco);
        parcours.addAlgo(new DFS());
        parcours.addAlgo(new BFS());

        JPanel dialPanel = new JPanel();
        dialPanel.setLayout(new GridLayout(3, 2));
        dialPanel.add(path);
        dialPanel.add(flow);
        dialPanel.add(tree);
        dialPanel.add(coloration);
        dialPanel.add(parcours);
        dialPanel.setVisible(true);

        this.add(dialPanel);
        this.setModal(true);
    }

    public void selectAlgo(Algorithm algo) {
        d.setAlgo(algo);
        this.dispose();
    }
}
