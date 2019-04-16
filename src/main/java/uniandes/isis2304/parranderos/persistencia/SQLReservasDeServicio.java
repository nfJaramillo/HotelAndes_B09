package uniandes.isis2304.parranderos.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

// TODO: Auto-generated Javadoc
/**
 * The Class SQLReservasDeServicio.
 */
public class SQLReservasDeServicio {
	
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
	public SQLReservasDeServicio (PersistenciaHotelAndes pp)
	{
		this.pp = pp;
	}
	

	/**
	 * Registrar reserva de servicio.
	 *
	 * @param pm the pm
	 * @param descripcion the descripcion
	 * @param fechaFin the fecha fin
	 * @param fechaInicio the fecha inicio
	 * @param id the id
	 * @param servicioAdicional the servicio adicional
	 * @param idUsuario the id usuario
	 * @param tipoIdUsuario the tipo id usuario
	 * @throws Exception the exception
	 */
	public void registrarReservaDeServicio (PersistenceManager pm, String descripcion, String fechaFin, String fechaInicio, long id, long servicioAdicional,long idUsuario, String tipoIdUsuario) throws Exception 
	{
		

			Query a = pm.newQuery(SQL, "SELECT * FROM RESERVAS_SERVICIOS WHERE IDSERVICIO = "
					+ servicioAdicional + " AND ( (('"+fechaInicio + "' between FECHAINICIO and FECHAFIN)"
					+ " OR ('"+ fechaFin + "' BETWEEN FECHAINICIO and FECHAFIN)) )  or"
					+ " ( ((FECHAINICIO between '" + fechaInicio + "' and '" + fechaFin + "') OR"
					+ " ( FECHAFIN between '" + fechaInicio + "' AND '" + fechaFin + "')) )" );
			List lista= a.executeList();

		if( !lista.isEmpty() )
			throw new Exception("Ya existe una reserva para el mismo servicio en ese periodo de tiempo");
		
		Query q = pm.newQuery(SQL, "INSERT INTO RESERVAS_SERVICIOS" + "(ID,DESCRIPCION,FECHAINICIO,FECHAFIN,IDSERVICIO,IDTIPOPERSONA,TIPOIDENTIFICACION) values (?, ?,?,?,?,?,?)");
		q.setParameters(id, descripcion, fechaInicio, fechaFin, servicioAdicional, idUsuario, tipoIdUsuario);
		q.executeUnique();


	}

}
