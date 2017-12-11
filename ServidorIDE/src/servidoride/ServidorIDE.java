/* Identificador del programa: SERVIDORIDE2017

   Clave única del programa: SIDE2017
   
   Nombre: Servidor IDE Colaborativo
 
   Nombre de los desarrolladores: José Alonso Lora González
                                  Raymundo de Jesús Pérez Castellanos
    
   Fecha en la que se inicio el desarrollo del programa: 20 de septiembre 2017

   Descripción: Servidor que permite conectar a los clientes de la aplicación con la base de datos del sistema.

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
 * Clase principal del servidor
 * @author Raymundo Perez
 * @author Alonso Lora
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
    
    /**
     * Busca si el colaborador ya esta en el arreglo de conectados
     * @param nombreColaborador Nombre del colaborador que sera buscado
     * @return Indica si el colaborador esta o no conectado
     */
    public static boolean buscarColaborador(String nombreColaborador) {
        boolean estaConectado = false;
        if (colaboradoresConectados.contains(nombreColaborador)) {
            estaConectado = true;
        }
        return estaConectado;
    }
    
    /**
     * Agrega el nombre de un colaborador al arreglo de colaboradores conectados
     * @param nombreColaborador Nombre del colaborador a agregar 
     */
    public static void agregarColaborador(String nombreColaborador) {
        colaboradoresConectados.add(nombreColaborador);
    }

    /**
     * Elimina el nombre de un colaborador del arreglo de colaboradores conectados
     * @param nombreColaborador Nombre del colaborador a eliminar
     */
    public static void eliminarColaborador(String nombreColaborador) {
        colaboradoresConectados.remove(nombreColaborador);
    }

}
