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
import servidoride.ServidorIDE;

/**
 * Implementa los metodos de la interfaz de comunicación remota.
 * @author Raymundo Perez
 * @author Alonso Lora
 */
public class ProgramadorDAO implements IProgramador {

    /**
     * Permite corroborar los datos de un colaborador al intentar ingresar al sistema
     * @param programador Datos del programador que desea iniciar sesión
     * @return Si los datos son válidos, invalidos, o si ya ha iniciado sesión previamente 
     * @throws RemoteException Excepcion relacionada con la comunicación, 
     * la cual puede ocurrir durante la ejecución de una llamada a un método remoto.
     */
    @Override
    public InformacionInicioSesion iniciarSesion(Programador programador) throws RemoteException {
        InformacionInicioSesion inicioSesion = InformacionInicioSesion.DatosInvalidos;
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ServidorIDEPU", null);

        ProgramadorJpaController controlador = new ProgramadorJpaController(entityManagerFactory);
        persistencia.Programador programadorBD;

        try {
            programadorBD = controlador.findProgramador(programador.getNombreUsuario());
            if(programadorBD != null && programador.getContraseña().equals(programadorBD.getContrasena())){
                inicioSesion = InformacionInicioSesion.DatosValidos;
                if(ServidorIDE.buscarColaborador(programador.getNombreUsuario())){
                    inicioSesion = InformacionInicioSesion.SesionIniciada;
                }else{
                    ServidorIDE.agregarColaborador(programador.getNombreUsuario());
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return inicioSesion;
    }

    /**
     * Permite registrar los datos de un programador en la base de datos
     * @param programador Datos del programador a registrar.
     * @return Si el registro se completo exitosamente.
     * @throws RemoteException Excepcion relacionada con la comunicación, 
     * la cual puede ocurrir durante la ejecución de una llamada a un método remoto.
     */
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

    /**
     * Permite eliminar el nombre de un programador del arreglo de colaboradores conectados
     * @param nombreProgramador Nombre del programador que desea cerrar sesión
     * @throws RemoteException Excepcion relacionada con la comunicación, 
     * la cual puede ocurrir durante la ejecución de una llamada a un método remoto. 
     */
    @Override
    public void cerrarSesion(String nombreProgramador) throws RemoteException {
        ServidorIDE.eliminarColaborador(nombreProgramador);
    }

}
