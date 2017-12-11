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
 *
 * @author raymu
 */
public interface IProgramador extends Remote{
    /**
     * 
     * @param programador
     * @return
     * @throws RemoteException 
     */
    public InformacionInicioSesion iniciarSesion(Programador programador) throws RemoteException;
    /**
     * 
     * @param programador
     * @return
     * @throws RemoteException 
     */
    public boolean registrarUsuario(Programador programador) throws RemoteException;
    /**
     * 
     * @param nombreProgramador
     * @throws RemoteException 
     */
    public void cerrarSesion(String nombreProgramador) throws RemoteException;
}
