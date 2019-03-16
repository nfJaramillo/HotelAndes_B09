package uniandes.isis2304.parranderos.negocio;


public class Habitacion {

	private int numero;
	private TipoHabitacion tipo;
	private ReservaServicio reserva;
	private Persona persona;

	public Habitacion() {

	}
	
	

	/**
	 * @param numero
	 * @param tipo
	 * @param gastos
	 * @param reserva
	 * @param persona
	 */
	public Habitacion(int numero, TipoHabitacion tipo, Gasto gastos, ReservaServicio reserva, Persona persona) {
		super();
		this.numero = numero;
		this.tipo = tipo;
		this.reserva = reserva;
		this.persona = persona;
	}



	/**
	 * @return the numero
	 */
	public int getNumero() {
		return numero;
	}

	/**
	 * @param numero the numero to set
	 */
	public void setNumero(int numero) {
		this.numero = numero;
	}

	/**
	 * @return the tipo
	 */
	public TipoHabitacion getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(TipoHabitacion tipo) {
		this.tipo = tipo;
	}

	

	/**
	 * @return the reserva
	 */
	public ReservaServicio getReserva() {
		return reserva;
	}

	/**
	 * @param reserva the reserva to set
	 */
	public void setReserva(ReservaServicio reserva) {
		this.reserva = reserva;
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
		return "Habitacion [numero=" + numero + ", tipo=" + tipo + ", reserva=" + reserva
				+ ", persona=" + persona + "]";
	}
	
	
	
	

	
}