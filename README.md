# EduTechCursosFinal
🔧 Introducción
El sistema está compuesto por dos microservicios independientes: cursos y soporte, desarrollados con Spring Boot y empaquetados en contenedores Docker. Se comunican internamente mediante docker-compose utilizando nombres de servicio en lugar de direcciones IP. Cada microservicio expone su propia API REST con documentación Swagger (OpenAPI) y respuestas enriquecidas con HATEOAS. La persistencia se maneja con Spring Data JPA y MySQL, y se incluyen pruebas unitarias con JUnit 5 y Mockito.

🧱 Arquitectura de Microservicios
Independencia funcional: cada microservicio opera de forma desacoplada.

Comunicación interna: mediante red Docker.

Componentes principales:

Cursos: CRUD de cursos con DTOs, ensambladores HATEOAS y generación de datos de prueba.

Soporte: Gestión de técnicos, tickets y generación de datos para testing.

🧩 Detalle de Microservicios
📘 Microservicio Cursos
Funciones: CRUD de cursos, soporte de versiones API (/api/cursos y /api/v2/cursos), carga automática de datos ficticios.

Modelo de datos: Curso, CursoDTO, CursoMapper.

Tecnologías: Spring Boot, JPA, MySQL, HATEOAS, DataFaker, JUnit 5.

🛠️ Microservicio Soporte Técnico
Funciones: CRUD de técnicos y tickets, generación de datos vía /api/test, uso de HATEOAS en /api/v2/tickets.

Modelo de datos: Técnico, Ticket.

Tecnologías: Spring Boot, JPA, MySQL, HATEOAS, DataFaker, JUnit 5.

📚 Swagger UI
Cada microservicio expone su documentación OpenAPI accesible desde:

http://localhost:8081/swagger-ui/index.html → Cursos

http://localhost:8082/swagger-ui/index.html → Soporte

En servidores, reemplazar localhost por la IP correspondiente.

🐳 Docker y Empaquetado
Dockerfile (común a ambos servicios)
dockerfile
Copiar
Editar
FROM openjdk:17-jdk-slim
COPY *.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
Empaquetado con Maven
bash
Copiar
Editar
cd <directorio del microservicio>
mvn clean package -DskipTests
Esto genera el .jar dentro de la carpeta target/.

🧩 Docker Compose
El archivo docker-compose.yml orquesta todos los servicios: microservicios y sus respectivas bases de datos. Cada servicio tiene su propio contenedor MySQL, con volúmenes persistentes y healthcheck. depends_on asegura el orden de inicialización. Las imágenes de los microservicios se construyen desde sus Dockerfiles.

🚀 Ejecución del sistema
Ir al directorio raíz con el archivo docker-compose.yml.

Verificar que los .jar estén en target/.

Ejecutar:

bash
Copiar
Editar
docker compose up --build
Esto construye las imágenes, inicia los contenedores y crea la red interna para permitir comunicación entre ellos.

🌐 Acceso desde navegador
http://198.199.77.180:8081/api/cursos

http://198.199.77.180:8082/api/tecnicos

