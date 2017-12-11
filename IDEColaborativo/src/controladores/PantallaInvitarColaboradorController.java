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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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
    @FXML
    private Label etiquetaProyecto;
    @FXML
    private JFXComboBox<String> comboProyectos;
    
    private static PantallaPrincipalController controlador;
    private static Stage stagePantallaInivitar;
    private static Proyecto proyecto;
    private Socket socket;
    private static ResourceBundle recurso;
    private ArrayList<Proyecto> proyectos;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        recurso = rb;
        configurarIdioma();
        mostrarProyectos();
    }
    
    /**
     * Configura el idioma de todas etiquetas de la pantalla
     */
    public void configurarIdioma() {
        etiquetaNombreColaborador.setText(recurso.getString("etNombreColaborador"));
        botonInvitar.setText(recurso.getString("btInvitar"));
        botonCancelar.setText(recurso.getString("btCancelar"));
    }
    
    /**
     * Da valor al socket que contiene la conexión con el servidor de NodeJS
     * @param socket Socket con la conexión al servidor
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * Dar valor al stage para poder manipular la pantalla invitar colaborador
     * @param stagePantallaInivitar Stage de la instancia actual
     */
    public void setStagePantallaInivitar(Stage stagePantallaInivitar) {
        this.stagePantallaInivitar = stagePantallaInivitar;
        PantallaInvitarColaboradorController.stagePantallaInivitar.setOnCloseRequest((WindowEvent event) -> {
            controlador.hacerVisiblePantallaprincipal();
        });
    }

     /**
     * Da valor al controlador para poder manipular componentes de la pantalla principal
     *
     * @param controlador Instancia del controlador
     */
    public void setControlador(PantallaPrincipalController controlador) {
        this.controlador = controlador;
    }

    /**
     * Muestra los proyectos disponibles
     */
    public void mostrarProyectos() {
        Proyecto proyecto = new Proyecto();
        this.proyectos = proyecto.cargarProyectos();
        ObservableList<String> items = FXCollections.observableArrayList();
        items.addAll(obtenerNombresProyectos());
        comboProyectos.setItems(items);
    }

    /**
     * Obtiene el nombre de los proyectos
     * @return Lista con los nombres de los proyectos
     */
    public ArrayList<String> obtenerNombresProyectos() {
        ArrayList<String> nombreProyectos = new ArrayList();
        proyectos.forEach((proyecto) -> {
            nombreProyectos.add(proyecto.getNombreProyecto());
        });
        return nombreProyectos;
    }

    /**
     * Busca el proyeto por su nombre
     * @param nombre Nombre del proyecto
     * @return Proyecto seleccionado
     */
    public Proyecto buscarProyecto(String nombre) {
        Proyecto proyectoEnviar = new Proyecto();
        for (Proyecto proyecto : proyectos) {
            if (proyecto.getNombreProyecto().equals(nombre)) {
                proyectoEnviar = proyecto;
            }
        }
        return proyectoEnviar;
    }

    /**
     * Evento para invitar a un colaborador
     * @param event Clic del usuario
     */
    @FXML
    private void invitarColaborador(ActionEvent event) {

        if (!campoTextoNombreColaborador.getText().isEmpty() && comboProyectos.getValue() != null) {
            proyecto = obtenerProyectoCombo();
            socket.emit("invitarColaborador", campoTextoNombreColaborador.getText(), crearObjetoJSON(proyecto));

        } else {
            mensajeAlert(recurso.getString("atencion"), recurso.getString("mensajeCamposVacios"));
        }
    }

    /**
     * Despliega la pantalla host
     */
    public static void mostrarVentanaHost() {
        controlador.hacerInVisiblePantallaprincipal();
        ventanaHost(recurso, proyecto, controlador);
        stagePantallaInivitar.close();
    }
    
    /**
     * Muestra el mensaje donde indica que la invitación fue enviada
     */
    public static void invitacionEnviada(){
        controlador.hacerVisiblePantallaprincipal();
        controlador.invitacionEnviada();
        stagePantallaInivitar.close();
    }

    /**
     * Muestra el mensaje donde indica que el colaborador no existe
     */
    public static void invitacionErronea() {
        mensajeAlert(recurso.getString("atencion"), recurso.getString("mensajeColaboradorNoEncontrado"));
    }

    /**
     * Muestra el mensaje donde indica que no te puedes invitar a ti mismo
     */
    public static void mensajeRecursivo() {
        mensajeAlert(recurso.getString("atencion"), recurso.getString("mensajeRecursivo"));
    }

    /**
     * Obtene el proyecto seleccionado por el usuario
     * @return Proyecto seleccionado 
     */
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

    /**
     * Evento para salir de la pantalla
     *
     * @param event Clic del usuario
     */
    @FXML
    private void cancelar(ActionEvent event) {
        stagePantallaInivitar.close();
        controlador.hacerVisiblePantallaprincipal();
    }

    /**
     * Transforma un objeto proyecto a un objeto tipo JSON
     * @param proyecto Proyecto que será transformado a un objeto tipo JSON
     * @return Proyecto transformado a objeto JSON
     */
    public static JSONObject crearObjetoJSON(Proyecto proyecto) {
        JSONObject proyectoJSON = new JSONObject();
        proyectoJSON.put("nombreProyecto", proyecto.getNombreProyecto());
        proyectoJSON.put("lenguaje", proyecto.getLenguaje());
        JSONArray arregloCarpetas = new JSONArray();
        proyecto.getCarpetas().stream().map((carpeta) -> {
            JSONObject carpetaJSON = new JSONObject();
            carpetaJSON.put("nombreCarpeta", carpeta.getNombreCarpeta());
            carpetaJSON.put("rutaCarpeta", carpeta.getRutaCarpeta());
            JSONArray arregloArchivos = new JSONArray();
            carpeta.getArchivos().stream().map((archivo) -> {
                JSONObject archivoJSON = new JSONObject();
                archivoJSON.put("nombreArchivo", archivo.getNombreArchivo());
                archivoJSON.put("contenido", archivo.getContenido());
                archivoJSON.put("ruta", archivo.getRuta());
                archivoJSON.put("rutaClases", archivo.getRutaClases());
                archivoJSON.put("paquete", archivo.getPaquete());
                return archivoJSON;
            }).forEachOrdered((archivoJSON) -> {
                arregloArchivos.put(archivoJSON);
            });
            carpetaJSON.put("archivos", arregloArchivos);
            return carpetaJSON;
        }).forEachOrdered((carpetaJSON) -> {
            arregloCarpetas.put(carpetaJSON);
        });
        proyectoJSON.put("carpetas", arregloCarpetas);
        return proyectoJSON;
    }

}
