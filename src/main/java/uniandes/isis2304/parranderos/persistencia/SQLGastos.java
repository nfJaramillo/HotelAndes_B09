package uniandes.isis2304.parranderos.persistencia;

import java.math.BigDecimal;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class SQLGastos {
	
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
	public SQLGastos (PersistenciaHotelAndes pp)
	{
		this.pp = pp;
	}


	public double registrarGasto (PersistenceManager pm, long id, long idUsuario, long idServicio, String fecha, int pagado) throws Exception 
	{
		
			Query a = pm.newQuery(SQL, "SELECT valor FROM SERVICIOS_ADICIONALES inner join reservas_servicios on SERVICIOS_ADICIONALES.id = reservas_servicios.idservicio where reservas_servicios.id =  "+idServicio);
			List<Number> lista= a.executeList();



		if( lista.isEmpty() )
			throw new Exception("No existe esa reserva o servicio");
		
		Query b = pm.newQuery(SQL, "SELECT SERVICIOS_ADICIONALES.id FROM SERVICIOS_ADICIONALES inner join reservas_servicios on SERVICIOS_ADICIONALES.id = reservas_servicios.idservicio where reservas_servicios.id =  "+idServicio);
		List<Number> lista1= b.executeList();
		

		if(pagado == 2)
		{
			Query c = pm.newQuery(SQL, "select planes_de_consumo.descripcion\r\n" + 
					"from reservas_de_alojamiento\r\n" + 
					"inner join planes_de_consumo\r\n" + 
					"on reservas_de_alojamiento.plandeconsumo = planes_de_consumo.id\r\n" + 
					"where reservas_de_alojamiento.id = "+idUsuario);
			
			String descripcionPlanConsumo = (String) c.executeUnique();
			if(!descripcionPlanConsumo.startsWith("Convencion"))
			{
				throw new Exception("Esa reserva no pertenece al plan de consumo de una convencion");
			}
		}
		
		

		Query q = pm.newQuery(SQL, "INSERT INTO GASTOS (Id, IdServicio, Fecha,  Pagado, Precio, Idreserva ) values (?,?,?,?,?,?)");
		q.setParameters(id, lista1.get(0), fecha, pagado, lista.get(0),idUsuario );
		q.executeUnique();
		return lista.get(0).intValue() ;
		


	}

}
