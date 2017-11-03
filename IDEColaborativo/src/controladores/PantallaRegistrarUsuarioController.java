/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import conexion.operaciones.IProgramador;
import static idecolaborativo.IDEColaborativo.ventanaInicioSesion;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import modelo.negocio.Programador;

/**
 * FXML Controller class
 *
 * @author alonso
 */
public class PantallaRegistrarUsuarioController implements Initializable {

    private ResourceBundle recurso;
    private PantallaPrincipalController controlador;
    @FXML
    private Label etiquetaCrearCuenta;
    @FXML
    private Label etiquetaNombreUsuario;
    @FXML
    private Label etiquetaEmail;
    @FXML
    private Label etiquetaContraseña;
    @FXML
    private JFXButton botonCrearCuenta;
    @FXML
    private JFXButton botonCancelar;

    private IProgramador stub;
    @FXML
    private JFXTextField campoTextoNombreUsuario;
    @FXML
    private JFXTextField campoTextoCorreoElectronico;
    @FXML
    private JFXPasswordField campoTextoContraseña;

    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.recurso = rb;
        configurarIdioma();
        inicializarRegistro();
        // TODO
    }

    public void inicializarRegistro() {
        try {
            Registry registry = LocateRegistry.getRegistry(null);
            stub = (IProgramador) registry.lookup("AdministrarUsuarios");
        } catch (RemoteException | NotBoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void setControlador(PantallaPrincipalController controlador) {
        this.controlador = controlador;
    }

    public void configurarIdioma() {
        etiquetaCrearCuenta.setText(recurso.getString("etCrearCuenta"));
        etiquetaNombreUsuario.setText(recurso.getString("etNombreUsuario"));
        etiquetaEmail.setText(recurso.getString("etEmail"));
        etiquetaContraseña.setText(recurso.getString("etContraseña"));
        botonCancelar.setText(recurso.getString("btCancelar"));
        botonCrearCuenta.setText(recurso.getString("btCrearCuenta"));
    }

    @FXML
    private void botonCancelar(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        ventanaInicioSesion(recurso, controlador);
    }

    @FXML
    private void botonCrearCuenta(ActionEvent event) {
        Programador programador = new Programador();
        if (campoTextoContraseña.getText().isEmpty() || campoTextoCorreoElectronico.getText().isEmpty() || campoTextoNombreUsuario.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(recurso.getString("atencion"));
            String s = recurso.getString("mensajeCamposVacios");
            alert.setContentText(s);
            alert.show();
        } else {
            if (validarCorreo(campoTextoCorreoElectronico.getText())) {
                programador.setNombreUsuario(campoTextoNombreUsuario.getText());
                programador.setContraseña(PantallaIniciarSesionController.makeHash(campoTextoContraseña.getText()));
                programador.setCorreoElectronico(campoTextoCorreoElectronico.getText());

                try {

                    if (stub.registrarUsuario(programador)) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText(recurso.getString("felicidades"));
                        String s = recurso.getString("mensajeCuentaCreada");
                        alert.setContentText(s);
                        alert.show();
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.close();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText(recurso.getString("atencion"));
                        String s = recurso.getString("mensajeNombreUsuarioExistente");
                        alert.setContentText(s);
                        alert.show();

                    }
                } catch (RemoteException | java.lang.NullPointerException ex) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(recurso.getString("atencion"));
                    String s = recurso.getString("mensajeNoConexion");
                    alert.setContentText(s);
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(recurso.getString("atencion"));
                String s = recurso.getString("mensajeCorreoInvalido");
                alert.setContentText(s);
                alert.show();
            }
        }
    }

    @FXML
    private void limitarCaracteresNombreUsuario(KeyEvent event) {
        int limiteCaracteres = 20;
        if (campoTextoNombreUsuario.getText().length() > limiteCaracteres) {
            event.consume();
        }
    }

    @FXML
    private void limitarCaracteresCorreoElectronico(KeyEvent event) {
        int limiteCaracteres = 50;
        if (campoTextoCorreoElectronico.getText().length() > limiteCaracteres) {
            event.consume();
        }
    }

    public static boolean validarCorreo(String correo) {
        Pattern patron = Pattern.compile(PATTERN_EMAIL);
        Matcher matcher = patron.matcher(correo);
        return matcher.matches();
    }

}
