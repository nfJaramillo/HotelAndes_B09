package uniandes.isis2304.parranderos.persistencia;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.datanucleus.store.types.converters.LocalDateDateConverter;

// TODO: Auto-generated Javadoc
/**
 * The Class SQLConsultas.
 */
public class SQLConsultas {

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
	public SQLConsultas (PersistenciaHotelAndes pp)
	{
		this.pp = pp;
	}

	/**
	 * Rfc1.
	 *
	 * @param pm the pm
	 * @param idHotel the id hotel
	 * @param fechaInicio the fecha inicio
	 * @param fechaFin the fecha fin
	 * @param fechaActual the fecha actual
	 * @return the list
	 * @throws Exception the exception
	 */
	public List<ClaseAsistente>  RFC1 (PersistenceManager pm, long idHotel, String fechaInicio, String fechaFin, String fechaActual) throws Exception 
	{

		Query a = pm.newQuery(SQL, "SELECT tablita1.idhabitacion, dineroPeriodoEspecifico, dineroAnioCorrido\r\n" + 
				"FROM\r\n" + 
				"    (   SELECT reservas_de_alojamiento.idhabitacion, SUM(gastos.precio) AS dineroPeriodoEspecifico\r\n" + 
				"        FROM\r\n" + 
				"        ( reservas_de_alojamiento\r\n" + 
				"            INNER JOIN reservas_de_clientes ON reservas_de_alojamiento.id = reservas_de_clientes.idreserva )\r\n" + 
				"            INNER JOIN gastos ON reservas_de_clientes.idreserva = gastos.idreserva\r\n" + 
				"            WHERE gastos.fecha BETWEEN '"+ fechaInicio+ "' AND '" +  fechaFin+"' and reservas_de_alojamiento.idhotel = "+idHotel+" \r\n" + 
				"            GROUP BY reservas_de_alojamiento.idhabitacion\r\n" + 
				"        ) tablita1\r\n" + 
				"FULL OUTER JOIN\r\n" + 
				"        (   SELECT reservas_de_alojamiento.idhabitacion, SUM(gastos.precio) AS dineroAnioCorrido\r\n" + 
				"        FROM\r\n" + 
				"        ( reservas_de_alojamiento\r\n" + 
				"            INNER JOIN reservas_de_clientes ON reservas_de_alojamiento.id = reservas_de_clientes.idreserva )\r\n" + 
				"            INNER JOIN gastos ON reservas_de_clientes.idreserva = gastos.idreserva\r\n" +
				"            WHERE gastos.fecha BETWEEN '01/01/2019' AND '"+ fechaActual +"' and reservas_de_alojamiento.idhotel = "+idHotel+" \r\n" + 
				"            GROUP BY reservas_de_alojamiento.idhabitacion \r\n" + 
				"        ) tablita2       \r\n" + 
				"ON tablita1.idhabitacion = tablita2.idhabitacion");

		a.setResultClass(ClaseAsistente.class);
		List<ClaseAsistente> lista= a.executeList();
		return lista;


	}

	/**
	 * Rfc2.
	 *
	 * @param pm the pm
	 * @param idHotel the id hotel
	 * @param fechaInicio the fecha inicio
	 * @param fechaFin the fecha fin
	 * @return the list
	 * @throws Exception the exception
	 */
	public List<ClaseAsistente>  RFC2 (PersistenceManager pm, long idHotel, String fechaInicio, String fechaFin) throws Exception 
	{

		Query a = pm.newQuery(SQL, "SELECT\r\n" + 
				"    COUNT(gastos.id) as conteoServicios,\r\n" + 
				"    servicios_adicionales.id as idServicios,\r\n" + 
				"    servicios_adicionales.nombre AS nombreServicios\r\n" + 
				"FROM\r\n" + 
				"    gastos\r\n" + 
				"    INNER JOIN servicios_adicionales ON gastos.idservicio = servicios_adicionales.id\r\n" + 
				"    where gastos.fecha between '"+ fechaInicio+"' and '" + fechaFin+ " ' and servicios_adicionales.idhotel = " + idHotel +"\r\n" + 
				"GROUP BY\r\n" + 
				"    servicios_adicionales.id,\r\n" + 
				"    servicios_adicionales.nombre\r\n" + 
				"ORDER BY\r\n" + 
				"    COUNT(gastos.id) DESC");

		a.setResultClass(ClaseAsistente.class);
		List<ClaseAsistente> lista= a.executeList();

		return lista;


	}

