package uniandes.isis2304.parranderos.interfazApp;

/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad	de los Andes (Bogotá - Colombia)
 *
 * Departamento	de	Ingeniería	de	Sistemas y Computación
 * Licenciado bajo el esquema Academic Free License versión 2.1
 * 
 * Curso: isis2304 - Sistemas Transaccionales
 * Proyecto: Parranderos Uniandes
 * @version 1.0
 * @author Germán Bravo
 * Julio de 2018
 * Revisado por: Claudia Jiménez, Christian Ariza
 *
 * Modificado por Juan Pablo Correa y Nicolás Jaramillo en marzo del 2019
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
 * @author Germán Bravo
 * Modificado por Juan Pablo Correa y Nicolás Jaramillo
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

	//~~~~~~~~~~~~~~
	//   ATRIBUTOS
	//~~~~~~~~~~~~~~
	/**
	 * Asociación a la clase de persistencia del negocio
	 */
	private PersistenciaHotelAndes persistencia;

	/**
	 * Objeto JSON con la configuración de interfaz de la app
	 */
	private JsonObject guiConfig;

	/**
	 * Panel de despliegue de interacción para los requerimientos
	 */
	private PanelDatos panelDatos;

	/**
	 * Menú de la aplicación
	 */
	private JMenuBar menuBar;

	/**
	 * Atributo para modelar el rol de quien interactúa con la aplicación
	 */
	private boolean esAdmin;


	//~~~~~~~~~~~~~~
	//   MÉTODOS
	//~~~~~~~~~~~~~~
	/**
	 * Construye la ventana principal de la aplicación. <br>
	 * <b>post:</b> Todos los componentes de la interfaz fueron inicializados.
	 */
	public InterfazIteracionUno( )
	{
		// Carga la configuración de la interfaz desde un archivo JSON
		guiConfig = openConfig ("Interfaz", CONFIG_INTERFAZ);

		// Pregunta por el rol del usuario que interactúa con la aplicación
		preguntarPorElRol();

		// Configura la apariencia del frame que contiene la interfaz gráfica
		configurarFrame();

		// Crea el menú
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
		Object[] options = { "Administrador", "Cliente" };
		int n = JOptionPane.showOptionDialog(this, "          ¿Cuál es su rol?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		esAdmin = n!=JOptionPane.NO_OPTION;	
	}

	/**
	 * Registra un nuevo rol con la información dada por el usuario
	 * <Post> Se crea una nueva tupla en la tabla ROLES, lo cual implica ciertos permisos
	 * y restricciones en lo que puede o no hacer en el sistema dicho usuario. 
	 */
	public void registrarRolDeUsuario( )
	{
		try 
		{
			String tipoDeRol = JOptionPane.showInputDialog ( this, "¿Cuál es el tipo de rol?", "Tipo de rol", JOptionPane.QUESTION_MESSAGE );
			String info = JOptionPane.showInputDialog ( this, "¿Cuál es el id del rol?", "Identificador", JOptionPane.QUESTION_MESSAGE );

			int idDelRol = -1;

			try
			{
				idDelRol = Integer.parseInt( info ); 
			}
			catch (Exception e)
			{
				panelDatos.actualizarInterfaz( "Por favor ingrese un nï¿½mero de id vï¿½lido." );
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
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		}
		catch (Exception e) 
		{
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}


	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//   CRUD DE LOS REQUERIMIENTOS FUNCIONALES DE MODIFICACIÓN
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * RF7 - Registra una reserva de alojamiento
	 * Sólo los clientes pueden hacer este método
	 */
	public void registrarUnaReservaDeAlojamiento()
	{
		try 
		{
			// Esta función está permitida sólo para clientes
			verificarRol( 'C' );

			long idHabitacion;
			long idHotel;
			String fechaLlegada;
			String fechaSalida;
			long idPlanDeConsumo;
			String[] tiposDeIdentificaciones;
			long[] identificaciones;

			try
			{
				idHabitacion = Long.parseLong( JOptionPane.showInputDialog( this, "Identificador de la habitación:", "ID habitación", JOptionPane.QUESTION_MESSAGE ) );
				idHotel = Long.parseLong( JOptionPane.showInputDialog( this, "Identificador del hotel:", "ID hotel", JOptionPane.QUESTION_MESSAGE ) );
				fechaLlegada =  JOptionPane.showInputDialog( this, "Fecha de llegada (formato DD/MM/AAAA, p.e. 31/01/2019):", "Fecha de llegada", JOptionPane.QUESTION_MESSAGE );
				fechaSalida =  JOptionPane.showInputDialog( this, "Fecha de salida (formato DD/MM/AAAA, p.e. 31/01/2019):", "Fecha de salida", JOptionPane.QUESTION_MESSAGE ) ;
				idPlanDeConsumo = Long.parseLong( JOptionPane.showInputDialog( this, "Identificador del plan de consumo:", "ID plan de consumo", JOptionPane.QUESTION_MESSAGE ) );

				// Lo siguiente guarda la información de las personas en dos arreglos
				String[] aux = JOptionPane.showInputDialog( this, "Datos de las personas (tipoIdentificacionPersona1:idPersona1;...)", "Información de las personas", JOptionPane.QUESTION_MESSAGE ).split(";");

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
				throw new Exception( "No se pudo crear una reserva de alojamiento en la habitación " + idHabitacion );

			String resultado = "\n-> En RF7 registrarUnaReservaDeAlojamiento:";
			resultado += "\n       Reserva de alojamiento registrada exitosamente:";
			resultado += "\n         Información de la reserva: " + ra;
			resultado += "\n\nOperación terminada.";
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
			panelDatos.actualizarInterfaz( generarMensajeError(e) );
		}
	}

	/**
	 * RF8 - Registra una reserva de un servicio del hotel por parte de un cliente
	 * Sólo los clientes pueden hacer esta acción
	 */
	public void registarUnaReservaDeUnServicioDelHotelPorParteDeUnCliente()
	{
		try 
		{
			verificarRol( 'C' ); // Esta función está permitida sólo para clientes

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
				descripcion = JOptionPane.showInputDialog( this, "Descripción adicional:", "Descripción", JOptionPane.QUESTION_MESSAGE );
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
			resultado += "\n\nOperación terminada.";
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
			panelDatos.actualizarInterfaz( generarMensajeError(e) );
		}
	}

	/**
	 * RF9 - Registra una llegada de un cliente al hotel
	 * Sólo los recepcionistas (administradores) pueden ejecutar esta acción 
	 */
	public void registrarLaLLegadaDeUnClienteAlHotel()
	{
		try 
		{
			// Esta función está permitida sólo para administradores
			verificarRol( 'A' );

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
			resultado += "\n\nOperación terminada.";
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
			panelDatos.actualizarInterfaz( generarMensajeError(e) );
		}
	}

	/**
	 * RF10 - Registra un consumo de un servicio del hotel por parte de un cliente o sus acompañantes
	 * Sólo los administradores pueden ejecutar esta acción
	 */
	public void registrarUnConsumoDeUnServicioDelHotelPorParteDeUnClienteOSusAcompanantes()
	{
		try 
		{
			// Esta función está permitida sólo para administradores
			verificarRol( 'A' );

			String tipoIdentificacion;
			long idUsuario;
			long idReservaServicio;
			int seCargaALaHabitacion = 1;

			try
			{
				tipoIdentificacion = JOptionPane.showInputDialog( this, "Tipo de identificación:", "Tipo de identificación", JOptionPane.QUESTION_MESSAGE );
				idUsuario = Long.parseLong( JOptionPane.showInputDialog( this, "Identificador del usuario:", "ID usuario", JOptionPane.QUESTION_MESSAGE ) );
				idReservaServicio = Long.parseLong( JOptionPane.showInputDialog( this, "Identificador de la reserva del servicio:", "ID reserva servicio", JOptionPane.QUESTION_MESSAGE ) );

				Object[] options = { "Sí", "No" };
				int n = JOptionPane.showOptionDialog(this, "¿Desea cargar el valor a la habitación?", "Cargar valor", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0] );
				if( n == JOptionPane.NO_OPTION ) seCargaALaHabitacion = 0;

				System.out.println( seCargaALaHabitacion + ( seCargaALaHabitacion == 0 ? "No la carga a la habitación" : "Sí la carga a la habitación" ) );
			}
			catch (Exception e)
			{
				throw new Exception( "Error convirtiendo uno de los datos de entrada, por favor verifique que los formatos sean los correctos." );
			}

			Gasto g = persistencia.registrarConsumo( tipoIdentificacion, idUsuario, idReservaServicio, darFechaDeHoy(), seCargaALaHabitacion );

			if( g == null )
				throw new Exception( "No se pudo registrar un consumo." );

			String resultado = "\n-> En RF10 registrarUnConsumoDeUnServicioDelHotelPorParteDeUnClienteOSusAcompanantes:";
			resultado += "\n       Se ha solicitado exitosamente el consumo.";
			resultado += "\n\nOperación terminada.";
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
			panelDatos.actualizarInterfaz( generarMensajeError(e) );
		}
	}

	/**
	 * RF11 - Registra la salida de un cliente del hotel
	 * Sólo los recepcionistas (administradores) pueden ejecutar esta acción
	 */
	public void registarLaSalidaDeUnCliente()
	{
		try
		{
			verificarRol( 'A' ); //Esta función está permitida sólo para administradores

			long idReserva;
			int pagoCuentaRestante;

			try
			{
				idReserva = Long.parseLong( JOptionPane.showInputDialog( this, "Identificador de la reserva:", "ID reserva", JOptionPane.QUESTION_MESSAGE ) );

				int loQueDebePagar = persistencia.cuentaAPagar( idReserva );
				pagoCuentaRestante = (int) Double.parseDouble( JOptionPane.showInputDialog( this, "Usted debe un total de $" + loQueDebePagar + "\n¿Cuánto va a pagar por los gastos?", "Pago", JOptionPane.QUESTION_MESSAGE ) );
				if( pagoCuentaRestante < loQueDebePagar ) throw new Exception( "El dinero no alcanza para pagar la reserva. No se puede dar salida sin cancelar la totalidad." );
			}
			catch (Exception e)
			{
				throw new Exception( "Error convirtiendo uno de los datos de entrada, por favor verifique que los formatos sean los correctos." );
			}

			persistencia.checkOut( idReserva, darFechaDeHoy() );

			String resultado = "\n-> En RF11 registarLaSalidaDeUnCliente:";
			resultado += "     \n       Se ha dado salida a los clientes de la reserva '" + idReserva + "'.";
			resultado += "\n\nOperación terminada.";
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
	 * habitación durante un periodo de tiempo y el año corrido.
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
			resultado       += "         El dinero recolectado por cada habitación del hotel de id '" + idHotel + "' fue de:\n\n";
			resultado       +=        "\tID de la habitación \t Dinero recolectado entre el \t\t Dinero en el año corrido (" + darFechaDeHoy().substring(6) + ")\n";
			resultado       +=        "\t                    \t\t" + fechaInicio + " y el" + fechaFin + "\n";
			resultado       +=        "\t------------------------------------------------------------------------------------------------------------------------------------------------------\n";

			for( ClaseAsistente ca : lista )
				resultado += "\t" + ca.getIDHABITACION() + "\t\t $" + ca.getDINEROPERIODOESPECIFICO()  + "\t\t\t $" + ca.getDINEROANIOCORRIDO() +"\n";

			resultado += "\n\n\n\n-  [RFC1] Operación terminada.";
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
			panelDatos.actualizarInterfaz( generarMensajeError(e) );
		}
	}

	/**
	 * RFC2 - Muestra los 20 servicios que más fueron consumidos en un periodo de tiempo dado.
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
			resultado       += "         Los veinte servicios más populares del hotel de id '" + idHotel + "' fue de:\n\n";
			resultado       +=        "\tID del servicio     \t Nombre del servicio         \t\t Número de apariciones\n";
			resultado       +=        "\t----------------------------------------------------------------------------------------------------------------------------------------------\n";

			for( ClaseAsistente ca : lista )
				resultado += "\t" + ca.getIDSERVICIOS() + "\t\t  " + ca.getIDSERVICIOS()  + "\t\t\t  " + ca.getCONTEOSERVICIOS() +"\n";

			resultado += "\n\n\n\n  [RFC2] Operación terminada.";
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
			resultado       +=        "\tNúmero de habitación\tÍndice \n";
			resultado       +=        "\t----------------------------------------------------------------------------------------------------------------------------------------------\n";
			//indice numhabitacion
			for( ClaseAsistente ca : lista )
				resultado += "\t" + ca.getNUMHABITACION() + "\t\t  " + ca.getINDICE() + "\n";

			resultado += "\n\n\n\n  [RFC2] Operación terminada.";
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
				opcion = JOptionPane.showInputDialog( this, "Especifique la característica sobre la que desea filtrar\n\n Opciones:\n    P para precio.\n    F para fecha.\n    T para tipo.\n    C para consumo.", "Filtrar servicios", JOptionPane.QUESTION_MESSAGE ).charAt(0);

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
					int contador = Integer.parseInt( JOptionPane.showInputDialog( this, "¿Cuántas veces han usado ese consumo?:", "RFC4", JOptionPane.QUESTION_MESSAGE ) );
					String fecha1 = JOptionPane.showInputDialog( this, "Fecha inicial (formato DD/MM/AAAA, p.e. 31/01/2019):", "Fecha inicial", JOptionPane.QUESTION_MESSAGE );
					String fecha2 = JOptionPane.showInputDialog( this, "Fecha final (formato DD/MM/AAAA, p.e. 31/03/2019):", "Fecha final", JOptionPane.QUESTION_MESSAGE );
					respuesta = persistencia.RFC4CONSUMO( contador, fecha1, fecha2 );
					break;
				}

				String resultado = "\n-> En RFC4 mostrarIndiceOcupacion:\n\n\n";

				for( ClaseAsistente ca : respuesta )
					resultado += "\t" + ca.toString() + "\n";

				resultado += "\n\n\n\n  [RFC4] Operación terminada.";
				panelDatos.actualizarInterfaz(resultado);
			}
			catch (Exception e)
			{
				throw new Exception( "Ingresó una opción que no está permitida, o uno de los valores no siguió el formato que debería." );
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
				resultado += "\t" + ca.toString2() + "\n";

			resultado += "\n\n\n\n  [RFC5] Operación terminada.";
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
			panelDatos.actualizarInterfaz( generarMensajeError(e) );
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//   MÉTODOS PARA LA PRESENTACIÓN DE LOS DATOS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * Genera una cadena para indicar al usuario que hubo un error en la aplicación
	 * @param e - La excepción generada
	 * @return La cadena con la información de la excepción y detalles adicionales
	 */
	private String generarMensajeError(Exception e) 
	{
		String resultado = "Error en la ejecución:\n";
		resultado += "----->" + e.getLocalizedMessage() + darDetalleException(e);
		return resultado;
	}

	/**
	 * Genera una cadena de caracteres con la descripción de la excepcion e, haciendo énfasis en las excepciones de JDO
	 * @param e - La excepción recibida
	 * @return La descripción de la excepción cuando es javax.jdo.JDODataStoreException o "" de lo contrario
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
	 * Verifica el rol de quien interactúa con la aplicación, para permitirle o no ciertas acciones
	 * @param seEsperaQueSea Lo que se espera que sea el usuario
	 * @throws Exception Cuando se espera que el usuario tenga ciertos permisos y no los tiene
	 */
	private void verificarRol( char seEsperaQueSea ) throws Exception
	{
		if( seEsperaQueSea == 'A' && !esAdmin ) throw new Exception( "¡Usted no posee permisos de administrador para ejecutar esta opción!" );
		else if( seEsperaQueSea == 'C' && esAdmin ) throw new Exception( "Esta opción es exclusiva para los clientes." );
	}

	/**
	 * Abre el archivo dado como parámetro con la aplicación por defecto del sistema
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
	 * Retorna un String con la fecha del día de hoy
	 * @return String de la fecha actual en formato dd/MM/AAAA
	 */
	private String darFechaDeHoy()
	{
		Date date = (Calendar.getInstance()).getTime();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
		return dateFormat.format(date);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//  MÉTODOS DE INTERACCIÓN
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * Método para la ejecución de los eventos que enlazan el menú con los mÃ©todos de negocio
	 * Invoca al método correspondiente segÃºn el evento recibido
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
	 * Este método ejecuta la aplicación, creando una nueva interfaz
	 * @param args Arreglo de argumentos que se recibe por línea de comandos
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
	//   MÉTODOS DE CONFIGURACIÓN PARA LA INTERFAZ
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * Lee datos de configuración para la aplicación, a partir de un archivo JSON o con valores por defecto si hay errores.
	 * @param tipo - El tipo de configuración deseada
	 * @param archConfig - Archivo Json que contiene la configuración
	 * @return Un objeto JSON con la configuración del tipo especificado
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
			log.info ( "Se encontró un archivo de configuración válido: " + tipo );
		} 
		catch (Exception e)
		{	
			log.info ( "No se encontró un archivo de configuración válido" );			
			JOptionPane.showMessageDialog(null, "No se encontró un archivo de configuración válido: " + tipo, "HotelAndes", JOptionPane.ERROR_MESSAGE );
		}	
		return config;
	}

	/**
	 * Método para configurar el frame principal de la aplicación
	 */
	private void configurarFrame(  )
	{
		int alto = 0;
		int ancho = 0;
		String titulo = "";	

		if ( guiConfig == null )
		{
			log.info ( "Se aplica configuración por defecto" );			
			titulo = "Parranderos APP Default";
			alto = 300;
			ancho = 500;
		}
		else
		{
			log.info ( "Se aplica configuración indicada en el archivo de configuración" );
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
	 * Método para crear el menú de la aplicación con base en el objeto JSON leído
	 * Genera una barra de menú y los menús con sus respectivas opciones
	 * @param jsonMenu - Arreglo Json con los menús deseados
	 */
	private void crearMenu( JsonArray jsonMenu )
	{   
		// Creación de la barra de menús
		menuBar = new JMenuBar();       
		for (JsonElement men : jsonMenu)
		{
			// Creación de cada uno de los menús
			JsonObject jom = men.getAsJsonObject(); 

			String menuTitle = jom.get("menuTitle").getAsString();        	
			JsonArray opciones = jom.getAsJsonArray("options");

			JMenu menu = new JMenu( menuTitle);

			for (JsonElement op : opciones)
			{       	
				// Creación de cada una de las opciones del menú
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
	 * Muestra la información acerca del desarrollo de esta apicación
	 */
	public void acercaDe()
	{
		String resultado = "\n***********************************************************************\n";
		resultado += " * Universidad de los Andes (Bogotá - Colombia)\n";
		resultado += " * Curso: ISIS 2304 - Sistemas Transaccionales\n";
		resultado += " * Proyecto HotelAndes\n";
		resultado += " * Hecho por Juan Pablo Correa y Nicolás Jaramillo\n *\n";
		resultado += " * El código original del proyecto es propiedad del profesor Germán Bravo,\n"
				+ " * y fue tomado del proyecto Parranderos\n";
		resultado += "***********************************************************************\n\n";

		panelDatos.actualizarInterfaz(resultado);		
	}
}
