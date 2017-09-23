/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author alonso
 */
public class PantallaCrearProyectoController implements Initializable {

    private ResourceBundle recurso;

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
        fileChooser.setTitle("Selecciona ruta");
        if (!campoTextoNombreProyecto.getText().equals("")) {
            fileChooser.setInitialFileName(campoTextoNombreProyecto.getText());
            File file = fileChooser.showSaveDialog(null);
            if (file == null) {
                 Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Atención");
                String s = "Debe elegir una ruta";
                alert.setContentText(s);
                alert.show();
            }else{
                System.out.println("tallo");
                campoTextoRuta.setText(file.getPath());
            }
        }
    }

}
