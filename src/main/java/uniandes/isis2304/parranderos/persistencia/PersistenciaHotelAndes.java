package uniandes.isis2304.parranderos.persistencia;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.JDODataStoreException;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
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



// TODO: Auto-generated Javadoc
/**
 * The Class PersistenciaHotelAndes.
 */
public class PersistenciaHotelAndes {

	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/** Logger para escribir la traza de la ejecuci�n. */
	private static Logger log = Logger.getLogger(PersistenciaHotelAndes.class.getName());

	/** Cadena para indicar el tipo de sentencias que se va a utilizar en una consulta. */
	public final static String SQL = "javax.jdo.query.SQL";

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/

	/** Atributo privado que es el �nico objeto de la clase - Patr�n SINGLETON. */
	private static PersistenciaHotelAndes instance;

	/** F�brica de Manejadores de persistencia, para el manejo correcto de las transacciones. */
	private PersistenceManagerFactory pmf;

	/** Arreglo de cadenas con los nombres de las tablas de la base de datos, en su orden: Secuenciador, tipoBebida, bebida, bar, bebedor, gustan, sirven y visitan. */
	private List <String> tablas;

	/** Atributo para el acceso a las sentencias SQL propias a PersistenciaParranderos. */
	private SQLUtil sqlUtil;

	/* ****************************************************************
	 * 			Atributos otrosss
	 *****************************************************************/

	/** The sql tipo persona. */
	private SQLTipoPersona sqlTipoPersona;

	/** The sql reservas de alojamiento. */
	private SQLReservasDeAlojamiento sqlReservasDeAlojamiento;

	/** The sql reservas de servicio. */
	private SQLReservasDeServicio sqlReservasDeServicio;

	/** The sql gastos. */
	private SQLGastos sqlGastos;

	/** The sql consultas. */
	private SQLConsultas sqlConsultas;

	/* ****************************************************************
	 * 			Métodos de manejador de persistencia
	 *****************************************************************/


	/**
	 * Constructor privado con valores por defecto - Patr�n SINGLETON.
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
	 * El constructor que recibe los nombres de las tablas en tableConfig
	 * @param tableConfig - Objeto Json con los nombres de las tablas y de la unidad de persistencia
	 */
	public PersistenciaHotelAndes (JsonObject tableConfig)
	{
		//instance = PersistenciaHotelAndes.getInstance(tableConfig);
	}

	/**
	 * Cierra la conexi�n con la base de datos.
	 */
	public void cerrarUnidadPersistencia ()
	{
		pmf.close ();
		instance = null;
	}

	/**
	 * Genera una lista con los nombres de las tablas de la base de datos.
	 *
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
	 * Crea los atributos de clases de apoyo SQL.
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
	 * Dar seq hotel andes.
	 *
	 * @return La cadena de caracteres con el nombre del secuenciador
	 */
	public String darSeqHotelAndes ()
	{
		return tablas.get (0);
	}

	/**
	 * Dar tabla tipo persona.
	 *
	 * @return La cadena de caracteres con el nombre de la tabla de TipoPersona
	 */
	public String darTablaTipoPersona ()
	{
		return tablas.get (1);
	}

	/**
	 * Nextval.
	 *
	 * @return the long
	 */
	private long nextval ()
	{
		long resp = sqlUtil.nextval (pmf.getPersistenceManager());
		log.trace ("Generando secuencia: " + resp);
		return resp;
	}

	/**
	 * Extrae el mensaje de la exception JDODataStoreException embebido en la Exception e, que da el detalle espec�fico del problema encontrado.
	 *
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
	 * Adiciona entradas al log de la aplicaci�n.
	 *
	 * @param nombre - El nombre del tipo de bebida
	 * @param id the id
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


	/**
	 * Registrar reserva de alojamiento.
	 *
	 * @param idHabitacion the id habitacion
	 * @param idHotel the id hotel
	 * @param fechallegadateorica the fechallegadateorica
	 * @param fechallegadareal the fechallegadareal
	 * @param fechasalidateorica the fechasalidateorica
	 * @param fechasalidareal the fechasalidareal
	 * @param plandeconsumo the plandeconsumo
	 * @param tipos the tipos
	 * @param idPersonas the id personas
	 * @return the reservas de alojamiento
	 * @throws Exception the exception
	 */
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

