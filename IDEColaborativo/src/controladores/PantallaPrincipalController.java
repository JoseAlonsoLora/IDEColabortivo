/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import clasesApoyo.ArchivoConfiguracion;
import static clasesApoyo.GraficosTreeItem.crearIconoArchivo;
import static clasesApoyo.GraficosTreeItem.crearIconoCarpeta;
import static clasesApoyo.GraficosTreeItem.crearIconoLenguaje;
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
 * @author Alonso Lora
 * @author Raymundo Pérez
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
     * @param url
     * @param rb
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

    /**
     * Regresa TabPane donde se encuentran los archivos abiertos
     *
     * @return TabPane con los arhivos abiertos
     */
    public TabPane getTablaArchivos() {
        return tablaArchivos;
    }

    /**
     * Regresa una lista de archivos abiertos
     *
     * @return Lista de archivos
     */
    public ArrayList<MyTab> getTabsAbiertos() {
        return tabsAbiertos;
    }

    /**
     * Da valor al recurso estableciendo el idioma que sera usado en todo el
     * sistema
     *
     * @param recurso Idioma (Español e Inglés)
     */
    public void setRecurso(ResourceBundle recurso) {
        this.recurso = recurso;
        configurarIdioma();
    }

    /**
     * Da valor al controlador de la instancia actual
     *
     * @param controlador Instancia del controlador
     */
    public void setControlador(PantallaPrincipalController controlador) {
        this.controlador = controlador;
    }

    /**
     * Regresa la conexión con el servidor de node
     *
     * @return Conexión con el servidor de node
     */
    public ConexionNode getConexionNode() {
        return conexionNode;
    }

    /**
     * Da valor a la conexión con el servidor de node
     *
     * @param conexionNode Conexión con el servidor de node
     */
    public void setConexionNode(ConexionNode conexionNode) {
        this.conexionNode = conexionNode;
    }

    /**
     * Da valor al socket para la comunicación en red
     *
     * @param socket Socket
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * Regresa el valor del socket
     *
     * @return Valor del socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Regresa el valor de la IP del servidor
     *
     * @return IP del servidor
     */
    public String getDireccionIP() {
        return direccionIP;
    }

    /**
     * Da valor a la dirección IP del servidor
     *
     * @param direccionIP IP del servidor
     */
    public void setDireccionIP(String direccionIP) {
        this.direccionIP = direccionIP;
    }

    /**
     * Da valor al Stage de la pantalla principal
     *
     * @param stagePantallaPrincipal Instancia del Stage de la pantalla
     * principal
     */
    public void setStagePantallaPrincipal(Stage stagePantallaPrincipal) {
        this.stagePantallaPrincipal = stagePantallaPrincipal;
        this.stagePantallaPrincipal.setOnCloseRequest((WindowEvent event) -> {
            if (!etiquetaNombreUsuario.getText().isEmpty()) {
                cerrarSesion(null);
                stagePantallaPrincipal.close();
            }
        });
    }

    /**
     * Se conecta con el servidor RMI
     */
    public void inicializarRegistro() {
        try {
            Registry registry = LocateRegistry.getRegistry(direccionIP);
            stub = (IProgramador) registry.lookup("AdministrarUsuarios");
        } catch (RemoteException | NotBoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Evento del TreeView para abrir Tabs y porder escribir código
     *
     * @param tablaProyectos Árbol donde se encuentran los proyectos
     * @param tabsAbiertos Tabs abiertos en el TabPane
     * @param tablaArchivos TabPane para agregar el Tab
     * @param esColaborativo Inidica si el proyecto es de manera colaborativa
     */
    public void handlerTablaProyectos(TreeTableView<String> tablaProyectos, ArrayList<MyTab> tabsAbiertos, TabPane tablaArchivos,
            boolean esColaborativo) {
        tablaProyectos.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldVal, Object newVal) {
                if (newVal != null) {
                    if ("class clasesApoyo.MyTreeItem".equals(newVal.getClass().toString())) {
                        MyTreeItem treeItem = (MyTreeItem) newVal;
                        if (!buscarArchivosAbiertos(treeItem, tabsAbiertos)) {
                            MyTab tab = new MyTab(treeItem.getArchivo().getNombreArchivo());
                            FormatoCodigo areaCodigo = new FormatoCodigo();
                            areaCodigo.setSampleCode(treeItem.getArchivo().getContenido());
                            tab.setContent(areaCodigo.crearAreaCodigo());
                            if (!esColaborativo) {
                                areaCodigo.getCodeArea().setOnKeyTyped((KeyEvent event) -> {
                                    treeItem.setModificado(true);
                                });
                            } else {
                                areaCodigo.getCodeArea().setOnKeyTyped((KeyEvent event) -> {
                                    treeItem.setModificado(true);
                                    
                                    socket.emit("escribirCodigo", areaCodigo.getCodeArea().getText(), treeItem.getArchivo().getRuta() + treeItem.getArchivo().getNombreArchivo());
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
            }
        });
    }

    /**
     * Transforma un archivo a un objeto JSON para enviarlo de manera remota
     *
     * @param archivo Archivo que será transformado
     * @return Archivo transformado a JSON
     */
    public JSONObject crearObjetoJSONArchivo(Archivo archivo) {
        JSONObject archivoJSON = new JSONObject();
        archivoJSON.put("nombreArchivo", archivo.getNombreArchivo());
        archivoJSON.put("ruta", archivo.getRuta());
        archivoJSON.put("contenido", archivo.getContenido());
        archivoJSON.put("rutaClases", archivo.getRutaClases());
        archivoJSON.put("paquete", archivo.getPaquete());
        return archivoJSON;
    }

    /**
     * Busca en el TabPane archivos abiertos
     *
     * @param myTreeItem Archivo en el árbol de proyectos
     * @param tabsAbiertos Tabs abiertos en el TabPane
     * @return Indica si el archivo esta abierto
     */
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

    /**
     * Evento para cerrar los Tabs
     *
     * @param tab Tabs que será cerrado
     * @param treeItem Archivo en el árbol de proyectos
     * @param tabsAbiertos Tabs abiertos en el TabPane
     * @param tablaArchivos TabPane donde se cerrara el Tab
     */
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

    /**
     * Configura el idioma de todas etiquetas de la pantalla
     */
    public void configurarIdioma() {
        iniciarSesion.setText(recurso.getString("etInicioSesion"));
        cambiarIdioma.setText(recurso.getString("etCambiarIdioma"));
        cerrarSesion.setText(recurso.getString("btCerrarSesion"));
        botonConfigurarIP.setText(recurso.getString("btConfigurarIP"));

    }

    /**
     * Despliega la ventana para crear un proyeto
     *
     * @param event Clic del usuario
     */
    @FXML
    private void botonCrearProyecto(ActionEvent event) {
        stagePantallaPrincipal.hide();
        ventanaCrearProyecto(recurso, controlador);
    }

    /**
     * Despliega la ventana para iniciar sesión
     *
     * @param event Clic del usuario
     */
    @FXML
    private void botonIniciarSesion(ActionEvent event) {
        stagePantallaPrincipal.hide();
        ventanaInicioSesion(recurso, controlador);
    }

    /**
     * Despliega la pantalla para cambiar idioma
     *
     * @param event Clic del usuario
     */
    @FXML
    private void botonCambiarIdioma(ActionEvent event) {
        stagePantallaPrincipal.hide();
        ventanaCambiarIdioma(recurso, controlador);
    }

    /**
     * Cierra la sesión del usuario
     *
     * @param event Clic del usuario
     */
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

    /**
     * Muestra el nombre de usuario en la pantalla principal
     *
     * @param nombreUsuario Nombre de usuario
     */
    public void sesionIniciada(String nombreUsuario) {
        iconoSesionIniciada.setVisible(true);
        etiquetaNombreUsuario.setText(nombreUsuario);
        etiquetaNombreUsuario.setVisible(true);
        cerrarSesion.setVisible(true);
        botonConfigurarIP.setVisible(false);
        iniciarSesion.setVisible(false);
    }

    /**
     * Despliega un pantalla donde indica qué usuario te invito a colaborar
     *
     * @param lobby Lobby del usuario que te invito
     * @param nombreColaborador Nombre del colaborador que te invito
     * @param proyecto Proyecto al cual te han invitado
     */
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

    /**
     * Mensaje donde indica que la invitación fue enviada
     */
    public void invitacionEnviada() {
        mensajeAlert("", recurso.getString("mensajeInvitacionEnviada"));
    }

    /**
     * Carga los proyectos creados por el usario
     */
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

    /**
     * Carga el nuevo proyecto
     *
     * @param proyecto Proyecto creado
     */
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

    /**
     * Agrega las carpetas pertenecientes a un proyecto al árbol de proyectos
     *
     * @param proyecto Proyecto al cual se le agregarán las carpetas
     * @param nombreCarpetas Nombre de las carpetas
     * @return Lista de MyTreeItemCarpeta que será agregada al árbol proyectos
     */
    public ArrayList<MyTreeItemCarpeta> agregarCarpetasArbol(Proyecto proyecto, ArrayList<String> nombreCarpetas) {
        ArrayList<Carpeta> carpetasProyecto = proyecto.getCarpetas();
        ArrayList<MyTreeItemCarpeta> carpetas = new ArrayList();
        carpetasProyecto.forEach((carpeta) -> {
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
        });

        return carpetas;
    }

    /**
     * Agrega los archivos pertenecientes a un proyecto al árbol de proyectos
     *
     * @param carpeta Carpeta al cual se le agregarán los archivos
     * @param lenguaje Lenguaje de progamación
     * @param nombreArchivos Nombre del archivo
     * @param rutaProyecto Ruta específica donde se encuentra el proyecto
     * @return Lista de MyTreeItem que será agregada al árbol proyectos
     */
    public ArrayList<MyTreeItem> agregarArchivosArbol(Carpeta carpeta, String lenguaje, ArrayList<String> nombreArchivos, String rutaProyecto) {
        ArrayList<Archivo> archivos = carpeta.getArchivos();
        ArrayList<MyTreeItem> treeArchivos = new ArrayList();
        archivos.forEach((archivo) -> {
            MyTreeItem hijo = new MyTreeItem(archivo.getNombreArchivo(), crearIconoArchivo(lenguaje));
            hijo.setArchivo(archivo);
            hijo.setRutaProyecto(rutaProyecto);
            hijo.setRutaCarpeta(carpeta.getRutaCarpeta());
            nombreArchivos.add(archivo.getNombreArchivo());
            treeArchivos.add(hijo);
        });
        return treeArchivos;
    }

    /**
     * Guarda el archivo seleccionado
     *
     * @param event Clic del usuario
     */
    @FXML
    private void botonGuardarArchivo(ActionEvent event) {
        guardarArchivo(tablaArchivos);
    }

    /**
     * Guarda el archivo que el usuario selecciona desde el TabPane
     *
     * @param tablaArchivos Tab seleccionado
     */
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

    /**
     * Compila el archivo seleccionado
     *
     * @param event Clic del usuario
     */
    @FXML
    private void botonCompilar(ActionEvent event) {
        compilarArchivo(tablaArchivos, false, false);
    }

    /**
     * Complia el archivo seleccionado por el usuario y muestra su resultado
     *
     * @param tablaArchivos TabPane donde estan los archivos abiertos
     * @param esColaborativo Indica si la compilación es colaborativa
     * @param ejecucion Indica si necesita mostrar el mensaje de compilación
     * exitosa
     * @return
     */
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

    /**
     * Compila el archivo de manera local
     *
     * @param resultado Resultado de la compilación
     * @param ejecucion Indica si tiene que ser ejecutado
     * @return Indica si el programa tiene errores
     */
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

    /**
     * Complila de manera colaborativa, enviando el resultado a todos los
     * integrantes
     *
     * @param resultado Resultado de la compilación
     * @param ejecucion Indica si tiene que ser ejecutado
     * @return Indica si el programa tiene errores
     */
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

    /**
     * Ejecuta un archivo seleccionado
     * @param event Clic del usuario
     */
    @FXML
    private void botonEjecutar(ActionEvent event) {
        ejecutarArchivo(tablaArchivos, false);
    }

    /**
     * Ejecuta un archivo seleccionado por el usuario
     * @param tablaArchivos TabPane donde estan los archivos abiertos
     * @param esColaborativo Indica si el archivo será ejecutado 
     */
    public void ejecutarArchivo(TabPane tablaArchivos, boolean esColaborativo) {
        if (tablaArchivos.getSelectionModel().getSelectedItem() != null) {
            if (esColaborativo) {
                ejecutarColaborativo(tablaArchivos);
            } else {
                ejecutarLocal(tablaArchivos);
            }

        }
    }
    
    /**
     * Despliega la pantalla para ejecutar el programa
     * @param tablaArchivos TabPane donde estan los archivos abiertos
     */
    public void ejecutarLocal(TabPane tablaArchivos) {
        if (compilarArchivo(tablaArchivos, false, true)) {
            MyTab tabSeleccionado = (MyTab) tablaArchivos.getSelectionModel().getSelectedItem();
            ventanaEjecutar(recurso, tabSeleccionado.getTreeItem().getArchivo(), controlador, false);
        }
    }
    
    /**
     * Despliega la pantalla para ejecutar de manera colaborativa el programa
     * @param tablaArchivos TabPane donde estan los archivos abiertos
     */
    public void ejecutarColaborativo(TabPane tablaArchivos) {
        if (compilarArchivo(tablaArchivos, true, true)) {
            MyTab tabSeleccionado = (MyTab) tablaArchivos.getSelectionModel().getSelectedItem();
            ventanaEjecutar(recurso, tabSeleccionado.getTreeItem().getArchivo(), controlador, true);
        }
    }
    
    /**
     * Despliega la pantalla para invitar a un colaborador
     * @param event Clic del usuario
     */
    @FXML
    private void invitarColaborador(ActionEvent event) {
        if (!etiquetaNombreUsuario.getText().isEmpty()) {
            stagePantallaPrincipal.hide();
            ventanaInvitarColaborador(recurso, socket, controlador);
        } else {
            mensajeAlert(recurso.getString("atencion"), recurso.getString("mensajeDebesIniciarSesion"));
        }
    }

    /**
     * Hace visible la pantalla principal
     */
    public void hacerVisiblePantallaprincipal() {
        stagePantallaPrincipal.show();
    }

    /**
     * Oculta la pantalla principal
     */
    public void hacerInVisiblePantallaprincipal() {
        stagePantallaPrincipal.hide();
    }

    /**
     * Eliminar un proyecto,carpeta,archivo
     * @param event Clic del usuario
     */
    @FXML
    private void eliminar(ActionEvent event) {
        eliminarElementoArbol(tabsAbiertos, tablaProyectos, tablaArchivos);
    }

    /**
     * Elimina el elemnto seleccionado del árbol de proyectos
     * @param tabsAbiertos Tabs abiertos en el TabPane
     * @param tablaProyectos Árbol donde se encuentran los proyectos
     * @param tablaArchivos TabPane para cerrar el Tab
     */
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
                default:
                    break;
            }
        }
    }

    /**
     * Remueve los Tabs abiertos cuando se elimina un proyecto
     * @param myTreeItemProyecto Proyecto que será eliminado
     * @param tabsAbiertos Tabs abiertos en el TabPane
     * @param tablaArchivos TabPane para cerrar el Tab
     */
    public void removerTabsAbiertosProyectoEliminado(MyTreeItemProyecto myTreeItemProyecto, ArrayList<MyTab> tabsAbiertos, TabPane tablaArchivos) {
        ArrayList<MyTab> tabsAbiertosAux = crearAuxiliarTabsAbiertos(tabsAbiertos);
        tabsAbiertosAux.stream().filter((myTab) -> (myTab.getTreeItem().getRutaProyecto().equals(myTreeItemProyecto.getRuta()))).map((myTab) -> {
            tablaArchivos.getTabs().remove(myTab);
            return myTab;
        }).forEachOrdered((myTab) -> {
            tabsAbiertos.remove(myTab);
        });
    }
    
    /**
     * Remueve los Tabs abiertos cuando se elimina una carpeta
     * @param rutaCarpeta Ruta de la carpeta que será eliminada
     * @param tabsAbiertos Tabs abiertos en el TabPane
     * @param tablaArchivos TabPane para cerrar el Tab
     */
    public void removerTabsAbiertosCarpetaEliminada(String rutaCarpeta, ArrayList<MyTab> tabsAbiertos, TabPane tablaArchivos) {
        ArrayList<MyTab> tabsAbiertosAux = crearAuxiliarTabsAbiertos(tabsAbiertos);
        tabsAbiertosAux.stream().filter((myTab) -> (myTab.getTreeItem().getRutaCarpeta().equals(rutaCarpeta))).map((myTab) -> {
            tablaArchivos.getTabs().remove(myTab);
            return myTab;
        }).forEachOrdered((myTab) -> {
            tabsAbiertos.remove(myTab);
        });
    }

    /**
     * Crea un arreglo auxiliar para remover Tabs abiertos 
     * @param tabsAbiertos Tabs abiertos en el TabPane
     * @return Arreglo auxiliar con los tabs abiertos
     */
    public ArrayList<MyTab> crearAuxiliarTabsAbiertos(ArrayList<MyTab> tabsAbiertos) {
        ArrayList<MyTab> tabsAbiertosAux = new ArrayList();
        tabsAbiertos.forEach((myTab) -> {
            tabsAbiertosAux.add(myTab);
        });
        return tabsAbiertosAux;
    }

    /**
     * Remueve Tabs de archivos 
     * @param rutaArchivo Ruta del archivo que será eliminado
     * @param tabsAbiertos Tabs abiertos en el TabPane
     * @param tablaArchivos TabTane donde se cerrará el Tab
     */
    public void removerTabAbiertoArchivoEliminado(String rutaArchivo, ArrayList<MyTab> tabsAbiertos, TabPane tablaArchivos) {
        for (MyTab myTab : tabsAbiertos) {
            if ((myTab.getTreeItem().getArchivo().getRuta() + myTab.getTreeItem().getArchivo().getNombreArchivo()).equals(rutaArchivo)) {
                tablaArchivos.getTabs().remove(myTab);
                tabsAbiertos.remove(myTab);
                break;
            }
        }
    }

    /**
     * Agrega un paquete a un proyecto seleccionado
     * @param event Clic del usuario
     */
    @FXML
    private void agregarPaquete(ActionEvent event) {
        agregarPaqueteArbol(tablaProyectos, recurso);
    }

    /**
     * Agrega al árbol de proyetos una carpeta
     * @param tablaProyectos Árbol donde se encuentran los proyectos
     * @param recurso Idioma
     * @return MyTreeItemCarpeta para agregarla al de árbol proyectos
     */
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

    /**
     * Agrega el archivo al árbol de proyectos
     * @param event Clic del usuario
     */
    @FXML
    private void agregarArchivo(ActionEvent event) {
        agregarArchvioArbol(tablaProyectos, recurso);
    }

    /**
     * Agrega al árbol de proyetos un archivo
     * @param tablaProyectos Árbol donde se encuentran los proyectos
     * @param recurso Idioma
     * @return MyTreeItem para agregarlo al árbol de proyectos
     */
    public MyTreeItem agregarArchvioArbol(TreeTableView<String> tablaProyectos, ResourceBundle recurso) {
        MyTreeItem treeItemArchivo = null;
        if (tablaProyectos.getSelectionModel().getSelectedItem() != null
                && tablaProyectos.getSelectionModel().getSelectedItem().getClass().toString().equals("class clasesApoyo.MyTreeItemCarpeta")) {
            MyTreeItemCarpeta treeItem = (MyTreeItemCarpeta) tablaProyectos.getSelectionModel().getSelectedItem();
            TextInputDialog dialog = new TextInputDialog();
            dialog.getEditor().setOnKeyTyped((KeyEvent event) -> {
                String digito = event.getCharacter();
                if (digito.equals(".")) {
                    event.consume();
                }
            });
            dialog.setTitle("");
            dialog.setHeaderText(recurso.getString("mensajeCrearNuevoArchivo"));
            dialog.setContentText(recurso.getString("etNombreArchivo"));
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String nombreArchivo = result.get();
                if (nombreArchivo.contains(".") || nombreArchivo.isEmpty()) {
                    mensajeAlert(recurso.getString("atencion"), recurso.getString("mensajeDatosInvalidos"));
                } else {
                    if (treeItem.getLenguaje().equals("java")) {
                        nombreArchivo += ".java";
                    } else {
                        nombreArchivo += ".cpp";
                    }
                    if (treeItem.getNombreArchivos().contains(nombreArchivo)) {
                        mensajeAlert(recurso.getString("atencion"), recurso.getString("mensajeArchivoExistente"));
                    } else {
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
                            treeItem.getNombreArchivos().add(nombreArchivo);
                        }
                    }
                }

            }
        }
        return treeItemArchivo;
    }

    /**
     * Desplegar la pantalla para configurar la IP del servidor
     * @param event Clic del usuario
     */
    @FXML
    private void configurarIP(ActionEvent event) {
        stagePantallaPrincipal.hide();
        ventanaDireccionIP(recurso, controlador);
    }

}
