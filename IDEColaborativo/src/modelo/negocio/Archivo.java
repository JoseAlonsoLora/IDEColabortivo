/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.negocio;

import clasesApoyo.HiloCompilador;
import static com.sun.javafx.PlatformUtil.isWindows;
import controladores.PantallaPrincipalController;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * La clase Archivo permite guardar y eliminar archivos, compilarlos, así como ejecutarlos.
 * @author Raymundo Pérez
 * @author Alonso Lora
 */
public class Archivo {

    private String contenido;
    private String nombreArchivo;
    private String ruta;
    private String rutaClases;
    private String paquete;
    
    /**
     * Regresa el paquete del archivo
     * @return Paquete del archivo
     */
    public String getPaquete() {
        return paquete;
    }
    
    /**
     * Da valor al paquete del archivo
     * @param paquete El nombre del paquete del archivo
     */
    public void setPaquete(String paquete) {
        this.paquete = paquete;
    }
    
    /**
     * Regresa la ruta donde se encuentran los archivos compilados
     * @return La ruta de los archivos compilados
     */
    public String getRutaClases() {
        return rutaClases;
    }
    
    /**
     * Da valor a la ruta de los archivos compilados
     * @param rutaClases Ruta de los archivos compilados
     */
    public void setRutaClases(String rutaClases) {
        this.rutaClases = rutaClases;
    }
    /**
     * Regresa el código fuente del archivo
     * @return Código fuente del archivo
     */
    public String getContenido() {
        return contenido;
    }
    
    /**
     * Da valor al contenido del archivo
     * @param contenido Código fuente del archivo
     */
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    
    /**
     * Regresa el nombre del archivo
     * @return Nombre del archivo
     */
    public String getNombreArchivo() {
        return nombreArchivo;
    }
    
