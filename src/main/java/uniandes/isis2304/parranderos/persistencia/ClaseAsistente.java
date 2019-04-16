package uniandes.isis2304.parranderos.persistencia;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class ClaseAsistente.
 */
public class ClaseAsistente {
	
	
	/** The idhabitacion. */
	private int IDHABITACION;

	/** The dineroperiodoespecifico. */
	private int DINEROPERIODOESPECIFICO;
	
	/** The dineroaniocorrido. */
	private int DINEROANIOCORRIDO;
	
	/** The conteoservicios. */
	private int CONTEOSERVICIOS;
	
	/** The idservicios. */
	private int IDSERVICIOS;
	
	/** The nombreservicios. */
	private String NOMBRESERVICIOS;
	
	/** The numhabitacion. */
	private int NUMHABITACION;
	
	/** The indice. */
	private double INDICE ;
	
	/** The id. */
	private int ID;
	
	/** The idhotel. */
	private int IDHOTEL;
	
	/** The categoria. */
	private String CATEGORIA;
	
	/** The estaincluido. */
	private int ESTAINCLUIDO;
	
	/** The horarioinicio. */
	private String HORARIOINICIO;
	
	/** The horariofin. */
	private String HORARIOFIN;
	
	/** The nombre. */
	private String NOMBRE;
	
	/** The valor. */
	private String VALOR;
	

	/** The idusuario. */
	private int IDUSUARIO;
	
	/** The idtipoidentificacion. */
	private String IDTIPOIDENTIFICACION;
	
	/** The idservicio. */
	private int IDSERVICIO;
	
	/** The fecha. */
	private String FECHA;
	
	/** The pagado. */
	private int PAGADO;
	
	/** The precio. */
	private int PRECIO;
	
	/** The idreserva. */
	private int IDRESERVA;
	
	/** The idreserva 1. */
	private int IDRESERVA_1;
	
	/** The tipoidusuario. */
	private String TIPOIDUSUARIO;
	

	
	
	
	
	/**
	 * Gets the idreserva.
	 *
	 * @return the iDRESERVA
	 */
	public int getIDRESERVA() {
		return IDRESERVA;
	}



	/**
	 * Sets the idreserva.
	 *
	 * @param iDRESERVA the iDRESERVA to set
	 */
	public void setIDRESERVA(int iDRESERVA) {
		IDRESERVA = iDRESERVA;
	}



	/**
	 * Gets the idreserva 1.
	 *
	 * @return the iDRESERVA_1
	 */
	public int getIDRESERVA_1() {
		return IDRESERVA_1;
	}



	/**
	 * Sets the idreserva 1.
	 *
	 * @param iDRESERVA_1 the iDRESERVA_1 to set
	 */
	public void setIDRESERVA_1(int iDRESERVA_1) {
		IDRESERVA_1 = iDRESERVA_1;
	}



	/**
	 * Gets the tipoidusuario.
	 *
	 * @return the tIPOIDUSUARIO
	 */
	public String getTIPOIDUSUARIO() {
		return TIPOIDUSUARIO;
	}



	/**
	 * Sets the tipoidusuario.
	 *
	 * @param tIPOIDUSUARIO the tIPOIDUSUARIO to set
	 */
	public void setTIPOIDUSUARIO(String tIPOIDUSUARIO) {
		TIPOIDUSUARIO = tIPOIDUSUARIO;
	}



	/**
	 * Gets the idusuario.
	 *
	 * @return the iDUSUARIO
	 */
	public int getIDUSUARIO() {
		return IDUSUARIO;
	}



	/**
	 * Sets the idusuario.
	 *
	 * @param iDUSUARIO the iDUSUARIO to set
	 */
	public void setIDUSUARIO(int iDUSUARIO) {
		IDUSUARIO = iDUSUARIO;
	}



	/**
	 * Gets the idtipoidentificacion.
	 *
	 * @return the iDTIPOIDENTIFICACION
	 */
	public String getIDTIPOIDENTIFICACION() {
		return IDTIPOIDENTIFICACION;
	}



	/**
	 * Sets the idtipoidentificacion.
	 *
	 * @param iDTIPOIDENTIFICACION the iDTIPOIDENTIFICACION to set
	 */
	public void setIDTIPOIDENTIFICACION(String iDTIPOIDENTIFICACION) {
		IDTIPOIDENTIFICACION = iDTIPOIDENTIFICACION;
	}



	/**
	 * Gets the idservicio.
	 *
	 * @return the iDSERVICIO
	 */
	public int getIDSERVICIO() {
		return IDSERVICIO;
	}



	/**
	 * Sets the idservicio.
	 *
	 * @param iDSERVICIO the iDSERVICIO to set
	 */
	public void setIDSERVICIO(int iDSERVICIO) {
		IDSERVICIO = iDSERVICIO;
	}



	/**
	 * Gets the fecha.
	 *
	 * @return the fECHA
	 */
	public String getFECHA() {
		return FECHA;
	}



