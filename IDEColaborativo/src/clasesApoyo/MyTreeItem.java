/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clasesApoyo;

import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import modelo.negocio.Archivo;

/**
 *
 * @author Alonso Lora
 * @author Raymundo Pérez
 */
public class MyTreeItem extends TreeItem<String> {

    private Archivo archivo;
    private boolean modificado;
    private String rutaProyecto;
    private String rutaCarpeta;

    /**
     * Constructor por defecto de un TreeItem
     */
    public MyTreeItem() {

    }

    /**
     * Constructor sobrecargado de un TreeItem
     * @param nombreNodo Nombre que tendra el nodo
     * @param logo Imagen que tendra el nodo
     */
    public MyTreeItem(String nombreNodo, ImageView logo) {
        super(nombreNodo, logo);
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
     * Regresa la ruta donde se encuetra la carpeta
     * @return Ruta de la carpeta
     */
    public String getRutaCarpeta() {
        return rutaCarpeta;
    }

    /**
     * Da valor a la ruta donde se encuetra la carpeta
     * @param rutaCarpeta Ruta de la carpeta
     */
    public void setRutaCarpeta(String rutaCarpeta) {
        this.rutaCarpeta = rutaCarpeta;
    }
    
    /**
     * Regresa el archivo
     * @return Archivo
     */
    public Archivo getArchivo() {
        return archivo;
    }

    /**
     * Da valor al archivo
     * @param archivo Archivo
     */
    public void setArchivo(Archivo archivo) {
        this.archivo = archivo;
    }

    /**
     * Regresa un bandera indicado si el archivo se modifico
     * @return Indica si el archivo se modifico
     */
    public boolean isModificado() {
        return modificado;
    }

    /**
     * Da valor al atributo modificado
     * @param modificado Indica si fue modificado
     */
    public void setModificado(boolean modificado) {
        this.modificado = modificado;
    }

}
