/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clasesApoyo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.negocio.Proyecto;

/**
 *
 * @author Alonso Lora
 * @author Raymundo Pérez
 */
public class ArchivoConfiguracion {
    private String rutaProyectosLinux;
    private String rutaProyectosWindows;
    private String nombreCarpetaClases;
    private String nombreCarpetaCodigos;
    
    /**
     * Constructor por defecto
     */
    public ArchivoConfiguracion(){
        Properties p = new Properties();
        try (InputStream propertiesStream = ClassLoader.getSystemResourceAsStream("properties/config.properties")) {
            p.load(propertiesStream);
            rutaProyectosLinux = p.getProperty("rutaProyectosLinux");
            rutaProyectosWindows = p.getProperty("rutaProyectosWindows");
            nombreCarpetaClases = p.getProperty("nombreCarpetaClases");
            nombreCarpetaCodigos = p.getProperty("nombreCarpetaCodigos");            
        } catch (IOException ex) {
            Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, ex);
        }
        ClassLoader.getSystemResource("properties/config.properties");
    }

    /**
     * Regresa la ruta del archivo rutas en el sistema operativo Linux
     * @return Ruta del archivo rutas
     */
    public String getRutaProyectosLinux() {
        return rutaProyectosLinux;
    }

    /**
     * Da valor a la ruta del archivo rutas en el sistema operativo Linux
     * @param rutaProyectosLinux Ruta del archivo rutas
     */
    public void setRutaProyectosLinux(String rutaProyectosLinux) {
        this.rutaProyectosLinux = rutaProyectosLinux;
    }

    /**
     * Regresa la ruta del archivo rutas en el sistema operativo Windows
     * @return Ruta del archivo rutas
     */
    public String getRutaProyectosWindows() {
        return rutaProyectosWindows;
    }

    /**
     * Da valor a la ruta del archivo rutas en el sistema operativo Windows
     * @param rutaProyectosWindows Ruta del archivo rutas
     */
    public void setRutaProyectosWindows(String rutaProyectosWindows) {
        this.rutaProyectosWindows = rutaProyectosWindows;
    }

    /**
     * Nombre de la carpeta donde se encuentran los archivos compilados
     * @return Nombre de la carpeta de los archivos compilados
     */
    public String getNombreCarpetaClases() {
        return nombreCarpetaClases;
    }

    /**
     * Da valor al nombre de la carpeta donde se encuentran los archivos compilados
     * @param nombreCarpetaClases Nombre de la carpeta de los archivos compilados
     */
    public void setNombreCarpetaClases(String nombreCarpetaClases) {
        this.nombreCarpetaClases = nombreCarpetaClases;
    }

    /**
     * Nombre de la carpeta donde se encuentran los archivos con el código fuente
     * @return Nombre de la carpeta de los archivos con el código fuente
     */
    public String getNombreCarpetaCodigos() {
        return nombreCarpetaCodigos;
    }

    /**
     * Da valor al nombre de la carpeta donde se encuentran los archivos con el código fuente
     * @param nombreCarpetaCodigos Nombre de la carpeta de los archivos con el código fuente
     */
    public void setNombreCarpetaCodigos(String nombreCarpetaCodigos) {
        this.nombreCarpetaCodigos = nombreCarpetaCodigos;
    }
}