	/**
	 * Sets the fecha.
	 *
	 * @param fECHA the fECHA to set
	 */
	public void setFECHA(Date fECHA) {
		FECHA = fECHA.toString().substring(0, 10);
	}



	/**
	 * Gets the pagado.
	 *
	 * @return the pAGADO
	 */
	public int getPAGADO() {
		return PAGADO;
	}



	/**
	 * Sets the pagado.
	 *
	 * @param pAGADO the pAGADO to set
	 */
	public void setPAGADO(int pAGADO) {
		PAGADO = pAGADO;
	}



	/**
	 * Gets the precio.
	 *
	 * @return the pRECIO
	 */
	public int getPRECIO() {
		return PRECIO;
	}



	/**
	 * Sets the precio.
	 *
	 * @param pRECIO the pRECIO to set
	 */
	public void setPRECIO(int pRECIO) {
		PRECIO = pRECIO;
	}



	/**
	 * Gets the id.
	 *
	 * @return the iD
	 */
	public int getID() {
		return ID;
	}



	/**
	 * Sets the id.
	 *
	 * @param iD the iD to set
	 */
	public void setID(int iD) {
		ID = iD;
	}



	/**
	 * Gets the idhotel.
	 *
	 * @return the iDHOTEL
	 */
	public int getIDHOTEL() {
		return IDHOTEL;
	}



	/**
	 * Sets the idhotel.
	 *
	 * @param iDHOTEL the iDHOTEL to set
	 */
	public void setIDHOTEL(int iDHOTEL) {
		IDHOTEL = iDHOTEL;
	}



	/**
	 * Gets the categoria.
	 *
	 * @return the cATEGORIA
	 */
	public String getCATEGORIA() {
		return CATEGORIA;
	}



	/**
	 * Sets the categoria.
	 *
	 * @param cATEGORIA the cATEGORIA to set
	 */
	public void setCATEGORIA(String cATEGORIA) {
		CATEGORIA = cATEGORIA;
	}



	/**
	 * Gets the estaincluido.
	 *
	 * @return the eSTAINCLUIDO
	 */
	public int getESTAINCLUIDO() {
		return ESTAINCLUIDO;
	}



	/**
	 * Sets the estaincluido.
	 *
	 * @param eSTAINCLUIDO the eSTAINCLUIDO to set
	 */
	public void setESTAINCLUIDO(int eSTAINCLUIDO) {
		ESTAINCLUIDO = eSTAINCLUIDO;
	}



	/**
	 * Gets the horarioinicio.
	 *
	 * @return the hORARIOINICIO
	 */
	public String getHORARIOINICIO() {
		return HORARIOINICIO;
	}



	/**
	 * Sets the horarioinicio.
	 *
	 * @param hORARIOINICIO the hORARIOINICIO to set
	 */
	public void setHORARIOINICIO(String hORARIOINICIO) {
		HORARIOINICIO = hORARIOINICIO;
	}



	/**
	 * Gets the horariofin.
	 *
	 * @return the hORARIOFIN
	 */
	public String getHORARIOFIN() {
		return HORARIOFIN;
	}



	/**
	 * Sets the horariofin.
	 *
	 * @param hORARIOFIN the hORARIOFIN to set
	 */
	public void setHORARIOFIN(String hORARIOFIN) {
		HORARIOFIN = hORARIOFIN;
	}



	/**
	 * Gets the nombre.
	 *
	 * @return the nOMBRE
	 */
	public String getNOMBRE() {
		return NOMBRE;
	}



	/**
	 * Sets the nombre.
	 *
	 * @param nOMBRE the nOMBRE to set
	 */
	public void setNOMBRE(String nOMBRE) {
		NOMBRE = nOMBRE;
	}



	/**
	 * Gets the valor.
	 *
	 * @return the vALOR
	 */
	public String getVALOR() {
		return VALOR;
	}



	/**
	 * Sets the valor.
	 *
	 * @param vALOR the vALOR to set
	 */
	public void setVALOR(String vALOR) {
		VALOR = vALOR;
	}



	/**
	 * Gets the descripcion.
	 *
	 * @return the dESCRIPCION
	 */
	public String getDESCRIPCION() {
		return DESCRIPCION;
	}



	/**
	 * Sets the descripcion.
	 *
	 * @param dESCRIPCION the dESCRIPCION to set
	 */
	public void setDESCRIPCION(String dESCRIPCION) {
		DESCRIPCION = dESCRIPCION;
	}



	/** The descripcion. */
	private String DESCRIPCION;
	

	/**
	 * Gets the numhabitacion.
	 *
	 * @return the nUMHABITACION
	 */
	public int getNUMHABITACION() {
		return NUMHABITACION;
	}



	/**
	 * Sets the numhabitacion.
	 *
	 * @param nUMHABITACION the nUMHABITACION to set
	 */
	public void setNUMHABITACION(int nUMHABITACION) {
		NUMHABITACION = nUMHABITACION;
	}



