/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import static idecolaborativo.IDEColaborativo.mensajeAlert;
import io.socket.client.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author alonso
 */
public class PantallaInvitarColaboradorController implements Initializable {

    @FXML
    private Label etiquetaNombreColaborador;
    @FXML
    private JFXTextField campoTextoNombreColaborador;
    @FXML
    private JFXButton botonInvitar;
    @FXML
    private JFXButton botonCancelar;
    
    private Socket socket;
    private String nombreUsuario;
    private ResourceBundle recurso;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        recurso = rb;
        configurarIdioma();
                
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    
    public void configurarIdioma(){
        etiquetaNombreColaborador.setText(recurso.getString("etNombreColaborador"));
        botonInvitar.setText(recurso.getString("btInvitar"));
        botonCancelar.setText(recurso.getString("btCancelar"));
    }
    
    @FXML
    private void invitarColaborador(ActionEvent event) {
        if(!campoTextoNombreColaborador.getText().isEmpty()){
        socket.emit("invitarColaborador", campoTextoNombreColaborador.getText());
        Stage ventanaInvitarColaborador = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ventanaInvitarColaborador.close();
        }else{
            mensajeAlert(recurso.getString("atencion"), recurso.getString("mensajeCamposVacios"));
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        Stage ventanaInvitarColaborador = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ventanaInvitarColaborador.close();
    }
    
}
