package uniandes.isis2304.parranderos.persistencia;



import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;


import uniandes.isis2304.parranderos.negocio.Persona;

// TODO: Auto-generated Javadoc
/**
 * The Class SQLReservasDeAlojamiento.
 */
public class SQLReservasDeAlojamiento {


	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/** Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos Se renombra ac� para facilitar la escritura de las sentencias. */
	private final static String SQL = PersistenciaHotelAndes.SQL;

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/** El manejador de persistencia general de la aplicaci�n. */
	private PersistenciaHotelAndes pp;

	/* ****************************************************************
	 * 			Metodos
	 *****************************************************************/

	/**
	 * Constructor.
	 *
	 * @param pp - El Manejador de persistencia de la aplicaci�n
	 */
	public SQLReservasDeAlojamiento (PersistenciaHotelAndes pp)
	{
		this.pp = pp;
	}


	/**
	 * Registrar reserva de alojamiento.
	 *
	 * @param pm the pm
	 * @param id the id
	 * @param idHabitacion the id habitacion
	 * @param idHotel the id hotel
	 * @param fechallegadateorica the fechallegadateorica
	 * @param fechallegadareal the fechallegadareal
	 * @param fechasalidateorica the fechasalidateorica
	 * @param fechasalidareal the fechasalidareal
	 * @param plandeconsumo the plandeconsumo
	 * @param tipos the tipos
	 * @param idPersonas the id personas
	 * @throws Exception the exception
	 */
	public void registrarReservaDeAlojamiento (PersistenceManager pm, long id, long idHabitacion,long idHotel, String fechallegadateorica,String fechallegadareal, String fechasalidateorica, String fechasalidareal, long plandeconsumo,String[] tipos,long[]idPersonas) throws Exception 
	{
		Query a = pm.newQuery(SQL, "SELECT * FROM RESERVAS_DE_ALOJAMIENTO WHERE IDHABITACION = "
				+ idHabitacion + " AND ( ('"+fechallegadateorica + "' between FECHALLEGADATEORICA and FECHASALIDATEORICA)"
				+ " OR ('"+ fechasalidateorica + "' BETWEEN FECHALLEGADATEORICA and FECHASALIDATEORICA) or"
				+ " ( FECHALLEGADATEORICA between '" + fechallegadateorica + "' and '" + fechasalidateorica + "') OR"
				+ " ( FECHASALIDATEORICA between '" + fechallegadateorica + "' AND '" + fechasalidateorica + "'))" );
		List lista= a.executeList();

		if( !lista.isEmpty() )
			throw new Exception("Ya existe una reserva para la misma habitacion en ese periodo de tiempo");

		Query q = pm.newQuery(SQL, "INSERT INTO RESERVAS_DE_ALOJAMIENTO" + "(id,IDHABITACION,idhotel,fechallegadateorica,fechallegadareal,fechasalidateorica,fechasalidareal,plandeconsumo) values (?, ?,?,?,?,?,?,?)");
		q.setParameters(id, idHabitacion, idHotel, fechallegadateorica, fechallegadareal, fechasalidateorica, fechasalidareal, plandeconsumo);
		q.executeUnique();

		if(idPersonas!=null)
		{
			for (int i = 0; i < idPersonas.length; i++)
			{
				Query r = pm.newQuery(SQL, "insert into RESERVAS_DE_CLIENTES"+ "(IDRESERVA,IDUSUARIO, tipoidusuario) values (?,?,?)");
				r.setParameters(id,idPersonas[i],tipos[i]);
				r.executeUnique();
			}
		}
	}

	/**
	 * Check in.
	 *
	 * @param pm the pm
	 * @param idReserva the id reserva
	 * @param fechaActual the fecha actual
	 * @throws Exception the exception
	 */
	public void checkIn (PersistenceManager pm, Long idReserva, String fechaActual) throws Exception
	{
		Query a = pm.newQuery(SQL, "SELECT * FROM RESERVAS_DE_ALOJAMIENTO WHERE ID = "+idReserva);
		List lista =a.executeList();

		if( lista.isEmpty() )
			throw new Exception("No existe una reserva con dicho id");

		Query q = pm.newQuery(SQL, "UPDATE RESERVAS_DE_ALOJAMIENTO SET FECHALLEGADAREAL = '"+fechaActual+"' WHERE ID = "+idReserva);
		q.setParameters(fechaActual,idReserva);
		q.executeUnique()  ;
	}

