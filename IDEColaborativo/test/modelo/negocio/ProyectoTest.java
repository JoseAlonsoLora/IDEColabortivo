/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.negocio;

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author raymu
 */
public class ProyectoTest {
    
    public ProyectoTest() {
    }
    

    /**
     * Test of crearArchivoRutas method, of class Proyecto.
     */
    @Test
    public void testCrearArchivoRutas() {
        System.out.println("crearArchivoRutas");
        Proyecto instance = new Proyecto();
        boolean expResult = false;
        boolean result = instance.crearArchivoRutas();
        assertEquals(expResult, result);
    }
    

    /**
     * Test of actualizarArchivoRutas method, of class Proyecto.
     */
    @Test
    public void testActualizarArchivoRutas() {
        System.out.println("actualizarArchivoRutas");
        String cadenaActualizadda = "C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\holaprueba,c++,holaprueba";
        Proyecto instance = new Proyecto();
        boolean expResult = true;
        boolean result = instance.actualizarArchivoRutas(cadenaActualizadda);
        assertEquals(expResult, result);
    }

    

    /**
     * Test of obtenerRutaProyectos method, of class Proyecto.
     */
    @Test
    public void testObtenerRutaProyectos() {
        System.out.println("obtenerRutaProyectos");
        String expResult = "/Users/raymu/rutas.ide";
        String result = Proyecto.obtenerRutaProyectos();
        assertEquals(expResult, result);
    }


    /**
     * Test of cargarProyectos method, of class Proyecto.
     */
    @Test
    public void testCargarProyectos() {
        System.out.println("cargarProyectos");
        Proyecto instance = new Proyecto();
        ArrayList<Proyecto> result = instance.cargarProyectos();
        assertEquals("holaprueba", result.get(0).getNombreProyecto());
    }

    /**
     * Test of buscarCarpetas method, of class Proyecto.
     */
    @Test
    public void testBuscarCarpetas() {
        System.out.println("buscarCarpetas");
        String ruta = "C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\holaMundoo";
        Proyecto instance = new Proyecto();
        ArrayList<String> expResult = new ArrayList();
        expResult.add("holaMundoo");
        expResult.add("negocio");
        expResult.add("paquete");
        ArrayList<Carpeta> result = instance.buscarCarpetas(ruta);
        ArrayList<String> resultado = new ArrayList();
        resultado.add(result.get(0).getNombreCarpeta());
        resultado.add(result.get(1).getNombreCarpeta());
        resultado.add(result.get(2).getNombreCarpeta());
        assertEquals(expResult.toString(), resultado.toString());
    }

    /**
     * Test of buscarArchivos method, of class Proyecto.
     */
    @Test
    public void testBuscarArchivos() {
        System.out.println("buscarArchivos");
        String ruta = "C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\holaMundoParametros\\codigo\\holaMundoParametros";
        String paquete = "holaMundoParametros";
        String rutaClases = "C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\holaMundoParametros\\clases";
        Proyecto instance = new Proyecto();
        String expResult = "holaMundoParametros.java";
        ArrayList<Archivo> result = instance.buscarArchivos(ruta, paquete, rutaClases);
        assertEquals(expResult, result.get(0).getNombreArchivo());
    }

    /**
     * Test of leerArchivo method, of class Proyecto.
     */
    @Test
    public void testLeerArchivo() {
        System.out.println("leerArchivo");
        String ruta = "C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\prueba.cpp";
        Proyecto instance = new Proyecto();
        String expResult = "hola";
        String result = instance.leerArchivo(ruta);
        result = result.substring(0, 4);
        assertEquals(expResult, result);
    }

    /**
     * Test of cargarNuevoProyecto method, of class Proyecto.
     */
    @Test
    public void testCargarNuevoProyecto() {
        System.out.println("cargarNuevoProyecto");
        Proyecto proyecto = new Proyecto();
        proyecto.setNombreProyecto("holaMundoo");
        proyecto.setRutaProyecto("C:\\Users\\raymu\\Desktop\\ArchivosPruebas\\holaMundoo");
        proyecto.setLenguaje("java");
        Proyecto instance = new Proyecto();
        Proyecto result = instance.cargarNuevoProyecto(proyecto);
        assertEquals(proyecto.getNombreProyecto(), result.getNombreProyecto());
    }

    
}
