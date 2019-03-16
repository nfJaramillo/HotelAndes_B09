package uniandes.isis2304.parranderos.negocio;


public class TipoHabitacion {
	
	private int capacidad;
	private double costoAlojamientoNoche;
	private String dotacion;
	private int id;
	private String nombre;
	

	public TipoHabitacion() {

	}


	/**
	 * @param capacidad
	 * @param costoAlojamientoNoche
	 * @param dotacion
	 * @param id
	 * @param nombre
	 */
	public TipoHabitacion(int capacidad, double costoAlojamientoNoche, String dotacion, int id, String nombre) {
		super();
		this.capacidad = capacidad;
		this.costoAlojamientoNoche = costoAlojamientoNoche;
		this.dotacion = dotacion;
		this.id = id;
		this.nombre = nombre;
	}


	/**
	 * @return the capacidad
	 */
	public int getCapacidad() {
		return capacidad;
	}


	/**
	 * @param capacidad the capacidad to set
	 */
	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}


	/**
	 * @return the costoAlojamientoNoche
	 */
	public double getCostoAlojamientoNoche() {
		return costoAlojamientoNoche;
	}


	/**
	 * @param costoAlojamientoNoche the costoAlojamientoNoche to set
	 */
	public void setCostoAlojamientoNoche(double costoAlojamientoNoche) {
		this.costoAlojamientoNoche = costoAlojamientoNoche;
	}


	/**
	 * @return the dotacion
	 */
	public String getDotacion() {
		return dotacion;
	}


	/**
	 * @param dotacion the dotacion to set
	 */
	public void setDotacion(String dotacion) {
		this.dotacion = dotacion;
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


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TipoHabitacion [capacidad=" + capacidad + ", costoAlojamientoNoche=" + costoAlojamientoNoche
				+ ", dotacion=" + dotacion + ", id=" + id + ", nombre=" + nombre + "]";
	}
	
	

	
}