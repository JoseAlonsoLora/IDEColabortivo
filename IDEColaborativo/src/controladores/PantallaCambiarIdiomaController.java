
package controladores;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import static idecolaborativo.IDEColaborativo.mensajeAlert;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author raymu
 */
public class PantallaCambiarIdiomaController implements Initializable {

    private ResourceBundle recurso;
    private ToggleGroup grupoRadio = new ToggleGroup();
    private PantallaPrincipalController controlador;

    @FXML
    private Label etiquetaIdiomasDisponibles;
    @FXML
    private JFXRadioButton radioEspañol;
    @FXML
    private JFXRadioButton radioEnglish;
    @FXML
    private JFXButton botonAceptar;
    @FXML
    private JFXButton botonCancelar;
    private Stage stagePantallaCambiarIdioma;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        recurso = rb;
        configurarIdioma();
        radioEnglish.setToggleGroup(grupoRadio);
        radioEspañol.setToggleGroup(grupoRadio);
    }


    public void setControlador(PantallaPrincipalController controlador) {
        this.controlador = controlador;
    }

    public void setStagePantallaCambiarIdioma(Stage stagePantallaCambiarIdioma) {
        this.stagePantallaCambiarIdioma = stagePantallaCambiarIdioma;
        this.stagePantallaCambiarIdioma.setOnCloseRequest(new EventHandler<WindowEvent>(){
            @Override public void handle(WindowEvent event) {
                controlador.hacerVisiblePantallaprincipal();
            }  
        });
    }
    

    public void configurarIdioma() {
        etiquetaIdiomasDisponibles.setText(recurso.getString("etIdiomasDisponibles"));
        botonAceptar.setText(recurso.getString("btAceptar"));
        botonCancelar.setText(recurso.getString("btCancelar"));
    }

    @FXML
    private void botonAceptar(ActionEvent event) {
        JFXRadioButton radioSeleccionado = (JFXRadioButton) grupoRadio.getSelectedToggle();
        if (radioSeleccionado != null) {
            switch (radioSeleccionado.getText()) {
                case "Español(México)":
                    recurso = ResourceBundle.getBundle("recursos.idioma_es_MX");
                    controlador.setRecurso(recurso);
                    break;
                case "English(United States)":
                    recurso = ResourceBundle.getBundle("recursos.idioma_en_US");
                    controlador.setRecurso(recurso);
                    break;
                default:
                    break;
            }
            controlador.hacerVisiblePantallaprincipal();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        } else {
            mensajeAlert(recurso.getString("atencion"), recurso.getString("mensajeIdioma"));
        }

    }

    @FXML
    private void botonCancelar(ActionEvent event) {
        controlador.hacerVisiblePantallaprincipal();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
