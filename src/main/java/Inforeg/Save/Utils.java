package Inforeg.Save;

import java.awt.Color;
import java.io.File;

/**
 * Fonctions utilitaires pour la sauvegarde et le chargement
 *
 * @author François MARIE
 * @author Rémi RAVELLI
 */
public abstract class Utils {

    /**
     * Formate le chemin du fichier et son nom avec l'extension si elle n'existe
     * pas.
     *
     * @param file Fichier cible
     * @return [name, path] : nom et chemin du fichier sous forme de String dans
     * une array de taille 2.
     */
    public static String[] formatPath(File file, String ext) {
        String extFormat = ext.toLowerCase();
        if (!ext.substring(0, 0).equals(".")) {
            extFormat = "." + extFormat;
        }
        String name = file.getName();
        String path = "";
        if (name.length() < extFormat.length() || !name.toLowerCase().substring(name.length() - extFormat.length()).equals(extFormat)) {
            path = file.getPath() + extFormat;
            name += extFormat;
        } else {
            path = file.getPath();
        }
        return new String[]{name, path};
    }

    /**
     * Fonction pour convertir une couleur de Color en format hexadécimal
     *
     * @param color Couleur à convertir
     * @return Code RGB hexadécimal en string : RRGGBB
     */
    public static String color2Hex(Color color) {
        String r = Integer.toHexString(color.getRed());
        String g = Integer.toHexString(color.getGreen());
        String b = Integer.toHexString(color.getBlue());
        String[] rgb = new String[]{r, g, b};
        for (int i = 0; i < 3; i++) {
            if (rgb[i].length() < 2) {
                rgb[i] = "0" + rgb[i];
            }
        }
        return (rgb[0] + rgb[1] + rgb[2]);
    }

    /**
     * Fonction pour convertir une couleur en format hexadécimal RRGGBB en
     * couleur de Color
     *
     * @param colorHex Couleur à convertir
     * @return Objet Color correspondant
     */
    public static Color hex2Color(String colorHex) {
        int R = Integer.decode("0x" + colorHex.substring(0, 2));
        int G = Integer.decode("0x" + colorHex.substring(2, 4));
        int B = Integer.decode("0x" + colorHex.substring(4, 6));
        return new Color(R, G, B);
    }
    
    public static void copyToClipboard(String text) {
        java.awt.Toolkit.getDefaultToolkit().getSystemClipboard()
        .setContents(new java.awt.datatransfer.StringSelection(text), null);
    }
}
