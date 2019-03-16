package uniandes.isis2304.parranderos.negocio;

import java.sql.Date;

public class Reserva {

	private Date fechaLlegadaReal;
	private Date fechaLlegadaTeorica;
	private Date fechaSalidaReal;
	private Date fechaSalidaTeorica;
	private int id;
	private Habitacion habitacion;
	private Persona cliente;

	public Reserva() {

	}

	/**
	 * @param fechaLlegadaReal
	 * @param fechaLlegadaTeorica
	 * @param fechaSalidaReal
	 * @param fechaSalidaTeorica
	 * @param id
	 * @param habitacion
	 * @param cliente
	 */
	public Reserva(Date fechaLlegadaReal, Date fechaLlegadaTeorica, Date fechaSalidaReal, Date fechaSalidaTeorica,
			int id, Habitacion habitacion, Persona cliente) {
		super();
		this.fechaLlegadaReal = fechaLlegadaReal;
		this.fechaLlegadaTeorica = fechaLlegadaTeorica;
		this.fechaSalidaReal = fechaSalidaReal;
		this.fechaSalidaTeorica = fechaSalidaTeorica;
		this.id = id;
		this.habitacion = habitacion;
		this.cliente = cliente;
	}

	/**
	 * @return the fechaLlegadaReal
	 */
	public Date getFechaLlegadaReal() {
		return fechaLlegadaReal;
	}

	/**
	 * @param fechaLlegadaReal the fechaLlegadaReal to set
	 */
	public void setFechaLlegadaReal(Date fechaLlegadaReal) {
		this.fechaLlegadaReal = fechaLlegadaReal;
	}

	/**
	 * @return the fechaLlegadaTeorica
	 */
	public Date getFechaLlegadaTeorica() {
		return fechaLlegadaTeorica;
	}

	/**
	 * @param fechaLlegadaTeorica the fechaLlegadaTeorica to set
	 */
	public void setFechaLlegadaTeorica(Date fechaLlegadaTeorica) {
		this.fechaLlegadaTeorica = fechaLlegadaTeorica;
	}

	/**
	 * @return the fechaSalidaReal
	 */
	public Date getFechaSalidaReal() {
		return fechaSalidaReal;
	}

	/**
	 * @param fechaSalidaReal the fechaSalidaReal to set
	 */
	public void setFechaSalidaReal(Date fechaSalidaReal) {
		this.fechaSalidaReal = fechaSalidaReal;
	}

	/**
	 * @return the fechaSalidaTeorica
	 */
	public Date getFechaSalidaTeorica() {
		return fechaSalidaTeorica;
	}

	/**
	 * @param fechaSalidaTeorica the fechaSalidaTeorica to set
	 */
	public void setFechaSalidaTeorica(Date fechaSalidaTeorica) {
		this.fechaSalidaTeorica = fechaSalidaTeorica;
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
	 * @return the habitacion
	 */
	public Habitacion getHabitacion() {
		return habitacion;
	}

	/**
	 * @param habitacion the habitacion to set
	 */
	public void setHabitacion(Habitacion habitacion) {
		this.habitacion = habitacion;
	}

	/**
	 * @return the cliente
	 */
	public Persona getCliente() {
		return cliente;
	}

	/**
	 * @param cliente the cliente to set
	 */
	public void setCliente(Persona cliente) {
		this.cliente = cliente;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Reserva [fechaLlegadaReal=" + fechaLlegadaReal + ", fechaLlegadaTeorica=" + fechaLlegadaTeorica
				+ ", fechaSalidaReal=" + fechaSalidaReal + ", fechaSalidaTeorica=" + fechaSalidaTeorica + ", id=" + id
				+ ", habitacion=" + habitacion + ", cliente=" + cliente + "]";
	}

	
}