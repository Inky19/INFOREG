package Inforeg.Save;

import Inforeg.Draw.Draw;
import Inforeg.Graph.Graph;
import Inforeg.ObjetGraph.Arc;
import Inforeg.ObjetGraph.Nail;
import Inforeg.ObjetGraph.Node;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Export en format LaTeX
 *
 * @author François MARIE
 * @author Rémi RAVELLI
 */
public class ExportLatex {

    private static int SCALE = 4;

    private int id;
    private HashMap<Node, Integer> nodeID;
    private HashMap<Nail, Integer> nailID;
    private int nodeSize;
    private int arcSize;
    private boolean adaptNodeSize;
    private boolean showNails;

    public ExportLatex() {
        id = 0;
        nodeSize = 0;
        arcSize = 0;
        nodeID = new HashMap<Node, Integer>();
        nailID = new HashMap<Nail, Integer>();
        adaptNodeSize = false;
        showNails = false;
    }

    public String export(Draw d, Color nodeColor, Color arcColor, int nodeSize, int arcSize, boolean adaptNodeSize, boolean showNails) {
        Graph G = d.getG();
        String res = "\\resizebox{15cm}{!}{\n\\begin{tikzpicture}[scale=0.05]\n";
        this.arcSize = arcSize;
        this.nodeSize = nodeSize;
        this.adaptNodeSize = adaptNodeSize;
        this.showNails = showNails;

        if (nodeColor == null) {
            for (Node n : G.getNodes()) {
                res += exportNode(n, n.getColorDisplayed(), adaptNodeSize);
            }
        } else {
            for (Node n : G.getNodes()) {
                res += exportNode(n, nodeColor, adaptNodeSize);
            }
        }
        if (d.getG().isOriente()) {
            res += "\\tikzstyle{style}=[->,line width=" + arcSize / 10.f + "mm]\n";
        } else {
            res += "\\tikzstyle{style}=[-,line width=" + arcSize / 10.f + "mm]\n";
        }
        String arcs = "";
        Color arcColorVar = Color.BLACK;
        for (Arc a : G.getLines()) {
            if (arcColor == null) {
                arcColorVar = a.getColorDisplayed();
            } else {
                arcColorVar = arcColor;
            }
            if (a.getFrom() == a.getTo()) {
                Nail nail = a.getNails().get(0);
                res += exportNail(nail, arcColorVar);
                arcs += "\\path (" + nodeID.get(a.getFrom()) + ") edge[blue, line width=" + arcSize + "mm, bend left=90] (" + nailID.get(nail) + ");\n";
                arcs += "\\path (" + nodeID.get(a.getFrom()) + ") edge[blue, line width=" + arcSize + "mm, bend right=90] (" + nailID.get(nail) + ");\n";
            } else {
                ArrayList<Nail> nails = a.getNails();
                if (!nails.isEmpty()) {
                    res += exportNail(nails.get(0), arcColorVar);
                    arcs += exportArcSegment(nodeID.get(a.getFrom()), nailID.get(nails.get(0)), arcColorVar);
                    for (int i = 1; i < nails.size(); i++) {
                        res += exportNail(nails.get(i), arcColorVar);
                        arcs += exportArcSegment(nailID.get(nails.get(i - 1)), nailID.get(nails.get(i)), arcColorVar);
                    }
                    arcs += exportArcSegment(nailID.get(nails.get(nails.size() - 1)), nodeID.get(a.getTo()), arcColorVar);
                } else {
                    arcs += exportArcSegment(nodeID.get(a.getFrom()), nodeID.get(a.getTo()), arcColorVar);
                }
            }
        }
        res += arcs;
        res += "\\end{tikzpicture}\n}";
        return res;
    }

    private String exportNode(Node n, Color c, boolean adaptNodeSize) {
        nodeID.put(n, id);
        String nodeRes = "";
        if (adaptNodeSize) {
            nodeRes += "\\node[draw, circle, fill=" + colotTikz(c)
                    + ", minimum size=" + String.valueOf(nodeSize) + "] ("
                    + String.valueOf(id) + ") at (" + String.valueOf(n.getCx() / SCALE) + "," + String.valueOf(-n.getCy() / SCALE) + ") {" + n.getLabel() + "};\n";
        } else {
            nodeRes += "\\node[draw, circle, fill=" + colotTikz(c)
                    + ", minimum size=" + String.valueOf(nodeSize) + ", label=center:" + n.getLabel() + "] ("
                    + String.valueOf(id) + ") at (" + String.valueOf(n.getCx() / SCALE) + "," + String.valueOf(-n.getCy() / SCALE) + ") {};\n";
        }

        id++;
        return nodeRes;
    }

    private String exportNail(Nail n, Color c) {
        String nailRes = "";
        nailID.put(n, id);
        int nailSize = 0;
        if (showNails) {
            nailSize = arcSize + 2;
        }
        nailRes += "\\node[draw, circle, fill=" + colotTikz(c)
                + ", minimum size=" + nailSize + ", inner sep=0pt] (" + String.valueOf(id) + ") at (" + String.valueOf(n.getCx() / SCALE) + "," + String.valueOf(-n.getCy() / SCALE) + ") {};\n";
        id++;
        return nailRes;
    }

    private String exportArcSegment(int from, int to, Color c) {
        String arcRes = "";
        arcRes += "\\draw[color=" + colotTikz(c) + ",style] (" + String.valueOf(from) + ")--(" + String.valueOf(to) + ") {};\n";
        return arcRes;
    }

    private String colotTikz(Color c) {
        return ("{rgb,255:red," + String.valueOf(c.getRed()) + ";green," + String.valueOf(c.getGreen()) + ";blue," + String.valueOf(c.getBlue()) + "}");
    }

}
