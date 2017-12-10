/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import clasesApoyo.MyTab;
import clasesApoyo.MyTreeItem;
import clasesApoyo.MyTreeItemCarpeta;
import clasesApoyo.MyTreeItemProyecto;
import com.jfoenix.controls.JFXButton;
import componentes.FormatoCodigo;
import static controladores.PantallaHostController.obtenerNombreCarpetas;
import static idecolaborativo.IDEColaborativo.mensajeAlert;
import static idecolaborativo.IDEColaborativo.resultadoCompilacion;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
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
public class PantallaInvitadoController implements Initializable {

    @FXML
    private TabPane tablaArchivos;
    @FXML
    private JFXButton botonCancelarColaboracion;

    private JSONObject proyecto;

    private static PantallaPrincipalController controlador;
    private static Stage stagePantallaInvitado;
    @FXML
    private TreeTableView<String> tablaProyecto;
    @FXML
    private TreeTableColumn<String, String> columnaProyecto;
    private TreeItem<String> root;
    private static ArrayList<MyTab> tabsAbiertosInvitado;
    private static ResourceBundle recurso;
    private static final String mensajeAtencion = "atencion";

    /**
     * Initializes the controller class.
     */
    public void setProyecto(JSONObject proyecto) {
        this.proyecto = proyecto;
    }

    public void setControlador(PantallaPrincipalController controlador) {
        this.controlador = controlador;
        controlador.getConexionNode().setControladorInvitado(this);
    }

    public void setStagePantallaInvitado(Stage stagePantallaInvitado) {
        this.stagePantallaInvitado = stagePantallaInvitado;
        this.stagePantallaInvitado.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                controlador.hacerVisiblePantallaprincipal();
                controlador.getSocket().emit("terminarSesionInvitado");
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        recurso = rb;
        root = new TreeItem<>(recurso.getString("etProyectos"));
        tabsAbiertosInvitado = new ArrayList();
    }

    @FXML
    private void cancelarColaboracion(ActionEvent event) {
        controlador.hacerVisiblePantallaprincipal();
        controlador.getSocket().emit("terminarSesionInvitado");
        stagePantallaInvitado.close();
    }

    public void cargarProyecto() {
        Proyecto proyecto = transformarJSON();
        MyTreeItemProyecto hijo = new MyTreeItemProyecto(proyecto.getNombreProyecto(), controlador.crearIconoLenguaje(proyecto.getLenguaje()));
        hijo.getChildren().setAll(controlador.agregarCarpetasArbol(proyecto, obtenerNombreCarpetas(proyecto)));
        root.getChildren().add(hijo);
        hijo.setLenguaje(proyecto.getLenguaje());
        hijo.setNombreCarpetas(obtenerNombreCarpetas(proyecto));
        hijo.setNombreProyecto(proyecto.getNombreProyecto());
        hijo.setRuta(proyecto.getRutaProyecto());
        columnaProyecto.setCellValueFactory((TreeTableColumn.CellDataFeatures<String, String> p) -> new ReadOnlyStringWrapper(p.getValue().getValue()));
        tablaProyecto.setRoot(root);
        tablaProyecto.setShowRoot(true);
        controlador.handlerTablaProyectos(tablaProyecto, tabsAbiertosInvitado, tablaArchivos, true);
    }

