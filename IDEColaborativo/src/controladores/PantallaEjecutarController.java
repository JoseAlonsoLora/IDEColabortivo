/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import static idecolaborativo.IDEColaborativo.mensajeAlert;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import modelo.negocio.Archivo;

/**
 * FXML Controller class
 *
 * @author raymu
 */
public class PantallaEjecutarController implements Initializable {

    @FXML
    private JFXRadioButton radioConParametros;
    @FXML
    private JFXRadioButton radioSinParametros;
    @FXML
    private Label etiquetaParametros;
    @FXML
    private JFXTextField campoTextoParametros;
    @FXML
    private JFXButton botonEjecutar;
    @FXML
    private TextArea areaTextoResultadoEjecucion;
    @FXML
    private JFXButton botonSalir;

    private Archivo archivo;

    private ResourceBundle recurso;

    private final ToggleGroup grupoRadio = new ToggleGroup();

    private final String patron = "^[\u0400-\u04FFa-zA-Z ]+(,[\u0400-\u04FFa-zA-Z ]+)*$";

    private PantallaPrincipalController controlador;

    private boolean esColaborativo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.recurso = rb;
        etiquetaParametros.setVisible(false);
        campoTextoParametros.setVisible(false);
        radioConParametros.setToggleGroup(grupoRadio);
        radioSinParametros.setToggleGroup(grupoRadio);
        configurarIdioma();
    }

    public void configurarIdioma() {
        radioConParametros.setText(recurso.getString("etConParametros"));
        radioSinParametros.setText(recurso.getString("etSinParametros"));
        etiquetaParametros.setText(recurso.getString("etParametros"));
        botonEjecutar.setText(recurso.getString("btEjecutar"));
        botonSalir.setText(recurso.getString("btSalir"));
    }

    public void setArchivo(Archivo archivo) {
        this.archivo = archivo;
    }

    public void setControlador(PantallaPrincipalController controlador) {
        this.controlador = controlador;
    }

    public void setEsColaborativo(boolean esColaborativo) {
        this.esColaborativo = esColaborativo;
    }

    @FXML
    private void botonEjecutar(ActionEvent event) {
        JFXRadioButton radioSeleccionado = (JFXRadioButton) grupoRadio.getSelectedToggle();
        if (radioSeleccionado != null) {
            if (esColaborativo) {
                ejecutarColaborativo(radioSeleccionado);
            } else {
                ejecutarLocal(radioSeleccionado);
            }
        }

    }

    public void ejecutarLocal(JFXRadioButton radioSeleccionado) {
        if (radioSeleccionado.getText().equals(recurso.getString("etConParametros")) && !campoTextoParametros.getText().isEmpty()) {
            if (validarPatronParametros(campoTextoParametros.getText())) {
                areaTextoResultadoEjecucion.setText(archivo.ejecutarArchivo(archivo, campoTextoParametros.getText()));
            } else {
                mensajeAlert(recurso.getString("atencion"), recurso.getString("mensajeParametrosInvalidos"));
            }

        } else {
            areaTextoResultadoEjecucion.setText(archivo.ejecutarArchivo(archivo));
        }
    }

    public void ejecutarColaborativo(JFXRadioButton radioSeleccionado) {
        String resultadoEjecucion;
        if (radioSeleccionado.getText().equals(recurso.getString("etConParametros")) && !campoTextoParametros.getText().isEmpty()) {
            if (validarPatronParametros(campoTextoParametros.getText())) {
                resultadoEjecucion = archivo.ejecutarArchivo(archivo, campoTextoParametros.getText());
                areaTextoResultadoEjecucion.setText(resultadoEjecucion);
                controlador.getSocket().emit("resultadoEjecucion", resultadoEjecucion);
            } else {
                mensajeAlert(recurso.getString("atencion"), recurso.getString("mensajeParametrosInvalidos"));
            }

        } else {
            resultadoEjecucion = archivo.ejecutarArchivo(archivo);
            areaTextoResultadoEjecucion.setText(resultadoEjecucion);
            controlador.getSocket().emit("resultadoEjecucion", resultadoEjecucion);
        }
        
    }

    @FXML
    private void botonSalir(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void radioSeleccionOpciones(ActionEvent event) {
        String radioSeleccionado = ((JFXRadioButton) event.getSource()).getText();
        if (radioSeleccionado.equals(recurso.getString("etConParametros"))) {
            etiquetaParametros.setVisible(true);
            campoTextoParametros.setVisible(true);
        } else {
            etiquetaParametros.setVisible(false);
            campoTextoParametros.setVisible(false);
        }
    }

    @FXML
    private void campoTextoParametros(KeyEvent evt) {
        char digito = evt.getCharacter().charAt(0);
        int numeroCaracteres = campoTextoParametros.getText().length();
        char ultimo;
        if (numeroCaracteres > 0) {
            ultimo = campoTextoParametros.getText().charAt(numeroCaracteres - 1);
        } else {
            ultimo = ',';
        }
        if ((digito == 44 && (ultimo == ',')) || (digito == 39)) {
            evt.consume();
        }
    }

    public boolean validarPatronParametros(String parametros) {
        return parametros.matches(patron);
    }

}
