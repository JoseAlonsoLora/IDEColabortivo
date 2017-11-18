/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.negocio;

import static com.sun.javafx.PlatformUtil.isWindows;
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
 *
 * @author raymu
 */
public class Proyecto {

    private String lenguaje;
    private String nombreProyecto;
    private String rutaProyecto;
    private ArrayList<Carpeta> carpetas;

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
        if (!carpetaProyecto.exists()) {
            carpetaProyecto.mkdir();
            carpetaProyecto = new File(rutaProyecto + "/" + "codigo");
            carpetaProyecto.mkdir();
            carpetaProyecto = new File(rutaProyecto + "/" + "codigo/" + nombreProyecto);
            carpetaProyecto.mkdir();
            carpetaProyecto = new File(rutaProyecto + "/" + "clases");
            carpetaProyecto.mkdir();
            File archivoMain;
            try {
                switch (lenguaje) {
                    case "java":
                        archivoMain = new File(rutaProyecto + "/codigo/" + nombreProyecto + "/" + nombreProyecto + ".java");
                        archivoMain.createNewFile();
                        break;
                    case "c#":
                        archivoMain = new File(rutaProyecto + "/codigo/" + nombreProyecto + "/" + nombreProyecto + ".cs");
                        archivoMain.createNewFile();
                        break;
                    case "c++":
                        archivoMain = new File(rutaProyecto + "/codigo/" + nombreProyecto + "/" + nombreProyecto + ".cpp");
                        archivoMain.createNewFile();
                        break;

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
        String rutaArchivoProyectos;
        if (isWindows()) {
            rutaArchivoProyectos = "/Users/" + System.getProperty("user.name") + "/rutas.ide";
        } else {
            rutaArchivoProyectos = "/home/" + System.getProperty("user.name") + "/.rutas.ide";
        }
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
        String rutaArchivoProyectos;
        if (isWindows()) {
            rutaArchivoProyectos = "/Users/" + System.getProperty("user.name") + "/rutas.ide";
        } else {
            rutaArchivoProyectos = "/home/" + System.getProperty("user.name") + "/.rutas.ide";
        }
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

        } catch (FileNotFoundException ex) {
            Logger.getLogger(PantallaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PantallaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return proyectos;
    }

    public ArrayList<Carpeta> buscarCarpetas(String ruta) {
        ArrayList<Carpeta> carpetas = new ArrayList();
        ruta += "/codigo";
        File file = new File(ruta);
        if (file.exists()) {
            String[] carpetasCreadas = file.list();

            for (String carpeta1 : carpetasCreadas) {
                Carpeta carpeta = new Carpeta();
                carpeta.setNombreCarpeta(carpeta1);
                carpeta.setRutaCarpeta(ruta + "/" + carpeta1);
                carpeta.setArchivos(buscarArchivos(carpeta.getRutaCarpeta()));
                carpetas.add(carpeta);
            }
        }
        return carpetas;
    }

    public ArrayList<Archivo> buscarArchivos(String ruta) {
        ArrayList<Archivo> archivos = new ArrayList();
        File file = new File(ruta);
        String[] archivosCarpeta = file.list();

        for (String archivo1 : archivosCarpeta) {
            Archivo archivo = new Archivo();
            archivo.setRuta(ruta + "/" + archivo1);
            archivo.setNombreArchivo(archivo1);
            archivo.setContenido(leerArchivo(archivo.getRuta()));
            archivos.add(archivo);
        }

        return archivos;
    }

    public String leerArchivo(String ruta) {        
        String auxiliar = "";
        String contenidoArchivo = "";
        
        File file = new File(ruta);
        try (FileReader fileReader =new FileReader(file);
                BufferedReader contenido = new BufferedReader(fileReader)){            
            while ((auxiliar = contenido.readLine()) != null) {
                contenidoArchivo += auxiliar + "\n";
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(PantallaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PantallaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contenidoArchivo;
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

    public ArrayList<Programador> mostrarColaboradores() {
        return null;
    }

    public static void crearArchivoRutas() {
        File file;
        String ruta;
        if (isWindows()) {
            ruta = "/Users/" + System.getProperty("user.name") + "/rutas.ide";
        } else {
            ruta = "/home/" + System.getProperty("user.name") + "/.rutas.ide";
        }
        file = new File(ruta);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
