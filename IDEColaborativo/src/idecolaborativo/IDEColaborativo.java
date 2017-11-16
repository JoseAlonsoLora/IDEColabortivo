/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idecolaborativo;

import componentes.FormatoCodigo;
import controladores.PantallaCambiarIdiomaController;
import controladores.PantallaCrearProyectoController;
import controladores.PantallaIniciarSesionController;
import controladores.PantallaPrincipalController;
import controladores.PantallaRegistrarUsuarioController;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import javafx.stage.Stage;

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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
