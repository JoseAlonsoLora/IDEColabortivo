/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clasesApoyo;

import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;

/**
 *
 * @author alonso
 */
    public class MyTreeItem extends TreeItem<String> {

        private String contenido;
        private String ruta;
        private String nombreArchivo;

        public MyTreeItem() {

        }

        public MyTreeItem(String nombreNodo, ImageView logo) {
            super(nombreNodo, logo);
        }

        public String getNombreArchivo() {
            return nombreArchivo;
        }

        public void setNombreArchivo(String nombreArchivo) {
            this.nombreArchivo = nombreArchivo;
        }

        public String getRuta() {
            return ruta;
        }

        public void setRuta(String ruta) {
            this.ruta = ruta;
        }

        public String getContenido() {
            return contenido;
        }

        public void setContenido(String contenido) {
            this.contenido = contenido;
        }

    }