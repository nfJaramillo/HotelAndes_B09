package uniandes.isis2304.parranderos.persistencia;

import java.sql.Date;

import java.util.LinkedList;
import java.util.List;

import javax.jdo.JDODataStoreException;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import uniandes.isis2304.parranderos.negocio.Gasto;
import uniandes.isis2304.parranderos.negocio.ReservaServicio;
import uniandes.isis2304.parranderos.negocio.ReservasDeAlojamiento;
import uniandes.isis2304.parranderos.negocio.ServicioAdicional;
import uniandes.isis2304.parranderos.negocio.TipoPersona;



public class PersistenciaHotelAndes {
	
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecuci�n
	 */
	private static Logger log = Logger.getLogger(PersistenciaHotelAndes.class.getName());
	
	/**
	 * Cadena para indicar el tipo de sentencias que se va a utilizar en una consulta
	 */
	public final static String SQL = "javax.jdo.query.SQL";

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	
	/**
	 * Atributo privado que es el �nico objeto de la clase - Patr�n SINGLETON
	 */
	private static PersistenciaHotelAndes instance;
	
	/**
	 * F�brica de Manejadores de persistencia, para el manejo correcto de las transacciones
	 */
	private PersistenceManagerFactory pmf;
	
	/**
	 * Arreglo de cadenas con los nombres de las tablas de la base de datos, en su orden:
	 * Secuenciador, tipoBebida, bebida, bar, bebedor, gustan, sirven y visitan
	 */
	private List <String> tablas;
	
	/**
	 * Atributo para el acceso a las sentencias SQL propias a PersistenciaParranderos
	 */
	private SQLUtil sqlUtil;
	
	/* ****************************************************************
	 * 			Atributos otrosss
	 *****************************************************************/
	
	private SQLTipoPersona sqlTipoPersona;
	
	private SQLReservasDeAlojamiento sqlReservasDeAlojamiento;
	
	private SQLReservasDeServicio sqlReservasDeServicio;
	
	private SQLGastos sqlGastos;
	
	private SQLConsultas sqlConsultas;
	
	/* ****************************************************************
	 * 			M�todos del MANEJADOR DE PERSISTENCIA
	 *****************************************************************/
	
	
	/**
	 * Constructor privado con valores por defecto - Patr�n SINGLETON
	 */
	private PersistenciaHotelAndes ()
	{
		pmf = JDOHelper.getPersistenceManagerFactory("HotelAndes");		
		crearClasesSQL ();
		
		// Define los nombres por defecto de las tablas de la base de datos
		tablas = new LinkedList<String> ();
		tablas.add ("HotelAndes_sequence");
		tablas.add ("TIPO_DE_PERSONA");
		tablas.add("RESERVAS_DE_ALOJAMIENTO");
		tablas.add("RESERVAS_DE_SERVICIO");
		tablas.add("GASTOS");
}
	
	/**
	 * Cierra la conexi�n con la base de datos
	 */
	public void cerrarUnidadPersistencia ()
	{
		pmf.close ();
		instance = null;
	}
	