    /**
     * Da valor al nombre del archivo
     * @param nombreArchivo Nombre del archivo
     */
    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }
    
    /**
     * Regresa la ruta donde se encuentra el archivo
     * @return Ruta del archivo
     */
    public String getRuta() {
        return ruta;
    }
    
    /**
     * Da valor a la ruta del archivo
     * @param ruta Ruta donde se encuentra el archivo
     */
    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
    
    /**
     * Permite compilar el archivo en los lenguajes de programación Java y C++
     * @param archivo Archivo que será compilado
     * @param rutaProyecto Ruta específica donde se encuentra el proyecto
     * @return Resultado de la compilación
     */
    public String compilarArchivo(Archivo archivo, String rutaProyecto) {
        String resultado = "";
        String[] lenguaje = archivo.getNombreArchivo().split("\\.");
        switch (lenguaje[1]) {
            case "java":
                resultado = compilarJava(archivo, rutaProyecto);
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
    
    /**
     * Invoca a la linea de comandos para compilar o ejecutar en los lenguajes de programación Java y C++
     * @param comando Comando ejecutado en la linea de comandos  
     * @param ruta Ruta específica donde se ejecutará el comando
     * @return Resultado de la ejecución del comando
     */
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
            HiloCompilador hilo = new HiloCompilador(procesoCompilar);
            hilo.start();
            Thread.sleep(1000);
            hilo.interrupt();
            resultadoCompilacion = hilo.getResultadoCompilacion();          
        } catch (InterruptedException ex) {
            Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultadoCompilacion.toString();
    }
    
    /**
     * Compila un archivo específico del lenguaje Java
     * @param archivo Archivo que será compilado
     * @param rutaProyecto Ruta específica donde se encuentra el proyecto
     * @return Resultado de compilar el archivo
     */
    public String compilarJava(Archivo archivo, String rutaProyecto) {
        String rutaClase = archivo.getRutaClases();
        StringBuilder comando = new StringBuilder();
        String caracter;
        File file = new File(rutaClase);
        if (!file.exists()) {
            file.mkdir();
        }
        if (isWindows()) {
            caracter = ".;";
        } else {
            caracter = ".:";
        }
        comando.append("javac -cp ").append("\"").append(caracter).append(rutaProyecto).append("/codigo/").append("\"")
                .append(" -d ").append(rutaClase).append(" ").append(archivo.getNombreArchivo());
        return compilador(comando.toString(), archivo.getRuta());
    }
    
    /**
     * Compila un archivo específico del lenguaje C++
     * @param archivo Archivo que será compilado
     * @param nombre Nombre del archivo compilado
     * @return Resultado de compilar el archivo
     */
    public String complilarCPlusPlus(Archivo archivo, String nombre) {
        String rutaClase = archivo.getRutaClases()+"/"+archivo.getPaquete();
        File file = new File(rutaClase);
        if (!file.exists()) {
            file.mkdir();
        }
        rutaClase += "/" + nombre;
        String comando = "g++ -o " + rutaClase + " " + archivo.getNombreArchivo();
        return compilador(comando, archivo.getRuta());
    }
    
    /**
     * Ejecuta un archivo específico en los lenguajes Java y C++
     * @param archivo Archivo que será ejecutado 
     * @return Resultado de ejecutar el programa
     */
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
    
    /**
     * Ejecuta un archivo específico en los lenguajes Java y C++
     * @param archivo Archivo que va a ser ejecutado 
     * @param parametros Parámetros que tomará el programa
     * @return Resultado de la ejecución del programa
     */

    public String ejecutarArchivo(Archivo archivo, String parametros) {
        String resultado = "";
        String[] lenguaje = archivo.getNombreArchivo().split("\\.");      
        switch (lenguaje[1]) {
            case "java":
                crearArchivoParametros(archivo.getRutaClases(), parametros);
                resultado = ejecutarJava(archivo, lenguaje[0], true);
                break;
            case "cs":
                break;
            case "cpp":
                crearArchivoParametros(archivo.getRutaClases()+archivo.getPaquete()+"/", parametros);
                resultado = ejecutarCPlusPlus(archivo, lenguaje[0], true);
                break;
            default:
                break;
        }
        return resultado;
    }

    /**
     * Crea el archivo donde el compilador obtendrá los parámetros necesarios
     * @param ruta Ruta donde se crea el archivo
     * @param parametros Lista de parámetros del programa
     */
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
    /**
     * Ejecuta un archivo especifico en el lenguaje Java
     * @param archivo Archivo que va a ser ejecutado
     * @param nombre Nombre el archivo que va a ser ejecutado
     * @param tieneParametros Indica si el programa necesita parámetros
     * @return Resultado de la ejecución del programa
     */
    public String ejecutarJava(Archivo archivo, String nombre, boolean tieneParametros) {
        StringBuilder comando = new StringBuilder();
        if (tieneParametros) {
            comando.append("java ").append(archivo.getPaquete()).append(".").append(nombre).append(" < ").append("archivoParametros");
        } else {
            comando.append("java ").append(archivo.getPaquete()).append(".").append(nombre);
        }
        return compilador(comando.toString(), archivo.getRutaClases());
    }
    
    /**
     * Ejecuta un archivo especifico en el lenguaje C++
     * @param archivo Archivo que va a ser ejecutado
     * @param nombre Nombre el archivo que va a ser ejecutado
     * @param tieneParametros Indica si el programa necesita parámetros
     * @return Resultado de la ejecución del programa
     */
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

        return compilador(comando.toString(), archivo.getRutaClases()+"/"+archivo.getPaquete());
    }
    
    /**
     * Crea el archivo donde guarda el código
     * @param archivo Archivo que va a ser guardo en el sistema
     * @return Indica si el archivo se guardo correctamente
     */
    public boolean crearArchivo(Archivo archivo) {
        boolean seCreo = false;
        String ruta = archivo.getRuta() + "/" + archivo.getNombreArchivo();
        File file = new File(ruta);
        try {
            seCreo = file.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return seCreo;
    }
    
    /**
     * Elimina el archivo del sistema
     * @param archivo Archivo que va a ser eliminado
     * @return Indica si el archivo se elimino correctamente
     */
    public boolean eliminarArchivo(Archivo archivo) {
        boolean seElimino;
        String ruta = archivo.getRuta() + "/" + archivo.getNombreArchivo();
        File file = new File(ruta);
        seElimino = file.delete();
        return seElimino;

    }
    
    /**
     * Actualiza el archivo con el nuevo contenido
     * @param archivo Archivo que va a ser guardado
     * @return Indica si el archivo se actualizo correctamente
     */
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
    
    /**
     * Agrega el paquete a los archivos del lenguaje Java
     * @param archivo Archivo al cual se le va agregar el paquete
     */
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
