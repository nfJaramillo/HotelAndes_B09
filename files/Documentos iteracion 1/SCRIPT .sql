--////////////////////////////////////////////////////////////////
-- AQUI SE GENERAN TODAS LAS TABLAS SOLO CON LA RESTRICCION NO NULL
--////////////////////////////////////////////////////////////////


CREATE TABLE CADENA_DE_HOTELES(
	Id			NUMBER		NOT NULL,
	Nombre			VARCHAR(30)	NOT NULL);

CREATE TABLE PLANES_DE_CONSUMO(
	Id			NUMBER		NOT NULL,
	IdHotel			NUMBER		NOT NULL,
	Descripcion		VARCHAR(1000)	NOT NULL,
	Nombre			VARCHAR(30)	NOT NULL,
	FechaInicio		DATE,		
	FechaFin		DATE		);

CREATE TABLE RESERVAS_SERVICIOS(
	Id			NUMBER		NOT NULL,
	Descripcion		VARCHAR(100)	NOT NULL,
	FechaInicio		DATE		NOT NULL,
	FechaFin		DATE		NOT NULL,
	IdServicio		NUMBER		NOT NULL,
	TipoIdentificacion	VARCHAR(30)	NOT NULL,
	IdTipoPersona		NUMBER		NOT NULL);

CREATE TABLE 	 RESERVAS_DE_ALOJAMIENTO(
	Id			NUMBER		NOT NULL,
	IdHabitacion		NUMBER		NOT NULL,
	IdHotel			NUMBER		NOT NULL,
	FechaLlegadaTeorica	DATE		NOT NULL,
	FechaLlegadaReal	DATE		,
	FechaSalidaTeorica	DATE		NOT NULL,
	FechaSalidaReal		DATE		,
	PlanDeConsumo		NUMBER			);

CREATE TABLE HOTELES(
	Id			NUMBER		NOT NULL,
	Nombre			VARCHAR(30)	NOT NULL,
	Estrellas		INTEGER		NOT NULL,
	IdCadenaHoteles		NUMBER		NOT NULL);

CREATE TABLE PERSONAS(
	Id			NUMBER		NOT NULL,
	TipoIdentificacion	VARCHAR(30)	NOT NULL,
	IdHotel			NUMBER,
	IdTipoPersona		NUMBER		NOT NULL,
	Nombre			VARCHAR(30)	NOT NULL,
	Correo			VARCHAR(30)	NOT NULL);

CREATE TABLE HABITACIONES(
	Numero			NUMBER		NOT NULL,
	IdHotel			NUMBER		NOT NULL,
	TipoHabitacion		NUMBER		NOT NULL);

CREATE TABLE SERVICIOS_ADICIONALES(
	Id			NUMBER		NOT NULL,
	IdHotel			NUMBER		NOT NULL,
	Categoria		VARCHAR(30)	NOT NULL,
	EstaIncluido		INTEGER		NOT NULL,
	HorarioInicio		VARCHAR(30)	NOT NULL,
	HorarioFin		VARCHAR(30)     NOT NULL,
	Nombre			VARCHAR(30)	NOT NULL,
	Valor			NUMBER		NOT NULL,
	Descripcion		VARCHAR(100)	NOT NULL);

CREATE TABLE TIPOS_DE_PERSONAS(
	Id			NUMBER		NOT NULL,
	Nombre			VARCHAR(30)	NOT NULL);

CREATE TABLE TIPOS_DE_HABITACION(
	Id			NUMBER		NOT NULL,
	Capacidad		INTEGER		NOT NULL,
	CostoAlojamientoNoche	NUMBER		NOT NULL,
	Dotacion		VARCHAR(100)	NOT NULL,
	Nombre			VARCHAR(30)	NOT NULL);

CREATE TABLE GASTOS(
	Id			NUMBER		NOT NULL,
	IdUsuario		NUMBER		NOT NULL,
	IdTipoIdentificacion	VARCHAR(30)	NOT NULL,
	IdServicio		NUMBER		NOT NULL,
	Fecha			DATE		NOT NULL,
	Pagado			INTEGER		NOT NULL,
	Precio			NUMBER		NOT NULL);

