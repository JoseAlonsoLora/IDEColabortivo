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
    private static final String MENSAJE_ATENCION = "atencion";

    private Stage stagePantallaRegistrarUsuario;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.recurso = rb;
        configurarIdioma();
    }
    
    /**
     * Da valor al controlador para poder manipular componentes de la pantalla principal
     *
     * @param controlador Instancia del controlador
     */
    public void setControlador(PantallaPrincipalController controlador) {
        this.controlador = controlador;
        inicializarRegistro();
    }

    /**
     * Conecta con el servidor RMI
     */
    public void inicializarRegistro() {
        try {
            Registry registry = LocateRegistry.getRegistry(controlador.getDireccionIP());
            stub = (IProgramador) registry.lookup("AdministrarUsuarios");
        } catch (RemoteException | NotBoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    /**
     * ar valor al stage para poder manipular la pantalla registrar usuario
     * @param stagePantallaRegistrarUsuario Stage de la instancia actual
     */
    public void setStagePantallaRegistrarUsuario(Stage stagePantallaRegistrarUsuario) {
        this.stagePantallaRegistrarUsuario = stagePantallaRegistrarUsuario;
        this.stagePantallaRegistrarUsuario.setOnCloseRequest((WindowEvent event) -> {
            controlador.hacerVisiblePantallaprincipal();
        });
    }

    /**
     * Configura el idioma de todas etiquetas de la pantalla
     */
    public void configurarIdioma() {
        etiquetaCrearCuenta.setText(recurso.getString("etCrearCuenta"));
        etiquetaNombreUsuario.setText(recurso.getString("etNombreUsuario"));
        etiquetaEmail.setText(recurso.getString("etEmail"));
        etiquetaContraseña.setText(recurso.getString("etContraseña"));
        botonCancelar.setText(recurso.getString("btCancelar"));
        botonCrearCuenta.setText(recurso.getString("btCrearCuenta"));
    }

    /**
     * Evento para salir de la pantalla
     *
     * @param event Clic del usuario
     */
    @FXML
    private void botonCancelar(ActionEvent event) {
        stagePantallaRegistrarUsuario.close();
        ventanaInicioSesion(recurso, controlador);
    }

    /**
     * Evento para crear una cuenta en el sistema
     * @param event Clic del usuario
     */
    @FXML
    private void botonCrearCuenta(ActionEvent event) {
        Programador programador = new Programador();
        if (campoTextoContraseña.getText().isEmpty() || campoTextoCorreoElectronico.getText().isEmpty() || campoTextoNombreUsuario.getText().isEmpty()) {
            mensajeAlert(recurso.getString(MENSAJE_ATENCION), recurso.getString("mensajeCamposVacios"));
        } else {
            if (validarCorreo(campoTextoCorreoElectronico.getText())) {
                if (datosRegistroValidos()) {
                    programador.setNombreUsuario(campoTextoNombreUsuario.getText());
                    programador.setContraseña(PantallaIniciarSesionController.makeHash(campoTextoContraseña.getText()));
                    programador.setCorreoElectronico(campoTextoCorreoElectronico.getText());

                    try {

                        if (stub.registrarUsuario(programador)) {
                            mensajeAlert(recurso.getString("felicidades"), recurso.getString("mensajeCuentaCreada"));
                            controlador.hacerVisiblePantallaprincipal();
                            stagePantallaRegistrarUsuario.close();
                        } else {
                            mensajeAlert(recurso.getString(MENSAJE_ATENCION), recurso.getString("mensajeNombreUsuarioExistente"));

                        }
                    } catch (RemoteException | java.lang.NullPointerException ex) {
                        mensajeAlert(recurso.getString(MENSAJE_ATENCION), recurso.getString("mensajeNoConexion"));
                    }
                } else {
                    mensajeAlert(recurso.getString(MENSAJE_ATENCION), recurso.getString("mensajeDatosInvalidos"));
                }
            } else {
                mensajeAlert(recurso.getString(MENSAJE_ATENCION), recurso.getString("mensajeCorreoInvalido"));
            }
        }
    }

    /**
     * Valida el tamaño de la cadenas de nombre de usuario y correo electrónico
     * @return Indica si tienen el tamaño valido
     */
    public boolean datosRegistroValidos() {
        return campoTextoNombreUsuario.getText().length() <= 20 && campoTextoCorreoElectronico.getText().length() <= 50;
    }

    /**
     * Limita los caracteres del campo de texto nombre de usuario
     * @param event La presión de un tecla
     */
    @FXML
    private void limitarCaracteresNombreUsuario(KeyEvent event) {
        int limiteCaracteres = 20;
        if (campoTextoNombreUsuario.getText().length() > limiteCaracteres) {
            event.consume();
        }
    }

    /**
     * Limita los caracteres del campo de texto del correo electrónico
     * @param event La presión de un tecla
     */
    @FXML
    private void limitarCaracteresCorreoElectronico(KeyEvent event) {
        int limiteCaracteres = 50;
        if (campoTextoCorreoElectronico.getText().length() > limiteCaracteres) {
            event.consume();
        }
    }

    /**
     * Valida el formato del correo electrónico
     * @param correo Correo electrónico que será validado
     * @return Indica si el correo tiene un formato valido
     */
    public static boolean validarCorreo(String correo) {
        Pattern patron = Pattern.compile(PATTERN_EMAIL);
        Matcher matcher = patron.matcher(correo);
        return matcher.matches();
    }

}
