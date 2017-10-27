/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.negocio;

import conexion.operaciones.ICuenta;
import java.rmi.RemoteException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import persistencia.CuentaJpaController;

/**
 *
 * @author raymu
 */
public class CuentaDAO implements ICuenta{

    @Override
    public boolean iniciarSesion(Cuenta cuenta) throws RemoteException {
        boolean inicioSesion = false;
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ServidorIDEPU", null);

        CuentaJpaController controlador= new CuentaJpaController(entityManagerFactory);
         persistencia.Cuenta cuentaBD; 

        try {
            cuentaBD = controlador.findCuenta(cuenta.getNombreUsuario());
            inicioSesion = cuentaBD != null && cuenta.getContraseña().equals(cuentaBD.getContrasena());     
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return inicioSesion;
    }
    
}