	/**
	 * Gets the indice.
	 *
	 * @return the iNDICE
	 */
	public double getINDICE() {
		return INDICE;
	}



	/**
	 * Sets the indice.
	 *
	 * @param iNDICE the iNDICE to set
	 */
	public void setINDICE(double iNDICE) {
		INDICE = iNDICE;
	}



	/**
	 * Instantiates a new clase asistente.
	 */
	public ClaseAsistente()
	{
		
	}
	


	/**
	 * Gets the conteoservicios.
	 *
	 * @return the cONTEOSERVICIOS
	 */
	public int getCONTEOSERVICIOS() {
		return CONTEOSERVICIOS;
	}



	/**
	 * Sets the conteoservicios.
	 *
	 * @param cONTEOSERVICIOS the cONTEOSERVICIOS to set
	 */
	public void setCONTEOSERVICIOS(int cONTEOSERVICIOS) {
		CONTEOSERVICIOS = cONTEOSERVICIOS;
	}



	/**
	 * Gets the idservicios.
	 *
	 * @return the iDSERVICIOS
	 */
	public int getIDSERVICIOS() {
		return IDSERVICIOS;
	}



	/**
	 * Sets the idservicios.
	 *
	 * @param iDSERVICIOS the iDSERVICIOS to set
	 */
	public void setIDSERVICIOS(int iDSERVICIOS) {
		IDSERVICIOS = iDSERVICIOS;
	}



	/**
	 * Gets the dineroperiodoespecifico.
	 *
	 * @return the dINEROPERIODOESPECIFICO
	 */
	public int getDINEROPERIODOESPECIFICO() {
		return DINEROPERIODOESPECIFICO;
	}



	/**
	 * Gets the nombreservicios.
	 *
	 * @return the nOMBRESERVICIOS
	 */
	public String getNOMBRESERVICIOS() {
		return NOMBRESERVICIOS;
	}



	/**
	 * Sets the nombreservicios.
	 *
	 * @param nOMBRESERVICIOS the nOMBRESERVICIOS to set
	 */
	public void setNOMBRESERVICIOS(String nOMBRESERVICIOS) {
		NOMBRESERVICIOS = nOMBRESERVICIOS;
	}



	/**
	 * Sets the dineroperiodoespecifico.
	 *
	 * @param dINEROPERIODOESPECIFICO the dINEROPERIODOESPECIFICO to set
	 */
	public void setDINEROPERIODOESPECIFICO(int dINEROPERIODOESPECIFICO) {
		DINEROPERIODOESPECIFICO = dINEROPERIODOESPECIFICO;
	}



	/**
	 * Gets the idhabitacion.
	 *
	 * @return the iDHABITACION
	 */
	public int getIDHABITACION() {
		return IDHABITACION;
	}



	/**
	 * Sets the idhabitacion.
	 *
	 * @param iDHABITACION the iDHABITACION to set
	 */
	public void setIDHABITACION(int iDHABITACION) {
		IDHABITACION = iDHABITACION;
	}



	



	/**
	 * Gets the dineroaniocorrido.
	 *
	 * @return the dINEROANIOCORRIDO
	 */
	public int getDINEROANIOCORRIDO() {
		return DINEROANIOCORRIDO;
	}



	/**
	 * Sets the dineroaniocorrido.
	 *
	 * @param dINEROANIOCORRIDO the dINEROANIOCORRIDO to set
	 */
	public void setDINEROANIOCORRIDO(int dINEROANIOCORRIDO) {
		DINEROANIOCORRIDO = dINEROANIOCORRIDO;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ClaseAsistente [ID=" + ID + ", IDHOTEL=" + IDHOTEL + ", CATEGORIA=" + CATEGORIA + ", ESTAINCLUIDO="
				+ ESTAINCLUIDO + ", HORARIOINICIO=" + HORARIOINICIO + ", HORARIOFIN=" + HORARIOFIN + ", NOMBRE="
				+ NOMBRE + ", VALOR=" + VALOR + ", DESCRIPCION=" + DESCRIPCION + "]";
	}




	/**
	 * To string 2.
	 *
	 * @return the string
	 */
	public String toString2() {
		return "ClaseAsistente [ID=" + ID + ", IDUSUARIO=" + IDUSUARIO + ", IDTIPOIDENTIFICACION="
				+ IDTIPOIDENTIFICACION + ", IDSERVICIO=" + IDSERVICIO + ", FECHA=" + FECHA + ", PAGADO=" + PAGADO
				+ ", PRECIO=" + PRECIO + ", DESCRIPCION=" + DESCRIPCION + "]";
	}

/**
 * To string 3.
 *
 * @return the string
 */
public String toString3()
{
	return "ID= "+ID+", Fecha: "+FECHA+", Pagado: "+PAGADO+", Precio: "+PRECIO+", Id reserva: "+IDRESERVA+", Id usuario: "+IDUSUARIO+", Tipo id usuario: "+TIPOIDUSUARIO;
}



	
}
