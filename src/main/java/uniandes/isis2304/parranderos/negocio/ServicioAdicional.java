package uniandes.isis2304.parranderos.negocio;


public class ServicioAdicional {

	private String categoria;
	private String descripcion;
	private boolean estaIncluidoEnCostoAlojamiento;
	private String horarioFin;
	private String horarioInicio;
	private int id;
	private String nombre;
	private double valor;

	public ServicioAdicional() {

	}

	/**
	 * @param categoria
	 * @param descripcion
	 * @param estaIncluidoEnCostoAlojamiento
	 * @param horarioFin
	 * @param horarioInicio
	 * @param id
	 * @param nombre
	 * @param valor
	 */
	public ServicioAdicional(String categoria, String descripcion, boolean estaIncluidoEnCostoAlojamiento,
			String horarioFin, String horarioInicio, int id, String nombre, double valor) {
		super();
		this.categoria = categoria;
		this.descripcion = descripcion;
		this.estaIncluidoEnCostoAlojamiento = estaIncluidoEnCostoAlojamiento;
		this.horarioFin = horarioFin;
		this.horarioInicio = horarioInicio;
		this.id = id;
		this.nombre = nombre;
		this.valor = valor;
	}

	/**
	 * @return the categoria
	 */
	public String getCategoria() {
		return categoria;
	}

	/**
	 * @param categoria the categoria to set
	 */
	public void setCategoria(String categoria) {
		this.categoria = categoria;
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
	 * @return the estaIncluidoEnCostoAlojamiento
	 */
	public boolean isEstaIncluidoEnCostoAlojamiento() {
		return estaIncluidoEnCostoAlojamiento;
	}

	/**
	 * @param estaIncluidoEnCostoAlojamiento the estaIncluidoEnCostoAlojamiento to set
	 */
	public void setEstaIncluidoEnCostoAlojamiento(boolean estaIncluidoEnCostoAlojamiento) {
		this.estaIncluidoEnCostoAlojamiento = estaIncluidoEnCostoAlojamiento;
	}

	/**
	 * @return the horarioFin
	 */
	public String getHorarioFin() {
		return horarioFin;
	}

	/**
	 * @param horarioFin the horarioFin to set
	 */
	public void setHorarioFin(String horarioFin) {
		this.horarioFin = horarioFin;
	}

	/**
	 * @return the horarioInicio
	 */
	public String getHorarioInicio() {
		return horarioInicio;
	}

	/**
	 * @param horarioInicio the horarioInicio to set
	 */
	public void setHorarioInicio(String horarioInicio) {
		this.horarioInicio = horarioInicio;
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
	 * @return the valor
	 */
	public double getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(double valor) {
		this.valor = valor;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ServicioAdicional [categoria=" + categoria + ", descripcion=" + descripcion
				+ ", estaIncluidoEnCostoAlojamiento=" + estaIncluidoEnCostoAlojamiento + ", horarioFin=" + horarioFin
				+ ", horarioInicio=" + horarioInicio + ", id=" + id + ", nombre=" + nombre + ", valor=" + valor + "]";
	}

	
}