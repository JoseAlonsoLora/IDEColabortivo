/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import clasesApoyo.MyTab;
import clasesApoyo.MyTreeItem;
import com.jfoenix.controls.JFXButton;
import componentes.FormatoCodigo;
import static controladores.PantallaInvitadoController.transformarJSONArchivo;
import static idecolaborativo.IDEColaborativo.mensajeAlert;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import modelo.negocio.Archivo;
import modelo.negocio.Carpeta;
import modelo.negocio.Proyecto;
import org.fxmisc.richtext.CodeArea;
import org.json.JSONObject;

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
    private static ResourceBundle recurso;
    private Proyecto proyecto;
    private PantallaPrincipalController controlador;
    private Stage stagePantallaHost;
    @FXML
    private TreeTableColumn<String, String> columnaProyecto;
     private TreeItem<String> root;
     private static ArrayList<MyTab> tabsAbiertosHost;
     
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        recurso = rb;
        root = new TreeItem<>(recurso.getString("etProyectos"));
        tabsAbiertosHost = new ArrayList();
    }    

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public void setControlador(PantallaPrincipalController controlador) {
        this.controlador = controlador;
        controlador.getConexionNode().setControladorHost(this);
    }

    public void setStagePantallaHost(Stage stagePantallaHost) {
        this.stagePantallaHost = stagePantallaHost;
        this.stagePantallaHost.setOnCloseRequest(new EventHandler<WindowEvent>(){
            @Override public void handle(WindowEvent event) {
                controlador.hacerVisiblePantallaprincipal();
                controlador.getSocket().emit("terminarSesionHost");
            }  
        });
    }
    
    

    @FXML
    private void guardarArchivo(ActionEvent event) {
        controlador.guardarArchivo(tablaArchivos);
    }

    @FXML
    private void compilar(ActionEvent event) {
        controlador.compilarArchivo(tablaArchivos, true, false);
    }

    @FXML
    private void ejecutar(ActionEvent event) {
        controlador.ejecutarArchivo(tablaArchivos, true);
    }
    
    public void cargarProyecto(){
        TreeItem<String> hijo = new TreeItem<>(proyecto.getNombreProyecto(), controlador.crearIconoLenguaje(proyecto.getLenguaje()));
        hijo.getChildren().setAll(controlador.agregarCarpetasArbol(proyecto,obtenerNombreCarpetas(proyecto)));
        root.getChildren().add(hijo);
        columnaProyecto.setCellValueFactory((TreeTableColumn.CellDataFeatures<String, String> p) -> new ReadOnlyStringWrapper(p.getValue().getValue()));
        tablaProyecto.setRoot(root);
        tablaProyecto.setShowRoot(true);
        controlador.handlerTablaProyectos(tablaProyecto, tabsAbiertosHost, tablaArchivos,true);
    }
   
    public static ArrayList<String> obtenerNombreCarpetas(Proyecto proyecto){
        ArrayList<String> nombreCarpetas = new ArrayList();
        for(Carpeta carpeta: proyecto.getCarpetas()){
            nombreCarpetas.add(carpeta.getNombreCarpeta());
        }
        return nombreCarpetas;
    }
    
    public static void colaboradorDesconectado(){
        mensajeAlert(recurso.getString("atencion"), recurso.getString("mensajeColaboradorDesconectado"));
    }
    
    public static void escribirCodigoHost(String texto,String ruta){
        for(MyTab myTab:tabsAbiertosHost){
            if((myTab.getTreeItem().getArchivo().getRuta()+myTab.getTreeItem().getArchivo().getNombreArchivo()).equals(ruta)){
                ((CodeArea) myTab.getContent()).replaceText(texto);
            }
        }
    }
    
    public void abrirTabHost(JSONObject archivoJSON) {
        Archivo archivo = transformarJSONArchivo(archivoJSON);
        MyTreeItem treeItem = new MyTreeItem();
        treeItem.setArchivo(archivo);
        MyTab tab = new MyTab(archivo.getNombreArchivo());
        FormatoCodigo areaCodigo = new FormatoCodigo();
        areaCodigo.setSampleCode(treeItem.getArchivo().getContenido());
        tab.setContent(areaCodigo.crearAreaCodigo());
        areaCodigo.getCodeArea().setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                treeItem.setModificado(true);
                controlador.getSocket().emit("escribirCodigo", areaCodigo.getCodeArea().getText(), treeItem.getArchivo().getRuta()+treeItem.getArchivo().getNombreArchivo());
            }
        });
        tab.setTreeItem(treeItem);
        tablaArchivos.getTabs().add(tab);
        tabsAbiertosHost.add(tab);
    }
    
   

    
    
}
