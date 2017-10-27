package controladores;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import conexion.operaciones.ICuenta;
import static idecolaborativo.IDEColaborativo.ventanaRegistrarUsuario;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import modelo.negocio.Cuenta;

/**
 *
 * @author alonso
 */
public class PantallaIniciarSesionController implements Initializable {

    private ResourceBundle recurso;
    private PantallaPrincipalController controlador;

    @FXML
    private Label etiquetaIniciarSesion;
    @FXML
    private Label etiquetaNombreUsuario;
    @FXML
    private JFXButton botonIniciarSesion;
    @FXML
    private Label etiquetaCrearCuenta;
    @FXML
    private Label etiquetaContraseña;
    @FXML
    private JFXButton botonCancelar;
    @FXML
    private JFXTextField campoTextoNombreUsuario;
    @FXML
    private JFXPasswordField campoTextoContraseña;

    private ICuenta stub;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.recurso = rb;
        configurarIdioma();
        inicializarRegistro();
    }

    public void inicializarRegistro() {
        try {
            Registry registry = LocateRegistry.getRegistry(null);
            stub = (ICuenta) registry.lookup("Login");
        } catch (RemoteException | NotBoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void setControlador(PantallaPrincipalController controlador) {
        this.controlador = controlador;
    }

    public void configurarIdioma() {
        etiquetaIniciarSesion.setText(recurso.getString("etInicioSesion"));
        etiquetaNombreUsuario.setText(recurso.getString("etNombreUsuario"));
        botonIniciarSesion.setText(recurso.getString("btIniciarSesion"));
        etiquetaCrearCuenta.setText(recurso.getString("etCrearNuevaCuenta"));
        etiquetaContraseña.setText(recurso.getString("etContraseña"));
        botonCancelar.setText(recurso.getString("btCancelar"));
    }

    @FXML
    private void botonCancelar(ActionEvent event) {
        Stage ventanaIniciarSesion = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ventanaIniciarSesion.close();
    }

    @FXML
    private void botonIniciarSesion(ActionEvent event) throws IOException {
        Cuenta cuenta = new Cuenta();
        if (campoTextoNombreUsuario.getText().isEmpty() || campoTextoContraseña.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Atención");
            String s = "Algunos campos estan vacios";
            alert.setContentText(s);
            alert.show();
        } else {
            cuenta.setNombreUsuario(campoTextoNombreUsuario.getText());
            cuenta.setContraseña(makeHash(campoTextoContraseña.getText()));
            try {

                if (stub.iniciarSesion(cuenta)) {
                    controlador.sesionIniciada(campoTextoNombreUsuario.getText());
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.close();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Atención");
                    String s = "Datos incorrectos";
                    alert.setContentText(s);
                    alert.show();

                }
            } catch (NullPointerException ex) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Atención");
                String s = "Opsss, parece que no me pude conectar al servidor :(";
                alert.setContentText(s);
                alert.show();
            }
        }

    }

    @FXML
    private void etiquetaCrearCuenta(MouseEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        ventanaRegistrarUsuario(recurso, controlador);
    }

    private String makeHash(String string) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(string.getBytes());
            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < hash.length; i++) {
                stringBuilder.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(PantallaIniciarSesionController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