    public Proyecto transformarJSON() {
        Proyecto proyectoNegocio = new Proyecto();
        proyectoNegocio.setNombreProyecto(proyecto.getString("nombreProyecto"));
        proyectoNegocio.setLenguaje(proyecto.getString("lenguaje"));
        ArrayList<Carpeta> carpetas = new ArrayList();
        for (int i = 0; i < proyecto.getJSONArray("carpetas").length(); i++) {
            Carpeta carpetaNegocio = new Carpeta();
            JSONObject carpeta = proyecto.getJSONArray("carpetas").getJSONObject(i);
            carpetaNegocio.setNombreCarpeta(carpeta.getString("nombreCarpeta"));
            carpetaNegocio.setRutaCarpeta(carpeta.getString("rutaCarpeta"));
            ArrayList<Archivo> archivos = new ArrayList();
            for (int j = 0; j < carpeta.getJSONArray("archivos").length(); j++) {
                Archivo archivoNegocio = new Archivo();
                JSONObject archivo = carpeta.getJSONArray("archivos").getJSONObject(j);
                archivoNegocio.setNombreArchivo(archivo.getString("nombreArchivo"));
                archivoNegocio.setRuta(archivo.getString("ruta"));
                archivoNegocio.setContenido(archivo.getString("contenido"));
                archivoNegocio.setRutaClases(archivo.getString("rutaClases"));
                archivoNegocio.setPaquete(archivo.getString("paquete"));
                archivos.add(archivoNegocio);
            }
            carpetaNegocio.setArchivos(archivos);
            carpetas.add(carpetaNegocio);
        }
        proyectoNegocio.setCarpetas(carpetas);

        return proyectoNegocio;
    }

    public static void finalizarSesion() {
        stagePantallaInvitado.close();
        controlador.hacerVisiblePantallaprincipal();
        mensajeAlert(recurso.getString(mensajeAtencion), recurso.getString("mensajeSesionTerminada"));
        controlador.getSocket().emit("terminarSesion");
    }

    public static void escribirCodigoInvitado(String texto, String ruta) {
        for (MyTab myTab : tabsAbiertosInvitado) {
            if ((myTab.getTreeItem().getArchivo().getRuta() + myTab.getTreeItem().getArchivo().getNombreArchivo()).equals(ruta)) {
                ((CodeArea) myTab.getContent()).replaceText(texto);
            }
        }
    }

    public static Archivo transformarJSONArchivo(JSONObject archivoJSON) {
        Archivo archivo = new Archivo();
        archivo.setNombreArchivo(archivoJSON.getString("nombreArchivo"));
        archivo.setRuta(archivoJSON.getString("ruta"));
        archivo.setRutaClases(archivoJSON.getString("rutaClases"));
        archivo.setContenido(archivoJSON.getString("contenido"));
        archivo.setPaquete(archivoJSON.getString("paquete"));
        return archivo;
    }

