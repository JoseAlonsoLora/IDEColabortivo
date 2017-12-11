/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.negocio;

import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author raymu
 */
public class ArchivoTest {

    public ArchivoTest() {
    }

    /**
     * Test of compilarArchivo method, of class Archivo.
     */
    @Test
    public void testCompilarArchivo() {
        System.out.println("compilarArchivo");
        Archivo archivo = new Archivo();
        archivo.setNombreArchivo("holaMundoo.java");
        archivo.setRuta("C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\holaMundoo\\codigo\\holaMundoo");
        archivo.setPaquete("holaMundoo");
        archivo.setRutaClases("C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\holaMundoo\\clases\\holaMundoo");
        String rutaProyecto = "C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\holaMundoo";
        Archivo instance = new Archivo();
        String expResult = "";
        String result = instance.compilarArchivo(archivo, rutaProyecto);
        assertEquals(expResult, result);
    }

    /**
     * Test of compilador method, of class Archivo.
     */
    @Test
    public void testCompilador() {
        System.out.println("compilador");
        String comando = "javac -cp \".;C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\holaMundoo\\codigo\" "
                + "-d C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\holaMundoo\\clases holaMundoo.java";
        String ruta = "C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\holaMundoo\\codigo\\holaMundoo";
        Archivo instance = new Archivo();
        String expResult = "";
        String result = instance.compilador(comando, ruta);
        assertEquals(expResult, result);
    }

    /**
     * Test of compilarJava method, of class Archivo.
     */
    @Test
    public void testCompilarJava() {
        System.out.println("compilarJava");
        Archivo archivo = new Archivo();
        archivo.setNombreArchivo("holaMundoo.java");
        archivo.setRuta("C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\holaMundoo\\codigo\\holaMundoo");
        archivo.setPaquete("holaMundoo");
        archivo.setRutaClases("C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\holaMundoo\\clases\\holaMundoo");
        String rutaProyecto = "C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\holaMundoo";
        Archivo instance = new Archivo();
        String expResult = "";
        String result = instance.compilarJava(archivo, rutaProyecto);
        assertEquals(expResult, result);
    }

    /**
     * Test of complilarCPlusPlus method, of class Archivo.
     */
    @Test
    public void testComplilarCPlusPlus() {
        System.out.println("complilarCPlusPlus");
        Archivo archivo = new Archivo();
        archivo.setNombreArchivo("holaprueba.cpp");
        archivo.setRuta("C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\holaprueba\\codigo\\holaprueba");
        archivo.setPaquete("holaprueba");
        archivo.setRutaClases("C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\holaprueba\\clases");
        String nombre = "holaPrueba";
        Archivo instance = new Archivo();
        String expResult = "";
        String result = instance.complilarCPlusPlus(archivo, nombre);
        assertEquals(expResult, result);
    }

    /**
     * Test of ejecutarArchivo method, of class Archivo.
     */
    @Test
    public void testEjecutarArchivo_Archivo() {
        System.out.println("ejecutarArchivo");
        Archivo archivo = new Archivo();
        archivo.setNombreArchivo("holaMundoo.java");
        archivo.setRuta("C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\holaMundoo\\codigo\\holaMundoo");
        archivo.setPaquete("holaMundoo");
        archivo.setRutaClases("C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\holaMundoo\\clases\\holaMundoo");
        Archivo instance = new Archivo();
        String expResult = "ide";
        String result = instance.ejecutarArchivo(archivo);
        result = result.substring(0, 3);
        assertEquals(expResult, result);
    }

    /**
     * Test of ejecutarArchivo method, of class Archivo.
     */
    @Test
    public void testEjecutarArchivo_Archivo_String() {
        System.out.println("ejecutarArchivo");
        Archivo archivo = new Archivo();
        archivo.setNombreArchivo("holaMundoParametros.java");
        archivo.setRuta("C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\holaMundoParametros\\codigo\\holaMundoParametros");
        archivo.setPaquete("holaMundoParametros");
        archivo.setRutaClases("C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\holaMundoParametros\\clases\\");
        String parametros = "holaMundo";
        Archivo instance = new Archivo();
        String expResult = "holaMundo";
        String result = instance.ejecutarArchivo(archivo, parametros);
        result = result.substring(0, 9);
        assertEquals(expResult, result);
    }