	/**
	 * Genera una lista con los nombres de las tablas de la base de datos
	 * @param tableConfig - El objeto Json con los nombres de las tablas
	 * @return La lista con los nombres del secuenciador y de las tablas
	 */
	private List <String> leerNombresTablas (JsonObject tableConfig)
	{
		JsonArray nombres = tableConfig.getAsJsonArray("tablas") ;

		List <String> resp = new LinkedList <String> ();
		for (JsonElement nom : nombres)
		{
			resp.add (nom.getAsString ());
		}
		
		return resp;
	}
	
	
	/**
	 * Crea los atributos de clases de apoyo SQL
	 */
	private void crearClasesSQL ()
	{
		sqlTipoPersona = new SQLTipoPersona(this);
	
		sqlUtil = new SQLUtil(this);
		
		sqlReservasDeAlojamiento = new SQLReservasDeAlojamiento(this);
		
		sqlReservasDeServicio = new SQLReservasDeServicio(this);
		
		sqlGastos = new SQLGastos(this);
		
		sqlConsultas = new SQLConsultas(this);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre del secuenciador 
	 */
	public String darSeqHotelAndes ()
	{
		return tablas.get (0);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de TipoPersona
	 */
	public String darTablaTipoPersona ()
	{
		return tablas.get (1);
	}
	
	private long nextval ()
	{
        long resp = sqlUtil.nextval (pmf.getPersistenceManager());
        log.trace ("Generando secuencia: " + resp);
        return resp;
    }
	
	/**
	 * Extrae el mensaje de la exception JDODataStoreException embebido en la Exception e, que da el detalle espec�fico del problema encontrado
	 * @param e - La excepci�n que ocurrio
	 * @return El mensaje de la excepci�n JDO
	 */
	private String darDetalleException(Exception e) 
	{
		String resp = "";
		if (e.getClass().getName().equals("javax.jdo.JDODataStoreException"))
		{
			JDODataStoreException je = (javax.jdo.JDODataStoreException) e;
			return je.getNestedExceptions() [0].getMessage();
		}
		return resp;
	}
	
	/* ****************************************************************
	 * 			M�todos para manejar los TIPOS DE PERSONA
	 *****************************************************************/
	
	/**
	 * M�todo que inserta, de manera transaccional, una tupla en la tabla TipoBebida
	 * Adiciona entradas al log de la aplicaci�n
	 * @param nombre - El nombre del tipo de bebida
	 * @return El objeto TipoBebida adicionado. null si ocurre alguna Excepci�n
	 */
	public TipoPersona registrarRolDeUsuario(String nombre, int id)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            sqlTipoPersona.registrarRolDeUsuario(pm, id, nombre);
            tx.commit();
            
            log.trace ("Inserci�n de tipo persona: " + nombre + "con id: " + id + " tuplas insertadas");
            
            return new TipoPersona (id,nombre);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	System.out.println( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	
	public ReservasDeAlojamiento registrarReservaDeAlojamiento( long idHabitacion,long idHotel, String fechallegadateorica,String fechallegadareal, String fechasalidateorica, String fechasalidareal, long plandeconsumo, String[] tipos,long[]idPersonas) throws Exception
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        long id = nextval();
        try
        {
            tx.begin();
            sqlReservasDeAlojamiento.registrarReservaDeAlojamiento(pm,id, idHabitacion, idHotel, fechallegadateorica, fechallegadareal, fechasalidateorica, fechasalidareal, plandeconsumo,tipos,idPersonas);
            tx.commit();
            
            log.trace ("Insercion de reserva de alojamiento: " + "con id: " + id + " tuplas insertadas");
            
            if (tx.isActive())
                tx.rollback();
            
            pm.close();
            return new ReservasDeAlojamiento (id, idHabitacion, idHotel, fechallegadateorica, fechallegadareal, fechasalidateorica, fechasalidareal, plandeconsumo);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	System.out.println( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
        	
        	
            if (tx.isActive())
                tx.rollback();
            
            pm.close();
        	throw e;
        }
	}
	
	public ReservaServicio registrarReservaDeServicio(String descripcion, String fechaFin, String fechaInicio, long servicioAdicional,long idUsuario, String tipoIdUsuario) throws Exception
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        long id = nextval();
        try
        {
            tx.begin();
            sqlReservasDeServicio.registrarReservaDeServicio(pm, descripcion,  fechaFin,  fechaInicio,  id,  servicioAdicional, idUsuario,  tipoIdUsuario);
            tx.commit();
            
            log.trace ("Insercion de reserva de alojamiento: " + "con id: " + id + " tuplas insertadas");
            
            if (tx.isActive())
                tx.rollback();
            
            pm.close();
            return new ReservaServicio ( descripcion,  fechaFin,  fechaInicio,  id,  servicioAdicional, idUsuario,  tipoIdUsuario);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	System.out.println( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
        	
        	
            if (tx.isActive())
                tx.rollback();
            
            pm.close();
        	throw e;
        }
	}
	
	public void checkIn(Long idReserva, String fechaActual) throws Exception
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            sqlReservasDeAlojamiento.checkIn(pm, idReserva, fechaActual);
            tx.commit();
            
            log.trace ("Insercion de reserva de alojamiento: " + "con id: " + idReserva + " tuplas insertadas");
            
            if (tx.isActive())
                tx.rollback();
            
            pm.close();
            
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	System.out.println( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
        	
        	
            if (tx.isActive())
                tx.rollback();
            
            pm.close();
        	throw e;
        }
	}
	
	public Gasto registrarConsumo( long idUsuario, long idServicio, String fecha, int pagado) throws Exception
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        long id = nextval();
        try
        {
            tx.begin();
            double precio = sqlGastos.registrarGasto(pm, nextval(),  idUsuario,  idServicio,  fecha,  pagado);
            tx.commit();
            
            log.trace ("Insercion de reserva de alojamiento: " + "con id: " + id + " tuplas insertadas");
            
            if (tx.isActive())
                tx.rollback();
            
            pm.close();
            return new Gasto( fecha,  (int) id,  pagado,  precio,  idServicio,  idUsuario);
            
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	System.out.println( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
        	
        	
            if (tx.isActive())
                tx.rollback();
            
            pm.close();
        	throw e;
        }
	}
	
	
	public int cuentaAPagar (long idReserva) throws Exception
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        long id = nextval();
        try
        {
            tx.begin();
            int resp = sqlReservasDeAlojamiento.precioAPagar(pm, idReserva);
            tx.commit();
            
            log.trace ("Insercion de reserva de alojamiento: " + "con id: " + id + " tuplas insertadas");
            
            if (tx.isActive())
                tx.rollback();
            
            pm.close();
           return resp;
            
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	System.out.println( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
        	
        	
            if (tx.isActive())
                tx.rollback();
            
            pm.close();
        	throw e;
        }
	}
	public void checkOut (long idReserva, String fechaActual) throws Exception
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        long id = nextval();
        try
        {
            tx.begin();
          sqlReservasDeAlojamiento. checkOut(pm, idReserva, fechaActual);
            tx.commit();
            
            log.trace ("Insercion de reserva de alojamiento: " + "con id: " + id + " tuplas insertadas");
            
            if (tx.isActive())
                tx.rollback();
            
            pm.close();
            
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	System.out.println( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
        	
        	
            if (tx.isActive())
                tx.rollback();
            
            pm.close();
        	throw e;
        }
	}
	
	public List<ClaseAsistente> RFC1 (long idHotel, String fechaInicio, String fechaFin, String fechaActual) throws Exception
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        long id = nextval();
        try
        {
            tx.begin();
            List<ClaseAsistente> resp = sqlConsultas.RFC1(pm, idHotel, fechaInicio, fechaFin, fechaActual);
            tx.commit();
            
            log.trace ("Insercion de reserva de alojamiento: " + "con id: " + id + " tuplas insertadas");
            
            if (tx.isActive())
                tx.rollback();
            
            pm.close();
            return resp;
            
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	System.out.println( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
        	
        	
            if (tx.isActive())
                tx.rollback();
            
            pm.close();
        	throw e;
        }
	}
	
	public List<ClaseAsistente> RFC2 (long idHotel, String fechaInicio, String fechaFin) throws Exception
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        long id = nextval();
        try
        {
            tx.begin();
            List<ClaseAsistente> resp = sqlConsultas.RFC2(pm, idHotel, fechaInicio, fechaFin);
            tx.commit();
            
            log.trace ("Insercion de reserva de alojamiento: " + "con id: " + id + " tuplas insertadas");
            
            if (tx.isActive())
                tx.rollback();
            
            pm.close();
            return resp;
            
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	System.out.println( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
        	
        	
            if (tx.isActive())
                tx.rollback();
            
            pm.close();
        	throw e;
        }
	}
	
	public List<ClaseAsistente> RFC3(long idHotel, String fechaActual) throws Exception
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        long id = nextval();
        try
        {
            tx.begin();
            List<ClaseAsistente> resp = sqlConsultas.RFC3(pm, idHotel, fechaActual);
            tx.commit();
            
            log.trace ("Insercion de reserva de alojamiento: " + "con id: " + id + " tuplas insertadas");
            
            if (tx.isActive())
                tx.rollback();
            
            pm.close();
            return resp;
            
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	System.out.println( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
        	
        	
            if (tx.isActive())
                tx.rollback();
            
            pm.close();
        	throw e;
        }
	}
	
	public List<ClaseAsistente> RFC4PRECIO (int precio1, int precio2)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        long id = nextval();
        try
        {
            tx.begin();
            List<ClaseAsistente> resp = sqlConsultas.RFC4PRECIO(pm, precio1, precio2);	
            tx.commit();
            
            log.trace ("Insercion de reserva de alojamiento: " + "con id: " + id + " tuplas insertadas");
            
            if (tx.isActive())
                tx.rollback();
            
            pm.close();
            return resp;
            
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	System.out.println( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
        	
        	
            if (tx.isActive())
                tx.rollback();
            
            pm.close();
        	throw e;
        }
	}
	public List<ClaseAsistente> RFC4FECHA (String precio1, String precio2)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        long id = nextval();
        try
        {
            tx.begin();
            List<ClaseAsistente> resp = sqlConsultas.RFC4FECHA(pm, precio1, precio2);	
            tx.commit();
            
            log.trace ("Insercion de reserva de alojamiento: " + "con id: " + id + " tuplas insertadas");
            
            if (tx.isActive())
                tx.rollback();
            
            pm.close();
            return resp;
            
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	System.out.println( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
        	
        	
            if (tx.isActive())
                tx.rollback();
            
            pm.close();
        	throw e;
        }
	}
	
	public List<ClaseAsistente> RFC4TIPO (String precio1)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        long id = nextval();
        try
        {
            tx.begin();
            List<ClaseAsistente> resp = sqlConsultas.RFC4TIPO(pm, precio1);	
            tx.commit();
            
            log.trace ("Insercion de reserva de alojamiento: " + "con id: " + id + " tuplas insertadas");
            
            if (tx.isActive())
                tx.rollback();
            
            pm.close();
            return resp;
            
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	System.out.println( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
        	
        	
            if (tx.isActive())
                tx.rollback();
            
            pm.close();
        	throw e;
        }
	}
	
	public List<ClaseAsistente> RFC4CONSUMO(int consumo, String fecha1, String fecha2)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        long id = nextval();
        try
        {
            tx.begin();
            List<ClaseAsistente> resp = sqlConsultas.RFC4CONSUMO(pm, consumo, fecha1, fecha2)	;
            tx.commit();
            
            log.trace ("Insercion de reserva de alojamiento: " + "con id: " + id + " tuplas insertadas");
            
            if (tx.isActive())
                tx.rollback();
            
            pm.close();
            return resp;
            
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	System.out.println( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
        	
        	
            if (tx.isActive())
                tx.rollback();
            
            pm.close();
        	throw e;
        }
	}
	
	public List<ClaseAsistente> RFC5(int idUsuario, String tipoUsuario, String fecha1, String fecha2)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        long id = nextval();
        try
        {
            tx.begin();
            List<ClaseAsistente> resp = sqlConsultas.RFC5(pm, idUsuario, tipoUsuario, fecha1, fecha2);
            tx.commit();
            
            log.trace ("Insercion de reserva de alojamiento: " + "con id: " + id + " tuplas insertadas");
            
            if (tx.isActive())
                tx.rollback();
            
            pm.close();
            return resp;
            
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	System.out.println( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
        	
        	
            if (tx.isActive())
                tx.rollback();
            
            pm.close();
        	throw e;
        }
	}
	
	
	/**
	 * @return Retorna el �nico objeto PersistenciaParranderos existente - Patron SINGLETON
	 */
	public static PersistenciaHotelAndes getInstance ()
	{
		if (instance == null)
		{
			instance = new PersistenciaHotelAndes ();
		}
		return instance;
	}
	
	//public 

}
