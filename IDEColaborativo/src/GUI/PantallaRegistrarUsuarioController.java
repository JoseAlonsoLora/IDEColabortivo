/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author alonso
 */
public class PantallaRegistrarUsuarioController implements Initializable {
    
    private ResourceBundle rb;
    
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
        this.rb = rb;
        configurarIdioma();
        // TODO
    }
    
    public void configurarIdioma(){
        etiquetaCrearCuenta.setText(rb.getString("etCrearCuenta"));
        etiquetaNombreUsuario.setText(rb.getString("etNombreUsuario"));
        etiquetaEmail.setText(rb.getString("etEmail"));
        etiquetaContraseña.setText(rb.getString("etContraseña"));
        botonCancelar.setText(rb.getString("btCancelar"));
        botonCrearCuenta.setText(rb.getString("btCrearCuenta"));
    }
    

    @FXML
    private void lanzarPantallaInicioSesion(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/GUI/PantallaIniciarSesion.fxml"),rb);
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
    
}
