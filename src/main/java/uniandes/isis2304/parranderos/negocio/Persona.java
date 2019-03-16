package uniandes.isis2304.parranderos.negocio;


public class Persona {

	private String correoElectronico;
	private int id;
	private String nombre;
	private String tipoIdentificacion;
	private TipoPersona m_TipoPersona;

	public Persona() {

	}

	/**
	 * @param correoElectronico
	 * @param id
	 * @param nombre
	 * @param tipoIdentificacion
	 * @param m_TipoPersona
	 */
	public Persona(String correoElectronico, int id, String nombre, String tipoIdentificacion,
			TipoPersona m_TipoPersona) {
		super();
		this.correoElectronico = correoElectronico;
		this.id = id;
		this.nombre = nombre;
		this.tipoIdentificacion = tipoIdentificacion;
		this.m_TipoPersona = m_TipoPersona;
	}

	/**
	 * @return the correoElectronico
	 */
	public String getCorreoElectronico() {
		return correoElectronico;
	}

	/**
	 * @param correoElectronico the correoElectronico to set
	 */
	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the tipoIdentificacion
	 */
	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	/**
	 * @param tipoIdentificacion the tipoIdentificacion to set
	 */
	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	/**
	 * @return the m_TipoPersona
	 */
	public TipoPersona getM_TipoPersona() {
		return m_TipoPersona;
	}

	/**
	 * @param m_TipoPersona the m_TipoPersona to set
	 */
	public void setM_TipoPersona(TipoPersona m_TipoPersona) {
		this.m_TipoPersona = m_TipoPersona;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Persona [correoElectronico=" + correoElectronico + ", id=" + id + ", nombre=" + nombre
				+ ", tipoIdentificacion=" + tipoIdentificacion + ", m_TipoPersona=" + m_TipoPersona + "]";
	}

	
}