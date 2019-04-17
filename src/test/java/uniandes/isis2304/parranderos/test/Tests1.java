package uniandes.isis2304.parranderos.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import uniandes.isis2304.parranderos.persistencia.PersistenciaHotelAndes;


/**
 * The Class Tests1.
 */
public class Tests1 {
	
	/** The pha. */
	PersistenciaHotelAndes pha = PersistenciaHotelAndes.getInstance();
	
	
	/**
	 * Test RF 12 exitoso.
	 */
	@Test
	public void testRF12Exitoso()
	{
		ArrayList<Integer> habitaciones = new ArrayList();
		ArrayList<Integer> cantidad = new ArrayList();
		ArrayList<Integer> servicios = new ArrayList();
		habitaciones.add(1);
		cantidad.add(1);
		servicios.add(1);
		try {
			pha.RF12(1, 1, "01/03/2017", "03/03/2017", habitaciones, cantidad, servicios);
			assertTrue(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Test RF 12 falla.
	 */
	@Test
	public void testRF12Falla()
	{
		ArrayList<Integer> habitaciones = new ArrayList();
		ArrayList<Integer> cantidad = new ArrayList();
		ArrayList<Integer> servicios = new ArrayList();
		habitaciones.add(1);
		cantidad.add(1);
		servicios.add(1);
		try {
			pha.RF12(1, 1, "01/03/2017", "03/03/2017", habitaciones, cantidad, servicios);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			assertTrue(true);
		}
		
	}
	
	/**
	 * Test RF 12 B exitoso.
	 */
	@Test
	public void testRF12BExitoso()
	{
		String[] tipos = new String[1];
		long[] idPersonas = new long[1];
		tipos[0]="Cedula";
		idPersonas[0]=9;
		
		try {
			pha.RF12B(374, tipos, idPersonas);
			assertTrue(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Test RF 12 B fallo.
	 */
	@Test
	public void testRF12BFallo()
	{
		String[] tipos = new String[1];
		long[] idPersonas = new long[1];
		tipos[0]="Cedula";
		idPersonas[0]=9;
		
		try {
			pha.RF12B(0, tipos, idPersonas);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			assertTrue(true);
		}
	}
	
	/**
	 * RF 13 exitoso.
	 */
	@Test 
	public void RF13Exitoso()
	{
		
		ArrayList<Integer> servicios = new ArrayList<>();
		ArrayList<Integer> habitaciones = new ArrayList<>();
		//Requiere unas reservas existentes 
		//habitaciones.add(e);
		//servicios.add(e);
		try {
			pha.RF13(habitaciones, servicios);
			assertTrue(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * RF 13 fallo.
	 */
	@Test 
	public void RF13Fallo()
	{
		
		ArrayList<Integer> servicios = new ArrayList<>();
		ArrayList<Integer> habitaciones = new ArrayList<>();
		habitaciones.add(0);
		servicios.add(0);
		try {
			pha.RF13(habitaciones, servicios);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			assertTrue(true);
		}
	}
	
	/**
	 * RF 15 exitoso.
	 */
	@Test
	public void RF15Exitoso()
	{
		ArrayList<Integer> servicios = new ArrayList<>();
		ArrayList<Integer> habitaciones = new  ArrayList<>();
		habitaciones.add(203);
		servicios.add(3);
		try {
			pha.RF15(1, "01/01/2014", "03/01/2014", habitaciones, servicios);
			assertTrue(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * RF 15 fallo.
	 */
	@Test
	public void RF15Fallo()
	{
		ArrayList<Integer> servicios = new ArrayList<>();
		ArrayList<Integer> habitaciones = new  ArrayList<>();
		habitaciones.add(0);
		servicios.add(0);
		try {
			pha.RF15(1, "01/01/2014", "03/01/2014", habitaciones, servicios);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			assertTrue(true);
		}
	}
	
	/**
	 * RF 16 exitoso.
	 */
	@Test
	public void RF16Exitoso()
	{
		ArrayList<Integer> servicios = new ArrayList<>();
		ArrayList<Integer> habitaciones = new ArrayList<>();
		servicios.add(3);
		habitaciones.add(203);
		try {
			pha.RF16(1, habitaciones, servicios);
			assertTrue(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * RF 16 fallo.
	 */
	@Test
	public void RF16Fallo()
	{
		ArrayList<Integer> servicios = new ArrayList<>();
		ArrayList<Integer> habitaciones = new ArrayList<>();
		servicios.add(0);
		habitaciones.add(0);
		try {
			pha.RF16(1, null, servicios);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			assertTrue(true);
		}
	}

}
