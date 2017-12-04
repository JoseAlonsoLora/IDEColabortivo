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
import io.socket.client.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
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
    private ResourceBundle recurso;
    private ArrayList<Proyecto> proyectos;
    @FXML
    private Label etiquetaProyecto;
    @FXML
    private JFXComboBox<String> comboProyectos;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        recurso = rb;
        configurarIdioma();
        ObservableList<String> items = FXCollections.observableArrayList();
        items.addAll(obtenerNombresProyectos());
        comboProyectos.setItems(items);
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    
    public void setProyectos(ArrayList<Proyecto> proyectos){
        this.proyectos = proyectos;
    }
    
    
    public void configurarIdioma(){
        etiquetaNombreColaborador.setText(recurso.getString("etNombreColaborador"));
        botonInvitar.setText(recurso.getString("btInvitar"));
        botonCancelar.setText(recurso.getString("btCancelar"));
    }
    
    public ArrayList<String> obtenerNombresProyectos(){
        ArrayList<String> nombreProyectos = new ArrayList();
        for(Proyecto proyecto: proyectos){
            nombreProyectos.add(proyecto.getNombreProyecto());
        }
        return nombreProyectos;
    }
    
    public Proyecto buscarProyecto(String nombre){
        Proyecto proyectoEnviar = new Proyecto();
        for(Proyecto proyecto: proyectos){
            if(proyecto.getNombreProyecto().equals(nombre)){
                proyectoEnviar = proyecto;
            }
        }
        return proyectoEnviar;
    }
    
    @FXML
    private void invitarColaborador(ActionEvent event) {
        if(!campoTextoNombreColaborador.getText().isEmpty()){
        socket.emit("invitarColaborador", campoTextoNombreColaborador.getText());
        Stage ventanaInvitarColaborador = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ventanaInvitarColaborador.close();
        }else{
            mensajeAlert(recurso.getString("atencion"), recurso.getString("mensajeCamposVacios"));
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        Stage ventanaInvitarColaborador = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ventanaInvitarColaborador.close();
    }
    
    
    public static JSONObject crearObjetoJSON(Proyecto proyecto){
        JSONObject proyectoJSON = new JSONObject();
        proyectoJSON.put("nombreProyecto", proyecto.getNombreProyecto());
        proyectoJSON.put("lenguaje", proyecto.getLenguaje());
        JSONArray arregloCarpetas = new JSONArray();
        for(Carpeta carpeta:proyecto.getCarpetas()){
            JSONObject carpetaJSON = new JSONObject();
            carpetaJSON.put("nombreCarpeta", carpeta.getNombreCarpeta());
            JSONArray arregloArchivos = new JSONArray();
            for(Archivo archivo:carpeta.getArchivos()){
                JSONObject archivoJSON = new JSONObject();
                archivoJSON.put("nombreArchivo", archivo.getNombreArchivo());
                archivoJSON.put("contenido", archivo.getContenido());
                arregloArchivos.put(archivoJSON);
            }
            carpetaJSON.put("archivos", arregloArchivos);
            arregloCarpetas.put(carpetaJSON);
        }
        proyectoJSON.put("carpetas",arregloCarpetas);
        crearArbolProyecto(proyectoJSON);
        return proyectoJSON;   
    }
    
    public static void crearArbolProyecto(JSONObject proyecto){
        System.out.println(proyecto.getString("nombreProyecto"));
        for(int i=0;i<proyecto.getJSONArray("carpetas").length();i++){
            JSONObject carpeta = proyecto.getJSONArray("carpetas").getJSONObject(i);
            System.out.println(" "+carpeta.getString("nombreCarpeta"));
            for(int j = 0;j < carpeta.getJSONArray("archivos").length();j++){
                System.out.println("  "+carpeta.getJSONArray("archivos").getJSONObject(j).getString("nombreArchivo"));
            }
        }   
        
    }
}
