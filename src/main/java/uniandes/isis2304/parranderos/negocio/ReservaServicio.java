package uniandes.isis2304.parranderos.negocio;

import java.sql.Date;

public class ReservaServicio {

	private String descripcion;
	private String fechaFin;
	private String fechaInicio;
	private long id;
	private long servicioAdicional;
	private long idUsuario;
	private String tipoIdUsuario;
	

	public ReservaServicio() {

	}


	/**
	 * @param descripcion
	 * @param fechaFin
	 * @param fechaInicio
	 * @param id
	 * @param servicioAdicional
	 * @param idUsuario
	 * @param tipoIdUsuario
	 */
	public ReservaServicio(String descripcion, String fechaFin, String fechaInicio, long id, long servicioAdicional,long idUsuario, String tipoIdUsuario) {
		super();
		this.descripcion = descripcion;
		this.fechaFin = fechaFin;
		this.fechaInicio = fechaInicio;
		this.id = id;
		this.servicioAdicional = servicioAdicional;
		this.idUsuario = idUsuario;
		this.tipoIdUsuario = tipoIdUsuario;
	}


	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}


	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	/**
	 * @return the fechaFin
	 */
	public String getFechaFin() {
		return fechaFin;
	}


	/**
	 * @param fechaFin the fechaFin to set
	 */
	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}


	/**
	 * @return the fechaInicio
	 */
	public String getFechaInicio() {
		return fechaInicio;
	}


	/**
	 * @param fechaInicio the fechaInicio to set
	 */
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}


	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}


	/**
	 * @return the servicioAdicional
	 */
	public long getServicioAdicional() {
		return servicioAdicional;
	}


	/**
	 * @param servicioAdicional the servicioAdicional to set
	 */
	public void setServicioAdicional(long servicioAdicional) {
		this.servicioAdicional = servicioAdicional;
	}


	/**
	 * @return the idUsuario
	 */
	public long getIdUsuario() {
		return idUsuario;
	}


	/**
	 * @param idUsuario the idUsuario to set
	 */
	public void setIdUsuario(long idUsuario) {
		this.idUsuario = idUsuario;
	}


	/**
	 * @return the tipoIdUsuario
	 */
	public String getTipoIdUsuario() {
		return tipoIdUsuario;
	}


	/**
	 * @param tipoIdUsuario the tipoIdUsuario to set
	 */
	public void setTipoIdUsuario(String tipoIdUsuario) {
		this.tipoIdUsuario = tipoIdUsuario;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ReservaServicio [descripcion=" + descripcion + ", fechaFin=" + fechaFin + ", fechaInicio=" + fechaInicio
				+ ", id=" + id + ", servicioAdicional=" + servicioAdicional + ", idUsuario=" + idUsuario
				+ ", tipoIdUsuario=" + tipoIdUsuario + "]";
	}


	



	
	
}