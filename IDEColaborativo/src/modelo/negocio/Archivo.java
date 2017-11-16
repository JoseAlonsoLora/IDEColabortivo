/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.negocio;

import static com.sun.javafx.PlatformUtil.isWindows;
import controladores.PantallaPrincipalController;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.IntStream.builder;

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

    public String compilarArchivo(Archivo archivo) {
        String resultado = "";
        String[] lenguaje = archivo.getNombreArchivo().split("\\.");
        switch (lenguaje[1]) {
            case "java":
                resultado = compilarJava(archivo);
                break;
            case "cs":
                break;
            case "cpp":
                break;
        }
        return resultado;

    }

    public String compilarJava(Archivo archivo) {
        String resultado = "";
        try {
            String ruta = archivo.getRuta().replace("/"+archivo.getNombreArchivo(), "");
            String[] rutaClasesDividida = archivo.getRuta().split("/");
            String rutaClase = "";
            for(int i=0;i<rutaClasesDividida.length-3;i++){
                rutaClase+= rutaClasesDividida[i]+ "/" ;
            }
            rutaClase+= "clases";
            ProcessBuilder procesoCompilar;
            String comando = "javac -d "+rutaClase+" "+ archivo.getNombreArchivo();
            if (isWindows()) {
                procesoCompilar = new ProcessBuilder("cmd.exe", "/c", comando);
            } else {          
                procesoCompilar = new ProcessBuilder("bash", "-c", comando);
            }
            procesoCompilar.directory(new File(ruta));
            procesoCompilar.redirectErrorStream(true);
            Process process = procesoCompilar.start();
            InputStream out = process.getInputStream();
            byte[] buffer = new byte[4000];
            while (isAlive(process)) {
                int no = out.available();
                if (no > 0) {
                    int n = out.read(buffer, 0, Math.min(no, buffer.length));
                    resultado = new String(buffer, 0, n);

                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultado;
    }

    public static boolean isAlive(Process p) {
        try {
            p.exitValue();
            return false;
        } catch (IllegalThreadStateException e) {
            return true;
        }
    }

    public String ejecutarArchivo(File archivo) {
        return null;

    }

    public boolean crearArchivo(String nombre) {
        return false;

    }

    public boolean eliminarArchivo(File archivo) {
        return false;

    }

    public boolean guardarArchivo(Archivo archivo) {
        FileWriter fileWriter = null;
        PrintWriter printWriter = null;
        boolean seGuardo = false;
        try {
            File file = new File(archivo.getRuta());
            fileWriter = new FileWriter(file);
            printWriter = new PrintWriter(fileWriter);
            printWriter.write(archivo.getContenido());
            seGuardo = true;
        } catch (IOException ex) {
            Logger.getLogger(PantallaPrincipalController.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fileWriter.close();
                printWriter.close();
            } catch (IOException ex) {
                Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return seGuardo;

    }

}
