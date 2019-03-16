package uniandes.isis2304.parranderos.negocio;

public class Producto {
	
	
	private int id;
	private String nombre;
	private ServicioAdicional servicio;
	
	public Producto() {

	}

	/**
	 * @param id
	 * @param nombre
	 * @param servicio
	 */
	public Producto(int id, String nombre, ServicioAdicional servicio) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.servicio = servicio;
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
	 * @return the servicio
	 */
	public ServicioAdicional getServicio() {
		return servicio;
	}

	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(ServicioAdicional servicio) {
		this.servicio = servicio;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Producto [id=" + id + ", nombre=" + nombre + ", servicio=" + servicio + "]";
	}
	
	
	
	

}