CREATE TABLE PRODUCTOS(
	Id			NUMBER		NOT NULL,
	Nombre			VARCHAR(30)	NOT NULL,
	IdServicio		NUMBER		NOT NULL);

CREATE TABLE RESERVAS_DE_CLIENTES(
	IdReserva		NUMBER		NOT NULL,
	IdUsuario		NUMBER		NOT NULL,
	TipoIdUsuario		NUMBER		NOT NULL);


--////////////////////////////////////////////////////////////////
-- AQUI ESTAN LAS RESTRICCIONES
--////////////////////////////////////////////////////////////////


--////////////////////////////////////////////////////////////////
-- AQUI ESTAN LAS RESTRICCIONES DE PK 
--//////////////////////////////////////////////////////////////

ALTER TABLE CADENA_DE_HOTELES
  ADD CONSTRAINT IdHoteles_PK
  PRIMARY KEY (Id)
  RELY DISABLE NOVALIDATE;

ALTER TABLE PLANES_DE_CONSUMO
  ADD CONSTRAINT IdPlanes_PK
  PRIMARY KEY (Id)
  RELY DISABLE NOVALIDATE;

ALTER TABLE RESERVAS_SERVICIOS
  ADD CONSTRAINT IdReservasServicios_PK
  PRIMARY KEY (Id)
  RELY DISABLE NOVALIDATE;


ALTER TABLE HOTELES
  ADD CONSTRAINT IdHotelesHoteles_PK
  PRIMARY KEY (Id)
  RELY DISABLE NOVALIDATE;

ALTER TABLE PERSONAS
  ADD CONSTRAINT IdPersonas_PK
  PRIMARY KEY (Id,TipoIdentificacion)
  RELY DISABLE NOVALIDATE;


ALTER TABLE SERVICIOS_ADICIONALES
  ADD CONSTRAINT NumeroServiciosAdicionales_PK
  PRIMARY KEY (Id)
  RELY DISABLE NOVALIDATE;

ALTER TABLE TIPOS_DE_PERSONAS
  ADD CONSTRAINT IdTiposPersonas_PK
  PRIMARY KEY (Id)
  RELY DISABLE NOVALIDATE;

ALTER TABLE TIPOS_DE_HABITACION
  ADD CONSTRAINT IdTiposHabitacion_PK
  PRIMARY KEY (Id)
  RELY DISABLE NOVALIDATE;

ALTER TABLE GASTOS
  ADD CONSTRAINT IdGatos_PK
  PRIMARY KEY (Id)
  RELY DISABLE NOVALIDATE;

ALTER TABLE HABITACIONES
  ADD CONSTRAINT NumeroHabitaciones_PK
  PRIMARY KEY (Numero)
  RELY DISABLE NOVALIDATE;

ALTER TABLE PRODUCTOS
  ADD CONSTRAINT IdDelProducto_PK
  PRIMARY KEY (Id)
  RELY DISABLE NOVALIDATE;

ALTER TABLE RESERVAS_DE_ALOJAMIENTO
  ADD CONSTRAINT IdReservasAlojamiento_PK
  PRIMARY KEY (Id)
  RELY DISABLE NOVALIDATE;

ALTER TABLE RESERVAS_DE_CLIENTES
  ADD CONSTRAINT PrimaryKeys_PK
  PRIMARY KEY (IdReserva,IdUsuario,TipoIdUsuario)
  RELY DISABLE NOVALIDATE;





--////////////////////////////////////////////////////////////////
-- AQUI ESTAN LAS RESTRICCIONES DE FK Y CK
--//////////////////////////////////////////////////////////////


ALTER TABLE PLANES_DE_CONSUMO
  ADD CONSTRAINT IdHotelPlanes_FK
  FOREIGN KEY (IdHotel) REFERENCES HOTELES (Id)
  RELY DISABLE NOVALIDATE;



ALTER TABLE RESERVAS_SERVICIOS
  ADD CONSTRAINT ServicioDeLaReserva_FK
  FOREIGN KEY (IdServicio) REFERENCES SERVICIOS_ADICIONALES(Id)
  RELY DISABLE NOVALIDATE;

ALTER TABLE RESERVAS_SERVICIOS
  ADD CONSTRAINT IdUsuarioServiciosR_FK
  FOREIGN KEY (IdTipoPersona,TipoIdentificacion) REFERENCES PERSONAS(Id,TipoIdentificacion)
  RELY DISABLE NOVALIDATE;


