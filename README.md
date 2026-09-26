# Inventario de productos

Aplicación con Angular 18, PrimeNG, Spring Boot 2.7 y MySQL 8. Permite iniciar sesión, buscar productos por nombre, crearlos, editarlos y eliminarlos.

## Estructura

```text
backend/
  src/main/java/com/inventario/producto/
    autenticacion/   Inicio de sesión y datos de autenticación
    configuracion/  Seguridad, CORS y OpenAPI
    controlador/    Rutas de productos
    dto/            Solicitudes y respuestas
    entidad/        Modelo de persistencia
    excepcion/      Manejo de errores
    repositorio/    Acceso a datos
    seguridad/      Validación de JWT
    servicio/       Operaciones de productos
  src/main/resources/application.properties
  pom.xml
  Dockerfile
frontend/
  src/aplicacion/
    funcionalidades/  Inicio de sesión y gestión de productos
    nucleo/           Servicios, modelos y autenticación
  src/principal.ts
  src/estilos.scss
  public/
  angular.json
  package.json
  package-lock.json
  tsconfig.json
  tsconfig.app.json
  Dockerfile
docker-compose.yml
README.md
```

## Ejecutar con Docker

Requiere Docker y Docker Compose. Desde la raíz del proyecto:

```bash
docker compose up -d --build
```

| Servicio | Dirección |
|---|---|
| Aplicación | http://localhost:4200 |
| API | http://localhost:8081/api |
| Swagger | http://localhost:8081/swagger-ui.html |
| MySQL | localhost:3306 |

Credenciales de acceso: **admin / admin123**. MySQL usa **root / root** y la base `inventario_db`.

Para detener los servicios conservando los datos:

```bash
docker compose down
```

## Ejecutar localmente

Requiere JDK 8 u 11, Maven, Node.js 20.11.1 o superior de la rama 20 y MySQL 8.

Puedes iniciar solo MySQL con Docker desde la raíz:

```bash
docker compose up -d mysql
```

Si usas una instalación propia de MySQL, crea primero la base `inventario_db` y configura las credenciales del backend.

En una terminal, inicia el backend en el puerto que usa el frontend:

```bash
cd backend
SERVER_PORT=8081 mvn spring-boot:run
```

En otra terminal:

```bash
cd frontend
npm ci
npm start
```

Abre `http://localhost:4200`. Sin `SERVER_PORT`, Spring Boot usa `8080`. La dirección de la API está definida en `frontend/src/aplicacion/nucleo/configuracion/api.configuracion.ts`.

## Configuración del backend

Los valores se encuentran en `backend/src/main/resources/application.properties` y pueden sobrescribirse mediante variables de entorno.

| Variable | Valor predeterminado | Uso |
|---|---|---|
| `DB_HOST` | `localhost` | Servidor MySQL |
| `DB_PORT` | `3306` | Puerto MySQL |
| `DB_NAME` | `inventario_db` | Base de datos |
| `DB_USER` | `root` | Usuario MySQL |
| `DB_PASSWORD` | `root` | Contraseña MySQL |
| `AUTH_USERNAME` | `admin` | Usuario de la aplicación |
| `AUTH_PASSWORD` | `admin123` | Contraseña de la aplicación |
| `JWT_SECRET` | Clave de desarrollo incluida | Firma de los tokens |
| `JWT_EXPIRATION_MS` | `3600000` | Duración del token en milisegundos |
| `CORS_ALLOWED_ORIGINS` | `http://localhost:4200` | Orígenes permitidos, separados por comas |

En Docker, configura estas variables en `environment` del servicio `backend` en `docker-compose.yml`. Cambia las credenciales y `JWT_SECRET` antes de usar el proyecto fuera de desarrollo.

Hibernate crea o actualiza la tabla `productos` dentro de la base configurada. Sus columnas son `id`, `nombre`, `descripcion`, `cantidad`, `precio`, `creado_en` y `actualizado_en`. La base de datos debe existir antes de iniciar el backend; Docker la crea al inicializar un volumen nuevo.

## API

| Método | Ruta | Operación |
|---|---|---|
| POST | `/api/autenticacion/iniciar-sesion` | Iniciar sesión |
| GET | `/api/productos` | Listar productos; admite `?nombre=teclado` |
| GET | `/api/productos/{id}` | Consultar un producto |
| POST | `/api/productos` | Crear un producto |
| PUT | `/api/productos/{id}` | Actualizar un producto |
| DELETE | `/api/productos/{id}` | Eliminar un producto |

El inicio de sesión recibe:

```json
{
  "usuario": "admin",
  "contrasena": "admin123"
}
```

Devuelve `token` y `tipoToken`. Las rutas de productos requieren la cabecera `Authorization: Bearer <token>`.

Para crear o actualizar un producto:

```json
{
  "nombre": "Teclado",
  "descripcion": "Teclado mecánico",
  "cantidad": 10,
  "precio": 150.00
}
```

Las respuestas incluyen `id`, `creadoEn` y `actualizadoEn`. La especificación OpenAPI está disponible en `http://localhost:8081/v3/api-docs`.

## Compilar

Backend:

```bash
cd backend
mvn clean package
```

El ejecutable se genera en `backend/target/`.

Frontend:

```bash
cd frontend
npm ci
npm run build
```

La aplicación compilada se genera en `frontend/dist/frontend/browser/`. Para desarrollo se utiliza `npm start`; para publicar la compilación, el servidor web debe redirigir las rutas de la aplicación a `index.html`.

Las dependencias instaladas, las cachés y los resultados de compilación se regeneran con estos comandos y están excluidos de Git y del contexto de Docker. El proyecto no incluye pruebas automatizadas.
