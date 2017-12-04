/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeTableColumn;

/**
 * FXML Controller class
 *
 * @author alonso
 */
public class PantallaInvitadoController implements Initializable {

    @FXML
    private TreeTableColumn<?, ?> arbolProyecto;
    @FXML
    private TabPane tablaArchivos;
    @FXML
    private JFXButton botonCancelarColaboracion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void cancelarColaboracion(ActionEvent event) {
    }
    
}
