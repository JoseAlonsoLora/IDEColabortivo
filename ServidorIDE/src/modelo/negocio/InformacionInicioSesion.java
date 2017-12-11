/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.negocio;

import java.io.Serializable;

/**
 *
 * @author Alonso Lora
 * @author Raymundo Perez
 */
public enum InformacionInicioSesion implements Serializable{
    /**
     * Los datos del usuarios son correctos
     */
    DatosValidos,
    /**
     * Los datos del usuarios son incorrectos
     */
    DatosInvalidos,
    /**
     * La sesión ya esta iniciada
     */
    SesionIniciada
    
}
