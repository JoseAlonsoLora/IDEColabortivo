/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion.operaciones;

import java.rmi.Remote;
import java.rmi.RemoteException;
import modelo.negocio.Cuenta;

/**
 *
 * @author raymu
 */
public interface ICuenta extends Remote {
    public boolean iniciarSesion(Cuenta cuenta) throws RemoteException;
    
}
