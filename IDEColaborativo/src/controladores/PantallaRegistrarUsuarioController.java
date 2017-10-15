/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import com.jfoenix.controls.JFXButton;
import static idecolaborativo.IDEColaborativo.ventanaInicioSesion;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author alonso
 */
public class PantallaRegistrarUsuarioController implements Initializable {
    private ResourceBundle recurso;
    private PantallaPrincipalController controlador;
    @FXML
    private Label etiquetaCrearCuenta;
    @FXML
    private Label etiquetaNombreUsuario;
    @FXML
    private Label etiquetaEmail;
    @FXML
    private Label etiquetaContraseña;
    @FXML
    private JFXButton botonCrearCuenta;
    @FXML
    private JFXButton botonCancelar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.recurso = rb;
        configurarIdioma();
        // TODO
    }


    public void setControlador(PantallaPrincipalController controlador) {
        this.controlador = controlador;
    }
    
    

    public void configurarIdioma() {
        etiquetaCrearCuenta.setText(recurso.getString("etCrearCuenta"));
        etiquetaNombreUsuario.setText(recurso.getString("etNombreUsuario"));
        etiquetaEmail.setText(recurso.getString("etEmail"));
        etiquetaContraseña.setText(recurso.getString("etContraseña"));
        botonCancelar.setText(recurso.getString("btCancelar"));
        botonCrearCuenta.setText(recurso.getString("btCrearCuenta"));
    }


    @FXML
    private void botonCancelar(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        ventanaInicioSesion(recurso,controlador);
    }

}