	/**
	 * Rfc3.
	 *
	 * @param pm the pm
	 * @param idHotel the id hotel
	 * @param fechaActual the fecha actual
	 * @return the list
	 * @throws Exception the exception
	 */
	public List<ClaseAsistente>  RFC3 (PersistenceManager pm, long idHotel, String fechaActual) throws Exception 
	{

		Query a = pm.newQuery(SQL, "SELECT tablita1.numHabitacion, (tablita2.cuantosHay/tablita1.cuantosCaben)*100 AS indice\r\n" + 
				"FROM\r\n" + 
				"    ( SELECT hab.numero AS numHabitacion, tipos.capacidad AS cuantosCaben\r\n" + 
				"    FROM habitaciones hab, tipos_de_habitacion tipos\r\n" + 
				"    WHERE hab.idhotel = "+idHotel+" \r\n" + 
				"    AND hab.tipohabitacion = tipos.id) tablita1\r\n" + 
				"JOIN\r\n" + 
				"    ( SELECT alojamiento.idHabitacion, cuantosporreserva.cuantoshay\r\n" + 
				"        FROM reservas_de_alojamiento alojamiento\r\n" + 
				"        FULL OUTER JOIN\r\n" + 
				"        ( SELECT reserva.idreserva, COUNT(*) as cuantosHay\r\n" + 
				"        FROM reservas_de_clientes reserva, personas cliente\r\n" + 
				"        WHERE reserva.idusuario = cliente.id\r\n" + 
				"            AND reserva.tipoidusuario = cliente.tipoidentificacion\r\n" + 
				"        GROUP BY reserva.idreserva ) cuantosPorReserva\r\n" + 
				"        ON alojamiento.id = cuantosporreserva.idReserva\r\n" + 
				"        WHERE alojamiento.fechallegadareal <= '"+ fechaActual+"' \r\n" + 
				"            AND alojamiento.fechasalidareal IS NULL\r\n" + 
				"            AND alojamiento.idhotel = "+idHotel+" ) tablita2\r\n" + 
				"ON tablita1.numHabitacion = tablita2.idHabitacion");

		a.setResultClass(ClaseAsistente.class);
		List<ClaseAsistente> lista= a.executeList();

		return lista;


	}

	/**
	 * Rfc4precio.
	 *
	 * @param pm the pm
	 * @param precio1 the precio 1
	 * @param precio2 the precio 2
	 * @return the list
	 */
	public  List<ClaseAsistente> RFC4PRECIO(PersistenceManager pm, int precio1, int precio2)
	{
		Query a = pm.newQuery(SQL, "select *\r\n" + 
				"from servicios_adicionales\r\n" + 
				"where servicios_adicionales.valor between "+precio1+" and "+precio2);

		a.setResultClass(ClaseAsistente.class);
		List<ClaseAsistente> lista= a.executeList();

		return lista;
	}

	/**
	 * Rfc4fecha.
	 *
	 * @param pm the pm
	 * @param fecha1 the fecha 1
	 * @param fecha2 the fecha 2
	 * @return the list
	 */
	public  List<ClaseAsistente> RFC4FECHA (PersistenceManager pm, String fecha1, String fecha2)
	{
		Query a = pm.newQuery(SQL, "select  DISTINCT(servicios_adicionales.id),servicios_adicionales.categoria,servicios_adicionales.descripcion,servicios_adicionales.estaincluido,servicios_adicionales.horariofin,servicios_adicionales.horarioinicio,servicios_adicionales.idhotel,servicios_adicionales.nombre,servicios_adicionales.valor\r\n" + 
				"from servicios_adicionales inner join gastos\r\n" + 
				"on servicios_adicionales.id = gastos.idservicio\r\n" + 
				"where gastos.fecha  between '" + fecha1 + "' and '"+ fecha2+ "'");

		a.setResultClass(ClaseAsistente.class);
		List<ClaseAsistente> lista= a.executeList();

		return lista;
	}

