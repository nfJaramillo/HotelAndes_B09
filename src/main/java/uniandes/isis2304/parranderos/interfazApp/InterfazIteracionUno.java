package uniandes.isis2304.parranderos.interfazApp;

/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad	de los Andes (BogotÃ¡ - Colombia)
 *
 * Departamento	de	IngenierÃ­a	de	Sistemas y ComputaciÃ³n
 * Licenciado bajo el esquema Academic Free License versiÃ³n 2.1
 * 
 * Curso: isis2304 - Sistemas Transaccionales
 * Proyecto: Parranderos Uniandes
 * @version 1.0
 * @author GermÃ¡n Bravo
 * Julio de 2018
 * Revisado por: Claudia JimÃ©nez, Christian Ariza
 *
 * Modificado por Juan Pablo Correa y NicolÃ¡s Jaramillo en marzo del 2019
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.jdo.JDODataStoreException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.apache.log4j.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import uniandes.isis2304.parranderos.negocio.Gasto;
import uniandes.isis2304.parranderos.negocio.ReservaServicio;
import uniandes.isis2304.parranderos.negocio.ReservasDeAlojamiento;
import uniandes.isis2304.parranderos.negocio.TipoPersona;
import uniandes.isis2304.parranderos.persistencia.*;

/**
 * Clase principal de la interfaz
 * @author GermÃ¡n Bravo
 * Modificado por Juan Pablo Correa y NicolÃ¡s Jaramillo
 */
@SuppressWarnings("serial")
public class InterfazIteracionUno extends JFrame implements ActionListener
{
	//~~~~~~~~~~~~~~
	//  CONSTANTES
	//~~~~~~~~~~~~~~
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(InterfazIteracionUno.class.getName());

	/**
	 * Ruta al archivo de configuración de la interfaz
	 */
	private static final String CONFIG_INTERFAZ = "./src/main/resources/config/interfaceConf.json";

	/**
	 * Ruta al archivo de configuración de la interfaz
	 */
	private static final String RUTA_BANNER = "./src/main/resources/config/bannerHotelAndes.png";

	/**
	 * Constante para modelar el rol administrador
	 */
	private final static int ADMIN = 0;

	/**
	 * Constante para modelar el rol cliente
	 */
	private final static int CLIENTE = 1;

	/**
	 * Constante para modelar el rol organizador
	 */
	private final static int ORGANIZADOR = 2;


	//~~~~~~~~~~~~~~
	//   ATRIBUTOS
	//~~~~~~~~~~~~~~
	/**
	 * AsociaciÃ³n a la clase de persistencia del negocio
	 */
	private PersistenciaHotelAndes persistencia;

	/**
	 * Objeto JSON con la configuraciÃ³n de interfaz de la app
	 */
	private JsonObject guiConfig;

	/**
	 * Panel de despliegue de interacciÃ³n para los requerimientos
	 */
	private PanelDatos panelDatos;

	/**
	 * MenÃº de la aplicaciÃ³n
	 */
	private JMenuBar menuBar;

	/**
	 * Atributo para modelar el rol de quien interactÃºa con la aplicaciÃ³n
	 * 0 - Administrador
	 * 1 - Cliente
	 * 2 - Organizador de eventos
	 */
	private int rol;


	//~~~~~~~~~~~~~~
	//   METODOS
	//~~~~~~~~~~~~~~
	/**
	 * Construye la ventana principal de la aplicaciÃ³n. <br>
	 * <b>post:</b> Todos los componentes de la interfaz fueron inicializados.
	 */
	public InterfazIteracionUno( )
	{
		// Carga la configuraciÃ³n de la interfaz desde un archivo JSON
		guiConfig = openConfig ("Interfaz", CONFIG_INTERFAZ);

		// Pregunta por el rol del usuario que interactÃºa con la aplicaciÃ³n
		preguntarPorElRol();

		// Configura la apariencia del frame que contiene la interfaz grÃ¡fica
		configurarFrame();

		// Crea el menÃº
		if (guiConfig != null)
			crearMenu( guiConfig.getAsJsonArray("menuBar") );

		// Inicializa la persistencia
		persistencia = PersistenciaHotelAndes.getInstance( );

		panelDatos = new PanelDatos ( );

		setLayout (new BorderLayout());
		add( new JLabel(new ImageIcon(RUTA_BANNER)), BorderLayout.NORTH );          
		add( panelDatos, BorderLayout.CENTER );
	}

