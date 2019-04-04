package uniandes.isis2304.parranderos.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class SQLConsultas {
	
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
	 * Se renombra ac� para facilitar la escritura de las sentencias
	 */
	private final static String SQL = PersistenciaHotelAndes.SQL;

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia general de la aplicaci�n
	 */
	private PersistenciaHotelAndes pp;

	/* ****************************************************************
	 * 			Metodos
	 *****************************************************************/

	/**
	 * Constructor
	 * @param pp - El Manejador de persistencia de la aplicaci�n
	 */
	public SQLConsultas (PersistenciaHotelAndes pp)
	{
		this.pp = pp;
	}
	
	public List<ClaseAsistente>  RFC1 (PersistenceManager pm, long idHotel, String fechaInicio, String fechaFin, String fechaActual) throws Exception 
	{

			Query a = pm.newQuery(SQL, "SELECT tablita1.idhabitacion, dineroPeriodoEspecifico, dineroAnioCorrido\r\n" + 
					"FROM\r\n" + 
					"    (   SELECT reservas_de_alojamiento.idhabitacion, SUM(gastos.precio) AS dineroPeriodoEspecifico\r\n" + 
					"        FROM\r\n" + 
					"        ( reservas_de_alojamiento\r\n" + 
					"            INNER JOIN reservas_de_clientes ON reservas_de_alojamiento.id = reservas_de_clientes.idreserva )\r\n" + 
					"            INNER JOIN gastos ON reservas_de_clientes.idusuario = gastos.idusuario\r\n" + 
					"                         AND reservas_de_clientes.tipoidusuario = gastos.idtipoidentificacion\r\n" + 
					"            WHERE gastos.fecha BETWEEN '"+ fechaInicio+ "' AND '" +  fechaFin+"' and reservas_de_alojamiento.idhotel = "+idHotel+" \r\n" + 
					"            GROUP BY reservas_de_alojamiento.idhabitacion\r\n" + 
					"        ) tablita1\r\n" + 
					"FULL OUTER JOIN\r\n" + 
					"        (   SELECT reservas_de_alojamiento.idhabitacion, SUM(gastos.precio) AS dineroAnioCorrido\r\n" + 
					"        FROM\r\n" + 
					"        ( reservas_de_alojamiento\r\n" + 
					"            INNER JOIN reservas_de_clientes ON reservas_de_alojamiento.id = reservas_de_clientes.idreserva )\r\n" + 
					"            INNER JOIN gastos ON reservas_de_clientes.idusuario = gastos.idusuario\r\n" + 
					"                         AND reservas_de_clientes.tipoidusuario = gastos.idtipoidentificacion\r\n" + 
					"            WHERE gastos.fecha BETWEEN '01/01/2019' AND '"+ fechaActual +"' and reservas_de_alojamiento.idhotel = "+idHotel+" \r\n" + 
					"            GROUP BY reservas_de_alojamiento.idhabitacion \r\n" + 
					"        ) tablita2       \r\n" + 
					"ON tablita1.idhabitacion = tablita2.idhabitacion");

			a.setResultClass(ClaseAsistente.class);
			List<ClaseAsistente> lista= a.executeList();
		return lista;


	}
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
	
	public  List<ClaseAsistente> RFC4PRECIO(PersistenceManager pm, int precio1, int precio2)
	{
		Query a = pm.newQuery(SQL, "select *\r\n" + 
				"from servicios_adicionales\r\n" + 
				"where servicios_adicionales.valor between "+precio1+" and "+precio2);
		
		a.setResultClass(ClaseAsistente.class);
		List<ClaseAsistente> lista= a.executeList();

	return lista;
	}
	
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
	
	public  List<ClaseAsistente> RFC4TIPO (PersistenceManager pm, String fecha1)
	{
		Query a = pm.newQuery(SQL, "select *\r\n" + 
				"from servicios_adicionales\r\n" + 
				"where servicios_adicionales.categoria = '"+ fecha1+ "'");
		
		a.setResultClass(ClaseAsistente.class);
		List<ClaseAsistente> lista= a.executeList();

	return lista;
	}
	
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
	
	public  List<ClaseAsistente> RFC5 (PersistenceManager pm, int idUsuario,  String tipoUsuario, String fecha1, String fecha2)
	{
		Query a = pm.newQuery(SQL, "select *\r\n" + 
				"from gastos\r\n" + 
				"where idusuario = "+idUsuario+ " and  idtipoidentificacion = '"+tipoUsuario + "' and fecha between '"+ fecha1+"' and '"+ fecha2+"'");
		
		a.setResultClass(ClaseAsistente.class);
		List<ClaseAsistente> lista= a.executeList();

	return lista;
	}

}