    public void abrirTabInvitado(JSONObject archivoJSON) {
        Archivo archivo = transformarJSONArchivo(archivoJSON);
        MyTreeItem treeItem = new MyTreeItem();
        treeItem.setArchivo(archivo);
        treeItem.setRutaCarpeta(archivo.getRuta());
        MyTab tab = new MyTab(archivo.getNombreArchivo());
        FormatoCodigo areaCodigo = new FormatoCodigo();
        areaCodigo.setSampleCode(treeItem.getArchivo().getContenido());
        tab.setContent(areaCodigo.crearAreaCodigo());
        areaCodigo.getCodeArea().setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                treeItem.setModificado(true);
                controlador.getSocket().emit("escribirCodigo", areaCodigo.getCodeArea().getText(), treeItem.getArchivo().getRuta() + treeItem.getArchivo().getNombreArchivo());
            }
        });
        tab.setTreeItem(treeItem);
        tablaArchivos.getTabs().add(tab);
        tabsAbiertosInvitado.add(tab);
    }

    public void mostrarMensajeCompilacionExitosa() {
        mensajeAlert(recurso.getString("felicidades"), recurso.getString("mensajeCompilacionExitosa"));
    }

    public void mostrarMensajeErrorCompilacion(String errorCompilacion) {
        resultadoCompilacion(errorCompilacion, recurso);
    }

    public void mostrarResultadoEjecucion(String resultado) {
        mensajeAlert(recurso.getString("mensajeResultadoEjecucion"), resultado);
    }

    public void agregarPaqueteArbol(JSONObject carpeta) {
        MyTreeItemCarpeta treeItemCarpeta = transformarJSONCarpeta(carpeta);
        MyTreeItemProyecto treeItemProyecto = (MyTreeItemProyecto) root.getChildren().get(0);
        treeItemProyecto.getChildren().add(treeItemCarpeta);
        treeItemProyecto.getNombreCarpetas().add(treeItemCarpeta.getNombreCarpeta());
    }

    public MyTreeItemCarpeta transformarJSONCarpeta(JSONObject carpeta) {
        MyTreeItemCarpeta treeItemCarpeta = new MyTreeItemCarpeta(carpeta.getString("nombreCarpeta"), controlador.crearIconoCarpeta());
        treeItemCarpeta.setNombreCarpeta(carpeta.getString("nombreCarpeta"));
        treeItemCarpeta.setLenguaje(carpeta.getString("lenguaje"));
        treeItemCarpeta.setRuta(carpeta.getString("rutaCarpeta"));
        treeItemCarpeta.setRutaProyecto(carpeta.getString("rutaProyecto"));
        treeItemCarpeta.setNombreArchivos(new ArrayList());
        return treeItemCarpeta;
    }

    public void agregarArchivoArbol(JSONObject archivoJSON, String rutaCarpeta) {
        Archivo archivo = transformarJSONArchivo(archivoJSON);
        MyTreeItem treeItemArchivo = new MyTreeItem(archivo.getNombreArchivo(), controlador.crearIconoArchivo(proyecto.getString("lenguaje")));
        treeItemArchivo.setArchivo(archivo);
        treeItemArchivo.setRutaCarpeta(rutaCarpeta);
        ObservableList<TreeItem<String>> observableList = root.getChildren().get(0).getChildren();
        for (TreeItem<String> treeItem : observableList) {
            MyTreeItemCarpeta treeItemCarpeta = (MyTreeItemCarpeta) treeItem;
            if (treeItemCarpeta.getRuta().equals(rutaCarpeta)) {
                treeItemCarpeta.getChildren().add(treeItemArchivo);
                treeItemCarpeta.getNombreArchivos().add(archivo.getNombreArchivo());
                break;
            }
        }
    }

    public void eliminarPaqueteArbol(String nombrePaquete) {
        ObservableList<TreeItem<String>> observableList = root.getChildren().get(0).getChildren();
        for (TreeItem<String> treeItem : observableList) {
            MyTreeItemCarpeta treeItemCarpeta = (MyTreeItemCarpeta) treeItem;
            if (treeItemCarpeta.getNombreCarpeta().equals(nombrePaquete)) {
                MyTreeItemProyecto myTreeItemProyecto = (MyTreeItemProyecto) treeItemCarpeta.getParent();
                controlador.removerTabsAbiertosCarpetaEliminada(treeItemCarpeta.getRuta(), tabsAbiertosInvitado, tablaArchivos);
                myTreeItemProyecto.getNombreCarpetas().remove(treeItemCarpeta.getNombreCarpeta());
                myTreeItemProyecto.getChildren().remove(treeItemCarpeta);

                break;
            }
        }
    }

    public void elimanrArchivoArbol(String nombrePaquete, String nombreArchivo) {
        ObservableList<TreeItem<String>> observableList = root.getChildren().get(0).getChildren();
        for (TreeItem<String> treeItem : observableList) {
            MyTreeItemCarpeta treeItemCarpeta = (MyTreeItemCarpeta) treeItem;
            if (treeItemCarpeta.getNombreCarpeta().equals(nombrePaquete)) {
                ObservableList<TreeItem<String>> observableListArchivos = treeItemCarpeta.getChildren();
                for (TreeItem treeItemArchivo : observableListArchivos) {
                    MyTreeItem myTreeItem = (MyTreeItem) treeItemArchivo;
                    if (myTreeItem.getArchivo().getNombreArchivo().equals(nombreArchivo)) {
                        controlador.removerTabAbiertoArchivoEliminado(myTreeItem.getArchivo().getRuta() + myTreeItem.getArchivo().getNombreArchivo(), tabsAbiertosInvitado, tablaArchivos);
                        treeItemCarpeta.getChildren().remove(myTreeItem);
                        treeItemCarpeta.getNombreArchivos().remove(myTreeItem.getArchivo().getNombreArchivo());
                        break;
                    }
                }

                break;
            }
        }

    }
}
