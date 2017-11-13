/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(recurso.getString("atencion"));
            String s = recurso.getString("mensajeRuta");
            alert.setContentText(s);
            alert.show();
        } else {
            campoTextoRuta.setText(file.getPath());
            campoTextoNombreProyecto.setText(file.getName());
        }

    }

    @FXML
    private void botonCancelar(ActionEvent event) {
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
            }
            proyecto = proyecto.cargarNuevoProyecto(proyecto);
            controlador.cargarNuevoProyecto(proyecto);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(recurso.getString("atencion"));
            String s = recurso.getString("mensajeCamposVacios");
            alert.setContentText(s);
            alert.show();
        }
    }

    public Proyecto crearProyecto(String lenguaje) {
        Proyecto proyecto = new Proyecto();
        proyecto.setNombreProyecto(campoTextoNombreProyecto.getText());
        proyecto.setRutaProyecto(campoTextoRuta.getText());
        proyecto.setLenguaje(lenguaje);
        if (!proyecto.crearProyecto()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(recurso.getString("atencion"));
            String s = recurso.getString("mensajeProyectoNoCreado");
            alert.setContentText(s);
            alert.show();
        }
        
        return proyecto;
    }


}