	/**
	 * Registrar reserva de alojamiento sin transaccion.
	 *
	 * @param idHabitacion the id habitacion
	 * @param idHotel the id hotel
	 * @param fechallegadateorica the fechallegadateorica
	 * @param fechallegadareal the fechallegadareal
	 * @param fechasalidateorica the fechasalidateorica
	 * @param fechasalidareal the fechasalidareal
	 * @param plandeconsumo the plandeconsumo
	 * @param tipos the tipos
	 * @param idPersonas the id personas
	 * @return the reservas de alojamiento
	 * @throws Exception the exception
	 */
	public ReservasDeAlojamiento registrarReservaDeAlojamientoSinTransaccion( long idHabitacion,long idHotel, String fechallegadateorica,String fechallegadareal, String fechasalidateorica, String fechasalidareal, long plandeconsumo, String[] tipos,long[]idPersonas) throws Exception
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		long id = nextval();
		try
		{
			sqlReservasDeAlojamiento.registrarReservaDeAlojamiento(pm,id, idHabitacion, idHotel, fechallegadateorica, fechallegadareal, fechasalidateorica, fechasalidareal, plandeconsumo,tipos,idPersonas);
			return new ReservasDeAlojamiento (id, idHabitacion, idHotel, fechallegadateorica, fechallegadareal, fechasalidateorica, fechasalidareal, plandeconsumo);
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			System.out.println( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );
			throw e;
		}
	}

	/**
	 * Registrar reserva de servicio sin transaccion.
	 *
	 * @param descripcion the descripcion
	 * @param fechaFin the fecha fin
	 * @param fechaInicio the fecha inicio
	 * @param servicioAdicional the servicio adicional
	 * @param idUsuario the id usuario
	 * @param tipoIdUsuario the tipo id usuario
	 * @return the reserva servicio
	 * @throws Exception the exception
	 */
	public ReservaServicio registrarReservaDeServicioSinTransaccion(String descripcion, String fechaFin, String fechaInicio, long servicioAdicional,long idUsuario, String tipoIdUsuario) throws Exception
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		long id = nextval();
		try
		{
			sqlReservasDeServicio.registrarReservaDeServicio(pm, descripcion,  fechaFin,  fechaInicio,  id,  servicioAdicional, idUsuario,  tipoIdUsuario);



			log.trace ("Insercion de reserva de alojamiento: " + "con id: " + id + " tuplas insertadas");

			return new ReservaServicio ( descripcion,  fechaFin,  fechaInicio,  id,  servicioAdicional, idUsuario,  tipoIdUsuario);
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			System.out.println( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );

			throw e;
		}
	}

	/**
	 * Registrar reserva de servicio.
	 *
	 * @param descripcion the descripcion
	 * @param fechaFin the fecha fin
	 * @param fechaInicio the fecha inicio
	 * @param servicioAdicional the servicio adicional
	 * @param idUsuario the id usuario
	 * @param tipoIdUsuario the tipo id usuario
	 * @return the reserva servicio
	 * @throws Exception the exception
	 */
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

	/**
	 * Check in.
	 *
	 * @param idReserva the id reserva
	 * @param fechaActual the fecha actual
	 * @throws Exception the exception
	 */
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

	/**
	 * Registrar consumo.
	 *
	 * @param idUsuario the id usuario
	 * @param idServicio the id servicio
	 * @param fecha the fecha
	 * @param pagado the pagado
	 * @return the gasto
	 * @throws Exception the exception
	 */
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


	/**
	 * Cuenta A pagar.
	 *
	 * @param idReserva the id reserva
	 * @return the int
	 * @throws Exception the exception
	 */
	public int cuentaAPagar (long idReserva, int valor ) throws Exception
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		long id = nextval();
		try
		{
			tx.begin();
			int resp = sqlReservasDeAlojamiento.precioAPagar( pm, idReserva, valor );
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
	 * Check out.
	 *
	 * @param idReserva the id reserva
	 * @param fechaActual the fecha actual
	 * @throws Exception the exception
	 */
	public void checkOut (long idReserva, String fechaActual) throws Exception
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		long id = nextval();
		try
		{
			tx.begin();
			sqlReservasDeAlojamiento.checkOut(pm, idReserva, fechaActual);
			tx.commit();

			log.trace ("Inserción de reserva de alojamiento: con id: " + id + " tuplas insertadas");

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

	/**
	 * Rfc1.
	 *
	 * @param idHotel the id hotel
	 * @param fechaInicio the fecha inicio
	 * @param fechaFin the fecha fin
	 * @param fechaActual the fecha actual
	 * @return the list
	 * @throws Exception the exception
	 */
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

	/**
	 * Rfc2.
	 *
	 * @param idHotel the id hotel
	 * @param fechaInicio the fecha inicio
	 * @param fechaFin the fecha fin
	 * @return the list
	 * @throws Exception the exception
	 */
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

	/**
	 * Rfc3.
	 *
	 * @param idHotel the id hotel
	 * @param fechaActual the fecha actual
	 * @return the list
	 * @throws Exception the exception
	 */
	public List<ClaseAsistente> RFC3(long idHotel, String fechaActual) throws Exception
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
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

	/**
	 * Rfc4precio.
	 *
	 * @param precio1 the precio 1
	 * @param precio2 the precio 2
	 * @return the list
	 */
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

	/**
	 * Rfc4fecha.
	 *
	 * @param precio1 the precio 1
	 * @param precio2 the precio 2
	 * @return the list
	 */
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

	/**
	 * Rfc4tipo.
	 *
	 * @param precio1 the precio 1
	 * @return the list
	 */
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

	/**
	 * Rfc4consumo.
	 *
	 * @param consumo the consumo
	 * @param fecha1 the fecha 1
	 * @param fecha2 the fecha 2
	 * @return the list
	 */
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

	/**
	 * Rfc5.
	 *
	 * @param idUsuario the id usuario
	 * @param tipoUsuario the tipo usuario
	 * @param fecha1 the fecha 1
	 * @param fecha2 the fecha 2
	 * @return the list
	 */
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
	 * Rfc12.
	 *
	 * @param idHotel the id hotel
	 * @param planDeConsumo the plan de consumo
	 * @param fecha1 the fecha 1
	 * @param fecha2 the fecha 2
	 * @param habitaciones the habitaciones
	 * @param cantHabitaciones the cant habitaciones
	 * @param servicios the servicios
	 * @return the array list
	 * @throws Exception the exception
	 */
	public ArrayList<Integer> RF12(int idHotel, int planDeConsumo, String fecha1, String fecha2, ArrayList<Integer> habitaciones, ArrayList<Integer> cantHabitaciones, ArrayList<Integer> servicios) throws Exception
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		long id = nextval();
		try
		{
			tx.begin();
			ArrayList<Integer> resp = sqlConsultas.RF12(pm, idHotel, planDeConsumo, fecha1, fecha2, habitaciones, cantHabitaciones, servicios);
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


			System.out.println("Exception yeiiiii!!!!");
			if (tx.isActive())
				tx.rollback();

			pm.close();
			throw e;
		}
	}

	/**
	 * Rfc12b.
	 *
	 * @param idReserva the id reserva
	 * @param tipos the tipos
	 * @param idPersonas the id personas
	 * @throws Exception the exception
	 */
	public void RF12B( int idReserva, String[] tipos,long[]idPersonas) throws Exception
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		long id = nextval();
		try
		{
			tx.begin();
			sqlConsultas.RF12B(pm, idReserva, tipos, idPersonas);
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

	/**
	 * Rfc13.
	 *
	 * @param habitaciones the habitaciones
	 * @param servicios the servicios
	 * @throws Exception the exception
	 */
	public void RF13( ArrayList<Integer> habitaciones,ArrayList<Integer> servicios) throws Exception
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		long id = nextval();
		try
		{
			tx.begin();
			sqlConsultas.RF13(pm, habitaciones, servicios);
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






	/**
	 * RF 14 registar cierre de convencion.
	 * @param idReservas the id reservas
	 * @param darFechaDeHoy the dar fecha de hoy
	 * @throws Exception the exception
	 */
	public void RF14( ArrayList<Integer> idHabitaciones, ArrayList<Integer> idReservasServicios, String fechaActual ) throws Exception
	{
		// Para hacer checkout de las reservas de alojamiento
		for ( int i = 0; i < idHabitaciones.size(); i++ )
			checkOut( idHabitaciones.get(i), fechaActual );

		// Para hacer checkout de los servicios
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		long id = nextval();
		try
		{
			tx.begin();

			for (Integer reserva : idReservasServicios)
			{
				Query q = pm.newQuery(SQL, "UPDATE RESERVAS_DE_ALOJAMIENTO SET FECHASALIDAREAL = '"+fechaActual+"' WHERE ID = "+reserva);
				q.setParameters(fechaActual,reserva);
				q.executeUnique();
			}

			tx.commit();

			if (tx.isActive())
				tx.rollback();

			pm.close();

		}
		catch (Exception e)
		{
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			System.out.println( "Exception : " + e.getMessage() + "\n" + darDetalleException(e) );


			if (tx.isActive())
				tx.rollback();

			pm.close();
			throw e;
		}
	}

	public void RF14B( int idReserva, String fechaActual )
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		long id = nextval();
		try
		{
			tx.begin();

			Query q = pm.newQuery(SQL, "UPDATE RESERVAS_DE_ALOJAMIENTO SET FECHASALIDAREAL = '"+fechaActual+"' WHERE ID = "+idReserva);
			q.setParameters(fechaActual,idReserva);
			q.executeUnique();

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

	/**
	 * Rfc13.
	 *
	 * @param habitaciones the habitaciones
	 * @param servicios the servicios
	 * @throws Exception the exception
	 */
	public void RF15( int idHotel,String fecha1, String fecha2, ArrayList<Integer> habitaciones,ArrayList<Integer> servicios) throws Exception
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		long id = nextval();
		try
		{
			tx.setIsolationLevel("serializable");
			tx.begin();

			System.out.println(tx.getIsolationLevel());
			sqlConsultas.RF15(pm, idHotel, fecha1, fecha2, habitaciones, servicios);
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

	public void RF16(int idHotel,ArrayList<Integer> habitaciones,ArrayList<Integer> servicios) throws Exception
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		long id = nextval();
		try
		{
			tx.setIsolationLevel("serializable");
			tx.begin();

			System.out.println(tx.getIsolationLevel());
			sqlConsultas.RF16(pm, idHotel, habitaciones, servicios);
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

	public int[] RFC6( char criterio, int tipoHabitacion ) throws Exception
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		long id = nextval();
		try
		{
			tx.begin();
						
			int[] demandaMes = sqlReservasDeAlojamiento.darDemandaMaxYMin( pm, tipoHabitacion );
			// Mes con mayor demanda:
			int masDemanda = demandaMes[0];

			// Mes con mayor ingreso:
			int mayorIngreso = sqlReservasDeAlojamiento.darMesMayorConsumo( pm, tipoHabitacion );
			
			// Mes con menor demanda:
			int menosDemanda = demandaMes[1];			
			
			tx.commit();

			if (tx.isActive())
				tx.rollback();

			pm.close();
			
			return new int[] { masDemanda, mayorIngreso, menosDemanda };
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
	
	public List<ClaseAsistente> RFC7( )
	{
		ArrayList<ClaseAsistente> array = new ArrayList<ClaseAsistente>();
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		long id = nextval();
		try
		{
			tx.begin();
			List<ClaseAsistente> resp = sqlConsultas.RFC7(pm);

			for (ClaseAsistente act : resp)
			{

				// Parsing the date
				LocalDate dateBefore = LocalDate.parse( act.getFECHALLEGADATEORICA() );
				LocalDate dateAfter = LocalDate.parse( act.getFECHASALIDATEORICA() );

				// Calculating number of days in between
				long noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);

				act.aumentarDiasEstadia( noOfDaysBetween );				
			}
			
			for (int i = 0; i < resp.size(); i++)
			{
				ClaseAsistente actual = resp.get(i);
				
				if( actual.getDiasEstadia() > 14 && !array.contains(actual) )
					array.add( actual );
			}

			tx.commit();

			if (tx.isActive())
				tx.rollback();

			pm.close();
			return array;

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
	
	public ArrayList<String> RFC8( )
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		long id = nextval();
		try
		{
			tx.begin();
			ArrayList<String> resp = sqlConsultas.RFC8(pm);
			
			
			
			/* per.id, per.tipoidentificacion as idtipoidentificacion, per.nombre, al.fechaLlegadaTeorica, al.fechaSalidaTeorica\n"
			*/
			
			tx.commit();

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
	 * Gets the single instance of PersistenciaHotelAndes.
	 *
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


}
