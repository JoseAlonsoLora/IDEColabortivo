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
 * @author alonso
 */
public class MyTreeItem extends TreeItem<String> {

    private Archivo archivo;
    private boolean modificado;
    private String rutaProyecto;
    private String rutaCarpeta;

    public MyTreeItem() {

    }

    public MyTreeItem(String nombreNodo, ImageView logo) {
        super(nombreNodo, logo);
    }

    public String getRutaProyecto() {
        return rutaProyecto;
    }

    public void setRutaProyecto(String rutaProyecto) {
        this.rutaProyecto = rutaProyecto;
    }

    public String getRutaCarpeta() {
        return rutaCarpeta;
    }

    public void setRutaCarpeta(String rutaCarpeta) {
        this.rutaCarpeta = rutaCarpeta;
    }

    
    
    public Archivo getArchivo() {
        return archivo;
    }

    public void setArchivo(Archivo archivo) {
        this.archivo = archivo;
    }

    public boolean isModificado() {
        return modificado;
    }

    public void setModificado(boolean modificado) {
        this.modificado = modificado;
    }

}
