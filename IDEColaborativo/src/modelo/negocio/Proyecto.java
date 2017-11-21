/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.negocio;

import clasesApoyo.ArchivoConfiguracion;
import static com.sun.javafx.PlatformUtil.isWindows;
import controladores.PantallaCrearProyectoController;
import controladores.PantallaPrincipalController;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author raymu
 */
public class Proyecto {

    private String lenguaje;
    private String nombreProyecto;
    private String rutaProyecto;
    private ArrayList<Carpeta> carpetas;
    private static ArchivoConfiguracion archivoConfig = new ArchivoConfiguracion();

    public String getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public String getRutaProyecto() {
        return rutaProyecto;
    }

    public void setRutaProyecto(String rutaProyecto) {
        this.rutaProyecto = rutaProyecto;
    }

    public ArrayList<Carpeta> getCarpetas() {
        return carpetas;
    }

    public void setCarpetas(ArrayList<Carpeta> carpetas) {
        this.carpetas = carpetas;
    }

    public boolean crearProyecto() {
        boolean proyetoCreado = false;
        File carpetaProyecto = new File(rutaProyecto);
        String rutaArchivoMain = rutaProyecto + archivoConfig.getNombreCarpetaCodigos() + nombreProyecto;
        if (!carpetaProyecto.exists()) {
            carpetaProyecto.mkdir();
            carpetaProyecto = new File(rutaProyecto + archivoConfig.getNombreCarpetaCodigos());
            carpetaProyecto.mkdir();
            carpetaProyecto = new File(rutaArchivoMain);
            carpetaProyecto.mkdir();
            carpetaProyecto = new File(rutaProyecto + archivoConfig.getNombreCarpetaClases());
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

    public void guardarRuta() {
        String rutaArchivoProyectos = obtenerRutaProyectos();
        File file = new File(rutaArchivoProyectos);
        try (FileWriter fileWriter = new FileWriter(file, true);
                PrintWriter pw = new PrintWriter(fileWriter)) {
            pw.append(rutaProyecto + "," + lenguaje + "," + nombreProyecto + "\n");
        } catch (IOException ex) {
            Logger.getLogger(PantallaCrearProyectoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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

    public ArrayList<Carpeta> buscarCarpetas(String ruta) {
        String rutaClases = ruta + archivoConfig.getNombreCarpetaClases();
        ArrayList<Carpeta> carpetas = new ArrayList();
        ruta += archivoConfig.getNombreCarpetaCodigos();
        File file = new File(ruta);
        if (file.exists()) {
            String[] carpetasCreadas = file.list();

            for (String carpeta1 : carpetasCreadas) {
                Carpeta carpeta = new Carpeta();
                carpeta.setNombreCarpeta(carpeta1);
                carpeta.setRutaCarpeta(ruta + carpeta1);
                carpeta.setArchivos(buscarArchivos(carpeta.getRutaCarpeta(),carpeta1,rutaClases));
                carpetas.add(carpeta);
            }
        }
        return carpetas;
    }

    public ArrayList<Archivo> buscarArchivos(String ruta,String paquete,String rutaClases) {
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

    public String leerArchivo(String ruta) {
        String auxiliar = "";
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

    public Proyecto cargarNuevoProyecto(Proyecto proyecto) {
        if ((new File(proyecto.getRutaProyecto())).exists()) {
            proyecto.setCarpetas(buscarCarpetas(proyecto.getRutaProyecto()));
        }
        return proyecto;
    }

    public boolean eliminarProyecto() {
        return false;

    }

    public List<Programador> mostrarColaboradores() {
        return null;
    }

    public void crearArchivoRutas() {
        String ruta = obtenerRutaProyectos();
        File file = new File(ruta);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public static String obtenerRutaProyectos() {
        String rutaArchivoProyectos;
        String palabraClave = "user.name";
        if (isWindows()) {
            rutaArchivoProyectos = archivoConfig.getRutaProyectosWindows().replace(palabraClave, System.getProperty(palabraClave));
        } else {
            rutaArchivoProyectos = archivoConfig.getRutaProyectosLinux().replace(palabraClave, System.getProperty(palabraClave));
        }
        return rutaArchivoProyectos;
    }

}