	/**
	 * Rfc4tipo.
	 *
	 * @param pm the pm
	 * @param fecha1 the fecha 1
	 * @return the list
	 */
	public  List<ClaseAsistente> RFC4TIPO (PersistenceManager pm, String fecha1)
	{
		Query a = pm.newQuery(SQL, "select *\r\n" + 
				"from servicios_adicionales\r\n" + 
				"where servicios_adicionales.categoria = '"+ fecha1+ "'");

		a.setResultClass(ClaseAsistente.class);
		List<ClaseAsistente> lista= a.executeList();

		return lista;
	}

	/**
	 * Rfc4consumo.
	 *
	 * @param pm the pm
	 * @param consumo the consumo
	 * @param fecha1 the fecha 1
	 * @param fecha2 the fecha 2
	 * @return the list
	 */
	public  List<ClaseAsistente> RFC4CONSUMO (PersistenceManager pm, int consumo, String fecha1, String fecha2)
	{
		Query a = pm.newQuery(SQL, "select *\r\n" + 
				"from servicios_adicionales\r\n" + 
				"where id in (\r\n" + 
				"\r\n" + 
				"SELECT\r\n" + 
				"    servicios_adicionales.id\r\n" + 
				"FROM\r\n" + 
				"    servicios_adicionales\r\n" + 
				"    INNER JOIN gastos ON servicios_adicionales.id = gastos.idservicio\r\n" + 
				"WHERE\r\n" + 
				"    gastos.fecha BETWEEN '"+ fecha1+"' AND '"+ fecha2+"' \r\n" + 
				"GROUP BY\r\n" + 
				"    servicios_adicionales.id\r\n" + 
				"HAVING\r\n" + 
				"    COUNT(gastos.idservicio) > "+consumo+")");

		a.setResultClass(ClaseAsistente.class);
		List<ClaseAsistente> lista= a.executeList();

		return lista;
	}

	/**
	 * Rfc5.
	 *
	 * @param pm the pm
	 * @param idUsuario the id usuario
	 * @param tipoUsuario the tipo usuario
	 * @param fecha1 the fecha 1
	 * @param fecha2 the fecha 2
	 * @return the list
	 */
	public  List<ClaseAsistente> RFC5 (PersistenceManager pm, int idUsuario,  String tipoUsuario, String fecha1, String fecha2)
	{
		Query a = pm.newQuery(SQL, "select *  \r\n" + 
				"from (gastos  \r\n" + 
				"inner join reservas_de_clientes\r\n" + 
				"on gastos.idreserva = reservas_de_clientes.idreserva)\r\n" + 
				"where idusuario = "+ idUsuario +" and  tipoidusuario = '"+ tipoUsuario +"' and fecha between '"+fecha1+"' and '"+fecha2+"'");

		a.setResultClass(ClaseAsistente.class);
		List<ClaseAsistente> lista= a.executeList();


		return lista;
	}


