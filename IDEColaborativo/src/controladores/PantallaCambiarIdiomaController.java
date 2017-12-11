
package controladores;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import static idecolaborativo.IDEColaborativo.mensajeAlert;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Alonso Lora
 * @author Raymundo Pérez
 */
public class PantallaCambiarIdiomaController implements Initializable {

    private ResourceBundle recurso;
    private final ToggleGroup grupoRadio = new ToggleGroup();
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
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        recurso = rb;
        configurarIdioma();
        radioEnglish.setToggleGroup(grupoRadio);
        radioEspañol.setToggleGroup(grupoRadio);
    }

    /**
     * Da valor al controlador para poder manipular componentes de la pantalla principal
     *
     * @param controlador Instancia del controlador
     */
    public void setControlador(PantallaPrincipalController controlador) {
        this.controlador = controlador;
    }

    /**
     * Dar valor al stage para poder manipular la pantalla cambiar idioma
     * @param stagePantallaCambiarIdioma Stage de la instancia actual
     */
    public void setStagePantallaCambiarIdioma(Stage stagePantallaCambiarIdioma) {
        this.stagePantallaCambiarIdioma = stagePantallaCambiarIdioma;
        this.stagePantallaCambiarIdioma.setOnCloseRequest((WindowEvent event) -> {
            controlador.hacerVisiblePantallaprincipal();  
        });
    }
    
    /**
     * Configura el idioma de todas etiquetas de la pantalla
     */
    public void configurarIdioma() {
        etiquetaIdiomasDisponibles.setText(recurso.getString("etIdiomasDisponibles"));
        botonAceptar.setText(recurso.getString("btAceptar"));
        botonCancelar.setText(recurso.getString("btCancelar"));
    }
    
    /**
     * Evento para configurar el idioma en todo el sistema
     * @param event Clic del usuario
     */
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
            stagePantallaCambiarIdioma.close();
        } else {
            mensajeAlert(recurso.getString("atencion"), recurso.getString("mensajeIdioma"));
        }

    }

    /**
     * Evento para salir de la pantalla cambiar idioma
     * @param event Clic del usuario
     */
    @FXML
    private void botonCancelar(ActionEvent event) {
        controlador.hacerVisiblePantallaprincipal();
        stagePantallaCambiarIdioma.close();
    }

}
