/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import static idecolaborativo.IDEColaborativo.ventanaCambiarIdioma;
import static idecolaborativo.IDEColaborativo.ventanaCrearProyecto;
import static idecolaborativo.IDEColaborativo.ventanaInicioSesion;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.Image;
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
    private AnchorPane panelCodigo;
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

    private static final Image icon = new Image(PantallaPrincipalController.class.getResourceAsStream("/Imagenes/carpeta.png"));
    private static ImageView lenguaje = new ImageView("/Imagenes/java.png");
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
        TreeItem<String> childNode1 = new TreeItem<>("Child Node 1", new ImageView(icon));
        TreeItem<String> childNode2 = new TreeItem<>("Child Node 2", new ImageView(icon));
        TreeItem<String> childNode3 = new TreeItem<>("Child Node 3", new ImageView(icon));

        TreeItem<String> root = new TreeItem<>("Root node",lenguaje);
        root.setExpanded(true);

        //Adding tree items to the root
        root.getChildren().setAll(childNode1, childNode2, childNode3);

        columnaProyectos.setCellValueFactory((CellDataFeatures<String, String> p)
                -> new ReadOnlyStringWrapper(p.getValue().getValue()));

        tablaProyectos.setRoot(root);
        tablaProyectos.setShowRoot(true);
    }

}
