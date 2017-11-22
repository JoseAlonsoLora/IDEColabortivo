/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.negocio;

import static com.sun.javafx.PlatformUtil.isWindows;
import controladores.PantallaPrincipalController;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author raymu
 */
public class Archivo {

    private String contenido;
    private String nombreArchivo;
    private String ruta;
    private String rutaClases;
    private String paquete;

    public String getPaquete() {
        return paquete;
    }

    public void setPaquete(String paquete) {
        this.paquete = paquete;
    }

    public String getRutaClases() {
        return rutaClases;
    }

    public void setRutaClases(String rutaClases) {
        this.rutaClases = rutaClases;
    }

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
                resultado = complilarCPlusPlus(archivo, lenguaje[0]);
                break;
            default:
                break;
        }
        return resultado;

    }

    public String compilador(String comando, String ruta) {
        StringBuilder resultadoCompilacion = new StringBuilder();
        ProcessBuilder procesoCompilar;
        try {
            if (isWindows()) {
                procesoCompilar = new ProcessBuilder("cmd.exe", "/c", comando);
            } else {
                procesoCompilar = new ProcessBuilder("bash", "-c", comando);
            }
            procesoCompilar.directory(new File(ruta));
            procesoCompilar.redirectErrorStream(true);
            Process process = procesoCompilar.start();
            InputStream out = process.getInputStream();
            String auxiliar;
            try (BufferedReader lector = new BufferedReader(new InputStreamReader(out))) {
                while ((auxiliar = lector.readLine()) != null) {
                    resultadoCompilacion.append(auxiliar).append("\n");
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultadoCompilacion.toString();
    }

    public String compilarJava(Archivo archivo) {
        String rutaClase = rutaClases;
        File file = new File(rutaClase);
        if (!file.exists()) {
            file.mkdir();
        }
        String comando = "javac -d " + rutaClase + " " + archivo.getNombreArchivo();
        return compilador(comando, archivo.getRuta());
    }

    public String complilarCPlusPlus(Archivo archivo, String nombre) {
        String rutaClase = rutaClases;
        File file = new File(rutaClase);
        if (!file.exists()) {
            file.mkdir();
        }
        rutaClase += "/" + nombre;
        String comando = "g++ -o " + rutaClase + " " + archivo.getNombreArchivo();
        return compilador(comando, archivo.getRuta());
    }

    public String ejecutarArchivo(Archivo archivo) {
        String resultado = "";
        String[] lenguaje = archivo.getNombreArchivo().split("\\.");
        switch (lenguaje[1]) {
            case "java":
                resultado = ejecutarJava(archivo, lenguaje[0], false);
                break;
            case "cs":
                break;
            case "cpp":
                resultado = ejecutarCPlusPlus(archivo, lenguaje[0], false);
                break;
            default:
                break;
        }
        return resultado;
    }

    public String ejecutarArchivo(Archivo archivo, String parametros) {
        String resultado = "";
        String[] lenguaje = archivo.getNombreArchivo().split("\\.");
        crearArchivoParametros(archivo.getRutaClases(), parametros);
        switch (lenguaje[1]) {
            case "java":
                resultado = ejecutarJava(archivo, lenguaje[0], true);
                break;
            case "cs":
                break;
            case "cpp":
                resultado = ejecutarCPlusPlus(archivo, lenguaje[0], true);
                break;
            default:
                break;
        }
        return resultado;
    }

    public void crearArchivoParametros(String ruta, String parametros) {
        ruta += "archivoParametros";
        File file = new File(ruta);
        try {
            file.createNewFile();
            try (FileWriter fileWriter = new FileWriter(file);
                    PrintWriter printWriter = new PrintWriter(fileWriter)) {
                String[] parametrosDivididos = parametros.split(",");
                for (String parametro : parametrosDivididos) {
                    printWriter.append(parametro + "\n");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String ejecutarJava(Archivo archivo, String nombre, boolean tieneParametros) {
        StringBuilder comando = new StringBuilder();
        if (tieneParametros) {
            comando.append("java ").append(archivo.getPaquete()).append(".").append(nombre).append(" < ").append("archivoParametros");
        } else {
            comando.append("java ").append(archivo.getPaquete()).append(".").append(nombre);
        }
        return compilador(comando.toString(), archivo.getRutaClases());
    }

    public String ejecutarCPlusPlus(Archivo archivo, String nombre, boolean tieneParametros) {
        StringBuilder comando = new StringBuilder();
        if (tieneParametros) {
            if (isWindows()) {
                comando.append(nombre).append(".exe").append(" < ").append("archivoParametros");
            } else {
                comando.append("./").append(nombre).append(" < ").append("archivoParametros");
            }
        } else {
            if (isWindows()) {
                comando.append(nombre).append(".exe");
            } else {
                comando.append("./").append(nombre);
            }
        }

        return compilador(comando.toString(), archivo.getRutaClases());
    }

    public boolean crearArchivo(String nombre) {
        return false;

    }

    public boolean eliminarArchivo(File archivo) {
        return false;

    }

    public boolean guardarArchivo(Archivo archivo) {
        boolean seGuardo = false;
        agregarPaquete(archivo);
        String rutaArchivo = archivo.getRuta() + "/" + archivo.getNombreArchivo();
        File file = new File(rutaArchivo);
        try (FileWriter fileWriter = new FileWriter(file);
                PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.write(archivo.getContenido());
            seGuardo = true;
        } catch (IOException ex) {
            Logger.getLogger(PantallaPrincipalController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return seGuardo;

    }

    public void agregarPaquete(Archivo archivo) {
        String[] lenguaje = archivo.getNombreArchivo().split("\\.");
        switch (lenguaje[1]) {
            case "java":
                if (!archivo.getContenido().contains("package " + archivo.getPaquete() + ";")) {
                    StringBuilder auxiliar = new StringBuilder();
                    auxiliar.append("package ").append(archivo.getPaquete()).append(";\n").append(archivo.getContenido());
                    archivo.setContenido(auxiliar.toString());
                }
                break;
            case "cs":
                break;
            default:
                break;
        }

    }

}
