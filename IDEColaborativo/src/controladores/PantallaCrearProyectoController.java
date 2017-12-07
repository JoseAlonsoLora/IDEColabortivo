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
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import modelo.negocio.Proyecto;

/**
 * FXML Controller class
 *
 * @author alonso
 */
public class PantallaCrearProyectoController implements Initializable {

    private ResourceBundle recurso;
    private PantallaPrincipalController controlador;
    private ToggleGroup grupoRadio = new ToggleGroup();

    @FXML
    private Label etiquetaEligeProyecto;
    @FXML
    private Label etiquetaNombreProyecto;
    @FXML
    private JFXTextField campoTextoNombreProyecto;
    @FXML
    private Label etiquetaRuta;
    @FXML
    private JFXTextField campoTextoRuta;
    @FXML
    private JFXButton botonExaminar;
    @FXML
    private JFXButton botonCrear;
    @FXML
    private JFXButton botonCancelar;
    @FXML
    private JFXRadioButton radioJava;
    @FXML
    private JFXRadioButton radioCPlusPlus;
    @FXML
    private JFXRadioButton radioCSharp;
    
    private final String mensajeAtencion = "atencion";
    
    private Stage stagePantallaCrearProyecto;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        recurso = rb;
        radioJava.setToggleGroup(grupoRadio);
        radioCPlusPlus.setToggleGroup(grupoRadio);
        radioCSharp.setToggleGroup(grupoRadio);
        configurarIdioma();

    }

    public void setControlador(PantallaPrincipalController controlador) {
        this.controlador = controlador;
    }

    public void setStagePantallaCrearProyecto(Stage stagePantallaCrearProyecto) {
        this.stagePantallaCrearProyecto = stagePantallaCrearProyecto;
        this.stagePantallaCrearProyecto.setOnCloseRequest(new EventHandler<WindowEvent>(){
            @Override public void handle(WindowEvent event) {
                controlador.hacerVisiblePantallaprincipal();
            }  
        });
    }

    public void configurarIdioma() {
        etiquetaEligeProyecto.setText(recurso.getString("etEligeProyecto"));
        etiquetaNombreProyecto.setText(recurso.getString("etNombreProyecto"));
        etiquetaRuta.setText(recurso.getString("etRuta"));
        botonExaminar.setText(recurso.getString("btExaminar"));
        botonCancelar.setText(recurso.getString("btCancelar"));
        botonCrear.setText(recurso.getString("btCrear"));
    }

    @FXML
    private void seleccionarRuta(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(campoTextoNombreProyecto.getText());
        File file = fileChooser.showSaveDialog(null);
        if (file == null) {
            mensajeAlert(recurso.getString(mensajeAtencion), recurso.getString("mensajeRuta"));
        } else {
            campoTextoRuta.setText(file.getPath());
            campoTextoNombreProyecto.setText(file.getName());
            campoTextoNombreProyecto.setEditable(false);
        }

    }

    @FXML
    private void botonCancelar(ActionEvent event) {
        controlador.hacerVisiblePantallaprincipal();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void botonCrearProyecto(ActionEvent event) {
        if (grupoRadio.getSelectedToggle() != null && !campoTextoRuta.getText().equals("") && !campoTextoNombreProyecto.equals("")) {
            JFXRadioButton radioSeleccionado = (JFXRadioButton) grupoRadio.getSelectedToggle();
            Proyecto proyecto = null;
            switch (radioSeleccionado.getText()) {
                case "Java":
                    proyecto = crearProyecto("java");
                    break;
                case "C#":
                    proyecto = crearProyecto("c#");
                    break;
                case "C++":
                    proyecto = crearProyecto("c++");
                    break;
                default:
                    break;
            }
            proyecto = proyecto.cargarNuevoProyecto(proyecto);
            controlador.cargarNuevoProyecto(proyecto);
            controlador.hacerVisiblePantallaprincipal();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

        } else {
            mensajeAlert(recurso.getString(mensajeAtencion), recurso.getString("mensajeCamposVacios"));
        }
    }

    public Proyecto crearProyecto(String lenguaje) {
        Proyecto proyecto = new Proyecto();
        proyecto.setNombreProyecto(campoTextoNombreProyecto.getText());
        proyecto.setRutaProyecto(campoTextoRuta.getText());
        proyecto.setLenguaje(lenguaje);
        if (!proyecto.crearProyecto()) {
            mensajeAlert(recurso.getString(mensajeAtencion), recurso.getString("mensajeProyectoNoCreado"));
        }
        
        return proyecto;
    }


}
