/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import clasesApoyo.ArchivoConfiguracion;
import clasesApoyo.MyTab;
import clasesApoyo.MyTreeItem;
import clasesApoyo.MyTreeItemCarpeta;
import clasesApoyo.MyTreeItemProyecto;
import com.jfoenix.controls.JFXButton;
import componentes.FormatoCodigo;
import conexion.node.ConexionNode;
import conexion.operaciones.IProgramador;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import static idecolaborativo.IDEColaborativo.mensajeAlert;
import static idecolaborativo.IDEColaborativo.ventanaEjecutar;
import static idecolaborativo.IDEColaborativo.resultadoCompilacion;
import static idecolaborativo.IDEColaborativo.ventanaCambiarIdioma;
import static idecolaborativo.IDEColaborativo.ventanaCrearProyecto;
import static idecolaborativo.IDEColaborativo.ventanaDireccionIP;
import static idecolaborativo.IDEColaborativo.ventanaInicioSesion;
import static idecolaborativo.IDEColaborativo.ventanaInvitado;
import static idecolaborativo.IDEColaborativo.ventanaInvitarColaborador;
import io.socket.client.Socket;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.ImageView;
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
    private ArrayList<MyTab> tabsAbiertos;
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

    private IProgramador stub;

    private ConexionNode conexionNode;
    @FXML
    private JFXButton botonEliminar;
    @FXML
    private JFXButton botonAgregarPaquete;
    @FXML
    private JFXButton botonAgregarArchivo;
    @FXML
    private MenuItem botonConfigurarIP;

    private String direccionIP;

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
        direccionIP = "localhost";
        etiquetaNombreUsuario.setText("");
        root = new TreeItem<>(recurso.getString("etProyectos"));
        configurarIdioma();
        tablaArchivos.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
        cargarProyectos();
        handlerTablaProyectos(tablaProyectos, tabsAbiertos, tablaArchivos, false);

    }

    public void inicializarRegistro() {
        try {
            Registry registry = LocateRegistry.getRegistry(direccionIP);
            stub = (IProgramador) registry.lookup("AdministrarUsuarios");
        } catch (RemoteException | NotBoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void handlerTablaProyectos(TreeTableView<String> tablaProyectos, ArrayList<MyTab> tabsAbiertos, TabPane tablaArchivos,
            boolean esColaborativo) {
        tablaProyectos.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldVal, Object newVal) {
                if ("class clasesApoyo.MyTreeItem".equals(newVal.getClass().toString())) {
                    MyTreeItem treeItem = (MyTreeItem) newVal;
                    if (!buscarArchivosAbiertos(treeItem, tabsAbiertos)) {
                        MyTab tab = new MyTab(treeItem.getArchivo().getNombreArchivo());
                        FormatoCodigo areaCodigo = new FormatoCodigo();
                        areaCodigo.setSampleCode(treeItem.getArchivo().getContenido());
                        tab.setContent(areaCodigo.crearAreaCodigo());
                        if (!esColaborativo) {
                            areaCodigo.getCodeArea().setOnKeyTyped(new EventHandler<KeyEvent>() {
                                @Override
                                public void handle(KeyEvent event) {
                                    treeItem.setModificado(true);
                                }
                            });
                        } else {
                            areaCodigo.getCodeArea().setOnKeyTyped(new EventHandler<KeyEvent>() {
                                @Override
                                public void handle(KeyEvent event) {
                                    treeItem.setModificado(true);

                                    socket.emit("escribirCodigo", areaCodigo.getCodeArea().getText(), treeItem.getArchivo().getRuta() + treeItem.getArchivo().getNombreArchivo());

                                }
                            });
                            socket.emit("abrirTab", crearObjetoJSONArchivo(treeItem.getArchivo()));
                        }

                        tab.setTreeItem(treeItem);
                        tablaArchivos.getTabs().add(tab);
                        tabsAbiertos.add(tab);
                        handlerCerrarProyectoTab(tab, treeItem, tabsAbiertos, tablaArchivos);
                    }
                }

            }
        });
    }

    public JSONObject crearObjetoJSONArchivo(Archivo archivo) {
        JSONObject archivoJSON = new JSONObject();
        archivoJSON.put("nombreArchivo", archivo.getNombreArchivo());
        archivoJSON.put("ruta", archivo.getRuta());
        archivoJSON.put("contenido", archivo.getContenido());
        archivoJSON.put("rutaClases", archivo.getRutaClases());
        archivoJSON.put("paquete", archivo.getPaquete());
        return archivoJSON;
    }

    public boolean buscarArchivosAbiertos(MyTreeItem myTreeItem, ArrayList<MyTab> tabsAbiertos) {
        boolean estaAbierto = false;
        for (MyTab myTab : tabsAbiertos) {
            if ((myTab.getTreeItem().getArchivo().getRuta()
                    + myTab.getTreeItem().getArchivo().getNombreArchivo()).equals(myTreeItem.getArchivo().getRuta()
                    + myTreeItem.getArchivo().getNombreArchivo())) {
                estaAbierto = true;
                break;
            }
        }
        return estaAbierto;
    }

    public void handlerCerrarProyectoTab(MyTab tab, MyTreeItem treeItem, ArrayList<MyTab> tabsAbiertos, TabPane tablaArchivos) {
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
                    tabsAbiertos.remove(tab);
                } else if (result.get() == botonCancelar) {
                    t.consume();
                } else {
                    tabsAbiertos.remove(tab);
                    treeItem.setModificado(false);
                }
            } else {
                tabsAbiertos.remove(tab);
            }
        });

    }

    public void setStagePantallaPrincipal(Stage stagePantallaPrincipal) {
        this.stagePantallaPrincipal = stagePantallaPrincipal;
        this.stagePantallaPrincipal.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (!etiquetaNombreUsuario.getText().isEmpty()) {
                    cerrarSesion(null);
                    stagePantallaPrincipal.close();
                }
            }
        });
    }

    public TabPane getTablaArchivos() {
        return tablaArchivos;
    }

    public ArrayList<MyTab> getTabsAbiertos() {
        return tabsAbiertos;
    }

    public void setRecurso(ResourceBundle recurso) {
        this.recurso = recurso;
        configurarIdioma();
    }

    public void setControlador(PantallaPrincipalController controlador) {
        this.controlador = controlador;
    }

    public ConexionNode getConexionNode() {
        return conexionNode;
    }

    public void setConexionNode(ConexionNode conexionNode) {
        this.conexionNode = conexionNode;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getDireccionIP() {
        return direccionIP;
    }

    public void setDireccionIP(String direccionIP) {
        this.direccionIP = direccionIP;
    }

    public void configurarIdioma() {
        iniciarSesion.setText(recurso.getString("etInicioSesion"));
        cambiarIdioma.setText(recurso.getString("etCambiarIdioma"));
        cerrarSesion.setText(recurso.getString("btCerrarSesion"));
        botonConfigurarIP.setText(recurso.getString("btConfigurarIP"));

    }

    @FXML
    private void botonCrearProyecto(ActionEvent event) throws IOException {
        stagePantallaPrincipal.hide();
        ventanaCrearProyecto(recurso, controlador);
    }

    @FXML
    private void botonIniciarSesion(ActionEvent event) throws IOException {
        stagePantallaPrincipal.hide();
        ventanaInicioSesion(recurso, controlador);
    }

    @FXML
    private void botonCambiarIdioma(ActionEvent event) throws IOException {
        stagePantallaPrincipal.hide();
        ventanaCambiarIdioma(recurso, controlador);
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        inicializarRegistro();
        try {
            stub.cerrarSesion(etiquetaNombreUsuario.getText());
            socket.emit("cerrarSesion", etiquetaNombreUsuario.getText());
            iconoSesionIniciada.setVisible(false);
            etiquetaNombreUsuario.setVisible(false);
            etiquetaNombreUsuario.setText("");
            botonConfigurarIP.setVisible(true);
            cerrarSesion.setVisible(false);
            iniciarSesion.setVisible(true);
        } catch (RemoteException ex) {
            Logger.getLogger(PantallaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sesionIniciada(String nombreUsuario) {
        iconoSesionIniciada.setVisible(true);
        etiquetaNombreUsuario.setText(nombreUsuario);
        etiquetaNombreUsuario.setVisible(true);
        cerrarSesion.setVisible(true);
        botonConfigurarIP.setVisible(false);
        iniciarSesion.setVisible(false);
    }

    public void invitacionRecibida(String lobby, String nombreColaborador, JSONObject proyecto) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(nombreColaborador + " " + recurso.getString("mensajeInvitacion"));
        ButtonType botonAceptar = new ButtonType(recurso.getString("btAceptar"));
        ButtonType botonCancelar = new ButtonType(recurso.getString("btRechazar"), ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(botonAceptar, botonCancelar);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == botonAceptar) {
            socket.emit("conectarseALobby", lobby);
            stagePantallaPrincipal.hide();
            ventanaInvitado(recurso, proyecto, controlador);

        }

    }

    public void invitacionEnviada() {
        mensajeAlert("", recurso.getString("mensajeInvitacionEnviada"));
    }

    public void cargarProyectos() {
        Proyecto proyecto = new Proyecto();
        proyectos = proyecto.cargarProyectos();
        ArrayList<MyTreeItemCarpeta> carpetas;
        ArrayList<MyTreeItemProyecto> proyectosArbol = new ArrayList();
        for (Proyecto proyecto1 : proyectos) {
            ArrayList<String> nombreCarpetas = new ArrayList();
            MyTreeItemProyecto hijo = new MyTreeItemProyecto(proyecto1.getNombreProyecto(),
                    crearIconoLenguaje(proyecto1.getLenguaje()));
            carpetas = agregarCarpetasArbol(proyecto1, nombreCarpetas);
            hijo.getChildren().setAll(carpetas);
            hijo.setNombreProyecto(proyecto1.getNombreProyecto());
            hijo.setLenguaje(proyecto1.getLenguaje());
            hijo.setRuta(proyecto1.getRutaProyecto());
            hijo.setNombreCarpetas(nombreCarpetas);
            proyectosArbol.add(hijo);

        }
        root.getChildren().setAll(proyectosArbol);
        columnaProyectos.setCellValueFactory((CellDataFeatures<String, String> p) -> new ReadOnlyStringWrapper(p.getValue().getValue()));
        tablaProyectos.setRoot(root);
        tablaProyectos.setShowRoot(true);

    }

    public void cargarNuevoProyecto(Proyecto proyecto) {
        MyTreeItemProyecto hijo = new MyTreeItemProyecto(proyecto.getNombreProyecto(), crearIconoLenguaje(proyecto.getLenguaje()));
        ArrayList<String> nombreCarpetas = new ArrayList();
        hijo.getChildren().setAll(agregarCarpetasArbol(proyecto, nombreCarpetas));
        hijo.setNombreProyecto(proyecto.getNombreProyecto());
        hijo.setLenguaje(proyecto.getLenguaje());
        hijo.setRuta(proyecto.getRutaProyecto());
        hijo.setNombreCarpetas(nombreCarpetas);
        root.getChildren().add(hijo);
        proyectos.add(proyecto);
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

    public ArrayList<MyTreeItemCarpeta> agregarCarpetasArbol(Proyecto proyecto, ArrayList<String> nombreCarpetas) {
        ArrayList<Carpeta> carpetasProyecto = proyecto.getCarpetas();
        ArrayList<MyTreeItemCarpeta> carpetas = new ArrayList();
        for (Carpeta carpeta : carpetasProyecto) {
            ArrayList<String> nombreArchivos = new ArrayList();
            MyTreeItemCarpeta hijo = new MyTreeItemCarpeta(carpeta.getNombreCarpeta(), crearIconoCarpeta());
            hijo.getChildren().addAll(agregarArchivosArbol(carpeta, proyecto.getLenguaje(), nombreArchivos, proyecto.getRutaProyecto()));
            hijo.setNombreCarpeta(carpeta.getNombreCarpeta());
            hijo.setRuta(carpeta.getRutaCarpeta());
            hijo.setNombreArchivos(nombreArchivos);
            hijo.setRutaProyecto(proyecto.getRutaProyecto());
            hijo.setLenguaje(proyecto.getLenguaje());
            nombreCarpetas.add(carpeta.getNombreCarpeta());
            carpetas.add(hijo);
        }

        return carpetas;
    }

    public ArrayList<MyTreeItem> agregarArchivosArbol(Carpeta carpeta, String lenguaje, ArrayList<String> nombreArchivos, String rutaProyecto) {
        ArrayList<Archivo> archivos = carpeta.getArchivos();
        ArrayList<MyTreeItem> treeArchivos = new ArrayList();
        for (Archivo archivo : archivos) {
            MyTreeItem hijo = new MyTreeItem(archivo.getNombreArchivo(), crearIconoArchivo(lenguaje));
            hijo.setArchivo(archivo);
            hijo.setRutaProyecto(rutaProyecto);
            hijo.setRutaCarpeta(carpeta.getRutaCarpeta());
            nombreArchivos.add(archivo.getNombreArchivo());
            treeArchivos.add(hijo);

        }
        return treeArchivos;
    }

    @FXML
    private void botonGuardarArchivo(ActionEvent event) {
        guardarArchivo(tablaArchivos);
    }

    public void guardarArchivo(TabPane tablaArchivos) {
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
    private void botonCompilar(ActionEvent event) {
        compilarArchivo(tablaArchivos, false, false);
    }

    public boolean compilarArchivo(TabPane tablaArchivos, boolean esColaborativo, boolean ejecucion) {
        boolean compilo = false;
        if (tablaArchivos.getSelectionModel().getSelectedItem() != null) {
            guardarArchivo(tablaArchivos);
            MyTab tabSeleccionado = (MyTab) tablaArchivos.getSelectionModel().getSelectedItem();
            Archivo archivo = tabSeleccionado.getTreeItem().getArchivo();
            String rutaProyecto = tabSeleccionado.getTreeItem().getRutaProyecto();
            String resultado = archivo.compilarArchivo(archivo, rutaProyecto);
            if (esColaborativo) {
                compilo = compilarColaborativo(resultado, ejecucion);
            } else {
                compilo = compilarLocal(resultado, ejecucion);
            }

        }
        return compilo;
    }

    public boolean compilarLocal(String resultado, boolean ejecucion) {
        boolean compilo = false;
        if (resultado.isEmpty()) {
            compilo = true;
            if (!ejecucion) {
                mensajeAlert(recurso.getString("felicidades"), recurso.getString("mensajeCompilacionExitosa"));
            }
        } else {
            resultadoCompilacion(resultado, recurso);
        }
        return compilo;
    }

    public boolean compilarColaborativo(String resultado, boolean ejecucion) {
        boolean compilo = false;
        if (resultado.isEmpty()) {
            compilo = true;
            if (!ejecucion) {
                socket.emit("mensajeCompilacionExitosa");
                mensajeAlert(recurso.getString("felicidades"), recurso.getString("mensajeCompilacionExitosa"));
            }
        } else {
            socket.emit("mensajeErrorCompilacion", resultado);
            resultadoCompilacion(resultado, recurso);
        }
        return compilo;
    }

    @FXML
    private void botonEjecutar(ActionEvent event) {
        ejecutarArchivo(tablaArchivos, false);
    }

    public void ejecutarArchivo(TabPane tablaArchivos, boolean esColaborativo) {
        if (tablaArchivos.getSelectionModel().getSelectedItem() != null) {
            if (esColaborativo) {
                ejecutarColaborativo(tablaArchivos);
            } else {
                ejecutarLocal(tablaArchivos);
            }

        }
    }

    public void ejecutarLocal(TabPane tablaArchivos) {
        if (compilarArchivo(tablaArchivos, false, true)) {
            MyTab tabSeleccionado = (MyTab) tablaArchivos.getSelectionModel().getSelectedItem();
            ventanaEjecutar(recurso, tabSeleccionado.getTreeItem().getArchivo(), controlador, false);
        }
    }

    public void ejecutarColaborativo(TabPane tablaArchivos) {
        if (compilarArchivo(tablaArchivos, true, true)) {
            MyTab tabSeleccionado = (MyTab) tablaArchivos.getSelectionModel().getSelectedItem();
            ventanaEjecutar(recurso, tabSeleccionado.getTreeItem().getArchivo(), controlador, true);
        }
    }

    @FXML
    private void invitarColaborador(ActionEvent event) {
        if (!etiquetaNombreUsuario.getText().isEmpty()) {
            stagePantallaPrincipal.hide();
            ventanaInvitarColaborador(recurso, socket, controlador);
        } else {
            mensajeAlert(recurso.getString("atencion"), recurso.getString("mensajeDebesIniciarSesion"));
        }
    }

    public void hacerVisiblePantallaprincipal() {
        stagePantallaPrincipal.show();
    }

    public void hacerInVisiblePantallaprincipal() {
        stagePantallaPrincipal.hide();
    }

    @FXML
    private void eliminar(ActionEvent event) {
        eliminarElementoArbol(tabsAbiertos, tablaProyectos, tablaArchivos);
    }

    public void eliminarElementoArbol(ArrayList<MyTab> tabsAbiertos, TreeTableView<String> tablaProyectos, TabPane tablaArchivos) {
        MyTreeItemCarpeta myTreeItemCarpeta;
        MyTreeItemProyecto myTreeItemProyecto;
        if (tablaProyectos.getSelectionModel().getSelectedItem() != null) {
            switch (tablaProyectos.getSelectionModel().getSelectedItem().getClass().toString()) {
                case "class clasesApoyo.MyTreeItemProyecto":
                    myTreeItemProyecto = (MyTreeItemProyecto) tablaProyectos.getSelectionModel().getSelectedItem();
                    Proyecto proyecto = new Proyecto();
                    proyecto.setNombreProyecto(myTreeItemProyecto.getNombreProyecto());
                    proyecto.setRutaProyecto(myTreeItemProyecto.getRuta());
                    proyecto.setLenguaje(myTreeItemProyecto.getLenguaje());
                    proyecto.eliminarProyecto(myTreeItemProyecto.getRuta());
                    proyecto.eliminarRutaDeProyecto(proyecto);
                    removerTabsAbiertosProyectoEliminado(myTreeItemProyecto, tabsAbiertos, tablaArchivos);
                    myTreeItemProyecto.getParent().getChildren().remove(myTreeItemProyecto);
                    break;
                case "class clasesApoyo.MyTreeItemCarpeta":
                    myTreeItemCarpeta = (MyTreeItemCarpeta) tablaProyectos.getSelectionModel().getSelectedItem();
                    if (!myTreeItemCarpeta.getLenguaje().equals("c++")) {
                        Carpeta carpeta = new Carpeta();
                        carpeta.eliminarCarpeta(myTreeItemCarpeta.getRuta());
                        removerTabsAbiertosCarpetaEliminada(myTreeItemCarpeta.getRuta(), tabsAbiertos, tablaArchivos);
                        myTreeItemProyecto = (MyTreeItemProyecto) myTreeItemCarpeta.getParent();
                        myTreeItemProyecto.getNombreCarpetas().remove(myTreeItemCarpeta.getNombreCarpeta());
                        myTreeItemCarpeta.getParent().getChildren().remove(myTreeItemCarpeta);
                    }
                    break;
                case "class clasesApoyo.MyTreeItem":
                    MyTreeItem myTreeItem = (MyTreeItem) tablaProyectos.getSelectionModel().getSelectedItem();
                    myTreeItem.getArchivo().eliminarArchivo(myTreeItem.getArchivo());
                    removerTabAbiertoArchivoEliminado(myTreeItem.getArchivo().getRuta() + myTreeItem.getArchivo().getNombreArchivo(), tabsAbiertos, tablaArchivos);
                    myTreeItemCarpeta = (MyTreeItemCarpeta) myTreeItem.getParent();
                    myTreeItemCarpeta.getNombreArchivos().remove(myTreeItem.getArchivo().getNombreArchivo());
                    myTreeItem.getParent().getChildren().remove(myTreeItem);
                    break;
            }
        }
    }

    public void removerTabsAbiertosProyectoEliminado(MyTreeItemProyecto myTreeItemProyecto, ArrayList<MyTab> tabsAbiertos, TabPane tablaArchivos) {
        ArrayList<MyTab> tabsAbiertosAux = crearAuxiliarTabsAbiertos(tabsAbiertos);
        for (MyTab myTab : tabsAbiertosAux) {
            if (myTab.getTreeItem().getRutaProyecto().equals(myTreeItemProyecto.getRuta())) {
                tablaArchivos.getTabs().remove(myTab);
                tabsAbiertos.remove(myTab);
            }
        }
    }

    public void removerTabsAbiertosCarpetaEliminada(String rutaCarpeta, ArrayList<MyTab> tabsAbiertos, TabPane tablaArchivos) {
        ArrayList<MyTab> tabsAbiertosAux = crearAuxiliarTabsAbiertos(tabsAbiertos);
        for (MyTab myTab : tabsAbiertosAux) {
            if (myTab.getTreeItem().getRutaCarpeta().equals(rutaCarpeta)) {
                tablaArchivos.getTabs().remove(myTab);
                tabsAbiertos.remove(myTab);
            }
        }
    }

    public ArrayList<MyTab> crearAuxiliarTabsAbiertos(ArrayList<MyTab> tabsAbiertos) {
        ArrayList<MyTab> tabsAbiertosAux = new ArrayList();
        for (MyTab myTab : tabsAbiertos) {
            tabsAbiertosAux.add(myTab);
        }
        return tabsAbiertosAux;
    }

    public void removerTabAbiertoArchivoEliminado(String rutaArchivo, ArrayList<MyTab> tabsAbiertos, TabPane tablaArchivos) {
        for (MyTab myTab : tabsAbiertos) {
            if ((myTab.getTreeItem().getArchivo().getRuta() + myTab.getTreeItem().getArchivo().getNombreArchivo()).equals(rutaArchivo)) {
                tablaArchivos.getTabs().remove(myTab);
                tabsAbiertos.remove(myTab);
                break;
            }
        }
    }

    @FXML
    private void agregarPaquete(ActionEvent event) {
        agregarPaqueteArbol(tablaProyectos, recurso);
    }

    public MyTreeItemCarpeta agregarPaqueteArbol(TreeTableView<String> tablaProyectos, ResourceBundle recurso) {
        MyTreeItemCarpeta treeItemCarpeta = null;
        if (tablaProyectos.getSelectionModel().getSelectedItem() != null
                && tablaProyectos.getSelectionModel().getSelectedItem().getClass().toString().equals("class clasesApoyo.MyTreeItemProyecto")) {
            MyTreeItemProyecto treeItem = (MyTreeItemProyecto) tablaProyectos.getSelectionModel().getSelectedItem();
            if (treeItem.getLenguaje().equals("java")) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("");
                dialog.setHeaderText(recurso.getString("mensajeCrearNuevoPaquete"));
                dialog.setContentText(recurso.getString("etNombrePaquete"));
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    if (treeItem.getNombreCarpetas().contains(result.get())) {
                        mensajeAlert(recurso.getString("atencion"), recurso.getString("mensajePaqueteExistente"));
                    } else {

                        Carpeta carpeta = new Carpeta();
                        carpeta.setNombreCarpeta(result.get());
                        ArchivoConfiguracion archivoConfig = new ArchivoConfiguracion();
                        carpeta.setRutaCarpeta(treeItem.getRuta() + archivoConfig.getNombreCarpetaCodigos() + result.get());
                        if (carpeta.crearCarpeta(carpeta)) {
                            treeItemCarpeta = new MyTreeItemCarpeta(result.get(), crearIconoCarpeta());
                            treeItemCarpeta.setNombreCarpeta(carpeta.getNombreCarpeta());
                            treeItemCarpeta.setRuta(carpeta.getRutaCarpeta());
                            treeItemCarpeta.setNombreArchivos(new ArrayList());
                            treeItemCarpeta.setRutaProyecto(treeItem.getRuta());
                            treeItemCarpeta.setLenguaje(treeItem.getLenguaje());
                            treeItem.getNombreCarpetas().add(result.get());
                            treeItem.getChildren().add(treeItemCarpeta);
                        }

                    }
                }
            }
        }
        return treeItemCarpeta;
    }

    @FXML
    private void agregarArchivo(ActionEvent event) {
        agregarArchvioArbol(tablaProyectos, recurso);
    }

    public MyTreeItem agregarArchvioArbol(TreeTableView<String> tablaProyectos, ResourceBundle recurso) {
        MyTreeItem treeItemArchivo = null;
        if (tablaProyectos.getSelectionModel().getSelectedItem() != null
                && tablaProyectos.getSelectionModel().getSelectedItem().getClass().toString().equals("class clasesApoyo.MyTreeItemCarpeta")) {
            MyTreeItemCarpeta treeItem = (MyTreeItemCarpeta) tablaProyectos.getSelectionModel().getSelectedItem();
            TextInputDialog dialog = new TextInputDialog();
            dialog.getEditor().setOnKeyTyped(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    String digito = event.getCharacter();
                    if (digito.equals(".")) {
                        event.consume();
                    }
                }
            });
            dialog.setTitle("");
            dialog.setHeaderText(recurso.getString("mensajeCrearNuevoArchivo"));
            dialog.setContentText(recurso.getString("etNombreArchivo"));
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                if (treeItem.getNombreArchivos().contains(result.get())) {
                    mensajeAlert(recurso.getString("atencion"), recurso.getString("mensajeArchivoExistente"));
                } else {
                    String nombreArchivo = result.get();
                    if (treeItem.getLenguaje().equals("java")) {
                        nombreArchivo += ".java";
                    } else {
                        nombreArchivo += ".cpp";
                    }
                    Archivo archivo = new Archivo();
                    archivo.setNombreArchivo(nombreArchivo);
                    ArchivoConfiguracion archivoConfig = new ArchivoConfiguracion();
                    archivo.setRutaClases(treeItem.getRutaProyecto() + archivoConfig.getNombreCarpetaClases());
                    archivo.setRuta(treeItem.getRuta());
                    archivo.setContenido("");
                    archivo.setPaquete(treeItem.getNombreCarpeta());

                    if (archivo.crearArchivo(archivo)) {
                        treeItemArchivo = new MyTreeItem(nombreArchivo, crearIconoArchivo(treeItem.getLenguaje()));
                        treeItemArchivo.setArchivo(archivo);
                        treeItemArchivo.setModificado(false);
                        treeItemArchivo.setRutaCarpeta(treeItem.getRuta());
                        treeItemArchivo.setRutaProyecto(treeItem.getRutaProyecto());
                        treeItem.getChildren().add(treeItemArchivo);
                    }
                }

            }
        }
        return treeItemArchivo;
    }

    @FXML
    private void configurarIP(ActionEvent event) {
        stagePantallaPrincipal.hide();
        ventanaDireccionIP(recurso, controlador);
    }

}
