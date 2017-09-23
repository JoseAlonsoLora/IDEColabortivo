/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author raymu
 */
public class PantallaPrincipalController implements Initializable {
    private ResourceBundle recurso;
    
    @FXML
    private AnchorPane panelCodigo;
    @FXML
    private AnchorPane panelBarraMenu;
    @FXML
    private JFXButton botonCrearProyecto;
    @FXML
    private MaterialDesignIconView botonImportarProyecto;
    @FXML
    private JFXButton botonGuardar;
    @FXML
    private JFXButton botonDepurar;
    @FXML
    private AnchorPane panelProyectos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        recurso = rb;
    }    

    @FXML
    private void lanzarPantallaCrearProyecto(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/GUI/PantallaCrearProyecto.fxml"),recurso);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
}
