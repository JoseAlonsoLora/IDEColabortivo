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
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import static idecolaborativo.IDEColaborativo.mensajeAlert;
import static idecolaborativo.IDEColaborativo.ventanaEjecutar;
import static idecolaborativo.IDEColaborativo.resultadoCompilacion;
import static idecolaborativo.IDEColaborativo.ventanaCambiarIdioma;
import static idecolaborativo.IDEColaborativo.ventanaCrearProyecto;
import static idecolaborativo.IDEColaborativo.ventanaInicioSesion;
import static idecolaborativo.IDEColaborativo.ventanaInvitado;
import static idecolaborativo.IDEColaborativo.ventanaInvitarColaborador;
import io.socket.client.Socket;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import modelo.negocio.Archivo;
import modelo.negocio.Carpeta;
import modelo.negocio.Proyecto;
import org.fxmisc.richtext.CodeArea;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author raymu
 */
public class PantallaPrincipalController implements Initializable {

    @FXML
    private AnchorPane panelBarraMenu;
    @FXML
    private JFXButton botonCrearProyecto;
    @FXML
    private JFXButton botonGuardar;
    @FXML
    private AnchorPane panelProyectos;
    @FXML
    private MenuItem iniciarSesion;
    @FXML
    private MenuItem cambiarIdioma;
    @FXML
    private MenuItem cerrarSesion;
    @FXML
    private MaterialDesignIconView iconoSesionIniciada;
    @FXML
    private Label etiquetaNombreUsuario;
    @FXML
    private MenuButton botonConfiguracion;
    @FXML
    private TreeTableView<String> tablaProyectos;
    @FXML
    private TreeTableColumn<String, String> columnaProyectos;
    @FXML
    private TabPane tablaArchivos;

    private ResourceBundle recurso;
    private PantallaPrincipalController controlador;
    private ArrayList<MyTreeItem> tabsAbiertos;
    private TreeItem<String> root;
    @FXML
    private JFXButton botonCompilar;
    @FXML
    private JFXButton botonEjecutar;
    @FXML
    private JFXButton botonInvitarColaborador;

    private Socket socket;
    
    private ArrayList<Proyecto> proyectos;
    
