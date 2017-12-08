/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import static idecolaborativo.IDEColaborativo.mensajeAlert;
import static idecolaborativo.IDEColaborativo.ventanaHost;
import io.socket.client.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import modelo.negocio.Archivo;
import modelo.negocio.Carpeta;
import modelo.negocio.Proyecto;
import org.json.JSONArray;
import org.json.JSONObject;

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
    private static ResourceBundle recurso;
    private ArrayList<Proyecto> proyectos;
    @FXML
    private Label etiquetaProyecto;
    @FXML
    private JFXComboBox<String> comboProyectos;

    private static PantallaPrincipalController controlador;
    private static Stage stagePantallaInivitar;
    private static Proyecto proyecto;

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

    public void setStagePantallaInivitar(Stage stagePantallaInivitar) {
        this.stagePantallaInivitar = stagePantallaInivitar;
        this.stagePantallaInivitar.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                controlador.hacerVisiblePantallaprincipal();
            }
        });
    }

    public void setControlador(PantallaPrincipalController controlador) {
        this.controlador = controlador;
    }

    public void setProyectos(ArrayList<Proyecto> proyectos) {
        this.proyectos = proyectos;
        ObservableList<String> items = FXCollections.observableArrayList();
        items.addAll(obtenerNombresProyectos());
        comboProyectos.setItems(items);
    }

    public void configurarIdioma() {
        etiquetaNombreColaborador.setText(recurso.getString("etNombreColaborador"));
        botonInvitar.setText(recurso.getString("btInvitar"));
        botonCancelar.setText(recurso.getString("btCancelar"));
    }

    public ArrayList<String> obtenerNombresProyectos() {
        ArrayList<String> nombreProyectos = new ArrayList();
        for (Proyecto proyecto : proyectos) {
            nombreProyectos.add(proyecto.getNombreProyecto());
        }
        return nombreProyectos;
    }

    public Proyecto buscarProyecto(String nombre) {
        Proyecto proyectoEnviar = new Proyecto();
        for (Proyecto proyecto : proyectos) {
            if (proyecto.getNombreProyecto().equals(nombre)) {
                proyectoEnviar = proyecto;
            }
        }
        return proyectoEnviar;
    }

    @FXML
    private void invitarColaborador(ActionEvent event) {

        if (!campoTextoNombreColaborador.getText().isEmpty() && comboProyectos.getValue() != null) {
            proyecto = obtenerProyectoCombo();
            socket.emit("invitarColaborador", campoTextoNombreColaborador.getText(), crearObjetoJSON(proyecto));

        } else {
            mensajeAlert(recurso.getString("atencion"), recurso.getString("mensajeCamposVacios"));
        }
    }

    public static void mostrarVentanaHost() {
        ventanaHost(recurso, proyecto, controlador);
        stagePantallaInivitar.close();
    }

    public static void invitacionErronea() {
        mensajeAlert(recurso.getString("atencion"), recurso.getString("mensajeColaboradorNoEncontrado"));
    }

    public static void mensajeRecursivo() {
        mensajeAlert(recurso.getString("atencion"), recurso.getString("mensajeRecursivo"));
    }

    public Proyecto obtenerProyectoCombo() {
        Proyecto proyecto = null;
        for (Proyecto proyectoColaborativo : proyectos) {
            if (comboProyectos.getValue().equals(proyectoColaborativo.getNombreProyecto())) {
                proyecto = proyectoColaborativo;
                break;
            }
        }

        return proyecto;
    }

    @FXML
    private void cancelar(ActionEvent event) {
        Stage ventanaInvitarColaborador = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ventanaInvitarColaborador.close();
        controlador.hacerVisiblePantallaprincipal();
    }

    public static JSONObject crearObjetoJSON(Proyecto proyecto) {
        JSONObject proyectoJSON = new JSONObject();
        proyectoJSON.put("nombreProyecto", proyecto.getNombreProyecto());
        proyectoJSON.put("lenguaje", proyecto.getLenguaje());
        JSONArray arregloCarpetas = new JSONArray();
        for (Carpeta carpeta : proyecto.getCarpetas()) {
            JSONObject carpetaJSON = new JSONObject();
            carpetaJSON.put("nombreCarpeta", carpeta.getNombreCarpeta());
            JSONArray arregloArchivos = new JSONArray();
            for (Archivo archivo : carpeta.getArchivos()) {
                JSONObject archivoJSON = new JSONObject();
                archivoJSON.put("nombreArchivo", archivo.getNombreArchivo());
                archivoJSON.put("contenido", archivo.getContenido());
                archivoJSON.put("ruta", archivo.getRuta());
                archivoJSON.put("rutaClases", archivo.getRutaClases());
                archivoJSON.put("paquete", archivo.getPaquete());
                arregloArchivos.put(archivoJSON);
            }
            carpetaJSON.put("archivos", arregloArchivos);
            arregloCarpetas.put(carpetaJSON);
        }
        proyectoJSON.put("carpetas", arregloCarpetas);
        return proyectoJSON;
    }

}
