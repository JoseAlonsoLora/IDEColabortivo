/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clasesApoyo;

import javafx.scene.image.ImageView;

/**
 *
 * @author alonso
 */
public class GraficosTreeItem {
    /**
     * Crea el icono de carpeta para agregarlo al árbol de proyectos
     *
     * @return Icono carpeta
     */
    public static ImageView crearIconoCarpeta() {
        ImageView carpeta;
        carpeta = new ImageView("/Imagenes/carpeta_1.png");
        carpeta.setFitHeight(15);
        carpeta.setFitWidth(15);
        return carpeta;
    }

    /**
     * Crea el icono de lenguaje de programación para agregarlo al árbol de
     * proyectos
     *
     * @param lenguajeProgramacion Lenguaje de programación
     * @return Icono de lenguaje de programación
     */
    public static ImageView crearIconoLenguaje(String lenguajeProgramacion) {
        ImageView lenguaje = null;
        switch (lenguajeProgramacion) {
            case "java":
                lenguaje = new ImageView("/Imagenes/java.png");
                lenguaje.setFitHeight(35);
                lenguaje.setFitWidth(35);
                break;
            case "c#":
                lenguaje = new ImageView("/Imagenes/c#.png");
                lenguaje.setFitHeight(20);
                lenguaje.setFitWidth(20);
                break;
            case "c++":
                lenguaje = new ImageView("/Imagenes/cpp_logo.png");
                lenguaje.setFitHeight(15);
                lenguaje.setFitWidth(15);
                break;
            default:
                break;
        }

        return lenguaje;
    }

    /**
     * Crea el icono del archivo para agregarlo al árbol de proyectos
     *
     * @param lenguajeProgramacion Lenguaje de programación
     * @return Icono del archivo
     */
    public static ImageView crearIconoArchivo(String lenguajeProgramacion) {
        ImageView lenguaje = null;
        switch (lenguajeProgramacion) {
            case "java":
                lenguaje = new ImageView("/Imagenes/archivoJava.png");
                lenguaje.setFitHeight(20);
                lenguaje.setFitWidth(20);
                break;
            case "c#":
                lenguaje = new ImageView("/Imagenes/archivoCSharp.png");
                lenguaje.setFitHeight(20);
                lenguaje.setFitWidth(20);
                break;
            case "c++":
                lenguaje = new ImageView("/Imagenes/cpp_logo.png");
                lenguaje.setFitHeight(15);
                lenguaje.setFitWidth(15);
                break;
            default:
                break;
        }

        return lenguaje;
    }
}
