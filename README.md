# TP3 - API REST de Estudiantes y Carreras

API REST desarrollada con Spring Boot y Spring Data JPA para gestionar estudiantes, carreras y las matriculaciones entre ambos. Incluye consultas y reportes sobre inscriptos y egresados por carrera.

## Tecnologías
- Java 17
- Spring Boot 4.1.1 (Web MVC, Data JPA)
- Hibernate / JPA con consultas JPQL
- H2 (base de datos en memoria) con consola web habilitada
- Maven (wrapper incluido)
- Lombok

estudianteCarrera es la entidad intermedia que resuelve la relación muchos-a-muchos entre estudiantes y carreras, y guarda los datos propios de cada matriculación (año de inscripción, año de graduación y antigüedad).

Un estudiante se graduó en una carrera cuando anioGraduacion tiene valor; si es null, todavía no se graduó.

## Requisitos previos
- JDK 17 o superior

No hace falta instalar Maven ni una base de datos: se usa el wrapper (mvnw) y H2 en memoria.

## Cómo ejecutar
En bash:
git clone https://github.com/mailenramos/TP3.git
cd TP3/TP3
./mvnw spring-boot:run 

La aplicación queda disponible en http://localhost:8080.

### Base de datos H2

La consola web de H2 está habilitada en http://localhost:8080/h2-console. Como la base es en memoria, los datos se pierden al detener la aplicación. El esquema se genera automáticamente (spring.jpa.hibernate.ddl-auto=update).

### Carga automática de datos

Al iniciar la aplicación, la clase CsvDataLoader (paquete config) importa los datos de ejemplo desde src/main/resources/csv/.
Al terminar, la consola muestra:
CSV cargados: 15 carreras, 104 estudiantes, 109 matriculaciones.

## Endpoints:
### Estudiantes — /estudiantes
Método 		Ruta								Descripción
______________________________________________________________________________
POST 			/estudiante							Da de alta un estudiante
GET 			/estudiantes							Lista todos los estudiantes ordenados por apellido y nombre
GET 			/estudiantes/libreta/{libretaUniversitaria}			Busca un estudiante por número de libreta (404 si no existe)
GET			/estudiantes/genero/{genero}				Lista estudiantes por género (sin distinguir mayúsculas)

### Carreras — /carreras
Método 		Ruta								Descripción
______________________________________________________________________________
POST 			/carreras 							Da de alta una carrera
GET 			/carreras							Lista las carreras ordenadas por nombre
GET 			/carreras/inscriptos						Carreras con inscriptos, ordenadas por cantidad de inscriptos (descendente)
GET			/carreras/{carreraId}/estudiantes?ciudad={ciudad}	Estudiantes de una carrera filtrados por ciudad
GET			/carreras/reporte						Reporte por carrera: inscriptos y egresados agrupados por año

### Matriculación — /matriculas
Método 		Ruta								Descripción
______________________________________________________________________________
POST 			/matriculas							Matricula un estudiante (por libreta) en una carrera

### Ejemplos de uso

#### Crear un estudiante:
POST /estudiantes
{
  "dni": 40123456,
  "nombre": "Ana",
  "apellido": "Pérez",
  "edad": 22,
  "genero": "Female",
  "ciudad": "Tandil",
  "libretaUniversitaria": 12345
}

#### Crear una carrera
POST /carreras
{
  "nombre": "TUDAI",
  "duracion": 2
}

#### Matricular un estudiante en una carrera
POST /matriculas
{
  "libretaUniversitaria": 12345,
  "carreraId": 1,
  "anioInscripcion": 2023,
  "antiguedad": 2,
  "anioGraduacion": null
}

#### Respuesta de GET /carreras/reporte
[
  {
    "carrera": "TUDAI",
    "inscriptosPorAnio": [{ "anio": 2017, "cantidad": 4 }],
    "egresadosPorAnio": [{ "anio": 2022, "cantidad": 2 }]
  }
]

## Manejo de errores
Si se referencia un estudiante o una carrera inexistente, la API responde 404 Not Found con un mensaje descriptivo.

## Pruebas con Postman
En postman/TP3-Estudiantes.postman_collection.json hay una colección con una request por cada consulta de la consigna (desde punto a hasta punto h), más dos casos de error (404). Cada request incluye un test que verifica el código de estado.

Para usarla:

1. Levantar la aplicación (http://localhost:8080).
2. En Postman: Import y seleccionar el archivo.
3. Ejecutar las requests en orden, o toda la colección con Run collection.

La request a) Alta de estudiante genera un DNI y una libreta aleatorios en cada ejecución, y b), d) y g) reutilizan esos valores, por eso conviene respetar el orden. La URL base está definida en la variable baseUrl de la colección.