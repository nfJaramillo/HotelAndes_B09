package uniandes.isis2304.parranderos.negocio;

import java.sql.Date;


public class Gasto {

	private String fecha;
	private int id;
	private int pagado;
	private double precio;
	private long idServicio;
	private long idPersona;
	private String tipoId;

	
	
	public Gasto() {
		
		
	}



	/**
	 * @param fecha
	 * @param id
	 * @param pagado
	 * @param precio
	 * @param idServicio
	 * @param idPersona
	 * @param tipoId
	 */
	public Gasto(String fecha, int id, int pagado, double precio, long idServicio, long idPersona, String tipoId) {
		super();
		this.fecha = fecha;
		this.id = id;
		this.pagado = pagado;
		this.precio = precio;
		this.idServicio = idServicio;
		this.idPersona = idPersona;
		this.tipoId = tipoId;
	}



	/**
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}



	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
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
	public int getPagado() {
		return pagado;
	}



	/**
	 * @param pagado the pagado to set
	 */
	public void setPagado(int pagado) {
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
	 * @return the idServicio
	 */
	public long getIdServicio() {
		return idServicio;
	}



	/**
	 * @param idServicio the idServicio to set
	 */
	public void setIdServicio(long idServicio) {
		this.idServicio = idServicio;
	}



	/**
	 * @return the idPersona
	 */
	public long getIdPersona() {
		return idPersona;
	}



	/**
	 * @param idPersona the idPersona to set
	 */
	public void setIdPersona(long idPersona) {
		this.idPersona = idPersona;
	}



	/**
	 * @return the tipoId
	 */
	public String getTipoId() {
		return tipoId;
	}



	/**
	 * @param tipoId the tipoId to set
	 */
	public void setTipoId(String tipoId) {
		this.tipoId = tipoId;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Gasto [fecha=" + fecha + ", id=" + id + ", pagado=" + pagado + ", precio=" + precio + ", idServicio="
				+ idServicio + ", idPersona=" + idPersona + ", tipoId=" + tipoId + "]";
	}



	
	
	
	

}