	/**
	 * Precio A pagar.
	 *
	 * @param pm the pm
	 * @param idReserva the id reserva
	 * @return the int
	 * @throws Exception the exception
	 */
	public int precioAPagar (PersistenceManager pm, long idReserva, int valor ) throws Exception
	{		

		Query a = pm.newQuery(SQL, "SELECT FECHALLEGADAREAL FROM RESERVAS_DE_ALOJAMIENTO WHERE ID = "+idReserva);
		List lista =a.executeList();

		if( lista.isEmpty() )
			throw new Exception("Jamás se hizo el checkIn");


		Query b = pm.newQuery(SQL, "SELECT  tipoidusuario, idusuario FROM reservas_de_clientes where idreserva =  "+ idReserva);
		b.setResultClass(Persona.class);
		List<Persona> lista1 =b.executeList();


		if( lista.isEmpty() )
			throw new Exception("No hay usuarios en esta reserva");

		int precioAPagar = 0;
		for (int i = 0; i < lista1.size(); i++) {
			Query c = pm.newQuery(SQL, "select sum(precio) from gastos where pagado = " + valor + " and idReserva = "+idReserva);
			List<Number> lista2 = c.executeList();
			if(lista2.get(0)!=null)
				precioAPagar += lista2.get(0).intValue();
		}

		Query d = pm.newQuery(SQL, "SELECT  fechallegadateorica from reservas_de_alojamiento where id = " + idReserva);
		List<Timestamp> lista2 =d.executeList();



		Query e = pm.newQuery(SQL, "SELECT  fechasalidateorica from reservas_de_alojamiento where id = " + idReserva);
		List<Timestamp> lista3 =e.executeList();


		long dias = Math.abs((lista3.get(0).getTime() - lista2.get(0).getTime())/86400000);

		Query f = pm.newQuery(SQL, "SELECT tipos_de_habitacion.costoalojamientonoche from ((reservas_de_alojamiento  inner join habitaciones on reservas_de_alojamiento.idhabitacion = habitaciones.numero) inner join tipos_de_habitacion on tipohabitacion = tipos_de_habitacion.id) where reservas_de_alojamiento.id = "+ idReserva);
		List<Number> lista4 = f.executeList();

		precioAPagar += (dias*lista4.get(0).intValue()) ;

		return precioAPagar;
	}

	/**
	 * Da el mes con mayor y el mes con menor demanda, es decir, el mes en el
	 * que más y menos uso se le dio a una habitación de un tipo específico
	 * @param pm
	 * @param idTipoHabitacionDeseado
	 * @return
	 * @throws Exception
	 */
	public int[] darDemandaMaxYMin( PersistenceManager pm, long idTipoHabitacionDeseado ) throws Exception
	{		
		// Lista con las fechas de llegadas teoricas
		Query b = pm.newQuery(SQL, "SELECT res.fechaLlegadaTeorica\n" + 
				"FROM HABITACIONES hab, RESERVAS_DE_ALOJAMIENTO res\n" + 
				"WHERE res.idHabitacion = hab.numero\n" + 
				"    AND hab.tipoHabitacion = " + idTipoHabitacionDeseado );
		List<Timestamp> listaLlegadas = b.executeList();

		// Lista con las fechas de salidas teoricas
		Query c = pm.newQuery(SQL, "SELECT res.fechaSalidaTeorica\n" + 
				"FROM HABITACIONES hab, RESERVAS_DE_ALOJAMIENTO res\n" + 
				"WHERE res.idHabitacion = hab.numero\n" + 
				"    AND hab.tipoHabitacion = " + idTipoHabitacionDeseado );
		List<Timestamp> listaSalidas = c.executeList();

		// Arreglo para modelar los meses
		long[] conteoMes = new long[12];

		for (int i = 0; i < listaLlegadas.size(); i++)
		{
			int mesDeLlegada = listaLlegadas.get(i).getMonth();
			// Le aumenta los días al contador del mes
			conteoMes[mesDeLlegada] += Math.abs( (listaLlegadas.get(i).getTime() - listaSalidas.get(i).getTime()) / 86400000);
		}			

		// Busca el mes con mayor demanda
		long mayorValor = Long.MIN_VALUE;
		int mesMas = 0;

		// Busca el mes con menor demanda
		long menorValor = Long.MAX_VALUE;
		int mesMenos = 0;

		for (int i = 0; i < conteoMes.length; i++)
		{
			if( conteoMes[i] > mayorValor )
			{
				mayorValor = conteoMes[i];
				mesMas = i;
			}

			if( conteoMes[i] < menorValor )
			{
				menorValor = conteoMes[i];
				mesMenos = i;
			}
		}

		return new int[] { mesMas, mesMenos };
	}

	public int darMesMayorConsumo(PersistenceManager pm, int tipoHabitacion)
	{
		// Lista con las fechas de los consumos
		Query b = pm.newQuery(SQL, "SELECT fecha FROM GASTOS" );
		List<Timestamp> fechas = b.executeList();

		// Arreglo para modelar los meses
		int[] conteoMes = new int[12];

		for (int i = 0; i < fechas.size(); i++)
		{
			int mesDeLlegada = fechas.get(i).getMonth();
			// Le aumenta los días al contador del mes
			conteoMes[mesDeLlegada] ++;
		}			

		// Busca el mes con mayor numero
		int mayorValor = Integer.MIN_VALUE;
		int mesMas = 0;

		for ( int i = 0; i < conteoMes.length; i++)
		{
			if( conteoMes[i] > mayorValor )
			{
				mayorValor = conteoMes[i];
				mesMas = i;
			}
		}

		return mesMas;
	}


	/**
	 * Check out.
	 *
	 * @param pm the pm
	 * @param idReserva the id reserva
	 * @param fechaActual the fecha actual
	 */
	public void checkOut(PersistenceManager pm, long idReserva, String fechaActual)
	{
		Query q = pm.newQuery(SQL, "UPDATE RESERVAS_DE_ALOJAMIENTO SET FECHASALIDAREAL = '"+fechaActual+"' WHERE ID = "+idReserva);
		q.setParameters(fechaActual,idReserva);
		q.executeUnique();
	}

}
