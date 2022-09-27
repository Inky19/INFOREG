/*=============================================
Classe SauvDraw permettant de sauvegarder 
le graphe dessiné
Auteur : Béryl CASSEL
Date de création : 11/03/2022
Date de dernière modification : 18/03/2022
Commentaires ajoutés
=============================================*/

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.swing.JOptionPane;

public class SauvDraw{

    /**
     * Fichier de destination de la sauvegarde
     */
    private final File source;

    /**
     * BufferedWritter pour écrire dans le fichier
     */
    private BufferedWriter fichier;

    /**
     * Delimiter pour l'écriture du fichier
     */
    private final String del="§";

    /**
     * Constructeur d'une sauvegarde
     * @param source : fichier de destination
     */
    public SauvDraw(File source){
        this.source = source;
    }

    /**
     * Méthode de sauvegarde du Draw passé en paramètre
     * @param d
     */
    public void sauvegarderDraw(Draw d){
        try {

            //Si l'utilisateur ne précise pas l'extension ".inforeg", on l'ajoute
            if (source.getName().length() < 8 || !source.getName().toLowerCase().substring(source.getName().length()-8).equals(".inforeg")) {
                fichier = new BufferedWriter(new FileWriter(source.getPath()+".inforeg"));
            } else {
                fichier = new BufferedWriter(new FileWriter(source.getPath()));
            }

            /*Sauvegarde des caractéristiques principales du Draw 
            (caractère pondéré/orienté, nombre de cercles et d'arcs...)*/
            int b = 0;
            if (d.getPondere()){
                b = 1;
            }
            fichier.write(String.valueOf(d.getOriente())
                            + del + String.valueOf(d.getNumOfCircles())
                            + del + String.valueOf(d.getNumOfLines())
                            + del + String.valueOf(Math.round(d.getCircleW()))
                            + del + String.valueOf(Math.round(d.getLineWidth()))
                            + del + String.valueOf(b));

            //Sauvegarde des cercles du Draw et de leur label
            for (int i=0;i<d.getNumOfCircles();i++){
                fichier.newLine();
                fichier.write((int) d.getCirc()[i].x 
                            + del + (int) d.getCirc()[i].y
                            + del + d.getCircLbl()[i]);
            }

            //Sauvegarde des arcs du Draw
            for (int i=0;i<d.getNumOfLines();i++){
                fichier.newLine();
                fichier.write(d.getLines().get(i).getFromPoint().x 
                                + del + d.getLines().get(i).getFromPoint().y
                                + del + d.getLines().get(i).getToPoint().x
                                + del + d.getLines().get(i).getToPoint().y
                                + del + d.getLines().get(i).getPoids()
                                + del + d.getLines().get(i).getC().getRGB()
                                + del + d.getLines().get(i).getClouPoint().x
                                + del + d.getLines().get(i).getClouPoint().y);
            }

            fichier.flush();
            fichier.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossible de sauvegardé le graphe.", "Sauvegarde Impossible !", JOptionPane.INFORMATION_MESSAGE);
        }
    }

}