	/**
	 * Rf12.
	 *
	 * @param pm the pm
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
	public ArrayList<Integer> RF12 (PersistenceManager pm,int idHotel, int planDeConsumo, String fecha1, String fecha2, ArrayList<Integer> habitaciones, ArrayList<Integer> cantHabitaciones, ArrayList<Integer> servicios) throws Exception
	{

		// Registrar habitaciones
		ArrayList<Integer> habitacionesYServiciosReservados = new ArrayList<>();
		for (int i = 0; i < habitaciones.size(); i++) {
			Query a = pm.newQuery(SQL,"select habitaciones.numero\r\n" + 
					"from habitaciones\r\n" + 
					"where habitaciones.tipohabitacion = "+ habitaciones.get(i) + " and   numero not in (\r\n" + 
					"select habitaciones.numero\r\n" + 
					"from reservas_de_alojamiento \r\n" + 
					"inner join habitaciones \r\n" + 
					"on reservas_de_alojamiento.idhabitacion = habitaciones.numero\r\n" + 
					"where (reservas_de_alojamiento.fechallegadateorica  between '"+ fecha1+"' and '"+fecha2+"')\r\n" + 
					"or (reservas_de_alojamiento.fechasalidateorica  between '"+ fecha1+"' and '"+fecha2+"')\r\n" + 
					"or ('"+ fecha1+"' between reservas_de_alojamiento.fechallegadateorica and reservas_de_alojamiento.fechasalidateorica)\r\n" + 
					"or ('"+fecha2+"' between reservas_de_alojamiento.fechallegadateorica and reservas_de_alojamiento.fechasalidateorica))");
			List<BigDecimal> aux = a.executeList();
			ArrayList<Integer> listaHabitacionesDisponiblesPorTipo = new ArrayList<>();
			for (BigDecimal long1 : aux) {
				listaHabitacionesDisponiblesPorTipo.add(long1.intValue());
			}
			if(listaHabitacionesDisponiblesPorTipo.size()<cantHabitaciones.size())
			{
				throw new Exception ("No hay suficientes habitaciones del tipo: "+habitaciones.get(i)+" en el hotel");
			}
			for (int j = 0; j < cantHabitaciones.size(); j++) {
				pp.registrarReservaDeAlojamientoSinTransaccion(listaHabitacionesDisponiblesPorTipo.get(j), idHotel, fecha1, null, fecha2, null, planDeConsumo, null, null);
				habitacionesYServiciosReservados.add(listaHabitacionesDisponiblesPorTipo.get(j));
			}
		}
		System.out.println("Servicios");
		// Registrar servicios
		habitacionesYServiciosReservados.add(-1);
		for (Integer servicioAdicional : servicios) {
			pp.registrarReservaDeServicioSinTransaccion("Convencion", fecha2, fecha1, servicioAdicional, 13, "Cedula");
			habitacionesYServiciosReservados.add(servicioAdicional);
		}
		return habitacionesYServiciosReservados;
	}

	/**
	 * Rf12b.
	 *
	 * @param pm the pm
	 * @param idReserva the id reserva
	 * @param tipos the tipos
	 * @param idPersonas the id personas
	 */
	public void RF12B (PersistenceManager pm, int idReserva, String[] tipos,long[]idPersonas)
	{
		for (int i = 0; i < idPersonas.length; i++)
		{
			Query r = pm.newQuery(SQL, "insert into RESERVAS_DE_CLIENTES"+ "(IDRESERVA,IDUSUARIO, tipoidusuario) values (?,?,?)");
			r.setParameters(idReserva,idPersonas[i],tipos[i]);
			r.executeUnique();
		}
	}

	/**
	 * Rf13.
	 *
	 * @param pm the pm
	 * @param habitaciones the habitaciones
	 * @param servicios the servicios
	 */
	public void RF13 (PersistenceManager pm, ArrayList<Integer> habitaciones,ArrayList<Integer> servicios)
	{
		for (Integer integer : habitaciones) {
			Query a = pm.newQuery(SQL, "delete from reservas_de_alojamiento where id = "+integer);
			a.executeUnique();
			Query b = pm.newQuery(SQL, "delete from reservas_de_clientes where idreserva = "+integer);
			b.executeUnique();
		}
		for (Integer integer : servicios) {
			Query c = pm.newQuery(SQL, "delete from reservas_servicios where id = "+integer);
			c.executeUnique();
		}
	}

