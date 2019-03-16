package uniandes.isis2304.parranderos.negocio;


public class Hotel {

	private int estrellas;
	private int id;
	private String nombre;
	private Habitacion habitaciones;
	private ServicioAdicional serviciosAdicionales;
	private Reserva reservas;

	public Hotel() {

	}

	/**
	 * @param estrellas
	 * @param id
	 * @param nombre
	 * @param habitaciones
	 * @param serviciosAdicionales
	 * @param reservas
	 */
	public Hotel(int estrellas, int id, String nombre, Habitacion habitaciones, ServicioAdicional serviciosAdicionales,
			Reserva reservas) {
		super();
		this.estrellas = estrellas;
		this.id = id;
		this.nombre = nombre;
		this.habitaciones = habitaciones;
		this.serviciosAdicionales = serviciosAdicionales;
		this.reservas = reservas;
	}

	/**
	 * @return the estrellas
	 */
	public int getEstrellas() {
		return estrellas;
	}

	/**
	 * @param estrellas the estrellas to set
	 */
	public void setEstrellas(int estrellas) {
		this.estrellas = estrellas;
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
	 * @return the habitaciones
	 */
	public Habitacion getHabitaciones() {
		return habitaciones;
	}

	/**
	 * @param habitaciones the habitaciones to set
	 */
	public void setHabitaciones(Habitacion habitaciones) {
		this.habitaciones = habitaciones;
	}

	/**
	 * @return the serviciosAdicionales
	 */
	public ServicioAdicional getServiciosAdicionales() {
		return serviciosAdicionales;
	}

	/**
	 * @param serviciosAdicionales the serviciosAdicionales to set
	 */
	public void setServiciosAdicionales(ServicioAdicional serviciosAdicionales) {
		this.serviciosAdicionales = serviciosAdicionales;
	}

	/**
	 * @return the reservas
	 */
	public Reserva getReservas() {
		return reservas;
	}

	/**
	 * @param reservas the reservas to set
	 */
	public void setReservas(Reserva reservas) {
		this.reservas = reservas;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Hotel [estrellas=" + estrellas + ", id=" + id + ", nombre=" + nombre + ", habitaciones=" + habitaciones
				+ ", serviciosAdicionales=" + serviciosAdicionales + ", reservas=" + reservas + "]";
	}

	
}