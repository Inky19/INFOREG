/*=============================================
Classe ChargeDraw permettant de charger
un graphe sauvegardé
Auteur : Béryl CASSEL
Date de création : 11/03/2022
Date de dernière modification : 18/03/2022
Commentaires ajoutés
=============================================*/

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class ChargeDraw {

    /**
     * Fichier source pour la charge
     */
    private final File source;

    /**
     * Délimiter pour la lecture du fichier
     */
    private final String del = "§";

    /**
     * Constructeur d'une charge
     * @param source : fichier à charger
     */
    public ChargeDraw(File source){
        this.source = source;
    }

    /**
     * Méthode renvoyant un Draw correspondant à la charge
     * @return un Draw à afficher dans notre interface
     * @throws FileNotFoundException
     * @throws IOException
     */
    public Draw chargerDraw() throws FileNotFoundException, IOException{
        Draw d = new Draw();
        //On mémorise le chemin d'accès au fichier dans le Draw pour faciliter sa sauvegarde future
        d.setPathSauvegarde(this.source.getPath());

        try (BufferedReader fichier = new BufferedReader(new FileReader(source.getPath()))) {
            String ligne = fichier.readLine();
            StringTokenizer tokenizer = new StringTokenizer(ligne, del);

            /*On récupère les éléments principaux (caractère orienté/pondéré, 
            nombre d'arcs et de noeuds, taille des cercles et des arcs...)*/
            d.setOriente(Integer.parseInt(tokenizer.nextToken()));
            int nC = Integer.parseInt(tokenizer.nextToken());
            int nA = Integer.parseInt(tokenizer.nextToken());
            d.setCircleW((double) Integer.parseInt(tokenizer.nextToken()));
            d.setLineWidth((float) Integer.parseInt(tokenizer.nextToken()));
            int b = Integer.parseInt(tokenizer.nextToken());
            if (b==0){
                d.setPondere(false);
            }

            //On ajoute les cercles sauvegardés au Draw
            for (int j=0;j<nC;j++){
                addCircle(fichier.readLine(),j, d);
            }
            //On ajoute les arcs sauvegardés au Draw
            for (int i=0;i<nA;i++){
                addArc(fichier.readLine(), d);
            }
            fichier.close();
        }
        return d;
    }
    
    /**
     * Ajoute un cercle sauvegardé au Draw passé en paramètre
     * @param ligne : ligne de sauvegarde du cercle (coordonnées, label)
     * @param ind : indice du cercle dans la liste d.circ
     * @param d : Draw à modifier
     */
    private void addCircle(String ligne, int ind, Draw d){
        StringTokenizer tokenizer = new StringTokenizer(ligne, del);

        //On rajout ele cercle à la liste d.circ
        int x = Integer.parseInt(tokenizer.nextToken());
        int y = Integer.parseInt(tokenizer.nextToken());
        d.add(x,y);

        //On ajoute le label du cercle à la liste d.circLbl
        d.getCircLbl()[ind] = tokenizer.nextToken();
    }

    /**
     * Ajoute un arc au Draw passé en paramètre
     * @param ligne : ligne de sauvegarde de l'arc (nœuds correspondants, poids, clou)
     * @param d : Draw à modifier
     */
    private void addArc(String ligne, Draw d){
        StringTokenizer tokenizer = new StringTokenizer(ligne, del);
        int x1 = Integer.parseInt(tokenizer.nextToken());
        int y1 = Integer.parseInt(tokenizer.nextToken());
        int x2 = Integer.parseInt(tokenizer.nextToken());
        int y2 = Integer.parseInt(tokenizer.nextToken());
        Ellipse2D.Double from = d.getCirc()[d.findEllipse(x1,y1)];
        Ellipse2D.Double to = d.getCirc()[d.findEllipse(x2,y2)];
        int p = Integer.parseInt(tokenizer.nextToken());
        int rgb = Integer.parseInt(tokenizer.nextToken());
        Color c = new Color(rgb);
        MyLine arc = new MyLine(from,to, p, c);
        int x3 = Integer.parseInt(tokenizer.nextToken());
        int y3 = Integer.parseInt(tokenizer.nextToken());
        Ellipse2D.Double clou = new Ellipse2D.Double(x3,y3,MyLine.RCLOU,MyLine.RCLOU);
        arc.setClou(clou);
        d.addLine(arc);
    }
}
