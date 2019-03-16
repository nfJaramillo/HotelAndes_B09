package uniandes.isis2304.parranderos.negocio;

import java.sql.Date;


public class Gasto {

	private Date fecha;
	private int id;
	private boolean pagado;
	private double precio;
	private ServicioAdicional servicios;
	private Persona persona;

	
	
	public Gasto() {
		
		
	}

	/**
	 * @param fecha
	 * @param id
	 * @param pagado
	 * @param precio
	 * @param servicios
	 * @param persona
	 */
	public Gasto(Date fecha, int id, boolean pagado, double precio, ServicioAdicional servicios, Persona persona) {
		this.fecha = fecha;
		this.id = id;
		this.pagado = pagado;
		this.precio = precio;
		this.servicios = servicios;
		this.persona = persona;
	}

	/**
	 * @return the fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
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
	 * @return the pagado
	 */
	public boolean isPagado() {
		return pagado;
	}

	/**
	 * @param pagado the pagado to set
	 */
	public void setPagado(boolean pagado) {
		this.pagado = pagado;
	}

	/**
	 * @return the precio
	 */
	public double getPrecio() {
		return precio;
	}

	/**
	 * @param precio the precio to set
	 */
	public void setPrecio(double precio) {
		this.precio = precio;
	}

	/**
	 * @return the servicios
	 */
	public ServicioAdicional getServicios() {
		return servicios;
	}

	/**
	 * @param servicios the servicios to set
	 */
	public void setServicios(ServicioAdicional servicios) {
		this.servicios = servicios;
	}

	/**
	 * @return the persona
	 */
	public Persona getPersona() {
		return persona;
	}

	/**
	 * @param persona the persona to set
	 */
	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Gasto [fecha=" + fecha + ", id=" + id + ", pagado=" + pagado + ", precio=" + precio + ", servicios="
				+ servicios + ", persona=" + persona + "]";
	}

	
	
	

}