    private Stage stagePantallaPrincipal;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Proyecto proyecto = new Proyecto();
        proyecto.crearArchivoRutas();
        tabsAbiertos = new ArrayList();
        iconoSesionIniciada.setVisible(false);
        etiquetaNombreUsuario.setVisible(false);
        cerrarSesion.setVisible(false);
        recurso = rb;
        etiquetaNombreUsuario.setText("");
        root = new TreeItem<>(recurso.getString("etProyectos"));
        configurarIdioma();
        tablaArchivos.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
        cargarProyectos();
        handlerTablaProyectos(tablaProyectos,tabsAbiertos,tablaArchivos);

    }

    public void handlerTablaProyectos(TreeTableView<String> tablaProyectos,ArrayList<MyTreeItem> tabsAbiertos,TabPane tablaArchivos) {
        tablaProyectos.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldVal, Object newVal) {
                if ("class javafx.scene.control.TreeItem".equals(newVal.getClass().toString())) {
                    TreeItem treeItem = (TreeItem) newVal;
                } else {

                    MyTreeItem treeItem = (MyTreeItem) newVal;
                    if (!tabsAbiertos.contains(treeItem)) {
                        MyTab tab = new MyTab(treeItem.getArchivo().getNombreArchivo());
                        FormatoCodigo areaCodigo = new FormatoCodigo();
                        areaCodigo.setSampleCode(treeItem.getArchivo().getContenido());
                        tab.setContent(areaCodigo.crearAreaCodigo());
                        areaCodigo.getCodeArea().setOnKeyTyped(new EventHandler<KeyEvent>() {
                            @Override
                            public void handle(KeyEvent event) {
                                treeItem.setModificado(true);
                            }
                        });
                        tab.setTreeItem(treeItem);
                        handlerCerrarProyectoTab(tab, treeItem,tabsAbiertos,tablaArchivos);
                    }
                }

            }
        });
    }

    public void handlerCerrarProyectoTab(MyTab tab, MyTreeItem treeItem,ArrayList<MyTreeItem> tabsAbiertos,TabPane tablaArchivos) {
        tab.setOnCloseRequest((Event t) -> {
            if (treeItem.isModificado()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText(recurso.getString("atencion"));
                String s = recurso.getString("mensajeDeseaGuardar");
                alert.setContentText(s);
                ButtonType botonGuardar1 = new ButtonType(recurso.getString("btGuardar"));
                ButtonType botonDescartar = new ButtonType(recurso.getString("btDescartar"));
                ButtonType botonCancelar = new ButtonType(recurso.getString("btCancelar"), ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(botonGuardar1, botonDescartar, botonCancelar);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == botonGuardar1) {
                    botonGuardarArchivo(null);
                    tabsAbiertos.remove(treeItem);
                } else if (result.get() == botonCancelar) {
                    t.consume();
                } else {
                    tabsAbiertos.remove(treeItem);
                    treeItem.setModificado(false);
                }
            } else {
                tabsAbiertos.remove(treeItem);
            }
        });
        tablaArchivos.getTabs().add(tab);
        tabsAbiertos.add(treeItem);
    }

    public void setStagePantallaPrincipal(Stage stagePantallaPrincipal) {
        this.stagePantallaPrincipal = stagePantallaPrincipal;
    }
    

    public void setRecurso(ResourceBundle recurso) {
        this.recurso = recurso;
        configurarIdioma();
    }

    public void setControlador(PantallaPrincipalController controlador) {
        this.controlador = controlador;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void configurarIdioma() {
        iniciarSesion.setText(recurso.getString("etInicioSesion"));
        cambiarIdioma.setText(recurso.getString("etCambiarIdioma"));
        cerrarSesion.setText(recurso.getString("btCerrarSesion"));
    }

    @FXML
    private void botonCrearProyecto(ActionEvent event) throws IOException {
        ventanaCrearProyecto(recurso, controlador);
    }

    @FXML
    private void botonIniciarSesion(ActionEvent event) throws IOException {
        ventanaInicioSesion(recurso, controlador);
    }

    @FXML
    private void botonCambiarIdioma(ActionEvent event) throws IOException {
        ventanaCambiarIdioma(recurso, controlador);
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        iconoSesionIniciada.setVisible(false);
        etiquetaNombreUsuario.setVisible(false);
        etiquetaNombreUsuario.setText("");
        cerrarSesion.setVisible(false);
        iniciarSesion.setVisible(true);

    }

    public void sesionIniciada(String nombreUsuario) {
        iconoSesionIniciada.setVisible(true);
        etiquetaNombreUsuario.setText(nombreUsuario);
        etiquetaNombreUsuario.setVisible(true);
        cerrarSesion.setVisible(true);
        iniciarSesion.setVisible(false);
    }

    public void invitacionEnviada(String lobby, String nombreColaborador,JSONObject proyecto) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(nombreColaborador +" "+recurso.getString("mensajeInvitacion"));
        ButtonType botonAceptar = new ButtonType(recurso.getString("btAceptar"));
        ButtonType botonCancelar = new ButtonType(recurso.getString("btRechazar"), ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(botonAceptar, botonCancelar);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == botonAceptar) {
            socket.emit("conectarseALobby", lobby);
            stagePantallaPrincipal.hide();
            ventanaInvitado(recurso,proyecto,controlador);
            
        }

    }

    

    public void cargarProyectos() {
        Proyecto proyecto = new Proyecto();
        proyectos = proyecto.cargarProyectos();
        ArrayList<TreeItem<String>> carpetas;
        ArrayList<TreeItem<String>> proyectosArbol = new ArrayList();
        for (Proyecto proyecto1 : proyectos) {
            TreeItem<String> hijo = new TreeItem<>(proyecto1.getNombreProyecto(),
                    crearIconoLenguaje(proyecto1.getLenguaje()));
            carpetas = agregarCarpetasArbol(proyecto1);
            hijo.getChildren().setAll(carpetas);
            proyectosArbol.add(hijo);

        }
        root.getChildren().setAll(proyectosArbol);
        columnaProyectos.setCellValueFactory((CellDataFeatures<String, String> p) -> new ReadOnlyStringWrapper(p.getValue().getValue()));
        tablaProyectos.setRoot(root);
        tablaProyectos.setShowRoot(true);

    }

    public void cargarNuevoProyecto(Proyecto proyecto) {
        TreeItem<String> hijo = new TreeItem<>(proyecto.getNombreProyecto(), crearIconoLenguaje(proyecto.getLenguaje()));
        hijo.getChildren().setAll(agregarCarpetasArbol(proyecto));
        root.getChildren().add(hijo);
        columnaProyectos.setCellValueFactory((CellDataFeatures<String, String> p) -> new ReadOnlyStringWrapper(p.getValue().getValue()));
        tablaProyectos.setRoot(root);
        tablaProyectos.setShowRoot(true);

    }

    public ImageView crearIconoCarpeta() {
        ImageView carpeta;
        carpeta = new ImageView("/Imagenes/carpeta_1.png");
        carpeta.setFitHeight(15);
        carpeta.setFitWidth(15);
        return carpeta;
    }

    public ImageView crearIconoLenguaje(String lenguajeProgramacion) {
        ImageView lenguaje = null;
        switch (lenguajeProgramacion) {
            case "java":
                lenguaje = new ImageView("/Imagenes/java.png");
                lenguaje.setFitHeight(35);
                lenguaje.setFitWidth(35);
                break;
            case "c#":
                lenguaje = new ImageView("/Imagenes/c#.png");
                lenguaje.setFitHeight(20);
                lenguaje.setFitWidth(20);
                break;
            case "c++":
                lenguaje = new ImageView("/Imagenes/cpp_logo.png");
                lenguaje.setFitHeight(15);
                lenguaje.setFitWidth(15);
                break;
            default:
                break;
        }

        return lenguaje;
    }

    public ImageView crearIconoArchivo(String lenguajeProgramacion) {
        ImageView lenguaje = null;
        switch (lenguajeProgramacion) {
            case "java":
                lenguaje = new ImageView("/Imagenes/archivoJava.png");
                lenguaje.setFitHeight(20);
                lenguaje.setFitWidth(20);
                break;
            case "c#":
                lenguaje = new ImageView("/Imagenes/archivoCSharp.png");
                lenguaje.setFitHeight(20);
                lenguaje.setFitWidth(20);
                break;
            case "c++":
                lenguaje = new ImageView("/Imagenes/cpp_logo.png");
                lenguaje.setFitHeight(15);
                lenguaje.setFitWidth(15);
                break;
            default:
                break;
        }

        return lenguaje;
    }

    public ArrayList<TreeItem<String>> agregarCarpetasArbol(Proyecto proyecto) {
        ArrayList<Carpeta> carpetasProyecto = proyecto.getCarpetas();
        ArrayList<TreeItem<String>> carpetas = new ArrayList();
        for (Carpeta carpeta : carpetasProyecto) {
            TreeItem<String> hijo = new TreeItem<>(carpeta.getNombreCarpeta(), crearIconoCarpeta());
            hijo.getChildren().addAll(agregarArchivosArbol(carpeta, proyecto.getLenguaje()));
            carpetas.add(hijo);
        }

        return carpetas;
    }

    public ArrayList<MyTreeItem> agregarArchivosArbol(Carpeta carpeta, String lenguaje) {
        ArrayList<Archivo> archivos = carpeta.getArchivos();
        ArrayList<MyTreeItem> treeArchivos = new ArrayList();
        for (Archivo archivo : archivos) {
            MyTreeItem hijo = new MyTreeItem(archivo.getNombreArchivo(), crearIconoArchivo(lenguaje));
            hijo.setArchivo(archivo);
            treeArchivos.add(hijo);

        }
        return treeArchivos;
    }

    @FXML
    private void botonGuardarArchivo(ActionEvent event) {
        if (tablaArchivos.getSelectionModel().getSelectedItem() != null) {
            CodeArea area = (CodeArea) tablaArchivos.getSelectionModel().getSelectedItem().getContent();
            MyTab tabSeleccionado = (MyTab) tablaArchivos.getSelectionModel().getSelectedItem();
            Archivo archivo = tabSeleccionado.getTreeItem().getArchivo();
            archivo.setContenido(area.getText());
            archivo.guardarArchivo(archivo);
            tabSeleccionado.getTreeItem().setModificado(false);
        }
    }

    @FXML
    private boolean botonCompilar(ActionEvent event) {
        boolean compilo = false;
        if (tablaArchivos.getSelectionModel().getSelectedItem() != null) {
            botonGuardarArchivo(null);
            MyTab tabSeleccionado = (MyTab) tablaArchivos.getSelectionModel().getSelectedItem();
            Archivo archivo = tabSeleccionado.getTreeItem().getArchivo();
            String resultado = archivo.compilarArchivo(archivo);
            if (resultado.isEmpty()) {
                compilo = true;
                if (event != null) {
                    mensajeAlert(recurso.getString("felicidades"), recurso.getString("mensajeCompilacionExitosa"));
                }
            } else {
                resultadoCompilacion(resultado, recurso);
            }
        }
        return compilo;
    }

    @FXML
    private void botonEjecutar(ActionEvent event) {
        if (tablaArchivos.getSelectionModel().getSelectedItem() != null) {
            if (botonCompilar(null)) {
                MyTab tabSeleccionado = (MyTab) tablaArchivos.getSelectionModel().getSelectedItem();
                ventanaEjecutar(recurso, tabSeleccionado.getTreeItem().getArchivo());
            }

        }
    }

    @FXML
    private void invitarColaborador(ActionEvent event) {
        if(!etiquetaNombreUsuario.getText().isEmpty()){
        ventanaInvitarColaborador(recurso, socket,proyectos,controlador);
        }else{
            mensajeAlert(recurso.getString("atencion"), recurso.getString("mensajeDebesIniciarSesion"));
        }
    }
    
    public void hacerVisiblePantallaprincipal(){
        stagePantallaPrincipal.show();
    }
    
    public void hacerInvisiblePantallaprincipal(){
        stagePantallaPrincipal.hide();
    }
}
