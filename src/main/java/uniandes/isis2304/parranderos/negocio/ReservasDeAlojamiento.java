package uniandes.isis2304.parranderos.negocio;

import java.sql.Date;

public class ReservasDeAlojamiento {
	
	private long id;
	
	private long idHabitacion;
	
	private long idHotel;
	
	private String fechallegadateorica;
	
	private String fechallegadareal;
	
	private String fechasalidateorica;
	
	private String fechasalidareal;
	
	private long plandeconsumo;
	
	public ReservasDeAlojamiento()
	{
		
	}

	/**
	 * @param id
	 * @param idHabitacion
	 * @param idHotel
	 * @param fechallegadateorica
	 * @param fechallegadareal
	 * @param fechasalidateorica
	 * @param fechasalidareal
	 * @param plandeconsumo
	 */
	public ReservasDeAlojamiento(long id, long idHabitacion, long idHotel, String fechallegadateorica,
			String fechallegadareal, String fechasalidateorica, String fechasalidareal, long plandeconsumo) {
		super();
		this.id = id;
		this.idHabitacion = idHabitacion;
		this.idHotel = idHotel;
		this.fechallegadateorica = fechallegadateorica;
		this.fechallegadareal = fechallegadareal;
		this.fechasalidateorica = fechasalidateorica;
		this.fechasalidareal = fechasalidareal;
		this.plandeconsumo = plandeconsumo;
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
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the idHabitacion
	 */
	public long getIdHabitacion() {
		return idHabitacion;
	}

	/**
	 * @param idHabitacion the idHabitacion to set
	 */
	public void setIdHabitacion(long idHabitacion) {
		this.idHabitacion = idHabitacion;
	}

	/**
	 * @return the idHotel
	 */
	public long getIdHotel() {
		return idHotel;
	}

	/**
	 * @param idHotel the idHotel to set
	 */
	public void setIdHotel(long idHotel) {
		this.idHotel = idHotel;
	}

	/**
	 * @return the fechallegadateorica
	 */
	public String getFechallegadateorica() {
		return fechallegadateorica;
	}

	/**
	 * @param fechallegadateorica the fechallegadateorica to set
	 */
	public void setFechallegadateorica(String fechallegadateorica) {
		this.fechallegadateorica = fechallegadateorica;
	}

	/**
	 * @return the fechallegadareal
	 */
	public String getFechallegadareal() {
		return fechallegadareal;
	}

	/**
	 * @param fechallegadareal the fechallegadareal to set
	 */
	public void setFechallegadareal(String fechallegadareal) {
		this.fechallegadareal = fechallegadareal;
	}

	/**
	 * @return the fechasalidateorica
	 */
	public String getFechasalidateorica() {
		return fechasalidateorica;
	}

	/**
	 * @param fechasalidateorica the fechasalidateorica to set
	 */
	public void setFechasalidateorica(String fechasalidateorica) {
		this.fechasalidateorica = fechasalidateorica;
	}

	/**
	 * @return the fechasalidareal
	 */
	public String getFechasalidareal() {
		return fechasalidareal;
	}

	/**
	 * @param fechasalidareal the fechasalidareal to set
	 */
	public void setFechasalidareal(String fechasalidareal) {
		this.fechasalidareal = fechasalidareal;
	}

	/**
	 * @return the plandeconsumo
	 */
	public long getPlandeconsumo() {
		return plandeconsumo;
	}

	/**
	 * @param plandeconsumo the plandeconsumo to set
	 */
	public void setPlandeconsumo(long plandeconsumo) {
		this.plandeconsumo = plandeconsumo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ReservasDeAlojamiento [id=" + id + ", idHabitacion=" + idHabitacion + ", idHotel=" + idHotel
				+ ", fechallegadateorica=" + fechallegadateorica + ", fechallegadareal=" + fechallegadareal
				+ ", fechasalidateorica=" + fechasalidateorica + ", fechasalidareal=" + fechasalidareal
				+ ", plandeconsumo=" + plandeconsumo + "]";
	}


	
	

}
