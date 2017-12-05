/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import clasesApoyo.MyTreeItem;
import com.jfoenix.controls.JFXButton;
import static idecolaborativo.IDEColaborativo.mensajeAlert;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.stage.Stage;
import modelo.negocio.Proyecto;

/**
 * FXML Controller class
 *
 * @author alonso
 */
public class PantallaHostController implements Initializable {

    @FXML
    private TreeTableView<String> tablaProyecto;
    @FXML
    private JFXButton botonGuardar;
    @FXML
    private JFXButton botonCompilar;
    @FXML
    private JFXButton botonEjecutar;
    @FXML
    private TabPane tablaArchivos;
    private ResourceBundle recurso;
    private Proyecto proyecto;
    private PantallaPrincipalController controlador;
    private Stage stagePantallaHost;
    @FXML
    private TreeTableColumn<String, String> columnaProyecto;
     private TreeItem<String> root;
     private ArrayList<MyTreeItem> tabsAbiertos;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        recurso = rb;
        root = new TreeItem<>(recurso.getString("etProyectos"));
        tabsAbiertos = new ArrayList();
    }    

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public void setControlador(PantallaPrincipalController controlador) {
        this.controlador = controlador;
    }

    public void setStagePantallaHost(Stage stagePantallaHost) {
        this.stagePantallaHost = stagePantallaHost;
    }
    
    

    @FXML
    private void guardarArchivo(ActionEvent event) {
    }

    @FXML
    private void compilar(ActionEvent event) {
    }

    @FXML
    private void ejecutar(ActionEvent event) {
    }
    
    public static void colaboradorConectado(String nombre){
        mensajeAlert("",nombre +" se ha conectado");
    }
    
    public void cargarProyecto(){
        TreeItem<String> hijo = new TreeItem<>(proyecto.getNombreProyecto(), controlador.crearIconoLenguaje(proyecto.getLenguaje()));
        hijo.getChildren().setAll(controlador.agregarCarpetasArbol(proyecto));
        root.getChildren().add(hijo);
        columnaProyecto.setCellValueFactory((TreeTableColumn.CellDataFeatures<String, String> p) -> new ReadOnlyStringWrapper(p.getValue().getValue()));
        tablaProyecto.setRoot(root);
        tablaProyecto.setShowRoot(true);
        controlador.handlerTablaProyectos(tablaProyecto, tabsAbiertos, tablaArchivos);
    }
}
