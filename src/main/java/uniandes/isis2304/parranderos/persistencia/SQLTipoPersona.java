package uniandes.isis2304.parranderos.persistencia;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;


// TODO: Auto-generated Javadoc
/**
 * The Class SQLTipoPersona.
 */
public class SQLTipoPersona {
	
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
	 * 			M�todos
	 *****************************************************************/

	/**
	 * Constructor.
	 *
	 * @param pp - El Manejador de persistencia de la aplicaci�n
	 */
	public SQLTipoPersona (PersistenciaHotelAndes pp)
	{
		this.pp = pp;
	}
	

	/**
	 * Registrar rol de usuario.
	 *
	 * @param pm the pm
	 * @param id the id
	 * @param nombre the nombre
	 * @return the long
	 */
	public long registrarRolDeUsuario (PersistenceManager pm, long id, String nombre) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO tipos_de_personas" + "(id, nombre) values (?, ?)");
        q.setParameters(id, nombre);
        return (long) q.executeUnique();
	}

}