ALTER TABLE RESERVAS_DE_ALOJAMIENTO
  ADD CONSTRAINT IdHabitacionReservasA_FK
  FOREIGN KEY (IdHabitacion) REFERENCES HABITACIONES (Numero)
  RELY DISABLE NOVALIDATE;

ALTER TABLE RESERVAS_DE_ALOJAMIENTO
  ADD CONSTRAINT IdHotelReservasA_FK
  FOREIGN KEY (IdHotel) REFERENCES HOTELES (Id)
  RELY DISABLE NOVALIDATE;

ALTER TABLE RESERVAS_DE_ALOJAMIENTO
  ADD CONSTRAINT fechaDeSalidaMayor_CK
  CHECK(FechaLlegadaTeorica < FechaSalidaTeorica )
  RELY DISABLE NOVALIDATE;



ALTER TABLE HOTELES
  ADD CONSTRAINT IdCadenaHotelesHoteles_FK
  FOREIGN KEY (IdCadenaHoteles) REFERENCES CADENA_DE_HOTELES (Id)
  RELY DISABLE NOVALIDATE;

ALTER TABLE HOTELES
  ADD CONSTRAINT EstrellasHoteles_CK
  CHECK (Estrellas>=0 AND Estrellas <=5);

ALTER TABLE PERSONAS
  ADD CONSTRAINT IdHotelPersonas_FK
  FOREIGN KEY (IdHotel) REFERENCES HOTELES (Id)
  RELY DISABLE NOVALIDATE;

ALTER TABLE PERSONAS
  ADD CONSTRAINT IdTipoPersonaPersonas_FK
  FOREIGN KEY (IdTipoPersona) REFERENCES TIPOS_DE_PERSONAS (Id)
  RELY DISABLE NOVALIDATE;

ALTER TABLE PERSONAS
  ADD CONSTRAINT CorreoPersonas_CK
  CHECK (Correo LIKE ('%@%'));

ALTER TABLE HABITACIONES
  ADD CONSTRAINT IdHotelHabitaciones_FK
  FOREIGN KEY (IdHotel) REFERENCES HOTELES (Id)
  RELY DISABLE NOVALIDATE;

ALTER TABLE HABITACIONES
  ADD CONSTRAINT TipoHabitacionHabitaciones_FK
  FOREIGN KEY (TipoHabitacion) REFERENCES TIPOS_DE_HABITACION (Id)
  RELY DISABLE NOVALIDATE;

ALTER TABLE SERVICIOS_ADICIONALES
  ADD CONSTRAINT IdHotelServiciosA_FK
  FOREIGN KEY (IdHotel) REFERENCES HOTELES (Id)
  RELY DISABLE NOVALIDATE;


ALTER TABLE SERVICIOS_ADICIONALES
  ADD CONSTRAINT EstaIncluidoServiciosA_CK
  CHECK (EstaIncluido = 0 OR EstaIncluido = 1);

ALTER TABLE TIPOS_DE_HABITACION
  ADD CONSTRAINT TiposDeHabitacion_CK
  CHECK (Capacidad > 0 AND CostoAlojamientoNoche >0);

ALTER TABLE GASTOS
  ADD CONSTRAINT IdUsuarioGatos_FK
  FOREIGN KEY (IdUsuario,IdTipoIdentificacion) REFERENCES PERSONAS(Id,TipoIdentificacion)
  RELY DISABLE NOVALIDATE;
	
ALTER TABLE GASTOS
  ADD CONSTRAINT IdServicioGatos_FK
  FOREIGN KEY (IdServicio) REFERENCES SERVICIOS_ADICIONALES(Id)
  RELY DISABLE NOVALIDATE; 	

ALTER TABLE GASTOS
  ADD CONSTRAINT Gastos_CK
  CHECK (Pagado = 0 OR Pagado = 1 AND Precio >= 0);

ALTER TABLE PRODUCTOS
  ADD CONSTRAINT ServicioDelProducto_FK
  FOREIGN KEY (IdServicio) REFERENCES SERVICIOS_ADICIONALES (Id)
  RELY DISABLE NOVALIDATE;



