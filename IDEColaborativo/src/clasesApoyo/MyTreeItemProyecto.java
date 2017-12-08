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
public class MyTreeItemProyecto extends TreeItem<String>{
    private String nombreProyecto;
    private String ruta;
    private String lenguaje;
    private ArrayList<String> nombreCarpetas;
    
    public MyTreeItemProyecto() {

    }

    public MyTreeItemProyecto(String nombreNodo, ImageView logo) {
        super(nombreNodo, logo);
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
    }

    public ArrayList<String> getNombreCarpetas() {
        return nombreCarpetas;
    }

    public void setNombreCarpetas(ArrayList<String> nombreCarpetas) {
        this.nombreCarpetas = nombreCarpetas;
    }
    
    
    
    
}
