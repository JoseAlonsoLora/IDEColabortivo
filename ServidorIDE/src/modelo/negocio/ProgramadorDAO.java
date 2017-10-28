/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.negocio;

import conexion.operaciones.IProgramador;
import java.rmi.RemoteException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import persistencia.ProgramadorJpaController;

/**
 *
 * @author raymu
 */
public class ProgramadorDAO implements IProgramador {

    @Override
    public boolean iniciarSesion(Programador programador) throws RemoteException {
        boolean inicioSesion = false;
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ServidorIDEPU", null);

        ProgramadorJpaController controlador = new ProgramadorJpaController(entityManagerFactory);
        persistencia.Programador programadorBD;

        try {
            programadorBD = controlador.findProgramador(programador.getNombreUsuario());
            inicioSesion = programadorBD != null && programador.getContraseña().equals(programadorBD.getContrasena());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return inicioSesion;
    }

    @Override
    public boolean registrarUsuario(Programador programador) throws RemoteException {
        boolean esNombreValido = false;
        boolean registroCompletado = false;
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ServidorIDEPU", null);

        ProgramadorJpaController controlador = new ProgramadorJpaController(entityManagerFactory);
        persistencia.Programador programadorBD;
        

        try {
            programadorBD = controlador.findProgramador(programador.getNombreUsuario());
            esNombreValido = programadorBD == null;
            if(esNombreValido){
                programadorBD = new persistencia.Programador();
                programadorBD.setNombreUsuario(programador.getNombreUsuario());
                programadorBD.setContrasena(programador.getContraseña());
                programadorBD.setCorreoElectronico(programador.getCorreoElectronico());
                controlador.create(programadorBD);
                registroCompletado = true;
            }else{
                registroCompletado = false;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
        return registroCompletado;
    }

}
