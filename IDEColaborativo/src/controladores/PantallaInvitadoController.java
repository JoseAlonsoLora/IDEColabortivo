/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import clasesApoyo.MyTreeItem;
import com.jfoenix.controls.JFXButton;
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
import modelo.negocio.Archivo;
import modelo.negocio.Carpeta;
import modelo.negocio.Proyecto;
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

    private PantallaPrincipalController controlador;
    private Stage stagePantallaInvitado;
    @FXML
    private TreeTableView<String> tablaProyecto;
    @FXML
    private TreeTableColumn<String, String> columnaProyecto;
    private TreeItem<String> root;
    private ArrayList<MyTreeItem> tabsAbiertos;
    private ResourceBundle recurso;

    /**
     * Initializes the controller class.
     */
    public void setProyecto(JSONObject proyecto) {
        this.proyecto = proyecto;
        // TODO
    }

    public void setControlador(PantallaPrincipalController controlador) {
        this.controlador = controlador;
    }

    public void setStagePantallaInvitado(Stage stagePantallaInvitado) {
        this.stagePantallaInvitado = stagePantallaInvitado;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        recurso = rb;
        root = new TreeItem<>(recurso.getString("etProyectos"));
        tabsAbiertos = new ArrayList();
    }

    @FXML
    private void cancelarColaboracion(ActionEvent event) {
    }

    public void cargarProyecto() {
        Proyecto proyecto = transformarJSON();
        TreeItem<String> hijo = new TreeItem<>(proyecto.getNombreProyecto(), controlador.crearIconoLenguaje(proyecto.getLenguaje()));
        hijo.getChildren().setAll(controlador.agregarCarpetasArbol(proyecto));
        root.getChildren().add(hijo);
        columnaProyecto.setCellValueFactory((TreeTableColumn.CellDataFeatures<String, String> p) -> new ReadOnlyStringWrapper(p.getValue().getValue()));
        tablaProyecto.setRoot(root);
        tablaProyecto.setShowRoot(true);
        controlador.handlerTablaProyectos(tablaProyecto, tabsAbiertos, tablaArchivos);
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
            ArrayList<Archivo> archivos = new ArrayList();
            for (int j = 0; j < carpeta.getJSONArray("archivos").length(); j++) {
                Archivo archivoNegocio = new Archivo();
                JSONObject archivo = carpeta.getJSONArray("archivos").getJSONObject(j);
                archivoNegocio.setNombreArchivo(archivo.getString("nombreArchivo"));
                archivoNegocio.setRuta(archivo.getString("ruta"));
                archivoNegocio.setContenido(archivo.getString("contenido"));
                archivos.add(archivoNegocio);
            }
            carpetaNegocio.setArchivos(archivos);
            carpetas.add(carpetaNegocio);
        }
        proyectoNegocio.setCarpetas(carpetas);

        return proyectoNegocio;
    }
}
