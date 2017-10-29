/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import com.jfoenix.controls.JFXButton;
import componentes.FormatoCodigo;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import static idecolaborativo.IDEColaborativo.ventanaCambiarIdioma;
import static idecolaborativo.IDEColaborativo.ventanaCrearProyecto;
import static idecolaborativo.IDEColaborativo.ventanaInicioSesion;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author raymu
 */
public class PantallaPrincipalController implements Initializable {

    private ResourceBundle recurso;
    private PantallaPrincipalController controlador;
    private String idUsuario;

    @FXML
    private AnchorPane panelBarraMenu;
    @FXML
    private JFXButton botonCrearProyecto;
    @FXML
    private MaterialDesignIconView botonImportarProyecto;
    @FXML
    private JFXButton botonGuardar;
    @FXML
    private JFXButton botonDepurar;
    @FXML
    private AnchorPane panelProyectos;
    @FXML
    private MenuItem iniciarSesion;
    @FXML
    private MenuItem cambiarIdioma;
    @FXML
    private MenuItem cerrarSesion;
    @FXML
    private MaterialDesignIconView iconoSesionIniciada;
    @FXML
    private Label etiquetaNombreUsuario;
    @FXML
    private MenuButton botonConfiguracion;
    @FXML
    private TreeTableView<String> tablaProyectos;
    @FXML
    private TreeTableColumn<String, String> columnaProyectos;
    @FXML
    private TabPane tablaArchivos;

