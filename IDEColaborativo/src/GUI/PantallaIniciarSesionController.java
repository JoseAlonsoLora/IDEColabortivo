package GUI;

import com.jfoenix.controls.JFXButton;
import idecolaborativo.IDEColaborativo;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author alonso
 */
public class PantallaIniciarSesionController implements Initializable {
    private IDEColaborativo main;
    private ResourceBundle recurso;
   
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
    private Label etiquetaEspañol;
    @FXML
    private Label etiquetaEnglish;
    @FXML
    private JFXButton botonCancelar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.recurso = rb;
        configurarIdioma();
    }

    public void setMain(IDEColaborativo main) {
        this.main = main;
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
    private void cambiarIdiomaEspañol(MouseEvent event) {
        recurso = ResourceBundle.getBundle("recursos.idioma_es_MX");
        configurarIdioma();
        etiquetaEspañol.setOpacity(0.50);
        etiquetaEnglish.setOpacity(1.0);
    }

    @FXML
    private void cambiarIdiomaIngles(MouseEvent event) {
        recurso = ResourceBundle.getBundle("recursos.idioma_en_US");
        configurarIdioma();
        etiquetaEspañol.setOpacity(1.0);
        etiquetaEnglish.setOpacity(0.50);
    }

    @FXML
    private void botonCancelar(ActionEvent event) {
        Stage ventanaIniciarSesion = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ventanaIniciarSesion.close();
    }

    @FXML
    private void botonIniciarSesion(ActionEvent event) throws IOException {
        main.ventanaPrincipal(recurso);
    }

    @FXML
    private void etiquetaCrearCuenta(MouseEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        main.ventanaRegistrarUsuario(recurso, stage);
    }

}