    /**
     * Test of crearArchivoParametros method, of class Archivo.
     */
    @Test
    public void testCrearArchivoParametros() {
        System.out.println("crearArchivoParametros");
        String ruta = "C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\holaMundoParametros\\clases\\";
        String parametros = "holaMundo";
        Archivo instance = new Archivo();
        instance.crearArchivoParametros(ruta, parametros);
        File file = new File(ruta+"archivoParametros");
        assertEquals(true, file.exists());
    }

    /**
     * Test of ejecutarJava method, of class Archivo.
     */
    @Test
    public void testEjecutarJava() {
        System.out.println("ejecutarJava");
        Archivo archivo = new Archivo();
        archivo.setNombreArchivo("holaMundoo.java");
        archivo.setRuta("C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\holaMundoo\\codigo\\holaMundoo");
        archivo.setPaquete("holaMundoo");
        archivo.setRutaClases("C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\holaMundoo\\clases\\holaMundoo");
        String nombre = "holaMundoo";
        boolean tieneParametros = false;
        Archivo instance = new Archivo();
        String expResult = "ide";
        String result = instance.ejecutarJava(archivo, nombre, tieneParametros);
        result = result.substring(0, 3);
        assertEquals(expResult, result);
    }

    /**
     * Test of ejecutarCPlusPlus method, of class Archivo.
     */
    @Test
    public void testEjecutarCPlusPlus() {
        System.out.println("ejecutarCPlusPlus");
        Archivo archivo = new Archivo();
        archivo.setNombreArchivo("holaprueba.cpp");
        archivo.setRuta("C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\holaprueba\\codigo\\holaprueba");
        archivo.setPaquete("holaprueba");
        archivo.setRutaClases("C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\holaprueba\\clases");
        String nombre = "holaprueba";
        boolean tieneParametros = false;
        Archivo instance = new Archivo();
        String expResult = "hola";
        String result = instance.ejecutarCPlusPlus(archivo, nombre, tieneParametros);
        result = result.substring(0, 4);
        assertEquals(expResult, result);

    }

    /**
     * Test of crearArchivo method, of class Archivo.
     */
    @Test
    public void testCrearArchivo() {
        System.out.println("crearArchivo");
         Archivo archivo = new Archivo();
        archivo.setNombreArchivo("prueba.cpp");
        archivo.setRuta("C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\holaprueba\\codigo\\holaprueba");
        archivo.setPaquete("holaprueba");
        archivo.setRutaClases("C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\holaprueba\\clases");
        Archivo instance = new Archivo();
        boolean expResult = true;
        boolean result = instance.crearArchivo(archivo);
        assertEquals(expResult, result);
    }

    /**
     * Test of eliminarArchivo method, of class Archivo.
     */
    @Test
    public void testEliminarArchivo() {
        System.out.println("eliminarArchivo");
         Archivo archivo = new Archivo();
        archivo.setNombreArchivo("prueba.cpp");
        archivo.setRuta("C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\holaprueba\\codigo\\holaprueba");
        archivo.setPaquete("holaprueba");
        archivo.setRutaClases("C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\holaprueba\\clases");
        Archivo instance = new Archivo();
        boolean expResult = true;
        boolean result = instance.eliminarArchivo(archivo);
        assertEquals(expResult, result);
    }

    /**
     * Test of guardarArchivo method, of class Archivo.
     */
    @Test
    public void testGuardarArchivo() {
        System.out.println("guardarArchivo");
        Archivo archivo = new Archivo();
        archivo.setNombreArchivo("prueba.cpp");
        archivo.setRuta("C:\\Users\\raymu\\Desktop\\ArchivosPruebas");
        archivo.setContenido("hola");
        Archivo instance = new Archivo();
        boolean expResult = true;
        boolean result = instance.guardarArchivo(archivo);
        assertEquals(expResult, result);
    }


}
