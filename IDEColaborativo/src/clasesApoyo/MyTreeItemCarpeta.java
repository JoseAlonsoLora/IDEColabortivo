/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clasesApoyo;

import java.util.ArrayList;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;

/**
 *
 * @author Alonso Lora
 * @author Raymundo Pérez
 */
public class MyTreeItemCarpeta extends TreeItem<String>{
    private String nombreCarpeta;
    private String ruta;
    private ArrayList<String> nombreArchivos;
    private String rutaProyecto;
    private String lenguaje;
    
    /**
     * Constructor por defecto de un TreeItem
     */
    public MyTreeItemCarpeta() {

    }
    
    /**
     * Constructor sobrecargado de un TreeItem
     * @param nombreNodo Nombre que tendra el nodo
     * @param logo Imagen que tendra el nodo
     */
    public MyTreeItemCarpeta(String nombreNodo, ImageView logo) {
        super(nombreNodo, logo);
    }

    /**
     * Regresa el nombre de la carpeta
     * @return Nombre de la carpeta
     */
    public String getNombreCarpeta() {
        return nombreCarpeta;
    }

    /**
     * Da valor al nombre de la carpeta
     * @param nombreCarpeta Nombre de la carpeta
     */
    public void setNombreCarpeta(String nombreCarpeta) {
        this.nombreCarpeta = nombreCarpeta;
    }

    /**
     * Regresa la ruta donde se encuetra la carpeta
     * @return Ruta de la carpeta
     */
    public String getRuta() {
        return ruta;
    }

    /**
     * Da valor a la ruta donde se encuetra la carpeta
     * @param ruta Ruta de la carpeta
     */
    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    /**
     * Regresa la lista con el nombre de los archivos de la carpeta
     * @return Lista con el nombre de los archivos
     */
    public ArrayList<String> getNombreArchivos() {
        return nombreArchivos;
    }

    /**
     * Da valor a la lista con el nombre de los archivos de la carpeta
     * @param nombreArchivos Lista con el nombre de los archivos
     */
    public void setNombreArchivos(ArrayList<String> nombreArchivos) {
        this.nombreArchivos = nombreArchivos;
    }

    /**
     * Regresa la ruta donde se encuentra el proyecto
     * @return Ruta del proyecto
     */
    public String getRutaProyecto() {
        return rutaProyecto;
    }

    /**
     * Da valor a la ruta del proyecto
     * @param rutaProyecto Ruta del proyecto
     */
    public void setRutaProyecto(String rutaProyecto) {
        this.rutaProyecto = rutaProyecto;
    }

    /**
     * Regresa el lenguaje de programación del proyecto
     * @return Lenguaje de programación
     */
    public String getLenguaje() {
        return lenguaje;
    }

  /**
     * Da valor al leguaje de programación del proyecto
     * @param lenguaje Lenguaje de programación
     */
    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
    }    
}
