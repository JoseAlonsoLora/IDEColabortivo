/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.negocio;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author raymu
 */
public class ProgramadorDAOTest {
    
    public ProgramadorDAOTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of iniciarSesion method, of class ProgramadorDAO.
     */
    @Test
    public void testIniciarSesionUsuarioValido() throws Exception {
        System.out.println("iniciarSesion");
        Programador programador = new Programador();
        programador.setNombreUsuario("alonso");
        programador.setContraseña(makeHash("alonso"));
        ProgramadorDAO instance = new ProgramadorDAO();
        boolean expResult = true;
        boolean result = instance.iniciarSesion(programador);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testIniciarSesionUsuarioNoValido() throws Exception {
        System.out.println("iniciarSesion");
        Programador programador = new Programador();
        programador.setNombreUsuario("alonso");
        programador.setContraseña(makeHash("lorenzo"));
        ProgramadorDAO instance = new ProgramadorDAO();
        boolean expResult = false;
        boolean result = instance.iniciarSesion(programador);
        assertEquals(expResult, result);
    }

    /**
     * Test of registrarUsuario method, of class ProgramadorDAO.
     */
    @Test
    public void testRegistrarUsuarioValido() throws Exception {
        System.out.println("registrarUsuario");
        Programador programador = new Programador();
        programador.setNombreUsuario("usuario3");
        programador.setContraseña(makeHash("usuario3"));
        programador.setCorreoElectronico("usuario3@gmail.com");
        ProgramadorDAO instance = new ProgramadorDAO();
        boolean expResult = true;
        boolean result = instance.registrarUsuario(programador);
        assertEquals(expResult, result);
    }
    
     @Test
    public void testRegistrarUsuarioNoValido() throws Exception {
        System.out.println("registrarUsuario");
        Programador programador = new Programador();
        programador.setNombreUsuario("usuario1");
        programador.setContraseña(makeHash("usuario1"));
        programador.setCorreoElectronico("usuario1@gmail.com");
        ProgramadorDAO instance = new ProgramadorDAO();
        boolean expResult = false;
        boolean result = instance.registrarUsuario(programador);
        assertEquals(expResult, result);
    }
    
    public static String makeHash(String string) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(string.getBytes());
            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < hash.length; i++) {
                stringBuilder.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException ex) {
            
        }
        return null;
    }
    
}
