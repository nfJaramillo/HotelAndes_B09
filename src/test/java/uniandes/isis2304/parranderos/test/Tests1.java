package uniandes.isis2304.parranderos.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import uniandes.isis2304.parranderos.persistencia.PersistenciaHotelAndes;

public class Tests1 {
	
	PersistenciaHotelAndes pha = PersistenciaHotelAndes.getInstance();
	
//	@Test
//	public void testRF12Exitoso()
//	{
//		ArrayList<Integer> habitaciones = new ArrayList();
//		ArrayList<Integer> cantidad = new ArrayList();
//		ArrayList<Integer> servicios = new ArrayList();
//		habitaciones.add(1);
//		cantidad.add(1);
//		servicios.add(1);
//		try {
//			pha.RF12(1, 1, "01/03/2017", "03/03/2017", habitaciones, cantidad, servicios);
//			assertTrue(true);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
//	@Test
//	public void testRF12Falla()
//	{
//		ArrayList<Integer> habitaciones = new ArrayList();
//		ArrayList<Integer> cantidad = new ArrayList();
//		ArrayList<Integer> servicios = new ArrayList();
//		habitaciones.add(1);
//		cantidad.add(1);
//		servicios.add(1);
//		try {
//			pha.RF12(1, 1, "01/03/2017", "03/03/2017", habitaciones, cantidad, servicios);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			assertTrue(true);
//		}
//		
//	}
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
	@Test

}
