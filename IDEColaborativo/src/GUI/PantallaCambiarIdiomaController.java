/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import idecolaborativo.IDEColaborativo;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author raymu
 */
public class PantallaCambiarIdiomaController implements Initializable {

    private IDEColaborativo main;
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

    public void setMain(IDEColaborativo main) {
        this.main = main;
    }

    public void setControlador(PantallaPrincipalController controlador) {
        this.controlador = controlador;
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
            }
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            String s = recurso.getString("mensajeIdioma");
            alert.setContentText(s);
            alert.show();
        }

    }

    @FXML
    private void botonCancelar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
