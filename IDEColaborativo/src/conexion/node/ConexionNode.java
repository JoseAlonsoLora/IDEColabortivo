/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion.node;

import static controladores.PantallaHostController.colaboradorConectado;
import static controladores.PantallaInvitarColaboradorController.invitacionErronea;
import static controladores.PantallaInvitarColaboradorController.mostrarVentanaHost;
import controladores.PantallaPrincipalController;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import org.json.JSONObject;

/**
 *
 * @author raymu
 */
public class ConexionNode {
     private Socket socket;
     private PantallaPrincipalController controlador;

    public ConexionNode(PantallaPrincipalController controlador){
        this.controlador = controlador;
         try {
             socket = IO.socket("http://192.168.100.17:9000");
             socket.on("saludoDelBarrio", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                Platform.runLater(() -> {
                    //escribirMensaje((String) os[0]);
                });
            }
        }).on("colaboradorNoEncontrado", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                Platform.runLater(() -> {
                    invitacionErronea();
                });
            }
        }).on("mostrarInvitaion", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                Platform.runLater(() -> {
                   controlador.invitacionEnviada((String) os[0],(String) os[1],(JSONObject) os[2]);
                });
            }
        }).on("colaboradorConectado", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                Platform.runLater(() -> {
                  colaboradorConectado((String)os[0]);
                });
            }
        }).on("colaboradorEncontrado", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                Platform.runLater(() -> {
                  mostrarVentanaHost();
                });
            }
        });
        socket.connect();
         } catch (URISyntaxException ex) {
             Logger.getLogger(ConexionNode.class.getName()).log(Level.SEVERE, null, ex);
         }
         
        
    }
      
    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
     
    
}
