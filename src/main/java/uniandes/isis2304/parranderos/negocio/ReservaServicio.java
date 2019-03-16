package uniandes.isis2304.parranderos.negocio;

import java.sql.Date;

public class ReservaServicio {

	private String descripcion;
	private Date fechaFin;
	private Date fechaInicio;
	private int id;
	private ServicioAdicional servicioAdicional;

	public ReservaServicio() {

	}

	

	/**
	 * @param descripcion
	 * @param fechaFin
	 * @param fechaInicio
	 * @param id
	 * @param servicioAdicional
	 */
	public ReservaServicio(String descripcion, Date fechaFin, Date fechaInicio, int id,
			ServicioAdicional servicioAdicional) {
		super();
		this.descripcion = descripcion;
		this.fechaFin = fechaFin;
		this.fechaInicio = fechaInicio;
		this.id = id;
		this.servicioAdicional = servicioAdicional;
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
	public Date getFechaFin() {
		return fechaFin;
	}



	/**
	 * @param fechaFin the fechaFin to set
	 */
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}



	/**
	 * @return the fechaInicio
	 */
	public Date getFechaInicio() {
		return fechaInicio;
	}



	/**
	 * @param fechaInicio the fechaInicio to set
	 */
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
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
	 * @return the servicioAdicional
	 */
	public ServicioAdicional getServicioAdicional() {
		return servicioAdicional;
	}



	/**
	 * @param servicioAdicional the servicioAdicional to set
	 */
	public void setServicioAdicional(ServicioAdicional servicioAdicional) {
		this.servicioAdicional = servicioAdicional;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ReservaServicio [descripcion=" + descripcion + ", fechaFin=" + fechaFin + ", fechaInicio=" + fechaInicio
				+ ", id=" + id + ", servicioAdicional=" + servicioAdicional + "]";
	}

	
}