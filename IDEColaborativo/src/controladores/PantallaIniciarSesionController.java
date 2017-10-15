package controladores;

import com.jfoenix.controls.JFXButton;
import static idecolaborativo.IDEColaborativo.ventanaRegistrarUsuario;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author alonso
 */
public class PantallaIniciarSesionController implements Initializable {
    private ResourceBundle recurso;
    private PantallaPrincipalController controlador;
   
    @FXML
    private Label etiquetaIniciarSesion;
    @FXML
    private Label etiquetaNombreUsuario;
    @FXML
    private JFXButton botonIniciarSesion;
    @FXML
    private Label etiquetaCrearCuenta;
    @FXML
    private Label etiquetaContraseña;
    @FXML
    private JFXButton botonCancelar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.recurso = rb;
        configurarIdioma();
    }


    public void setControlador(PantallaPrincipalController controlador) {
        this.controlador = controlador;
    }
    
    
    

    public void configurarIdioma() {
        etiquetaIniciarSesion.setText(recurso.getString("etInicioSesion"));
        etiquetaNombreUsuario.setText(recurso.getString("etNombreUsuario"));
        botonIniciarSesion.setText(recurso.getString("btIniciarSesion"));
        etiquetaCrearCuenta.setText(recurso.getString("etCrearNuevaCuenta"));
        etiquetaContraseña.setText(recurso.getString("etContraseña"));
        botonCancelar.setText(recurso.getString("btCancelar"));
    }

    @FXML
    private void botonCancelar(ActionEvent event) {
        Stage ventanaIniciarSesion = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ventanaIniciarSesion.close();
    }

    @FXML
    private void botonIniciarSesion(ActionEvent event) throws IOException {
        controlador.sesionIniciada("RayPerez");
        Stage ventanaIniciarSesion = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ventanaIniciarSesion.close();
    }

    @FXML
    private void etiquetaCrearCuenta(MouseEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        ventanaRegistrarUsuario(recurso,controlador);
    }

}
