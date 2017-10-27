/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidoride;

import conexion.operaciones.ICuenta;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import modelo.negocio.CuentaDAO;

/**
 *
 * @author raymu
 */
public class ServidorIDE {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            CuentaDAO cuenta = new CuentaDAO();
            ICuenta stub = (ICuenta) UnicastRemoteObject.exportObject(cuenta, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("Login", stub);
            System.out.println("Servidor corriendo");
        } catch (RemoteException | AlreadyBoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