	public void RF15(PersistenceManager pm,int idHotel,String fecha1, String fecha2, ArrayList<Integer> habitaciones,ArrayList<Integer> servicios) throws Exception
	{
		Transaction tx = pm.currentTransaction();
		for (Integer integer : habitaciones) {
			try {
				pp.registrarReservaDeAlojamientoSinTransaccion(integer, idHotel, fecha1, null, fecha2, null, 6, null, null);
			} catch (Exception e) {
				Query b = pm.newQuery(SQL, "select tipohabitacion from habitaciones where numero = "+integer);
				BigDecimal tipo = (BigDecimal) b.executeUnique();
				long tipoHabitacion = tipo.longValue();

				// Registrar habitaciones

				Query a = pm.newQuery(SQL,"select habitaciones.numero\r\n" + 
						"from habitaciones\r\n" + 
						"where habitaciones.tipohabitacion = "+ tipoHabitacion + " and   numero not in (\r\n" + 
						"select habitaciones.numero\r\n" + 
						"from reservas_de_alojamiento \r\n" + 
						"inner join habitaciones \r\n" + 
						"on reservas_de_alojamiento.idhabitacion = habitaciones.numero\r\n" + 
						"where (reservas_de_alojamiento.fechallegadateorica  between '"+ fecha1+"' and '"+fecha2+"')\r\n" + 
						"or (reservas_de_alojamiento.fechasalidateorica  between '"+ fecha1+"' and '"+fecha2+"')\r\n" + 
						"or ('"+ fecha1+"' between reservas_de_alojamiento.fechallegadateorica and reservas_de_alojamiento.fechasalidateorica)\r\n" + 
						"or ('"+fecha2+"' between reservas_de_alojamiento.fechallegadateorica and reservas_de_alojamiento.fechasalidateorica))");
				List<BigDecimal> aux = a.executeList();
				ArrayList<Integer> listaHabitacionesDisponiblesPorTipo = new ArrayList<>();
				for (BigDecimal long1 : aux) {
					listaHabitacionesDisponiblesPorTipo.add(long1.intValue());
				}
				System.out.println("Calculo habitaciones disponibles: "+listaHabitacionesDisponiblesPorTipo.get(0));
				if(listaHabitacionesDisponiblesPorTipo.size()>=1)
				{
					Query c = pm.newQuery(SQL,"update reservas_de_alojamiento set idhabitacion = "+listaHabitacionesDisponiblesPorTipo.get(0)+" \r\n" + 
							"where idHabitacion = "+integer+" and(\r\n" + 
							"(reservas_de_alojamiento.fechallegadateorica  between '"+ fecha1+"' and '"+fecha2+"')\r\n" + 
							"or (reservas_de_alojamiento.fechasalidateorica  between '"+ fecha1+"' and '"+fecha2+"')\r\n" + 
							"or ('"+ fecha1+"'between reservas_de_alojamiento.fechallegadateorica and reservas_de_alojamiento.fechasalidateorica)\r\n" + 
							" or ('"+ fecha2+"' between reservas_de_alojamiento.fechallegadateorica and reservas_de_alojamiento.fechasalidateorica))");
					c.executeUnique();
					tx.commit();
					tx.begin();
					pp.registrarReservaDeAlojamientoSinTransaccion(integer, idHotel, fecha1, null, fecha2, null, 6, null, null);
				}
				else
				{
					throw new Exception("No se encontro ninguna habitacion disponible para reemplazar");
				}
			}
		}
		System.out.println("Serviciosssss");
		for (Integer integer : servicios) {
			try {
				pp.registrarReservaDeServicioSinTransaccion("Mantenimiento", fecha2, fecha1, integer, 14, "Cedula");
			} catch (Exception e) {
				Query d = pm.newQuery(SQL,"delete from reservas_servicios\r\n" + 
						"where idservicio = "+integer+" and(\r\n" + 
						"(fechainicio between '"+fecha1+"' and '"+fecha2+"')\r\n" + 
						"or (fechafin between '"+fecha1+"' and '"+fecha2+"')\r\n" + 
						"or ('"+fecha1+"' between fechainicio and fechafin)\r\n" + 
						"or ('"+fecha2+"' between fechainicio and fechafin))");
				d.executeUnique();
				tx.commit();
				tx.begin();
				pp.registrarReservaDeServicioSinTransaccion("Mantenimiento", fecha2, fecha1, integer, 14, "Cedula");
			}
		}
	}
	
	public void RF16(PersistenceManager pm,int idHotel,ArrayList<Integer> habitaciones,ArrayList<Integer> servicios) throws Exception
	{
		for (Integer integer : habitaciones) {
			Query a = pm.newQuery(SQL,"delete from reservas_de_alojamiento where idhabitacion = "+integer+" and plandeconsumo = 6");
					a.executeUnique();
		}
		for (Integer integer : servicios) {
			Query b = pm.newQuery(SQL,"delete from reservas_servicios where idservicio = "+integer+" and idtipopersona = 14");
			b.executeUnique();
		}
	}

