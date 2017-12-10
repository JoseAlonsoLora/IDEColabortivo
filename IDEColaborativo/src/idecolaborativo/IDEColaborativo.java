/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idecolaborativo;

import componentes.FormatoCodigo;
import controladores.PantallaCambiarIdiomaController;
import controladores.PantallaCrearProyectoController;
import controladores.PantallaDireccionIPController;
import controladores.PantallaEjecutarController;
import controladores.PantallaHostController;
import controladores.PantallaIniciarSesionController;
import controladores.PantallaInvitadoController;
import controladores.PantallaInvitarColaboradorController;
import controladores.PantallaPrincipalController;
import controladores.PantallaRegistrarUsuarioController;
import io.socket.client.Socket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import javafx.stage.Stage;
import modelo.negocio.Archivo;
import modelo.negocio.Proyecto;
import org.json.JSONObject;

/**
 *
 * @author alonso
 */
public class IDEColaborativo extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        ResourceBundle recurso = ResourceBundle.getBundle("recursos.idioma_en_US");
        ventanaPrincipal(recurso);
    }

    public static void ventanaPrincipal(ResourceBundle recurso) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(IDEColaborativo.class.getResource("/vistas/PantallaPrincipal.fxml"), recurso);
            Parent root = (Parent) loader.load();
            PantallaPrincipalController control = loader.getController();
            control.setControlador(control);
            stage.setTitle("IDE Colaborativo");
            Scene scene = new Scene(root);
            scene.getStylesheets().add(FormatoCodigo.class.getResource("/css/java-keywords.css").toExternalForm());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            control.setStagePantallaPrincipal(stage);
        } catch (IOException ex) {
            Logger.getLogger(IDEColaborativo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void ventanaInicioSesion(ResourceBundle recurso, PantallaPrincipalController controlador) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(IDEColaborativo.class.getResource("/vistas/PantallaIniciarSesion.fxml"), recurso);
            Parent root = (Parent) loader.load();
            PantallaIniciarSesionController control = loader.getController();
            control.setControlador(controlador);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            control.setStagePantallaIniciarSesion(stage);
        } catch (IOException ex) {
            Logger.getLogger(IDEColaborativo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void ventanaRegistrarUsuario(ResourceBundle recurso, PantallaPrincipalController controlador) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(IDEColaborativo.class.getResource("/vistas/PantallaRegistrarUsuario.fxml"), recurso);
            Parent root = (Parent) loader.load();
            PantallaRegistrarUsuarioController control = loader.getController();
            control.setControlador(controlador);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            control.setStagePantallaRegistrarUsuario(stage);
        } catch (IOException ex) {
            Logger.getLogger(IDEColaborativo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void ventanaCrearProyecto(ResourceBundle recurso, PantallaPrincipalController controlador) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(IDEColaborativo.class.getResource("/vistas/PantallaCrearProyecto.fxml"), recurso);
            Parent root = (Parent) loader.load();
            PantallaCrearProyectoController control = loader.getController();
            control.setControlador(controlador);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            control.setStagePantallaCrearProyecto(stage);
        } catch (IOException ex) {
            Logger.getLogger(IDEColaborativo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void ventanaCambiarIdioma(ResourceBundle recurso, PantallaPrincipalController controlador) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(IDEColaborativo.class.getResource("/vistas/PantallaCambiarIdioma.fxml"), recurso);
            Parent root = (Parent) loader.load();
            PantallaCambiarIdiomaController control = loader.getController();
            control.setControlador(controlador);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            control.setStagePantallaCambiarIdioma(stage);
        } catch (IOException ex) {
            Logger.getLogger(IDEColaborativo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void mensajeAlert(String encabezado, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(encabezado);
        alert.setContentText(mensaje);
        alert.show();
    }

    public static void resultadoCompilacion(String resultado, ResourceBundle recurso) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(recurso.getString("errorCompilacion"));
        alert.setHeaderText(recurso.getString("etErrorCompilacion"));
        TextArea textArea = new TextArea(resultado);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 1);
        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }

    public static void ventanaEjecutar(ResourceBundle recurso, Archivo archivo, 
            PantallaPrincipalController controlador,boolean esColaborativo) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(IDEColaborativo.class.getResource("/vistas/PantallaEjecutar.fxml"), recurso);
            Parent root = (Parent) loader.load();
            PantallaEjecutarController control = loader.getController();
            control.setArchivo(archivo);
            control.setControlador(controlador);
            control.setEsColaborativo(esColaborativo);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(IDEColaborativo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void ventanaInvitarColaborador(ResourceBundle recurso,Socket socket,PantallaPrincipalController controlador){
         try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(IDEColaborativo.class.getResource("/vistas/PantallaInvitarColaborador.fxml"), recurso);
            Parent root = (Parent) loader.load();
            PantallaInvitarColaboradorController control = loader.getController();
            control.setSocket(socket);
            control.setControlador(controlador);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            control.setStagePantallaInivitar(stage);
        } catch (IOException ex) {
            Logger.getLogger(IDEColaborativo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void ventanaHost(ResourceBundle recurso,Proyecto proyecto,PantallaPrincipalController controlador){
         try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(IDEColaborativo.class.getResource("/vistas/PantallaHost.fxml"), recurso);
            Parent root = (Parent) loader.load();
            PantallaHostController control = loader.getController();
            control.setProyecto(proyecto);
            control.setControlador(controlador);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(FormatoCodigo.class.getResource("/css/java-keywords.css").toExternalForm());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            control.setStagePantallaHost(stage);
            control.cargarProyecto();
        } catch (IOException ex) {
            Logger.getLogger(IDEColaborativo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void ventanaInvitado(ResourceBundle recurso,JSONObject proyecto,PantallaPrincipalController controlador){
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(IDEColaborativo.class.getResource("/vistas/PantallaInvitado.fxml"), recurso);
            Parent root = (Parent) loader.load();
            PantallaInvitadoController control = loader.getController();
            control.setProyecto(proyecto);
            control.setControlador(controlador);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(FormatoCodigo.class.getResource("/css/java-keywords.css").toExternalForm());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            control.setStagePantallaInvitado(stage);
            control.cargarProyecto();
        } catch (IOException ex) {
            Logger.getLogger(IDEColaborativo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void ventanaDireccionIP(ResourceBundle recurso, PantallaPrincipalController controlador){
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(IDEColaborativo.class.getResource("/vistas/PantallaDireccionIP.fxml"), recurso);
            Parent root = (Parent) loader.load();
            PantallaDireccionIPController control = loader.getController();
            control.setControlador(controlador);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            control.setStagePantallaDireccionIP(stage);
        } catch (IOException ex) {
            Logger.getLogger(IDEColaborativo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
