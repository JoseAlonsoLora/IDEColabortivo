/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import idecolaborativo.IDEColaborativo;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author raymu
 */
public class PantallaPrincipalController implements Initializable {
    
    private IDEColaborativo main;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        iconoSesionIniciada.setVisible(false);
        etiquetaNombreUsuario.setVisible(false);
        cerrarSesion.setVisible(false);
        recurso = rb;
        configurarIdioma();
    }
    
    public void setMain(IDEColaborativo main){
        this.main=main;
    }

    public void setRecurso(ResourceBundle recurso) {
        this.recurso = recurso;
        configurarIdioma();
    }
    
    

    public void setControlador(PantallaPrincipalController controlador) {
        this.controlador = controlador;
    }
    
    
    
    public void configurarIdioma(){
        iniciarSesion.setText(recurso.getString("etInicioSesion"));
        cambiarIdioma.setText(recurso.getString("etCambiarIdioma"));
        cerrarSesion.setText(recurso.getString("btCerrarSesion"));
    }

    @FXML
    private void botonCrearProyecto(ActionEvent event) throws IOException {
        main.ventanaCrearProyecto(recurso,controlador);
    }

    @FXML
    private void botonIniciarSesion(ActionEvent event) throws IOException {
        main.ventanaInicioSesion(recurso,controlador);
    }

    @FXML
    private void botonCambiarIdioma(ActionEvent event) throws IOException {
       main.ventanaCambiarIdioma(recurso,controlador);
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        idUsuario = "";
        iconoSesionIniciada.setVisible(false);
        etiquetaNombreUsuario.setVisible(false);
        cerrarSesion.setVisible(false);
        iniciarSesion.setVisible(true);
        
    }
    
    public void sesionIniciada(String nombreUsuario){
        iconoSesionIniciada.setVisible(true);
        etiquetaNombreUsuario.setText(nombreUsuario);
        etiquetaNombreUsuario.setVisible(true);
        cerrarSesion.setVisible(true);
        iniciarSesion.setVisible(false);
    }

}
