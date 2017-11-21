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
 * @author alonso
 */
public class ArchivoConfiguracion {
    private String rutaProyectosLinux;
    private String rutaProyectosWindows;
    private String nombreCarpetaClases;
    private String nombreCarpetaCodigos;
    
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
    }

    public String getRutaProyectosLinux() {
        return rutaProyectosLinux;
    }

    public void setRutaProyectosLinux(String rutaProyectosLinux) {
        this.rutaProyectosLinux = rutaProyectosLinux;
    }

    public String getRutaProyectosWindows() {
        return rutaProyectosWindows;
    }

    public void setRutaProyectosWindows(String rutaProyectosWindows) {
        this.rutaProyectosWindows = rutaProyectosWindows;
    }
    
    

    public String getNombreCarpetaClases() {
        return nombreCarpetaClases;
    }

    public void setNombreCarpetaClases(String nombreCarpetaClases) {
        this.nombreCarpetaClases = nombreCarpetaClases;
    }

    public String getNombreCarpetaCodigos() {
        return nombreCarpetaCodigos;
    }

    public void setNombreCarpetaCodigos(String nombreCarpetaCodigos) {
        this.nombreCarpetaCodigos = nombreCarpetaCodigos;
    }
    
    
    
}
