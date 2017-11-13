/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.negocio;

import clasesApoyo.MyTab;
import controladores.PantallaPrincipalController;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.fxmisc.richtext.CodeArea;

/**
 *
 * @author raymu
 */
public class Archivo {
    private String contenido;
    private String nombreArchivo;
    private String ruta;

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
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
    
    public String compilarArchivo(File archivo){
        return null;
        
    }
    
    public String ejecutarArchivo(File archivo){
        return null;
        
    }
    
    public  boolean crearArchivo(String nombre){
        return false;
        
    }
    
    public boolean eliminarArchivo(File archivo){
        return false;
        
    }
    
    public boolean guardarArchivo(Archivo archivo){
        FileWriter fileWriter = null;
        PrintWriter printWriter = null;
        try {
            File file = new File(archivo.getRuta());
            fileWriter = new FileWriter(file);
            printWriter = new PrintWriter(fileWriter);
            printWriter.write(archivo.getContenido());          
        } catch (IOException ex) {
            Logger.getLogger(PantallaPrincipalController.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fileWriter.close();
                printWriter.close();
                
            } catch (IOException ex) {
                Logger.getLogger(PantallaPrincipalController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
        
    }
    
}
