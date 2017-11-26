/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion.node;

import controladores.PantallaPrincipalController;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

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
             socket = IO.socket("http://localhost:9000");
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
                    controlador.invitacionErronea();
                });
            }
        }).on("mostrarInvitaion", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                Platform.runLater(() -> {
                   controlador.invitacionEnviada((String) os[0],(String) os[1]);
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
