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
public class MyTreeItemProyecto extends TreeItem<String>{
    private String nombreProyecto;
    private String ruta;
    private String lenguaje;
    private ArrayList<String> nombreCarpetas;
    
    /**
     * Constructor por defecto de un TreeItem
     */
    public MyTreeItemProyecto() {

    }
    
    /**
     * Constructor sobrecargado de un TreeItem
     * @param nombreNodo Nombre que tendra el nodo
     * @param logo Imagen que tendra el nodo
     */
    public MyTreeItemProyecto(String nombreNodo, ImageView logo) {
        super(nombreNodo, logo);
    }

    /**
     * Regresa el valor del nombre del proyecto
     * @return Nombre del proyecto
     */
    public String getNombreProyecto() {
        return nombreProyecto;
    }
    
    /**
     * Da valor al nombre del proyecto
     * @param nombreProyecto Nombre que tendrá el proyecto
     */
    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    /**
     * Regresa la ruta donde se encuentra el proyecto
     * @return Ruta del proyecto
     */
    public String getRuta() {
        return ruta;
    }

    /**
     * Da valor a la ruta del proyecto
     * @param ruta Ruta del proyecto
     */
    public void setRuta(String ruta) {
        this.ruta = ruta;
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

    /**
     * Regresa el nombre de las carpetas del proyecto
     * @return Lista con el nombre de las carpetas del proyecto
     */
    public ArrayList<String> getNombreCarpetas() {
        return nombreCarpetas;
    }

    /**
     * Da valor al nombre de las carpetas de un proyecto
     * @param nombreCarpetas Lista con el nombre de las carpetas del proyecto
     */
    public void setNombreCarpetas(ArrayList<String> nombreCarpetas) {
        this.nombreCarpetas = nombreCarpetas;
    }
    
    
    
    
}
