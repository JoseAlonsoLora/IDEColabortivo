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
import static idecolaborativo.IDEColaborativo.mensajeAlert;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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
    private final String mensajeAtencion = "atencion";

    private Stage stagePantallaRegistrarUsuario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.recurso = rb;
        configurarIdioma();
    }

    public void setControlador(PantallaPrincipalController controlador) {
        this.controlador = controlador;
        inicializarRegistro();
    }

    public void inicializarRegistro() {
        try {
            Registry registry = LocateRegistry.getRegistry(controlador.getDireccionIP());
            stub = (IProgramador) registry.lookup("AdministrarUsuarios");
        } catch (RemoteException | NotBoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void setStagePantallaRegistrarUsuario(Stage stagePantallaRegistrarUsuario) {
        this.stagePantallaRegistrarUsuario = stagePantallaRegistrarUsuario;
        this.stagePantallaRegistrarUsuario.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                controlador.hacerVisiblePantallaprincipal();
            }
        });
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
            mensajeAlert(recurso.getString(mensajeAtencion), recurso.getString("mensajeCamposVacios"));
        } else {
            if (validarCorreo(campoTextoCorreoElectronico.getText())) {
                if (datosRegistroValidos()) {
                    programador.setNombreUsuario(campoTextoNombreUsuario.getText());
                    programador.setContraseña(PantallaIniciarSesionController.makeHash(campoTextoContraseña.getText()));
                    programador.setCorreoElectronico(campoTextoCorreoElectronico.getText());

                    try {

                        if (stub.registrarUsuario(programador)) {
                            mensajeAlert(recurso.getString("felicidades"), recurso.getString("mensajeCuentaCreada"));
                            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            stage.close();
                        } else {
                            mensajeAlert(recurso.getString(mensajeAtencion), recurso.getString("mensajeNombreUsuarioExistente"));

                        }
                    } catch (RemoteException | java.lang.NullPointerException ex) {
                        mensajeAlert(recurso.getString(mensajeAtencion), recurso.getString("mensajeNoConexion"));
                    }
                } else {
                    mensajeAlert(recurso.getString(mensajeAtencion), recurso.getString("mensajeDatosInvalidos"));
                }
            } else {
                mensajeAlert(recurso.getString(mensajeAtencion), recurso.getString("mensajeCorreoInvalido"));
            }
        }
    }

    public boolean datosRegistroValidos() {
        return campoTextoNombreUsuario.getText().length() <= 20 && campoTextoCorreoElectronico.getText().length() <= 50;
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