	private void preguntarPorElRol()
	{
		Object[] options = { "Administrador", "Cliente", "Organizador de eventos" };
		rol= JOptionPane.showOptionDialog(this, "                                                 ¿Cual es su rol?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
	}

	/**
	 * Registra un nuevo rol con la informaciÃ³n dada por el usuario
	 * <Post> Se crea una nueva tupla en la tabla ROLES, lo cual implica ciertos permisos
	 * y restricciones en lo que puede o no hacer en el sistema dicho usuario. 
	 */
	public void registrarRolDeUsuario()
	{
		try 
		{
			String tipoDeRol = JOptionPane.showInputDialog ( this, "¿Cual es el tipo de rol?", "Tipo de rol", JOptionPane.QUESTION_MESSAGE );
			String info = JOptionPane.showInputDialog ( this, "¿Cual es el tipo de rol?", "Identificador", JOptionPane.QUESTION_MESSAGE );

			int idDelRol = -1;

			try
			{
				idDelRol = Integer.parseInt( info ); 
			}
			catch (Exception e)
			{
				throw new Exception( "Por favor ingrese un número de id válido." );
			}

			if ( tipoDeRol != null && idDelRol > 0 )
			{
				TipoPersona tp = persistencia.registrarRolDeUsuario( tipoDeRol, idDelRol );

				if (tp == null)
				{
					throw new Exception ( "No se pudo registrar un rol de usuario con tipo " + tipoDeRol );
				}

				String resultado = "En registrarRolDeUsuario\n\n";
				resultado += "Rol de usuario adicionado exitosamente: " + tp;
				resultado += "\n\nOperación terminada.";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("OperaciÃ³n cancelada por el usuario");
			}
		}
		catch (Exception e) 
		{
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}


	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//   CRUD DE LOS REQUERIMIENTOS FUNCIONALES DE MODIFICACIÃ“N
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * RF7 - Registra una reserva de alojamiento
	 */
	public void registrarUnaReservaDeAlojamiento()
	{
		try 
		{
			verificarRol( CLIENTE ); // Esta función está permitida sólo para clientes

			long idHabitacion;
			long idHotel;
			String fechaLlegada;
			String fechaSalida;
			long idPlanDeConsumo;
			String[] tiposDeIdentificaciones;
			long[] identificaciones;

			try
			{
				idHabitacion = Long.parseLong( JOptionPane.showInputDialog( this, "Identificador de la habitaciÃ³n:", "ID habitaciÃ³n", JOptionPane.QUESTION_MESSAGE ) );
				idHotel = Long.parseLong( JOptionPane.showInputDialog( this, "Identificador del hotel:", "ID hotel", JOptionPane.QUESTION_MESSAGE ) );
				fechaLlegada =  JOptionPane.showInputDialog( this, "Fecha de llegada (formato DD/MM/AAAA, p.e. 31/01/2019):", "Fecha de llegada", JOptionPane.QUESTION_MESSAGE );
				fechaSalida =  JOptionPane.showInputDialog( this, "Fecha de salida (formato DD/MM/AAAA, p.e. 31/01/2019):", "Fecha de salida", JOptionPane.QUESTION_MESSAGE ) ;
				idPlanDeConsumo = Long.parseLong( JOptionPane.showInputDialog( this, "Identificador del plan de consumo:", "ID plan de consumo", JOptionPane.QUESTION_MESSAGE ) );

				// Lo siguiente guarda la informaciÃ³n de las personas en dos arreglos
				String[] aux = JOptionPane.showInputDialog( this, "Datos de las personas (tipoIdentificacionPersona1:idPersona1;...)", "InformaciÃ³n de las personas", JOptionPane.QUESTION_MESSAGE ).split(";");

				tiposDeIdentificaciones = new String[ aux.length ];
				identificaciones = new long[ aux.length ];

				for( int i = 0; i < aux.length; i++ )
				{
					String[] aux2 = aux[i].split(":");
					tiposDeIdentificaciones[i] = aux2[0];
					identificaciones[i] = Long.parseLong( aux2[1] );
				}
			}
			catch (Exception e)
			{
				throw new Exception( "Error convirtiendo uno de los datos de entrada: " + e.getMessage() );
			}

			ReservasDeAlojamiento ra = persistencia.registrarReservaDeAlojamiento( idHabitacion, idHotel, fechaLlegada, null, fechaSalida, null, idPlanDeConsumo, tiposDeIdentificaciones, identificaciones );				

			if( ra == null )
				throw new Exception( "No se pudo crear una reserva de alojamiento en la habitaciÃ³n " + idHabitacion );

			String resultado = "\n-> En RF7 registrarUnaReservaDeAlojamiento:";
			resultado += "\n       Reserva de alojamiento registrada exitosamente:";
			resultado += "\n         InformaciÃ³n de la reserva: " + ra;
			resultado += "\n\nOperaciÃ³n terminada.";
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
			panelDatos.actualizarInterfaz( generarMensajeError(e) );
		}
	}

	/**
	 * RF8 - Registra una reserva de un servicio del hotel por parte de un cliente
	 * SÃ³lo los clientes pueden hacer esta acciÃ³n
	 */
	public void registarUnaReservaDeUnServicioDelHotelPorParteDeUnCliente()
	{
		try 
		{
			verificarRol( CLIENTE ); // Esta función está permitida sólo para clientes

			String descripcion;
			String fechaSalida;
			String fechaLlegada;
			long idServicio;
			long idPersona;
			String tipoIDPersona;

			try
			{
				idServicio = Long.parseLong( JOptionPane.showInputDialog( this, "Identificador del servicio", "ID del servicio", JOptionPane.QUESTION_MESSAGE ) );
				idPersona = Long.parseLong( JOptionPane.showInputDialog( this, "Identificador del cliente:", "ID cliente", JOptionPane.QUESTION_MESSAGE ) );
				tipoIDPersona =  JOptionPane.showInputDialog( this, "Tipo de identificador:", "Tipo id", JOptionPane.QUESTION_MESSAGE );
				fechaLlegada =  JOptionPane.showInputDialog( this, "Fecha de llegada (formato DD/MM/AAAA, p.e. 31/01/2019):", "Fecha de llegada", JOptionPane.QUESTION_MESSAGE );
				fechaSalida =  JOptionPane.showInputDialog( this, "Fecha de salida (formato DD/MM/AAAA, p.e. 31/01/2019):", "Fecha de salida", JOptionPane.QUESTION_MESSAGE );
				descripcion = JOptionPane.showInputDialog( this, "DescripciÃ³n adicional:", "DescripciÃ³n", JOptionPane.QUESTION_MESSAGE );
			}
			catch (Exception e)
			{
				throw new Exception( "Error convirtiendo uno de los datos de entrada: " + e.getMessage() );
			}

			ReservaServicio ra = persistencia.registrarReservaDeServicio( descripcion, fechaSalida, fechaLlegada,  idServicio, idPersona,  tipoIDPersona );

			if( ra == null )
				throw new Exception( "No se pudo crear una reserva de servicio con el identificador " + idServicio );

			String resultado = "\n-> En RF7 registrarUnaReservaDeAlojamiento:";
			resultado += "     \n       Reserva de alojamiento registrada exitosamente:\n      ----> " + ra;
			resultado += "\n\nOperaciÃ³n terminada.";
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
			panelDatos.actualizarInterfaz( generarMensajeError(e) );
		}
	}

	/**
	 * RF9 - Registra una llegada de un cliente al hotel
	 * SÃ³lo los recepcionistas (administradores) pueden ejecutar esta acciÃ³n 
	 */
	public void registrarLaLLegadaDeUnClienteAlHotel()
	{
		try 
		{
			verificarRol( ADMIN ); // Esta función está permitida sólo para administradores

			long idReserva;

			try
			{
				idReserva = Long.parseLong( JOptionPane.showInputDialog( this, "Identificador de la reserva:", "ID reserva", JOptionPane.QUESTION_MESSAGE ) );
			}
			catch (Exception e)
			{
				throw new Exception( "Error convirtiendo uno de los datos de entrada, por favor verifique que los formatos sean correctos." );
			}

			persistencia.checkIn( idReserva, darFechaDeHoy() );

			String resultado = "\n-> En RF9 registrarUnaReservaDeAlojamiento:";
			resultado += "\n       Se ha registrado exitosamente la llegada del cliente.";
			resultado += "\n\nOperaciÃ³n terminada.";
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
			panelDatos.actualizarInterfaz( generarMensajeError(e) );
		}
	}

	/**
	 * RF10 - Registra un consumo de un servicio del hotel por parte de un cliente o sus acompaÃ±antes
	 * SÃ³lo los administradores pueden ejecutar esta acciÃ³n
	 */
	public void registrarUnConsumoDeUnServicioDelHotelPorParteDeUnClienteOSusAcompanantes()
	{
		try 
		{
			verificarRol( ADMIN ); // Esta función está permitida sólo para administradores

			long idUsuario;
			long idReservaServicio;
			int seCargaALaHabitacion = -1;

			try
			{
				idUsuario = Long.parseLong( JOptionPane.showInputDialog( this, "Identificador de la reserva:", "ID reserva", JOptionPane.QUESTION_MESSAGE ) );
				idReservaServicio = Long.parseLong( JOptionPane.showInputDialog( this, "Identificador de la reserva del servicio:", "ID reserva servicio", JOptionPane.QUESTION_MESSAGE ) );

				seCargaALaHabitacion = Integer.parseInt(JOptionPane.showInputDialog(this, "Escriba 0, si el usuario ya lo pago, 1 si lo carga a su cuenta o 2 si lo carga a la cuenta de la convencion", "¿Desea pagar el servicio, cargarlo a su cuenta o cargarlo a la cuenta de su convenvion (Si esta en una) ?", JOptionPane.QUESTION_MESSAGE));

			}
			catch (Exception e)
			{
				throw new Exception( "Error convirtiendo uno de los datos de entrada, por favor verifique que los formatos sean los correctos." );
			}

			Gasto g = persistencia.registrarConsumo(  idUsuario, idReservaServicio, darFechaDeHoy(), seCargaALaHabitacion );

			if( g == null )
				throw new Exception( "No se pudo registrar un consumo." );

			String resultado = "\n-> En RF10 registrarUnConsumoDeUnServicioDelHotelPorParteDeUnClienteOSusAcompanantes:";
			resultado += "\n       Se ha solicitado exitosamente el consumo.";
			resultado += "\n\nOperaciÃ³n terminada.";
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
			panelDatos.actualizarInterfaz( generarMensajeError(e) );
		}
	}

	/**
	 * RF11 - Registra la salida de un cliente del hotel
	 * SÃ³lo los recepcionistas (administradores) pueden ejecutar esta acciÃ³n
	 */
	public void registarLaSalidaDeUnCliente()
	{
		try
		{
			verificarRol( ADMIN ); // Esta función está permitida sólo para administradores

			long idReserva;
			int pagoCuentaRestante;

			try
			{
				idReserva = Long.parseLong( JOptionPane.showInputDialog( this, "Identificador de la reserva:", "ID reserva", JOptionPane.QUESTION_MESSAGE ) );

				int loQueDebePagar = persistencia.cuentaAPagar( idReserva, 1 );
				pagoCuentaRestante = (int) Double.parseDouble( JOptionPane.showInputDialog( this, "Usted debe un total de $" + loQueDebePagar + "\nÂ¿CuÃ¡nto va a pagar por los gastos?", "Pago", JOptionPane.QUESTION_MESSAGE ) );
				if( pagoCuentaRestante < loQueDebePagar ) throw new Exception( "El dinero no alcanza para pagar la reserva. No se puede dar salida sin cancelar la totalidad." );
			}
			catch (Exception e)
			{
				throw new Exception( "Error convirtiendo uno de los datos de entrada, por favor verifique que los formatos sean los correctos." );
			}

			persistencia.checkOut( idReserva, darFechaDeHoy() );

			String resultado = "\n-> En RF11 registarLaSalidaDeUnCliente:";
			resultado += "     \n       Se ha dado salida a los clientes de la reserva '" + idReserva + "'.";
			resultado += "\n\nOperaciÃ³n terminada.";
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
			panelDatos.actualizarInterfaz( generarMensajeError(e) );
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//  CRUD DE LOS REQUERIMIENTOS FUNCIONALES DE CONSULTA
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * RFC1 - Muestra el dinero recolectado por servicios por servicios en cada
	 * habitaciÃ³n durante un periodo de tiempo y el aÃ±o corrido.
	 */
	public void mostrarElDineroRecolectado()
	{
		try 
		{
			long idHotel;
			String fechaInicio;
			String fechaFin;

			try
			{
				idHotel = Long.parseLong( JOptionPane.showInputDialog( this, "Identificador del hotel:", "ID hotel", JOptionPane.QUESTION_MESSAGE ) );
				fechaInicio = JOptionPane.showInputDialog( this, "Fecha inicial (formato DD/MM/AAAA, p.e. 31/01/2019):", "Fecha inicial", JOptionPane.QUESTION_MESSAGE );
				fechaFin = JOptionPane.showInputDialog( this, "Fecha final (formato DD/MM/AAAA, p.e. 31/03/2019):", "Fecha final", JOptionPane.QUESTION_MESSAGE );
			}
			catch (Exception e)
			{
				throw new Exception( "Error convirtiendo uno de los datos de entrada, por favor verifique que los formatos sean los correctos." );
			}

			List<ClaseAsistente> lista = persistencia.RFC1( idHotel, fechaInicio, fechaFin, darFechaDeHoy() );

			String resultado = "\n-> En RFC1 mostrarElDineroRecolectado:\n\n\n";
			resultado       += "         El dinero recolectado por cada habitaciÃ³n del hotel de id '" + idHotel + "' fue de:\n\n";
			resultado       +=        "\tID de la habitaciÃ³n \t Dinero recolectado entre el \t\t Dinero en el aÃ±o corrido (" + darFechaDeHoy().substring(6) + ")\n";
			resultado       +=        "\t                    \t\t" + fechaInicio + " y el" + fechaFin + "\n";
			resultado       +=        "\t------------------------------------------------------------------------------------------------------------------------------------------------------\n";

			for( ClaseAsistente ca : lista )
				resultado += "\t" + ca.getIDHABITACION() + "\t\t $" + ca.getDINEROPERIODOESPECIFICO()  + "\t\t\t $" + ca.getDINEROANIOCORRIDO() +"\n";

			resultado += "\n\n\n\n-  [RFC1] OperaciÃ³n terminada.";
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
			panelDatos.actualizarInterfaz( generarMensajeError(e) );
		}
	}

	/**
	 * RFC2 - Muestra los 20 servicios que mÃ¡s fueron consumidos en un periodo de tiempo dado.
	 */
	public void mostrar20ServiciosMasPopulares()
	{
		try 
		{
			long idHotel;
			String fechaInicio;
			String fechaFin;

			try
			{
				idHotel = Long.parseLong( JOptionPane.showInputDialog( this, "Identificador del hotel:", "ID hotel", JOptionPane.QUESTION_MESSAGE ) );
				fechaInicio = JOptionPane.showInputDialog( this, "Fecha inicial (formato DD/MM/AAAA, p.e. 31/01/2019):", "Fecha inicial", JOptionPane.QUESTION_MESSAGE );
				fechaFin = JOptionPane.showInputDialog( this, "Fecha final (formato DD/MM/AAAA, p.e. 31/03/2019):", "Fecha final", JOptionPane.QUESTION_MESSAGE );
			}
			catch (Exception e)
			{
				throw new Exception( "Error convirtiendo uno de los datos de entrada, por favor verifique que los formatos sean los correctos." );
			}

			List<ClaseAsistente> lista = persistencia.RFC2( idHotel, fechaInicio, fechaFin );

			String resultado = "\n-> En RFC2 mostrar20ServiciosMasPopulares:\n\n\n";
			resultado       += "         Los veinte servicios mÃ¡s populares del hotel de id '" + idHotel + "' fue de:\n\n";
			resultado       +=        "\tID del servicio     \t Nombre del servicio         \t\t NÃºmero de apariciones\n";
			resultado       +=        "\t----------------------------------------------------------------------------------------------------------------------------------------------\n";

			for( ClaseAsistente ca : lista )
				resultado += "\t" + ca.getIDSERVICIOS() + "\t\t  " + ca.getIDSERVICIOS()  + "\t\t\t  " + ca.getCONTEOSERVICIOS() +"\n";

			resultado += "\n\n\n\n  [RFC2] OperaciÃ³n terminada.";
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
			panelDatos.actualizarInterfaz( generarMensajeError(e) );
		}
	}

	/**
	 * 
	 */
	public void mostrarIndiceOcupacion()
	{
		try 
		{
			long idHotel;

			try
			{
				idHotel = Long.parseLong( JOptionPane.showInputDialog( this, "Identificador del hotel:", "ID hotel", JOptionPane.QUESTION_MESSAGE ) );
			}
			catch (Exception e)
			{
				throw new Exception( "Error convirtiendo el dato de entrada, por favor verifique que el formato sean el correcto." );
			}

			List<ClaseAsistente> lista = persistencia.RFC3( idHotel, darFechaDeHoy() );

			String resultado = "\n-> En RFC3 mostrarIndiceOcupacion:\n\n\n";
			resultado       +=        "\tNÃºmero de habitaciÃ³n\tÃ�ndice \n";
			resultado       +=        "\t----------------------------------------------------------------------------------------------------------------------------------------------\n";
			//indice numhabitacion
			for( ClaseAsistente ca : lista )
				resultado += "\t" + ca.getNUMHABITACION() + "\t\t  " + ca.getINDICE() + "\n";

			resultado += "\n\n\n\n  [RFC2] OperaciÃ³n terminada.";
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
			panelDatos.actualizarInterfaz( generarMensajeError(e) );
		}
	}

	public void RFC4()
	{
		try 
		{
			char opcion;

			try
			{
				opcion = JOptionPane.showInputDialog( this, "Especifique la caracterÃ­stica sobre la que desea filtrar\n\n Opciones:\n    P para precio.\n    F para fecha.\n    T para tipo.\n    C para consumo.", "Filtrar servicios", JOptionPane.QUESTION_MESSAGE ).charAt(0);

				List<ClaseAsistente> respuesta = null;

				switch (opcion)
				{
				case 'P': // PRECIO
					int precio1 = Integer.parseInt( JOptionPane.showInputDialog( this, "Precio 1:", "RFC4", JOptionPane.QUESTION_MESSAGE ) );
					int precio2 = Integer.parseInt( JOptionPane.showInputDialog( this, "Precio 2:", "RFC4", JOptionPane.QUESTION_MESSAGE ) );				
					respuesta = persistencia.RFC4PRECIO( precio1, precio2 );
					break;
				case 'F': // FECHA
					String fechaInicio = JOptionPane.showInputDialog( this, "Fecha inicial (formato DD/MM/AAAA, p.e. 31/01/2019):", "Fecha inicial", JOptionPane.QUESTION_MESSAGE );
					String fechaFin = JOptionPane.showInputDialog( this, "Fecha final (formato DD/MM/AAAA, p.e. 31/03/2019):", "Fecha final", JOptionPane.QUESTION_MESSAGE );
					respuesta = persistencia.RFC4FECHA( fechaInicio, fechaFin );
					break;
				case 'T': // TIPO
					String tipo = JOptionPane.showInputDialog( this, "Ingrese un tipo:", "Tipo", JOptionPane.QUESTION_MESSAGE );
					respuesta = persistencia.RFC4TIPO( tipo );
					break;
				case 'C': // CONSUMO
					int contador = Integer.parseInt( JOptionPane.showInputDialog( this, "Â¿CuÃ¡ntas veces han usado ese consumo?:", "RFC4", JOptionPane.QUESTION_MESSAGE ) );
					String fecha1 = JOptionPane.showInputDialog( this, "Fecha inicial (formato DD/MM/AAAA, p.e. 31/01/2019):", "Fecha inicial", JOptionPane.QUESTION_MESSAGE );
					String fecha2 = JOptionPane.showInputDialog( this, "Fecha final (formato DD/MM/AAAA, p.e. 31/03/2019):", "Fecha final", JOptionPane.QUESTION_MESSAGE );
					respuesta = persistencia.RFC4CONSUMO( contador, fecha1, fecha2 );
					break;
				}

				String resultado = "\n-> En RFC4 mostrarIndiceOcupacion:\n\n\n";

				for( ClaseAsistente ca : respuesta )
					resultado += "\t" + ca.toString() + "\n";

				resultado += "\n\n\n\n  [RFC4] OperaciÃ³n terminada.";
				panelDatos.actualizarInterfaz(resultado);
			}
			catch (Exception e)
			{
				throw new Exception( "IngresÃ³ una opciÃ³n que no estÃ¡ permitida, o uno de los valores no siguiÃ³ el formato que deberÃ­a." );
			}
		} 
		catch (Exception e) 
		{
			panelDatos.actualizarInterfaz( generarMensajeError(e) );
		}
	}

	/**
	 * rfc5
	 */
	public void mostrarConsumoUsuarioFechas()
	{
		try
		{
			String fecha1;
			String fecha2;
			Long idPersona;
			String tipoIDPersona;

			try
			{
				tipoIDPersona =  JOptionPane.showInputDialog( this, "Tipo de identificador:", "Tipo id", JOptionPane.QUESTION_MESSAGE );
				idPersona = Long.parseLong( JOptionPane.showInputDialog( this, "Identificador del cliente:", "ID cliente", JOptionPane.QUESTION_MESSAGE ) );
				fecha1=JOptionPane.showInputDialog( this, "Fecha 1 (formato DD/MM/AAAA, p.e. 31/01/2019):", "Fecha 1", JOptionPane.QUESTION_MESSAGE );
				fecha2 =  JOptionPane.showInputDialog( this, "Fecha 2 (formato DD/MM/AAAA, p.e. 31/01/2019):", "Fecha 2", JOptionPane.QUESTION_MESSAGE );
			}
			catch (Exception e)
			{
				throw new Exception( "Error convirtiendo un dato de entrada, por favor verifique que los formatos sean los correctos." );
			}


			List<ClaseAsistente> lista = persistencia.RFC5( idPersona.intValue(), tipoIDPersona, fecha1, fecha2 );


			String resultado = "\n-> En RFC5 mostrarConsumoUsuarioFechas:\n\n\n";
			resultado       +=        "\t-----------------------------------------------------\n";

			for( ClaseAsistente ca : lista )
				resultado += "\t" + ca.toString3() + "\n";

			resultado += "\n\n\n\n  [RFC5] OperaciÃ³n terminada.";
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
			panelDatos.actualizarInterfaz( generarMensajeError(e) );
		}
	}



	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Requerimientos de modificacion de la Iteración 2
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


	public void RF12()
	{
		try
		{
			verificarRol( ADMIN ); // Esta función está permitida sólo para administradores

			// Definicio de variables
			int idHotel;
			int planDeConsumo;
			String fecha1;
			String fecha2;
			ArrayList<Integer> habitaciones = new ArrayList<>();
			ArrayList<Integer> cantHabitaciones = new ArrayList<>();
			ArrayList<Integer> servicios = new ArrayList<>();

			try
			{
				// Pedido de datos al usuario
				idHotel = Integer.valueOf( JOptionPane.showInputDialog( this, "Identificador del hotel:", "ID hotel", JOptionPane.QUESTION_MESSAGE ));
				planDeConsumo = Integer.valueOf( JOptionPane.showInputDialog( this, "Identificador del plan de consumo:", "ID plan de consumo", JOptionPane.QUESTION_MESSAGE ) );
				fecha1 =  JOptionPane.showInputDialog( this, "Fecha de llegada (formato DD/MM/AAAA, p.e. 31/01/2019):", "Fecha de llegada", JOptionPane.QUESTION_MESSAGE );
				fecha2 =  JOptionPane.showInputDialog( this, "Fecha de salida (formato DD/MM/AAAA, p.e. 31/01/2019):", "Fecha de salida", JOptionPane.QUESTION_MESSAGE ) ;

				// Lo siguiente guarda la informaciÃ³n de las personas en dos arreglos
				String[] aux = JOptionPane.showInputDialog( this, "Datos de las habitaciones (idTipoDeHabitacion:cantidad;...)", "Informacion de las habitaciones", JOptionPane.QUESTION_MESSAGE ).split(";");

				for( int i = 0; i < aux.length; i++ )
				{
					String[] aux2 = aux[i].split(":");
					habitaciones.add(Integer.valueOf(aux2[0]));
					cantHabitaciones.add(Integer.valueOf(aux2[1]));
				}

				String[] aux3 = JOptionPane.showInputDialog( this, "Datos de las servicios (idTipoServicio1;idTipoServicio2;...)", "Informacion de las habitaciones", JOptionPane.QUESTION_MESSAGE ).split(";");
				for (String string : aux3) {
					servicios.add(Integer.valueOf(string));
				}
			}
			catch (Exception e)
			{
				throw new Exception( "Error convirtiendo un dato de entrada, por favor verifique que los formatos sean los correctos." );
			}

			// Llamado a la persistencia

			ArrayList<Integer> resp = persistencia.RF12(idHotel, planDeConsumo, fecha1, fecha2, habitaciones, cantHabitaciones, servicios);
			String ans = "Habitaciones reservadas: "+'\n';
			for (Integer integer : resp) {
				if(integer != -1)
				{
					ans+=""+integer+'\n';
				}
				else
				{
					ans+="Id de servicios reservados: "+'\n';
				}

			}
			panelDatos.actualizarInterfaz(ans);

		}
		catch (Exception e)
		{
			panelDatos.actualizarInterfaz( generarMensajeError(e) );
		}
	}

	public void RF12B()
	{
		try
		{
			verificarRol( 1 );

			// Declaracion de variables 
			int idReserva;
			String[] tiposDeIdentificaciones;
			long[] identificaciones;
			// Peticion de datos
			idReserva = Integer.parseInt( JOptionPane.showInputDialog( this, "Identificador de su reserva:", "Id reserva", JOptionPane.QUESTION_MESSAGE ) );
			// Lo siguiente guarda la informaciÃ³n de las personas en dos arreglos
			String[] aux = JOptionPane.showInputDialog( this, "Datos de las personas (tipoIdentificacionPersona1:idPersona1;...)", "InformaciÃ³n de las personas", JOptionPane.QUESTION_MESSAGE ).split(";");

			tiposDeIdentificaciones = new String[ aux.length ];
			identificaciones = new long[ aux.length ];

			try
			{
				for( int i = 0; i < aux.length; i++ )
				{
					String[] aux2 = aux[i].split(":");
					tiposDeIdentificaciones[i] = aux2[0];
					identificaciones[i] = Long.parseLong( aux2[1] );
				}
			}
			catch (Exception e)
			{
				throw new Exception( "Error convirtiendo uno de los datos de entrada: " + e.getMessage() );
			}

			// Llamado a la persistencia
			persistencia.RF12B(idReserva, tiposDeIdentificaciones, identificaciones);
			panelDatos.actualizarInterfaz( "Operacion exitosa" );
		}
		catch (Exception e)
		{
			panelDatos.actualizarInterfaz( generarMensajeError(e) );
		}
	}

	public void RF13()
	{
		try
		{
			// Declaracion de variables 
			ArrayList<Integer> habitaciones = new ArrayList<>();
			ArrayList<Integer> servicios = new ArrayList<>();

			try
			{
				// Peticion de datos
				String[] aux = JOptionPane.showInputDialog( this, "Datos de las reservas de habitaciones a cancelar (idReserva1;idReserva2;...)", "Id de las reservas a cancelar", JOptionPane.QUESTION_MESSAGE ).split(";");
				for (String string : aux)
					habitaciones.add(Integer.valueOf(string));

				// Peticion de datos
				String[] aux1 = JOptionPane.showInputDialog( this, "Datos de las reservas de servicios a cancelar (idReserva1;idReserva2;...)", "Id de las reservas a cancelar", JOptionPane.QUESTION_MESSAGE ).split(";");
				for (String string : aux1)
					servicios.add(Integer.valueOf(string));

			}
			catch (Exception e)
			{
				throw new Exception( "Error convirtiendo uno de los datos de entrada: " + e.getMessage() );
			}

			//Llamado a la persistencia

			persistencia.RF13(habitaciones, servicios);
			panelDatos.actualizarInterfaz( "Operacion exitosa" );
		}
		catch (Exception e) {
			panelDatos.actualizarInterfaz( generarMensajeError(e) );
		}
	}


	/**
	 * Método que suple el requerimiento RF14, terminar una convención.
	 * Es equivalente a registrar la salida de un cliente del hotel, haciendo las
	 * verificaciones de estado de todas las habitaciones y servicios asociados a
	 * la convención y las cuentas de todos los consumos asociados a la misma
	 * (alimentación, alquiler de salas, por ejemplo).
	 */
	public void RF14()
	{
		try
		{
			verificarRol( ORGANIZADOR ); // Esta función está permitida sólo para organizadores de eventos
			ArrayList<Integer> idReservasHabitaciones = new ArrayList<>();
			ArrayList<Integer> idServicios = new ArrayList<>();

			try
			{
				String[] aux1 = JOptionPane.showInputDialog( this, "Datos de las reservas de habitaciones a cancelar (idReserva1;idReserva2;...)", "Id de las reservas a cancelar", JOptionPane.QUESTION_MESSAGE ).split(";");
				for (String string : aux1 )
					idReservasHabitaciones.add( Integer.valueOf(string) );

				String[] aux2 = JOptionPane.showInputDialog( this, "Datos de las reservas de servicios a cancelar (idReserva1;idReserva2;...)", "Id de las reservas a cancelar", JOptionPane.QUESTION_MESSAGE ).split(";");
				for (String string : aux2)
					idServicios.add( Integer.valueOf(string) );
			}
			catch (Exception e)
			{
				throw new Exception( "Error convirtiendo uno de los datos de entrada: " + e.getMessage() );
			}

			// Valida que estén a paz y salvo
			for (int i = 0; i < idReservasHabitaciones.size(); i++)
			{
				int loQueDebePagar = persistencia.cuentaAPagar( idReservasHabitaciones.get(i), 2 );
				if( loQueDebePagar > 0 ) throw new Exception( "No se puede cerrar la convención, la reserva de habitación con ID " + idReservasHabitaciones.get(i) + " aún tiene cuentas pendientes.");
			}

			// Registra el cierre de la convención
			persistencia.RF14( idReservasHabitaciones, idServicios, darFechaDeHoy() );

			String resultado = "\n-> En RF14 registarCierreDeConvención:";
			resultado += "     \n       Se ha dado salida a los clientes de las reservas '" + idReservasHabitaciones + "'.";

			resultado += "\n\nOperación terminada.";
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
			panelDatos.actualizarInterfaz( generarMensajeError(e) );
		}
	}

	public void RF14B()
	{
		try
		{
			verificarRol( ORGANIZADOR ); // Esta función está permitida sólo para organizadores de eventos

			int idReserva;

			try
			{
				String aux1 = JOptionPane.showInputDialog( this, "Datos de las reservas de habitaciones a cancelar (idReserva1;idReserva2;...)", "Id de las reservas a cancelar", JOptionPane.QUESTION_MESSAGE );
				idReserva = Integer.parseInt( aux1 );
			}
			catch (Exception e)
			{
				throw new Exception( "Error convirtiendo uno de los datos de entrada: " + e.getMessage() );
			}

			// Registra el cierre de la reserva
			persistencia.RF14B( idReserva, darFechaDeHoy() );

			String resultado = "\n-> En RF14B registarCierreDeConvenciónIndividual:";
			resultado += "     \n       Se ha dado salida al cliente de la reservas " + idReserva + ".";
			resultado += "\n\nOperación terminada.";
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
			panelDatos.actualizarInterfaz( generarMensajeError(e) );
		}
	}

	public void RF15()
	{
		try
		{
			verificarRol( ADMIN );

			//Declaracion de variables
			int idHotel;
			String fecha1;
			String fecha2;
			ArrayList<Integer> habitaciones = new ArrayList<>();
			ArrayList<Integer> servicios = new ArrayList<>();

			//Obtencion de datos 

			try
			{
				idHotel = Integer.valueOf( JOptionPane.showInputDialog( this, "Identificador del hotel:", "ID hotel", JOptionPane.QUESTION_MESSAGE ));
				fecha1 =  JOptionPane.showInputDialog( this, "Fecha de inicio (formato DD/MM/AAAA, p.e. 31/01/2019):", "Fecha de llegada", JOptionPane.QUESTION_MESSAGE );
				fecha2 =  JOptionPane.showInputDialog( this, "Fecha fin (formato DD/MM/AAAA, p.e. 31/01/2019):", "Fecha de salida", JOptionPane.QUESTION_MESSAGE ) ;

				// Lo siguiente guarda la informaciÃ³n de las personas en dos arreglos
				String[] aux = JOptionPane.showInputDialog( this, "Datos de las habitaciones (idHabitacion1;idHabitacion2;...)", "Informacion de las habitaciones para mantenimiento", JOptionPane.QUESTION_MESSAGE ).split(";");
				for (String string : aux) {
					habitaciones.add(Integer.parseInt(string));
				}
				// Lo siguiente guarda la informaciÃ³n de las personas en dos arreglos
				String[] aux1 = JOptionPane.showInputDialog( this, "Datos de los servicios (idServicio1;idServicio2;...)", "Informacion de los servicios para mantenimiento", JOptionPane.QUESTION_MESSAGE ).split(";");
				for (String string : aux1)
					servicios.add(Integer.parseInt(string));
			}
			catch(Exception e)
			{
				throw new Exception( "Error convirtiendo uno de los datos de entrada: " + e.getMessage() );
			}
			//Llamado a la persistencia

			persistencia.RF15(idHotel, fecha1, fecha2, habitaciones, servicios);
			panelDatos.actualizarInterfaz("Operacion exitosa");
		}
		catch (Exception e)
		{
			panelDatos.actualizarInterfaz( generarMensajeError(e) );
		}
	}


	public void RF16()
	{
		try
		{
			verificarRol( ADMIN );

			//Declaracion de variables
			int idHotel;

			ArrayList<Integer> habitaciones = new ArrayList<>();
			ArrayList<Integer> servicios = new ArrayList<>();

			//Obtencion de datos 

			try
			{
				idHotel = Integer.valueOf( JOptionPane.showInputDialog( this, "Identificador del hotel:", "ID hotel", JOptionPane.QUESTION_MESSAGE ));

				// Lo siguiente guarda la informaciÃ³n de las personas en dos arreglos
				String[] aux = JOptionPane.showInputDialog( this, "Datos de las habitaciones (idHabitacion1;idHabitacion2;...)", "Informacion de las habitaciones para finalizar mantenimiento", JOptionPane.QUESTION_MESSAGE ).split(";");
				for (String string : aux) {
					habitaciones.add(Integer.parseInt(string));
				}
				// Lo siguiente guarda la informaciÃ³n de las personas en dos arreglos
				String[] aux1 = JOptionPane.showInputDialog( this, "Datos de los servicios (idServicio1;idServicio2;...)", "Informacion de los servicios para finalizar mantenimiento", JOptionPane.QUESTION_MESSAGE ).split(";");
				for (String string : aux1)
					servicios.add(Integer.parseInt(string));
			}
			catch(Exception e)
			{
				throw new Exception( "Error convirtiendo uno de los datos de entrada: " + e.getMessage() );
			}
			//Llamado a la persistencia

			persistencia.RF16(idHotel, habitaciones, servicios);
			panelDatos.actualizarInterfaz("Operacion exitosa");
		}
		catch (Exception e) {
			panelDatos.actualizarInterfaz( generarMensajeError(e) );
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Requerimientos de consulta de la Iteración 2
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * RESERV
	 */
	public void RFC6()
	{
		try 
		{
			char criterio;

			try
			{
				// Se pide la unidad de tiempo
				criterio = JOptionPane.showInputDialog( this, "Especifique la unidad de tiempo.\n\n Opciones:\n    S para semana.\n    M para mes.\n    A para año.", "Filtrar temporalidad", JOptionPane.QUESTION_MESSAGE ).charAt(0);
				int tipoHabitacion = Integer.parseInt( JOptionPane.showInputDialog( this, "Especifique un id tipo de habitación.", "Filtrar tipo habitación", JOptionPane.QUESTION_MESSAGE ) );
				
				persistencia.RFC6(tipoHabitacion, criterio);
			}
			catch (Exception e)
			{
				throw new Exception( "Ingresó una opción que no está permitida, o uno de los valores no siguió el formato esperado." );
			}

			String resultado = "\n-> En RFC6 mostrarIndiceOcupacion:\n\n\n";

			resultado += "\n\n\n\n  [RFC6] Operación terminada.";
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
			panelDatos.actualizarInterfaz( generarMensajeError(e) );
		}
	}

	public void RFC7()
	{
		/*
		SELECT per.id, per.nombre, gas.precio
		FROM Gastos gas, Reservas_Servicios res, Personas per
		WHERE gas.precio > -1
    		AND gas.idreserva = res.id
		    AND res.idtipopersona = per.id
		    AND res.tipoidentificacion LIKE per.tipoidentificacion;
		 */
	}

	public void RFC8()
	{

	}


	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//   MÉTODOS PARA LA PRESENTACIÃ“N DE LOS DATOS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * Genera una cadena para indicar al usuario que hubo un error en la aplicaciÃ³n
	 * @param e - La excepciÃ³n generada
	 * @return La cadena con la informaciÃ³n de la excepciÃ³n y detalles adicionales
	 */
	private String generarMensajeError(Exception e) 
	{
		String resultado = "Error en la ejecuciÃ³n:\n";
		resultado += "----->" + e.getLocalizedMessage() + darDetalleException(e);
		return resultado;
	}

	/**
	 * Genera una cadena de caracteres con la descripciÃ³n de la excepcion e, haciendo Ã©nfasis en las excepciones de JDO
	 * @param e - La excepciÃ³n recibida
	 * @return La descripciÃ³n de la excepciÃ³n cuando es javax.jdo.JDODataStoreException o "" de lo contrario
	 */
	private String darDetalleException(Exception e) 
	{
		String resp = "";
		if( e.getClass().getName().equals("javax.jdo.JDODataStoreException") )
		{
			JDODataStoreException je = (javax.jdo.JDODataStoreException) e;
			return ". " + je.getNestedExceptions()[0].getMessage();
		}
		return resp;
	}

	/**
	 * Verifica el rol de quien interactÃºa con la aplicaciÃ³n, para permitirle o no ciertas acciones
	 * @param seEsperaQueSea Lo que se espera que sea el usuario
	 * @throws Exception Cuando se espera que el usuario tenga ciertos permisos y no los tiene
	 */
	private void verificarRol( int seEsperaQueSea ) throws Exception
	{
		for ( int i = 0; i < 3; i++ )
			if( seEsperaQueSea == i && rol != i ) throw new Exception( "¡Usted no cuenta con los permisos necesarios para ejecutar esta acción!" );
	}

	/**
	 * Abre el archivo dado como parÃ¡metro con la aplicaciÃ³n por defecto del sistema
	 * @param nombreArchivo - El nombre del archivo que se quiere mostrar
	 */
	private void mostrarArchivo( String nombreArchivo )
	{
		try
		{
			Desktop.getDesktop().open(new File(nombreArchivo));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * Retorna un String con la fecha del dÃ­a de hoy
	 * @return String de la fecha actual en formato dd/MM/AAAA
	 */
	private String darFechaDeHoy()
	{
		Date date = (Calendar.getInstance()).getTime();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
		return dateFormat.format(date);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//  MÃ‰TODOS DE INTERACCIÃ“N
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * MÃ©todo para la ejecuciÃ³n de los eventos que enlazan el menÃº con los mÃƒÂ©todos de negocio
	 * Invoca al mÃ©todo correspondiente segÃƒÂºn el evento recibido
	 * @param pEvento - El evento del usuario
	 */
	@Override
	public void actionPerformed(ActionEvent pEvento)
	{
		String evento = pEvento.getActionCommand( );		
		try 
		{
			Method req = InterfazIteracionUno.class.getMethod ( evento );			
			req.invoke ( this );
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~
	//   PROGRAMA PRINCIPAL 
	//~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * Este mÃ©todo ejecuta la aplicaciÃ³n, creando una nueva interfaz
	 * @param args Arreglo de argumentos que se recibe por lÃ­nea de comandos
	 */
	public static void main( String[] args )
	{
		try
		{	
			// Unifica la interfaz para Mac y para Windows.
			UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName( ) );
			InterfazIteracionUno interfaz = new InterfazIteracionUno( );

			// Crea y centra la interfaz
			interfaz.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			interfaz.setLocationRelativeTo(null);
			interfaz.setVisible(true);
		}
		catch( Exception e )
		{
			e.printStackTrace( );
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//   MÃ‰TODOS DE CONFIGURACIÃ“N PARA LA INTERFAZ
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * Lee datos de configuraciÃ³n para la aplicaciÃ³n, a partir de un archivo JSON o con valores por defecto si hay errores.
	 * @param tipo - El tipo de configuraciÃ³n deseada
	 * @param archConfig - Archivo Json que contiene la configuraciÃ³n
	 * @return Un objeto JSON con la configuraciÃ³n del tipo especificado
	 * 			NULL si hay un error en el archivo.
	 */
	private JsonObject openConfig (String tipo, String archConfig)
	{
		JsonObject config = null;
		try 
		{
			Gson gson = new Gson( );
			FileReader file = new FileReader (archConfig);
			JsonReader reader = new JsonReader ( file );
			config = gson.fromJson(reader, JsonObject.class);
			log.info ( "Se encontrÃ³ un archivo de configuraciÃ³n vÃ¡lido: " + tipo );
		} 
		catch (Exception e)
		{	
			log.info ( "No se encontrÃ³ un archivo de configuraciÃ³n vÃ¡lido" );			
			JOptionPane.showMessageDialog(null, "No se encontrÃ³ un archivo de configuraciÃ³n vÃ¡lido: " + tipo, "HotelAndes", JOptionPane.ERROR_MESSAGE );
		}	
		return config;
	}

	/**
	 * MÃ©todo para configurar el frame principal de la aplicaciÃ³n
	 */
	private void configurarFrame(  )
	{
		int alto = 0;
		int ancho = 0;
		String titulo = "";	

		if ( guiConfig == null )
		{
			log.info ( "Se aplica configuraciÃ³n por defecto" );			
			titulo = "Parranderos APP Default";
			alto = 300;
			ancho = 500;
		}
		else
		{
			log.info ( "Se aplica configuraciÃ³n indicada en el archivo de configuraciÃ³n" );
			titulo = guiConfig.get("title").getAsString();
			alto= guiConfig.get("frameH").getAsInt();
			ancho = guiConfig.get("frameW").getAsInt();
		}

		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setLocation (50,50);
		setResizable( true );
		setBackground( Color.WHITE );

		setTitle( titulo );
		setSize ( ancho, alto);        
	}

	/**
	 * MÃ©todo para crear el menÃº de la aplicaciÃ³n con base en el objeto JSON leÃ­do
	 * Genera una barra de menÃº y los menÃºs con sus respectivas opciones
	 * @param jsonMenu - Arreglo Json con los menÃºs deseados
	 */
	private void crearMenu( JsonArray jsonMenu )
	{   
		// CreaciÃ³n de la barra de menÃºs
		menuBar = new JMenuBar();       
		for (JsonElement men : jsonMenu)
		{
			// CreaciÃ³n de cada uno de los menÃºs
			JsonObject jom = men.getAsJsonObject(); 

			String menuTitle = jom.get("menuTitle").getAsString();        	
			JsonArray opciones = jom.getAsJsonArray("options");

			JMenu menu = new JMenu( menuTitle);

			for (JsonElement op : opciones)
			{       	
				// CreaciÃ³n de cada una de las opciones del menÃº
				JsonObject jo = op.getAsJsonObject(); 
				String lb = jo.get("label").getAsString();
				String event = jo.get("event").getAsString();

				JMenuItem mItem = new JMenuItem( lb );
				mItem.addActionListener( this );
				mItem.setActionCommand(event);

				menu.add(mItem);
			}

			menuBar.add( menu );
		}        
		setJMenuBar ( menuBar );	
	}

	//~~~~~~~~~~~~~~~~~
	//   ACERCA DE
	//~~~~~~~~~~~~~~~~~
	/**
	 * Muestra la informaciÃ³n acerca del desarrollo de esta apicaciÃ³n
	 */
	public void acercaDe()
	{
		String resultado = "\n***********************************************************************\n";
		resultado += " * Universidad de los Andes (BogotÃ¡ - Colombia)\n";
		resultado += " * Curso: ISIS 2304 - Sistemas Transaccionales\n";
		resultado += " * Proyecto HotelAndes\n";
		resultado += " * Hecho por Juan Pablo Correa y NicolÃ¡s Jaramillo\n *\n";
		resultado += " * El cÃ³digo original del proyecto es propiedad del profesor GermÃ¡n Bravo,\n"
				+ " * y fue tomado del proyecto Parranderos\n";
		resultado += "***********************************************************************\n\n";

		panelDatos.actualizarInterfaz(resultado);		
	}
}