	public List<ClaseAsistente> RFC6Mes(PersistenceManager pm, int tipoHabitacion)
	{
		String instruccion = "EXTRACT(MONTH FROM (ROUND (TO_DATE (res.fechallegadareal),'MONTH')) ) AS mesRedondeado, hab.tipohabitacion\r"
				+ "\nFROM RESERVAS_DE_ALOJAMIENTO res\r"
				+ "\nJOIN HABITACIONES hab\r"
				+ "\nON res.idhabitacion = hab.numero\r"
				+ "\nWHERE hab.tipohabitacion = " + tipoHabitacion + "\r"
				+ "\nAND res.fechallegadareal IS NOT NULL;";
		
		Query r = pm.newQuery(SQL, instruccion );
		r.executeUnique();
		r.setResultClass(ClaseAsistente.class);

		return (List<ClaseAsistente>) r.executeList();
	}
	
	public List<ClaseAsistente> RFC7(PersistenceManager pm)
	{
		Query a = pm.newQuery(SQL, 	"SELECT per.id, per.tipoidentificacion as tipoIdUsuario, per.nombre, al.fechaLlegadaTeorica, al.fechaSalidaTeorica\n" + 
				"FROM RESERVAS_DE_ALOJAMIENTO al, RESERVAS_DE_CLIENTES resClien, PERSONAS per\n" + 
				"WHERE al.id = resClien.idreserva\n" + 
				"    AND resClien.idusuario = per.id" );
		
		a.setResultClass(ClaseAsistente.class);
		List<ClaseAsistente> lista= a.executeList();

		return lista;
	}
	public ArrayList <String> RFC8(PersistenceManager pm)
	{
		
		Date hoy = new Date();
		SimpleDateFormat dmyFormat = new SimpleDateFormat("dd/MM/yyyy");
		String actual = dmyFormat.format(hoy);
		hoy.setYear(hoy.getYear()-1);
		String anterior = dmyFormat.format(hoy);
		Query a = pm.newQuery(SQL,"select nombre\r\n" + 
				"from servicios_adicionales\r\n" + 
				"where servicios_adicionales.id \r\n" + 
				"not in (\r\n" + 
				"select idServicio\r\n" + 
				"from reservas_servicios\r\n" + 
				"where fechainicio >= '"+anterior+"' and reservas_servicios.fechafin<='"+actual+"'\r\n" + 
				"group by (idservicio)\r\n" + 
				"having count(idServicio)>=3)");
		List<String> ans = (List<String>) a.executeList();
		ArrayList<String> resp = new ArrayList<>();
		for (String string : ans) {
			resp.add(string);
		}
		return resp;
	}
	
	public  ArrayList <Integer> RFC11 (PersistenceManager pm)
	{
		 ArrayList <Integer> resp = new  ArrayList <Integer>();
		for (int i = 1; i < 54; i++) {
			Query a = pm.newQuery(SQL,"select   idservicio\r\n" + 
					"from reservas_servicios\r\n" + 
					"where  to_char(to_date(fechainicio,'DD/MM/YYYY'),'ww') = "+i+"\r\n" + 
					"group by to_char(to_date(fechainicio,'DD/MM/YYYY'),'ww'),idservicio\r\n" + 
					"order by (to_char(to_date(fechainicio,'DD/MM/YYYY'),'ww')),count(idservicio)  desc\r\n" + 
					"fetch first row only");
			BigDecimal b = (BigDecimal) a.executeUnique();
			if(b==null)
			{
				resp.add(0) ;
			}
			else 
			{
				resp.add((b.intValue())) ;
			}
		}
		for (int i = 1; i < 54; i++) {
			Query a = pm.newQuery(SQL,"select   idservicio\r\n" + 
					"from reservas_servicios\r\n" + 
					"where  to_char(to_date(fechainicio,'DD/MM/YYYY'),'ww') = "+i+"\r\n" + 
					"group by to_char(to_date(fechainicio,'DD/MM/YYYY'),'ww'),idservicio\r\n" + 
					"order by (to_char(to_date(fechainicio,'DD/MM/YYYY'),'ww')),count(idservicio)  asc\r\n" + 
					"fetch first row only");
			BigDecimal b = (BigDecimal) a.executeUnique();
			if(b==null)
			{
				resp.add(0) ;
			}
			else 
			{
				resp.add((b.intValue())) ;
			}
		}
		return resp;
	}
	

}
