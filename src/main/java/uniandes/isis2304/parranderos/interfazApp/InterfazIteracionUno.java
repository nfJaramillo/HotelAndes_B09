package uniandes.isis2304.parranderos.interfazApp;


/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad	de	los	Andes	(Bogotá	- Colombia)

 * Departamento	de	IngenierÃ­a	de	Sistemas	y	ComputaciÃ³n
 * Licenciado	bajo	el	esquema	Academic Free License versiÃ³n 2.1
 * 		
 * Curso: isis2304 - Sistemas Transaccionales
 * Proyecto: Parranderos Uniandes
 * @version 1.0
 * @author Germán Bravo
 * Modificado en marzo del 2019 por Juan Pablo Correa y Nicolás Jaramillo
 * Julio de 2018
 * 
 * Revisado por: Claudia Jiménez, Christian Ariza
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
import javax.jdo.JDODataStoreException;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
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
	// ----------------
	//   CONSTANTES
	// ----------------
	/**
	 * Logger para escribir la traza de la ejecuciÃ³n
	 */
	private static Logger log = Logger.getLogger(InterfazIteracionUno.class.getName());

	/**
	 * Ruta al archivo de configuraciÃ³n de la interfaz
	 */
	private static final String CONFIG_INTERFAZ = "./src/main/resources/config/interfaceConfig.json"; 

	/**
	 * Ruta al banner
	 */
	private static final String RUTA_BANNER = "./src/main/resources/config/bannerHotelAndes.png"; 
	
	/**
	 * Ruta al archivo de configuraciÃ³n de los nombres de tablas de la base de datos
	 */
	private static final String CONFIG_TABLAS = "./src/main/resources/config/TablasBD_A.json";

	// ---------------
	//   ATRIBUTOS
	// ---------------
	/**
	 * Objeto JSON con los nombres de las tablas de la base de datos que se quieren utilizar
	 */
	private JsonObject tableConfig;

	/**
	 * Asociación a la clase principal del negocio.
	 */
	private PersistenciaHotelAndes persistencia;

	// ---------------
	//   ATRIBUTOS 
	// ---------------
	/**
	 * Objeto JSON con la configuraciÃ³n de interfaz de la app.
	 */
	private JsonObject guiConfig;

	/**
	 * Panel de despliegue de interacciÃ³n para los requerimientos
	 */
	private PanelDatos panelDatos;

	/**
	 * Menú de la aplicación
	 */
	private JMenuBar menuBar;
	
	/**
	 * Indica cuál tipo de persona está usando la aplicación
	 */
	private boolean esAdmin;

	// -----------------------------------------------------------
	//   MÉTODOS
	// -----------------------------------------------------------

	/**
	 * Construye la ventana principal de la aplicación. <br>
	 * <b>post:</b> Todos los componentes de la interfaz fueron inicializados.
	 */
	public InterfazIteracionUno( )
	{
		// Carga la configuración de la interfaz desde un archivo JSON
		guiConfig = openConfig ("Interfaz", CONFIG_INTERFAZ);

		// Pregunta por el rol del usuario que interactúa con la aplicación		
		JCheckBox checkbox1 = new JCheckBox("Soy cliente");
		JCheckBox checkbox2 = new JCheckBox("Soy administrador");
		Object[] params = { new String( "Por favor, indíquenos cuál es su para usar la aplicación (seleccione sólo una opción)." ), checkbox1, checkbox2 };
		JOptionPane.showConfirmDialog( this, params, "Seleccionar rol", JOptionPane.OK_OPTION );
		esAdmin = checkbox2.isSelected();
		
		
		// Configura la apariencia del frame que contiene la interfaz gráfica
		configurarFrame();
		
		if (guiConfig != null)
			crearMenu( guiConfig.getAsJsonArray("menuBar") );

		/*
		tableConfig = openConfig ("Tablas BD", CONFIG_TABLAS);
		persistencia = PersistenciaHotelAndes.getInstance( );
		String path = guiConfig.get("bannerPath").getAsString();
		*/
		
		panelDatos = new PanelDatos ( );

		setLayout (new BorderLayout());
		add ( new JLabel (new ImageIcon(RUTA_BANNER)), BorderLayout.NORTH );          
		add( panelDatos, BorderLayout.CENTER );
	}

	// ----------------------------------------------
	//   MÉTODOS DE CONFIGURACIÓN PARA LA INTERFAZ
	// ----------------------------------------------
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
			titulo = "HotelAndes APP Default";
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
				// CreaciÃ³n de cada una de las opciones del menÃº
				JsonObject jo = op.getAsJsonObject(); 
				String lb =   jo.get("label").getAsString();
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

	// ----------------------
	//   CRUD DE USUARIO
	// ----------------------
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
				panelDatos.actualizarInterfaz( "Por favor ingrese un número de id válido." );
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
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	/**
	 * Muestra la presentaciÃ³n general del proyecto
	 */
	public void mostrarPresentacionGeneral ()
	{
		mostrarArchivo ("data/00-ST-ParranderosJDO.pdf");
	}

	/**
	 * Muestra el modelo conceptual de Parranderos
	 */
	public void mostrarModeloConceptual ()
	{
		mostrarArchivo ("data/Modelo Conceptual Parranderos.pdf");
	}

	/**
	 * Muestra el esquema de la base de datos de Parranderos
	 */
	public void mostrarEsquemaBD ()
	{
		mostrarArchivo ("data/Esquema BD Parranderos.pdf");
	}

	/**
	 * Muestra el script de creaciÃ³n de la base de datos
	 */
	public void mostrarScriptBD ()
	{
		mostrarArchivo ("data/EsquemaParranderos.sql");
	}

	/**
	 * Muestra la arquitectura de referencia para Parranderos
	 */
	public void mostrarArqRef ()
	{
		mostrarArchivo ("data/ArquitecturaReferencia.pdf");
	}

	/**
	 * Muestra la documentaciÃ³n Javadoc del proyectp
	 */
	public void mostrarJavadoc ()
	{
		mostrarArchivo ("doc/index.html");
	}

	/**
	 * Muestra la informaciÃ³n acerca del desarrollo de esta apicaciÃ³n
	 */
	public void acercaDe ()
	{
		String resultado = "\n\n ************************************\n\n";
		resultado += " * Universidad	de	los	Andes	(BogotÃ¡	- Colombia)\n";
		resultado += " * Departamento	de	IngenierÃ­a	de	Sistemas	y	ComputaciÃ³n\n";
		resultado += " * Licenciado	bajo	el	esquema	Academic Free License versiÃ³n 2.1\n";
		resultado += " * \n";		
		resultado += " * Curso: isis2304 - Sistemas Transaccionales\n";
		resultado += " * Proyecto: Parranderos Uniandes\n";
		resultado += " * @version 1.0\n";
		resultado += " * @author GermÃ¡n Bravo\n";
		resultado += " * Julio de 2018\n";
		resultado += " * \n";
		resultado += " * Revisado por: Claudia JimÃ©nez, Christian Ariza\n";
		resultado += "\n ************************************\n\n";

		panelDatos.actualizarInterfaz(resultado);		
	}


	/* ****************************************************************
	 * 			MÃ©todos privados para la presentaciÃ³n de resultados y otras operaciones
	 *****************************************************************/


	/**
	 * Genera una cadena de caracteres con la descripciÃ³n de la excepcion e, haciendo Ã©nfasis en las excepcionsde JDO
	 * @param e - La excepciÃ³n recibida
	 * @return La descripciÃ³n de la excepciÃ³n, cuando es javax.jdo.JDODataStoreException, "" de lo contrario
	 */
	private String darDetalleException(Exception e) 
	{
		String resp = "";
		if (e.getClass().getName().equals("javax.jdo.JDODataStoreException"))
		{
			JDODataStoreException je = (javax.jdo.JDODataStoreException) e;
			return je.getNestedExceptions() [0].getMessage();
		}
		return resp;
	}

	/**
	 * Genera una cadena para indicar al usuario que hubo un error en la aplicaciÃ³n
	 * @param e - La excepciÃ³n generada
	 * @return La cadena con la informaciÃ³n de la excepciÃ³n y detalles adicionales
	 */
	private String generarMensajeError(Exception e) 
	{
		String resultado = "************ Error en la ejecuciÃ³n\n";
		resultado += e.getLocalizedMessage() + ", " + darDetalleException(e);
		resultado += "\n\nRevise datanucleus.log y parranderos.log para mÃ¡s detalles";
		return resultado;
	}

	/**
	 * Limpia el contenido de un archivo dado su nombre
	 * @param nombreArchivo - El nombre del archivo que se quiere borrar
	 * @return true si se pudo limpiar
	 */
	private boolean limpiarArchivo(String nombreArchivo) 
	{
		BufferedWriter bw;
		try 
		{
			bw = new BufferedWriter(new FileWriter(new File (nombreArchivo)));
			bw.write ("");
			bw.close ();
			return true;
		} 
		catch (IOException e) 
		{
			//			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Abre el archivo dado como parÃ¡metro con la aplicaciÃ³n por defecto del sistema
	 * @param nombreArchivo - El nombre del archivo que se quiere mostrar
	 */
	private void mostrarArchivo (String nombreArchivo)
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

	/* ****************************************************************
	 * 			MÃ©todos de la InteracciÃ³n
	 *****************************************************************/
	/**
	 * MÃ©todo para la ejecuciÃ³n de los eventos que enlazan el menÃº con los mÃ©todos de negocio
	 * Invoca al mÃ©todo correspondiente segÃºn el evento recibido
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

	/* ****************************************************************
	 * 			Programa principal
	 *****************************************************************/
	/**
	 * Este mÃ©todo ejecuta la aplicaciÃ³n, creando una nueva interfaz
	 * @param args Arreglo de argumentos que se recibe por lÃ­nea de comandos
	 */
	public static void main( String[] args )
	{
//		PersistenciaHotelAndes pero = PersistenciaHotelAndes.getInstance();
//		pero.registrarRolDeUsuario("aaa", 99);
		
		try
		{

			// Unifica la interfaz para Mac y para Windows.
			UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName( ) );
			InterfazIteracionUno interfaz = new InterfazIteracionUno( );
			interfaz.setVisible( true );
		}
		catch( Exception e )
		{
			e.printStackTrace( );
		}
	}
}
