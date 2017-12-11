package controladores;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import conexion.node.ConexionNode;
import conexion.operaciones.IProgramador;
import static idecolaborativo.IDEColaborativo.mensajeAlert;
import static idecolaborativo.IDEColaborativo.ventanaRegistrarUsuario;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import modelo.negocio.Programador;

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
    private IProgramador stub;
    private final String mensajeAtencion = "atencion";
    private Stage stagePantallaIniciarSesion;

    /**
     *
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
     * Dar valor al stage para poder manipular la pantalla iniciar sesión
     * @param stagePantallaIniciarSesion Stage de la instancia actual
     */
    public void setStagePantallaIniciarSesion(Stage stagePantallaIniciarSesion) {
        this.stagePantallaIniciarSesion = stagePantallaIniciarSesion;
        this.stagePantallaIniciarSesion.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                controlador.hacerVisiblePantallaprincipal();
            }
        });
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
     * Configura el idioma de todas etiquetas de la pantalla
     */
    public void configurarIdioma() {
        etiquetaIniciarSesion.setText(recurso.getString("etInicioSesion"));
        etiquetaNombreUsuario.setText(recurso.getString("etNombreUsuario"));
        botonIniciarSesion.setText(recurso.getString("btIniciarSesion"));
        etiquetaCrearCuenta.setText(recurso.getString("etCrearNuevaCuenta"));
        etiquetaContraseña.setText(recurso.getString("etContraseña"));
        botonCancelar.setText(recurso.getString("btCancelar"));
    }

    /**
     * Evento para salir de la pantalla
     *
     * @param event Clic del usuario
     */
    @FXML
    private void botonCancelar(ActionEvent event) {
        controlador.hacerVisiblePantallaprincipal();
        Stage ventanaIniciarSesion = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ventanaIniciarSesion.close();
    }

    /**
     * Evento para iniciar sesisón
     *
     * @param event Clic del usuario
     */
    @FXML
    private void botonIniciarSesion(ActionEvent event) {
        Programador programador = new Programador();
        if (campoTextoNombreUsuario.getText().isEmpty() || campoTextoContraseña.getText().isEmpty()) {
            mensajeAlert(recurso.getString(mensajeAtencion), recurso.getString("mensajeCamposVacios"));
        } else {
            programador.setNombreUsuario(campoTextoNombreUsuario.getText());
            programador.setContraseña(makeHash(campoTextoContraseña.getText()));
            try {
                switch (stub.iniciarSesion(programador)) {
                    case DatosValidos:
                        ConexionNode conexionNode = new ConexionNode(controlador);
                        conexionNode.getSocket().emit("agregarNombre", campoTextoNombreUsuario.getText());
                        controlador.sesionIniciada(campoTextoNombreUsuario.getText());
                        controlador.setSocket(conexionNode.getSocket());
                        controlador.setConexionNode(conexionNode);
                        controlador.hacerVisiblePantallaprincipal();
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.close();
                        break;
                    case DatosInvalidos:
                        mensajeAlert(recurso.getString(mensajeAtencion), recurso.getString("mensajeDatosIncorrectos"));
                        break;
                    case SesionIniciada:
                        mensajeAlert(recurso.getString(mensajeAtencion), recurso.getString("mensajeSesionIniciada"));
                        break;
                }
            } catch (RemoteException | java.lang.NullPointerException ex) {
                mensajeAlert(recurso.getString(mensajeAtencion), recurso.getString("mensajeNoConexion"));
            }
        }

    }

    /**
     * Evento para mostrar la pantalla de registrar usuario
     * @param event Clic del usuario
     */
    @FXML
    private void etiquetaCrearCuenta(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        ventanaRegistrarUsuario(recurso, controlador);
    }

    /**
     * Encripta una cadena con el algoritmo SAH2
     * @param string Cadena que será encriptada
     * @return Cadena encriptada
     */
    public static String makeHash(String string) {
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
