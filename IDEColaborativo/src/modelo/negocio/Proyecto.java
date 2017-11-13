/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.negocio;

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
        FileWriter fileWriter = null;
        PrintWriter pw = null;
        try {
            File file = new File("C:/Users/raymu/Desktop/rutas.txt");
            fileWriter = new FileWriter(file, true);
            pw = new PrintWriter(fileWriter);
            pw.append(rutaProyecto + "," + lenguaje + "," + nombreProyecto + "\n");

        } catch (IOException ex) {
            Logger.getLogger(PantallaCrearProyectoController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fileWriter.close();
                pw.close();
            } catch (IOException ex) {
                Logger.getLogger(PantallaCrearProyectoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public ArrayList<Proyecto> cargarProyectos() {
        FileReader fileReader = null;
        BufferedReader contenido = null;
        ArrayList<Proyecto> proyectos = null;
        try {
            proyectos = new ArrayList();
            File file = new File("C:/Users/raymu/Desktop/rutas.txt");
            fileReader = new FileReader(file);
            contenido = new BufferedReader(fileReader);
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
        } finally {
            try {
                fileReader.close();
                contenido.close();
            } catch (IOException ex) {
                Logger.getLogger(PantallaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
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
        FileReader fileReader = null;
        String auxiliar = "";
        String contenidoArchivo = "";
        BufferedReader contenido = null;
        try {

            File file = new File(ruta);
            fileReader = new FileReader(file);
            contenido = new BufferedReader(fileReader);
            while ((auxiliar = contenido.readLine()) != null) {
                contenidoArchivo += auxiliar + "\n";
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(PantallaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PantallaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fileReader.close();
                contenido.close();
            } catch (IOException ex) {
                Logger.getLogger(PantallaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
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

}
