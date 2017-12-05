/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidoride;

import conexion.operaciones.IProgramador;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import modelo.negocio.ProgramadorDAO;

/**
 *
 * @author raymu
 */
public class ServidorIDE {

    private static ArrayList<String> colaboradoresConectados;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        colaboradoresConectados = new ArrayList();
        try {
            ProgramadorDAO programador = new ProgramadorDAO();
            IProgramador stub = (IProgramador) UnicastRemoteObject.exportObject(programador, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("AdministrarUsuarios", stub);
            System.out.println("Servidor corriendo");
        } catch (RemoteException | AlreadyBoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static boolean buscarColaborador(String nombreColaborador) {
        boolean estaConectado = false;
        if (colaboradoresConectados.contains(nombreColaborador)) {
            estaConectado = true;
        }
        return estaConectado;
    }

    public static void agregarColaborador(String nombreColaborador) {
        colaboradoresConectados.add(nombreColaborador);
    }

    public static void eliminarColaborador(String nombreColaborador) {
        colaboradoresConectados.remove(nombreColaborador);
    }

}
