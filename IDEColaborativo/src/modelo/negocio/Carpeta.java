/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.negocio;

import java.io.File;
import java.util.ArrayList;

/**
 * La clase Carpeta permite guardar archivos compilados y archivos con el código
 * fuente
 *
 * @author Raymundo Pérez
 * @author Alonso Lora
 */
public class Carpeta {

    private String nombreCarpeta;
    private String rutaCarpeta;
    private ArrayList<Archivo> archivos;

    /**
     * Regresa la lista de archivos pertenecientes a la carpeta
     *
     * @return Lista de archivos
     */
    public ArrayList<Archivo> getArchivos() {
        return archivos;
    }

    /**
     * Da valor a la lista de archivos
     *
     * @param archivos Lista de archivos pertecientes a la carpeta
     */
    public void setArchivos(ArrayList<Archivo> archivos) {
        this.archivos = archivos;
    }

    /**
     * Regresa el nombre de la carpeta
     * @return Nombre de carpeta
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
     * Regresa la ruta donde se encuentra la carpeta
     * @return Ruta de la carpeta
     */
    public String getRutaCarpeta() {
        return rutaCarpeta;
    }

    /**
     * Da valor a la ruta de la carpeta
     * @param rutaCarpeta Ruta donde se encuenta la carpeta
     */
    public void setRutaCarpeta(String rutaCarpeta) {
        this.rutaCarpeta = rutaCarpeta;
    }

    /**
     * Crea una carpeta en el proyecto
     *
     * @param carpeta Carpeta que será creada
     * @return Indica si la carpeta se creo correctamente
     */
    public boolean crearCarpeta(Carpeta carpeta) {
        boolean seCreo;
        File file = new File(carpeta.getRutaCarpeta());
        seCreo = file.mkdir();
        return seCreo;
    }

    /**
     * Elimina una carpeta específica de un proyecto
     *
     * @param ruta Ruta específica de la carpeta
     * @return Indica si la carpeta se elimino correctamente
     */
    public boolean eliminarCarpeta(String ruta) {
        boolean seElimino;
        File file = new File(ruta);
        if (file.list().length > 0) {
            eliminarArchivosCarpeta(file.list(), ruta);
        }
        seElimino = file.delete();
        return seElimino;
    }

    /**
     * Elimina el contenido de la carpeta del proyecto
     *
     * @param archivos Archivos que deben ser eliminados
     * @param ruta Ruta de los archivos que serán eliminados
     */
    public void eliminarArchivosCarpeta(String[] archivos, String ruta) {
        for (String archivo : archivos) {
            String rutaArchivo = ruta + "/" + archivo;
            File file = new File(rutaArchivo);
            file.delete();
        }
    }
}
