/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.negocio;

import clasesApoyo.ArchivoConfiguracion;
import static com.sun.javafx.PlatformUtil.isLinux;
import controladores.PantallaCrearProyectoController;
import controladores.PantallaPrincipalController;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * La clase Proyecto crea, actualiza y elimina proyectos del usuario
 * @author Raymundo Pérez
 * @author Alonso Lora
 */
public class Proyecto {

    private String lenguaje;
    private String nombreProyecto;
    private String rutaProyecto;
    private ArrayList<Carpeta> carpetas;
    private final static ArchivoConfiguracion ARCHIVO_CONFIGURACION = new ArchivoConfiguracion();
    
    /**
     * Regresa el lenguaje de programación del proyecto
     * @return Lenguaje de programación
     */
    public String getLenguaje() {
        return lenguaje;
    }
    
    /**
     * Da valor al lenguaje de programamción del proyecto
     * @param lenguaje Lenguaje de programación
     */
    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
    }
    
    /**
     * Regresa el nombre del proyecto
     * @return Nombre del proyecto
     */
    public String getNombreProyecto() {
        return nombreProyecto;
    }

    /**
     * Da valor al nombre del proyecto
     * @param nombreProyecto Nombre del proyecto
     */
    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }
    
    /**
     * Regresa la ruta donde se encuentra el proyecto
     * @return Ruta del proyecto
     */
    public String getRutaProyecto() {
        return rutaProyecto;
    }

    /**
     * Da valor a la ruta del proyecto
     * @param rutaProyecto Ruta donde se encuentra el proyecto
     */
    public void setRutaProyecto(String rutaProyecto) {
        this.rutaProyecto = rutaProyecto;
    }

    /**
     * Regresa la lista de carpetas pertenecientes a un proyecto
     * @return Lista de carpetas
     */
    public ArrayList<Carpeta> getCarpetas() {
        return carpetas;
    }

    /**
     * Da valor a la lista de carpetas
     * @param carpetas Lista de carpetas pertenecientes a un proyecto
     */
    public void setCarpetas(ArrayList<Carpeta> carpetas) {
        this.carpetas = carpetas;
    }
    
    /**
     * El archivo contiene las rutas de los proyectos creados, para su posterior lectura
     * @return Indica si el archivo se creo correctamente
     */   
    public boolean crearArchivoRutas() {
        boolean seCreo = false;
        String ruta = obtenerRutaProyectos();
        File file = new File(ruta);
        if (!file.exists()) {
            try {
                seCreo = file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return seCreo;
    }
    
    /**
     * Guarda la ruta del nuevo proyecto en el archivo de rutas
     * @return Indica si el archivo se actualizó con la nueva cadena
     */
    public boolean guardarRuta() {
        boolean seActualizo = false;
        String rutaArchivoProyectos = obtenerRutaProyectos();
        File file = new File(rutaArchivoProyectos);
        try (FileWriter fileWriter = new FileWriter(file, true);
                PrintWriter pw = new PrintWriter(fileWriter)) {
            pw.append(rutaProyecto + "," + lenguaje + "," + nombreProyecto + "\n");
            seActualizo = true;
        } catch (IOException ex) {
            Logger.getLogger(PantallaCrearProyectoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return seActualizo;
    }
    
    /**
     * Elimina la ruta de de un proyecto del archivo de rutas
     * @param cadenaActualizadda Contenido del archivo de rutas actualizado
     * @return Indica si el archivo se actualizó correctamente
     */
    public boolean actualizarArchivoRutas(String cadenaActualizadda) {
        boolean seActualizo = false;
        File file = new File(obtenerRutaProyectos());
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(file))) {
            printWriter.write(cadenaActualizadda);
            seActualizo= true;
        } catch (IOException ex) {
            Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return seActualizo;
    }
        
    /**
     * Elimina el proyecto del archivo rutas
     * @param proyecto Proyecto que será eliminado del archivo de rutas
     * @return Indica si el archivo se actualizó correctamente
     */
    public boolean eliminarRutaDeProyecto(Proyecto proyecto) {
        boolean seElimino= false;
        File file = new File(obtenerRutaProyectos());
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String auxiliar;
            String contenidoArchivo = "";
            StringBuilder rutaEliminar = new StringBuilder();
            rutaEliminar.append(proyecto.getRutaProyecto()).append(",").append(proyecto.getLenguaje()).append(",").append(proyecto.getNombreProyecto());
            while ((auxiliar = bufferedReader.readLine()) != null) {
                if (!auxiliar.equals(rutaEliminar.toString())) {
                    contenidoArchivo += auxiliar + "\n";
                }
            }       
            seElimino = actualizarArchivoRutas(contenidoArchivo);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return seElimino;
    }
    
    /**
     * Obtiene la ruta del archivo de rutas de los proyectos
     * @return La ruta del archivo rutas de los proyectos
     */
    public static String obtenerRutaProyectos() {
        String rutaArchivoProyectos;
        String palabraClave = "user.name";
        if (isLinux()) {
            rutaArchivoProyectos = ARCHIVO_CONFIGURACION.getRutaProyectosLinux().replace(palabraClave, System.getProperty(palabraClave));
        } else {
            rutaArchivoProyectos = ARCHIVO_CONFIGURACION.getRutaProyectosWindows().replace(palabraClave, System.getProperty(palabraClave));
        }

        return rutaArchivoProyectos;
    }

    /**
     * Crea un proyecto de los lenguajes Java y C++
     * @return Indica si el proyecto se creo correctamente
     */
    public boolean crearProyecto() {
        boolean proyetoCreado = false;
        File carpetaProyecto = new File(rutaProyecto);
        String rutaArchivoMain = rutaProyecto + ARCHIVO_CONFIGURACION.getNombreCarpetaCodigos() + nombreProyecto;
        if (!carpetaProyecto.exists()) {
            carpetaProyecto.mkdir();
            carpetaProyecto = new File(rutaProyecto + ARCHIVO_CONFIGURACION.getNombreCarpetaCodigos());
            carpetaProyecto.mkdir();
            carpetaProyecto = new File(rutaArchivoMain);
            carpetaProyecto.mkdir();
            carpetaProyecto = new File(rutaProyecto + ARCHIVO_CONFIGURACION.getNombreCarpetaClases());
            carpetaProyecto.mkdir();
            File archivoMain = null;
            rutaArchivoMain += "/";
            try {
                switch (lenguaje) {
                    case "java":
                        archivoMain = new File(rutaArchivoMain + nombreProyecto + ".java");
                        break;
                    case "c#":
                        archivoMain = new File(rutaArchivoMain + nombreProyecto + ".cs");
                        break;
                    case "c++":
                        archivoMain = new File(rutaArchivoMain + nombreProyecto + ".cpp");
                        break;
                    default:
                        break;
                }
                if (archivoMain != null) {
                    archivoMain.createNewFile();
                }

            } catch (IOException ex) {
                Logger.getLogger(PantallaCrearProyectoController.class.getName()).log(Level.SEVERE, null, ex);
            }
            guardarRuta();
            proyetoCreado = true;
        }

        return proyetoCreado;

    }

    /**
     * Carga en memoria todos los proyectos creados por el usuario
     * @return Proyectos creados por el usuario
     */
    public ArrayList<Proyecto> cargarProyectos() {
        ArrayList<Proyecto> proyectos = new ArrayList();
        String rutaArchivoProyectos = obtenerRutaProyectos();
        File file = new File(rutaArchivoProyectos);
        try (FileReader fileReader = new FileReader(file);
                BufferedReader contenido = new BufferedReader(fileReader)) {
            String ruta;
            while ((ruta = contenido.readLine()) != null) {
                String[] informacionProyecto = ruta.split(",");
                if ((new File(informacionProyecto[0])).exists()) {
                    Proyecto proyecto = new Proyecto();
                    proyecto.setRutaProyecto(informacionProyecto[0]);
                    proyecto.setLenguaje(informacionProyecto[1]);
                    proyecto.setNombreProyecto(informacionProyecto[2]);
                    proyecto.setCarpetas(buscarCarpetas(informacionProyecto[0]));
                    proyectos.add(proyecto);
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(PantallaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return proyectos;
    }
    
    /**
     * Busca las carpetas pertenecientes a un proyecto
     * @param ruta Ruta específica donde busca las carpetas
     * @return Carpetas pertenecientes a un proyecto
     */
    public ArrayList<Carpeta> buscarCarpetas(String ruta) {
        String rutaClases = ruta + ARCHIVO_CONFIGURACION.getNombreCarpetaClases();
        ArrayList<Carpeta> carpetas = new ArrayList();
        ruta += ARCHIVO_CONFIGURACION.getNombreCarpetaCodigos();
        File file = new File(ruta);
        if (file.exists()) {
            String[] carpetasCreadas = file.list();

            for (String carpeta1 : carpetasCreadas) {
                Carpeta carpeta = new Carpeta();
                carpeta.setNombreCarpeta(carpeta1);
                carpeta.setRutaCarpeta(ruta + carpeta1);
                carpeta.setArchivos(buscarArchivos(carpeta.getRutaCarpeta(), carpeta1, rutaClases));
                carpetas.add(carpeta);
            }
        }
        return carpetas;
    }
    
    /**
     * Busca los archivos pertenecientes a un proyecto
     * @param ruta Ruta específica donde buscar archivos pertenecientes a un proyecto
     * @param paquete Paquete en el que se encuentra el archivo
     * @param rutaClases Ruta donde se guardarán los archivos compilados
     * @return Archivos pertenecientes a un proyecto
     */
    public ArrayList<Archivo> buscarArchivos(String ruta, String paquete, String rutaClases) {
        ArrayList<Archivo> archivos = new ArrayList();
        File file = new File(ruta);
        String[] archivosCarpeta = file.list();
        for (String archivo1 : archivosCarpeta) {
            Archivo archivo = new Archivo();
            archivo.setRuta(ruta);
            String rutaArchivo = ruta + "/" + archivo1;
            file = new File(rutaArchivo);
            if (!file.isDirectory()) {
                archivo.setNombreArchivo(archivo1);
                archivo.setPaquete(paquete);
                archivo.setRutaClases(rutaClases);
                archivo.setContenido(leerArchivo(rutaArchivo));
                archivos.add(archivo);
            }
        }

        return archivos;
    }
    
    /**
     * Lee el código fuente de un archivo
     * @param ruta Ruta del archivo que será leido
     * @return El contenido del archivo
     */
    public String leerArchivo(String ruta) {
        String auxiliar;
        StringBuilder contenidoArchivo = new StringBuilder();
        File file = new File(ruta);
        if (!file.isDirectory()) {
            try (FileReader fileReader = new FileReader(file);
                    BufferedReader contenido = new BufferedReader(fileReader)) {
                while ((auxiliar = contenido.readLine()) != null) {
                    contenidoArchivo.append(auxiliar).append("\n");
                }
            } catch (IOException ex) {
                Logger.getLogger(PantallaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return contenidoArchivo.toString();
    }
    
    /**
     * Después de crear un nuevo proyecto se carga en memoria para su manipulación
     * @param proyecto Proyecto que será cargado en memoria
     * @return El contenido del proyecto
     */
    public Proyecto cargarNuevoProyecto(Proyecto proyecto) {
        if ((new File(proyecto.getRutaProyecto())).exists()) {
            proyecto.setCarpetas(buscarCarpetas(proyecto.getRutaProyecto()));
        }
        return proyecto;
    }

    /**
     * Elimina el proyecto indicado por el usuario
     * @param ruta Ruta específica donde se encuetra el proyecto
     * @return Indica si el proyecto se elimino correctamente
     */
    public boolean eliminarProyecto(String ruta) {
        boolean seElimino = false;
        File file = new File(ruta);
        String[] carpetas = file.list();
        for (String carpeta : carpetas) {
            File fileSegundoNivel = new File(ruta + "/" + carpeta);
            String[] carpetasSegundoNivel = fileSegundoNivel.list();
            for (String carpetaSegundoNivel : carpetasSegundoNivel) {
                Carpeta carpetaNegocio = new Carpeta();
                carpetaNegocio.eliminarCarpeta(fileSegundoNivel.getPath() + "/" + carpetaSegundoNivel);
            }
            fileSegundoNivel.delete();
        }
        seElimino = file.delete();
        return seElimino;
    }

}
