/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion.operaciones;

import java.rmi.Remote;
import java.rmi.RemoteException;
import modelo.negocio.InformacionInicioSesion;
import modelo.negocio.Programador;

/**
 * Interfaz con los métodos para la comunicación remota.
 * @author Raymundo Perez
 * @author Alonso Lora
 */
public interface IProgramador extends Remote{
    /**
     * Permite corroborar los datos de un colaborador al intentar ingresar al sistema
     * @param programador Datos del programador que desea iniciar sesión
     * @return Si los datos son válidos, invalidos, o si ya ha iniciado sesión previamente 
     * @throws RemoteException Excepcion relacionada con la comunicación, 
     * la cual puede ocurrir durante la ejecución de una llamada a un método remoto.
     */
    public InformacionInicioSesion iniciarSesion(Programador programador) throws RemoteException;
    /**
     * Permite registrar los datos de un programador en la base de datos
     * @param programador Datos del programador a registrar.
     * @return Si el registro se completo exitosamente.
     * @throws RemoteException Excepcion relacionada con la comunicación, 
     * la cual puede ocurrir durante la ejecución de una llamada a un método remoto.
     */
    public boolean registrarUsuario(Programador programador) throws RemoteException;
    /**
     * Permite eliminar el nombre de un programador del arreglo de colaboradores conectados
     * @param nombreProgramador Nombre del programador que desea cerrar sesión
     * @throws RemoteException Excepcion relacionada con la comunicación, 
     * la cual puede ocurrir durante la ejecución de una llamada a un método remoto. 
     */
    public void cerrarSesion(String nombreProgramador) throws RemoteException;
}
