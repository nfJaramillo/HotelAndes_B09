
  CREATE TABLE "ISIS2304B181910"."RESERVAS_SERVICIOS" 
   (	"ID" NUMBER, 
	"DESCRIPCION" VARCHAR2(100 BYTE), 
	"FECHAINICIO" DATE, 
	"FECHAFIN" DATE, 
	"IDSERVICIO" NUMBER, 
	"IDTIPOPERSONA" NUMBER, 
	"TIPOIDENTIFICACION" VARCHAR2(30 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS NOLOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TBSPROD" ;


  CREATE TABLE "ISIS2304B181910"."CADENA_DE_HOTELES" 
   (	"ID" NUMBER, 
	"NOMBRE" VARCHAR2(30 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS NOLOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TBSPROD" ;


  CREATE TABLE "ISIS2304B181910"."GASTOS" 
   (	"ID" NUMBER, 
	"IDSERVICIO" NUMBER, 
	"FECHA" DATE, 
	"PAGADO" NUMBER(*,0), 
	"PRECIO" FLOAT(126), 
	"IDRESERVA" NUMBER
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS NOLOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TBSPROD" ;


  CREATE TABLE "ISIS2304B181910"."HABITACIONES" 
   (	"NUMERO" NUMBER, 
	"IDHOTEL" NUMBER, 
	"TIPOHABITACION" NUMBER
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS NOLOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TBSPROD" ;


  CREATE TABLE "ISIS2304B181910"."HOTELES" 
   (	"ID" NUMBER, 
	"NOMBRE" VARCHAR2(30 BYTE), 
	"ESTRELLAS" NUMBER(*,0), 
	"IDCADENAHOTELES" NUMBER
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS NOLOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TBSPROD" ;


  CREATE TABLE "ISIS2304B181910"."PERSONAS" 
   (	"ID" NUMBER, 
	"TIPOIDENTIFICACION" VARCHAR2(30 BYTE), 
	"IDHOTEL" NUMBER, 
	"IDTIPOPERSONA" NUMBER, 
	"NOMBRE" VARCHAR2(30 BYTE), 
	"CORREO" VARCHAR2(30 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS NOLOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TBSPROD" ;

  CREATE TABLE "ISIS2304B181910"."PRODUCTOS" 
   (	"ID" NUMBER, 
	"NOMBRE" VARCHAR2(30 BYTE), 
	"IDSERVICIO" NUMBER
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS NOLOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TBSPROD" ;


  CREATE TABLE "ISIS2304B181910"."PLANES_DE_CONSUMO" 
   (	"ID" NUMBER, 
	"IDHOTEL" NUMBER, 
	"DESCRIPCION" VARCHAR2(1000 BYTE), 
	"NOMBRE" VARCHAR2(30 BYTE), 
	"FECHAINICIO" DATE, 
	"FECHAFIN" DATE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS NOLOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TBSPROD" ;


  CREATE TABLE "ISIS2304B181910"."RESERVAS_DE_ALOJAMIENTO" 
   (	"ID" NUMBER, 
	"IDHABITACION" NUMBER, 
	"IDHOTEL" NUMBER, 
	"FECHALLEGADATEORICA" DATE, 
	"FECHALLEGADAREAL" DATE, 
	"FECHASALIDATEORICA" DATE, 
	"FECHASALIDAREAL" DATE, 
	"PLANDECONSUMO" NUMBER
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS NOLOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TBSPROD" ;


  CREATE TABLE "ISIS2304B181910"."RESERVAS_DE_CLIENTES" 
   (	"IDRESERVA" NUMBER, 
	"IDUSUARIO" NUMBER, 
	"TIPOIDUSUARIO" VARCHAR2(30 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS NOLOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TBSPROD" ;


  CREATE TABLE "ISIS2304B181910"."SERVICIOS_ADICIONALES" 
   (	"ID" NUMBER, 
	"IDHOTEL" NUMBER, 
	"CATEGORIA" VARCHAR2(30 BYTE), 
	"ESTAINCLUIDO" NUMBER(*,0), 
	"HORARIOINICIO" VARCHAR2(30 BYTE), 
	"HORARIOFIN" VARCHAR2(30 BYTE), 
	"NOMBRE" VARCHAR2(30 BYTE), 
	"VALOR" NUMBER, 
	"DESCRIPCION" VARCHAR2(100 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS NOLOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TBSPROD" ;


  CREATE TABLE "ISIS2304B181910"."TIPOS_DE_HABITACION" 
   (	"ID" NUMBER, 
	"CAPACIDAD" NUMBER(*,0), 
	"COSTOALOJAMIENTONOCHE" FLOAT(126), 
	"DOTACION" VARCHAR2(100 BYTE), 
	"NOMBRE" VARCHAR2(30 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS NOLOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TBSPROD" ;


  CREATE TABLE "ISIS2304B181910"."TIPOS_DE_PERSONAS" 
   (	"ID" NUMBER, 
	"NOMBRE" VARCHAR2(30 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS NOLOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TBSPROD" ;

