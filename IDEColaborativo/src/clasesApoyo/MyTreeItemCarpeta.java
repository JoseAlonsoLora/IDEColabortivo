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
 * @author alonso
 */
public class MyTreeItemCarpeta extends TreeItem<String>{
    private String nombreCarpeta;
    private String ruta;
    private ArrayList<String> nombreArchivos;
    private String rutaProyecto;
    private String lenguaje;
    
    public MyTreeItemCarpeta() {

    }

    public MyTreeItemCarpeta(String nombreNodo, ImageView logo) {
        super(nombreNodo, logo);
    }

    public String getNombreCarpeta() {
        return nombreCarpeta;
    }

    public void setNombreCarpeta(String nombreCarpeta) {
        this.nombreCarpeta = nombreCarpeta;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public ArrayList<String> getNombreArchivos() {
        return nombreArchivos;
    }

    public void setNombreArchivos(ArrayList<String> nombreArchivos) {
        this.nombreArchivos = nombreArchivos;
    }

    public String getRutaProyecto() {
        return rutaProyecto;
    }

    public void setRutaProyecto(String rutaProyecto) {
        this.rutaProyecto = rutaProyecto;
    }

    public String getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
    }
    
    
    
    
    
}
