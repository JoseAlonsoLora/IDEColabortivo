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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author alonso
 */
public class PantallaIniciarSesionController implements Initializable {
    
    
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

    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.recurso = rb;
        configurarIdioma();
    }
    

    @FXML
    private void lanzarVentanaRegistrarUsuario(MouseEvent event) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("PantallaRegistrarUsuario.fxml"),recurso);
        
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        
    }
    
    public void configurarIdioma(){
        etiquetaIniciarSesion.setText(recurso.getString("etInicioSesion"));
        etiquetaNombreUsuario.setText(recurso.getString("etNombreUsuario"));
        botonIniciarSesion.setText(recurso.getString("btIniciarSesion"));
        etiquetaCrearCuenta.setText(recurso.getString("etCrearNuevaCuenta"));
        etiquetaContraseña.setText(recurso.getString("etContraseña"));
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
    private void lanzarVentanaPrincipal(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("PantallaPrincipal.fxml"),recurso);
        
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

}
