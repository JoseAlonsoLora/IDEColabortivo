/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import idecolaborativo.IDEColaborativo;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author raymu
 */
public class PantallaDireccionIPController implements Initializable {

    @FXML
    private JFXButton botonGuardar;
    @FXML
    private JFXButton botonCancelar;
    @FXML
    private Label etiquetaIP;
    @FXML
    private JFXTextField campoTextoIP1;
    @FXML
    private JFXTextField campoTextoIP2;
    @FXML
    private JFXTextField campoTextoIP3;
    @FXML
    private JFXTextField campoTextoIP4;

    private PantallaPrincipalController controlador;

    private Stage stagePantallaDireccionIP;

    private ResourceBundle recurso;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.recurso = rb;// TODO
    }

    public void configurarIdioma() {
        etiquetaIP.setText(recurso.getString("etDireccionIP"));
        botonCancelar.setText(recurso.getString("btCancelar"));
        botonGuardar.setText(recurso.getString("btGuardar"));
    }

    public void setControlador(PantallaPrincipalController controlador) {
        this.controlador = controlador;
    }

    public void setStagePantallaDireccionIP(Stage stagePantallaDireccionIP) {
        this.stagePantallaDireccionIP = stagePantallaDireccionIP;
        this.stagePantallaDireccionIP.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                controlador.hacerVisiblePantallaprincipal();
            }
        });
    }

    @FXML
    private void guardarIP(ActionEvent event) {
        StringBuilder direccionIP = new StringBuilder();
        if (campoTextoIP1.getText().isEmpty() || campoTextoIP2.getText().isEmpty()
                || campoTextoIP3.getText().isEmpty() || campoTextoIP4.getText().isEmpty()) {         
            IDEColaborativo.mensajeAlert(recurso.getString("atencion"), recurso.getString("mensajeCamposVacios"));
        } else {
            direccionIP.append(campoTextoIP1.getText()).append(".").append(campoTextoIP2.getText())
                    .append(".").append(campoTextoIP3.getText()).append(".").append(campoTextoIP4.getText());
            controlador.setDireccionIP(direccionIP.toString());
            controlador.hacerVisiblePantallaprincipal();
            stagePantallaDireccionIP.close();
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        controlador.hacerVisiblePantallaprincipal();
        stagePantallaDireccionIP.close();
    }

    @FXML
    private void validarEntradas(KeyEvent event) {
        int limiteCaracteres = 3;
        char digito = event.getCharacter().charAt(0);
        if ((digito < 48 || digito > 57) || ((JFXTextField) event.getSource()).getText().length() >= limiteCaracteres) {
            event.consume();
        }
    }

}
