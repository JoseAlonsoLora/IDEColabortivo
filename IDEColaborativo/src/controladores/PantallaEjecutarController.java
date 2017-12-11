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
 * @author Alonso Lora
 * @author Raymundo Pérez
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

    private static final String PATRON = "^[\u0400-\u04FFa-zA-Z ]+(,[\u0400-\u04FFa-zA-Z ]+)*$";

    private PantallaPrincipalController controlador;

    private boolean esColaborativo;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
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

    /**
     * Configura el idioma de todas etiquetas de la pantalla
     */
    public void configurarIdioma() {
        radioConParametros.setText(recurso.getString("etConParametros"));
        radioSinParametros.setText(recurso.getString("etSinParametros"));
        etiquetaParametros.setText(recurso.getString("etParametros"));
        botonEjecutar.setText(recurso.getString("btEjecutar"));
        botonSalir.setText(recurso.getString("btSalir"));
    }

    /**
     * Da valor al archivo que será ejecutado por el comilador
     * @param archivo Archivo que será ejecutado
     */
    public void setArchivo(Archivo archivo) {
        this.archivo = archivo;
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
     * Define si la ejecución es de manera colaborativa
     * @param esColaborativo Indica si la ejecución es colaborativa
     */
    public void setEsColaborativo(boolean esColaborativo) {
        this.esColaborativo = esColaborativo;
    }

    /**
     * Evento para ejecutar un archivo en el compilador
     * @param event Clic del usuario
     */
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

    /**
     * Ejecuta el archivo de manera local
     * @param radioSeleccionado Indica si se ejecuta con parámetros o sin parámetros
     */
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

        /**
     * Ejecuta el archivo de manera colaborativa, mostrando los resultados a todos los colaboradores
     * @param radioSeleccionado Indica si se ejecuta con parámetros o sin parámetros
     */
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

    /**
     * Evento para salir de la pantalla ejecutar archivo
     * @param event Clic del usuario
     */
    @FXML
    private void botonSalir(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Evento para mostrar el campo para ingresar los parámetros
     * @param event Clic del usuario
     */
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

    /**
     * Evento para validar el formato de entrada de parámetros
     * @param evt Clic del usuario
     */
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

    /**
     * Valida si formato de entrada de parámetros es la correcta
     * @param parametros Parámetros
     * @return 
     */
    public boolean validarPatronParametros(String parametros) {
        return parametros.matches(PATRON);
    }

}