    private static ImageView lenguaje = new ImageView("/Imagenes/java.png");
    @FXML
    private Tab tab1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        iconoSesionIniciada.setVisible(false);
        etiquetaNombreUsuario.setVisible(false);
        cerrarSesion.setVisible(false);
        recurso = rb;
        lenguaje.setFitHeight(40);
        lenguaje.setFitWidth(40);
        configurarIdioma();
        cargarProyectos();
        FormatoCodigo areaCodigo = new FormatoCodigo();
        tab1.setContent(areaCodigo.crearAreaCodigo());
    }

    public void setRecurso(ResourceBundle recurso) {
        this.recurso = recurso;
        configurarIdioma();
    }

    public void setControlador(PantallaPrincipalController controlador) {
        this.controlador = controlador;
    }

    public void configurarIdioma() {
        iniciarSesion.setText(recurso.getString("etInicioSesion"));
        cambiarIdioma.setText(recurso.getString("etCambiarIdioma"));
        cerrarSesion.setText(recurso.getString("btCerrarSesion"));
    }

    @FXML
    private void botonCrearProyecto(ActionEvent event) throws IOException {
        ventanaCrearProyecto(recurso, controlador);
    }

    @FXML
    private void botonIniciarSesion(ActionEvent event) throws IOException {
        ventanaInicioSesion(recurso, controlador);
    }

    @FXML
    private void botonCambiarIdioma(ActionEvent event) throws IOException {
        ventanaCambiarIdioma(recurso, controlador);
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        idUsuario = "";
        iconoSesionIniciada.setVisible(false);
        etiquetaNombreUsuario.setVisible(false);
        cerrarSesion.setVisible(false);
        iniciarSesion.setVisible(true);

    }

    public void sesionIniciada(String nombreUsuario) {
        iconoSesionIniciada.setVisible(true);
        etiquetaNombreUsuario.setText(nombreUsuario);
        etiquetaNombreUsuario.setVisible(true);
        cerrarSesion.setVisible(true);
        iniciarSesion.setVisible(false);
    }

    public void cargarProyectos() {
        FileReader fileReader = null;
        BufferedReader contenido = null;
        try {
            File file = new File("/home/alonso/Escritorio/rutas.txt");
            fileReader = new FileReader(file);
            contenido = new BufferedReader(fileReader);
            String ruta;
            ArrayList<TreeItem<String>> proyectos = new ArrayList();
            while ((ruta = contenido.readLine()) != null) {
                String[] rutas = ruta.split(",");
                TreeItem<String> hijo = new TreeItem<>(rutas[rutas.length - 1], crearIconoLenguaje(rutas[1]));
                hijo.getChildren().setAll(buscarCarpetas(rutas[0],rutas[1]));
                proyectos.add(hijo);
            }
            TreeItem<String> root = new TreeItem<>("Proyectos");
            root.getChildren().setAll(proyectos);
            columnaProyectos.setCellValueFactory((CellDataFeatures<String, String> p) -> new ReadOnlyStringWrapper(p.getValue().getValue()));
            tablaProyectos.setRoot(root);
            tablaProyectos.setShowRoot(true);

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

    }

    public ImageView crearIconoCarpeta() {
        ImageView carpeta;
        carpeta = new ImageView("/Imagenes/carpeta_1.png");
        carpeta.setFitHeight(15);
        carpeta.setFitWidth(15);
        return carpeta;
    }

    public ImageView crearIconoLenguaje(String lenguajeProgramacion) {
        ImageView lenguaje = null;
        switch (lenguajeProgramacion) {
            case "java":
                lenguaje = new ImageView("/Imagenes/java.png");
                lenguaje.setFitHeight(35);
                lenguaje.setFitWidth(35);
                break;
            case "c#":
                lenguaje = new ImageView("/Imagenes/c#.png");
                lenguaje.setFitHeight(20);
                lenguaje.setFitWidth(20);
                break;
            case "c++":
                lenguaje = new ImageView("/Imagenes/cpp_logo.png");
                lenguaje.setFitHeight(15);
                lenguaje.setFitWidth(15);
                break;
        }

        return lenguaje;
    }
    
    public ImageView crearIconoArchivo(String lenguajeProgramacion) {
        ImageView lenguaje = null;
        switch (lenguajeProgramacion) {
            case "java":
                lenguaje = new ImageView("/Imagenes/archivoJava.png");
                lenguaje.setFitHeight(20);
                lenguaje.setFitWidth(20);
                break;
            case "c#":
                lenguaje = new ImageView("/Imagenes/archivoCSharp.png");
                lenguaje.setFitHeight(20);
                lenguaje.setFitWidth(20);
                break;
            case "c++":
                lenguaje = new ImageView("/Imagenes/cpp_logo.png");
                lenguaje.setFitHeight(15);
                lenguaje.setFitWidth(15);
                break;
        }

        return lenguaje;
    }

    public ArrayList<TreeItem<String>> buscarCarpetas(String ruta, String lenguaje) {
        ruta += "/codigo";
        ArrayList<TreeItem<String>> carpetas = new ArrayList();
        File file = new File(ruta);
        String[] carpetasCreadas = file.list();

        for (String carpeta : carpetasCreadas) {
            TreeItem<String> hijo = new TreeItem<>(carpeta, crearIconoCarpeta());
            hijo.getChildren().addAll(buscarArchivos(ruta + "/" + carpeta, lenguaje));
            carpetas.add(hijo);
        }

        return carpetas;
    }

    public ArrayList<MyTreeItem> buscarArchivos(String ruta, String lenguale) {
        File file = new File(ruta);
        String[] archivos = file.list();
        ArrayList<MyTreeItem> treeArchivos = new ArrayList();
        for(String archivo: archivos){
            MyTreeItem hijo = new MyTreeItem(archivo,crearIconoArchivo(lenguale));
            hijo.setContenido(leerArchivo(ruta+"/"+archivo));
            hijo.setRuta(ruta+"/"+archivo);
            treeArchivos.add(hijo);
        
        }
        return treeArchivos;
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
                contenidoArchivo += auxiliar;
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

    public class MyTreeItem extends TreeItem<String> {
        private String contenido;
        private String ruta;

        public MyTreeItem(String nombreNodo,ImageView logo) {
            super(nombreNodo,logo);
        }

        public String getRuta() {
            return ruta;
        }

        public void setRuta(String ruta) {
            this.ruta = ruta;
        }
        
        

        public String getContenido() {
            return contenido;
        }

        public void setContenido(String contenido) {
            this.contenido = contenido;
        }

    }

}
