/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.negocio;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author raymu
 */
public class Carpeta {
    private String nombreCarpeta;
    private String rutaCarpeta;
    private ArrayList<Archivo> archivos;

    public ArrayList<Archivo> getArchivos() {
        return archivos;
    }

    public void setArchivos(ArrayList<Archivo> archivos) {
        this.archivos = archivos;
    }
    

    public String getNombreCarpeta() {
        return nombreCarpeta;
    }

    public void setNombreCarpeta(String nombreCarpeta) {
        this.nombreCarpeta = nombreCarpeta;
    }

    public String getRutaCarpeta() {
        return rutaCarpeta;
    }

    public void setRutaCarpeta(String rutaCarpeta) {
        this.rutaCarpeta = rutaCarpeta;
    }
    
    public boolean crearCarpeta(Carpeta carpeta){
        boolean seCreo;
        File file = new File(carpeta.getRutaCarpeta());
        seCreo = file.mkdir();
        return seCreo;
    }
    
    public boolean eliminarCarpeta(String ruta){
        boolean seElimino = false;
        File file = new File(ruta);
        String[] archivos = file.list();
        if(archivos.length > 0){
            eliminarArchivosCarpeta(archivos, ruta);
        }
        seElimino = file.delete();
        return seElimino;
    }
    
    public void eliminarArchivosCarpeta(String[] archivos,String ruta){
        for(String archivo:archivos){
            String rutaArchivo = ruta+"/"+archivo;
            File file = new File(rutaArchivo);
            file.delete();
        }
    }
}
