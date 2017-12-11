/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.negocio;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class CarpetaTest {
    
    public CarpetaTest() {
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
     * Test of crearCarpeta method, of class Carpeta.
     */
    @Test
    public void testCrearCarpeta() {
        System.out.println("crearCarpeta");
        Carpeta carpeta = new Carpeta();
        carpeta.setNombreCarpeta("pruebaCarpeta");
        carpeta.setRutaCarpeta("C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\pruebaCarpeta");
        Carpeta instance = new Carpeta();
        boolean expResult = true;
        boolean result = instance.crearCarpeta(carpeta);
        File file = new File ("C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\pruebaCarpeta\\prueba");
        try {
            file.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(CarpetaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals(expResult, result);
    }

    /**
     * Test of eliminarCarpeta method, of class Carpeta.
     */
    @Test
    public void testEliminarCarpeta() {
        System.out.println("eliminarCarpeta");
        String ruta = "C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\pruebaCarpeta";
        Carpeta instance = new Carpeta();
        boolean expResult = true;
        boolean result = instance.eliminarCarpeta(ruta);
        assertEquals(expResult, result);
    }

    /**
     * Test of eliminarArchivosCarpeta method, of class Carpeta.
     */
    @Test
    public void testEliminarArchivosCarpeta() {
        System.out.println("eliminarArchivosCarpeta");
        String[] archivos = {"prueba"};
        String ruta = "C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\pruebaCarpeta";
        Carpeta instance = new Carpeta();
        instance.eliminarArchivosCarpeta(archivos, ruta);
        File file = new File ("C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\pruebaCarpeta\\prueba");
        boolean seElimino = file.exists();
        assertEquals(false, seElimino);
    }
    
}
