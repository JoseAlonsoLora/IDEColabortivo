/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.negocio;

import java.io.Serializable;

/**
 * La clase Programador para enviar de manera remota los datos de l programador
 * @author Raymundo Pérez
 * @author Alonso Lora
 */
public class Programador implements Serializable{
    private String nombreUsuario;
    private String contraseña;
    private String correoElectronico;
    
    /**
     * Regresa el nombre del usurio
     * @return Nmbre del usuario
     */
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    /**
     * Da valor al nombre de usuario
     * @param nombreUsuario Nombre de usuario
     */
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    
    /**
     * Regresa la contraseña del usuario
     * @return Contraseña del usuario
     */
    public String getContraseña() {
        return contraseña;
    }
    
    /**
     * Da valor a la contraseña de usuario
     * @param contraseña Contraseña de usuario
     */
    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    /**
     * Regresa el correo electrónico del usurario
     * @return Correo electrónico
     */
    public String getCorreoElectronico() {
        return correoElectronico;
    }
    
    /**
     * Da valor al correo electrónico del usuario
     * @param correoElectronico Correo electrónico
     */
    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }
    
}
