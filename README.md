# Inventario de Productos — CRUD (Angular 18 + Spring Boot 2.7 / Java 8)

Evaluación técnica TIP-SS-01: módulo de mantenimiento (CRUD) de productos, con:

- **Frontend**: Angular 18 (standalone components) + PrimeNG.
- **Backend**: Spring Boot 2.7 (Java 8) + Spring Data JPA + Spring Security con JWT.
- **Base de datos**: MySQL 8.
- **Documentación de API**: Swagger / OpenAPI (springdoc).

## Estructura del repositorio

```
backend/     API REST en Spring Boot
frontend/    SPA en Angular
docker-compose.yml   Levanta MySQL + backend + frontend
```

## Opción A: levantar todo con Docker Compose (recomendado)

Requisitos: Docker y Docker Compose.

```bash
docker compose up -d --build
```

Esto levanta:

- MySQL en `localhost:3306` (db `products_db`, usuario `root`, password `root`)
- Backend en `http://localhost:8081`
- Frontend en `http://localhost:4200`

Para detener todo:

```bash
docker compose down
```

## Opción B: correr cada parte manualmente

### Backend

Requisitos: JDK 8 y Maven (o usa el wrapper/Docker si no tienes Java 8 instalado localmente).

```bash
cd backend
# necesitas una instancia de MySQL accesible (ver docker-compose.yml para las credenciales por defecto)
mvn spring-boot:run
```

Variables de entorno relevantes (todas tienen defaults para desarrollo local):

| Variable | Descripción | Default |
|---|---|---|
| `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD` | Conexión a MySQL | `localhost`, `3306`, `products_db`, `root`, `root` |
| `AUTH_USERNAME`, `AUTH_PASSWORD` | Credenciales fijas para el login | `admin` / `admin123` |
| `JWT_SECRET` | Clave para firmar los JWT | valor de desarrollo incluido, **cámbialo en producción** |
| `JWT_EXPIRATION_MS` | Expiración del token en ms | `3600000` (1 hora) |
| `CORS_ALLOWED_ORIGINS` | Orígenes permitidos por CORS | `http://localhost:4200` |

El backend corre en `http://localhost:8080` por defecto (en Docker Compose se expone en `8081`).

### Frontend

Requisitos: Node.js 18+ y npm.

```bash
cd frontend
npm install
npm start   # ng serve, http://localhost:4200
```

Por defecto el frontend apunta a `http://localhost:8081/api` (ver `src/app/core/config/api.config.ts`).

## Autenticación

No hay registro de usuarios: el login (`POST /api/auth/login`) valida contra credenciales fijas configurables por variables de entorno.

- Usuario por defecto: `admin`
- Contraseña por defecto: `admin123`

El login devuelve un JWT que el frontend guarda y envía como `Authorization: Bearer <token>` en cada request a `/api/products/**`.

## Documentación de la API (Swagger)

Con el backend corriendo:

- Swagger UI: `http://localhost:8081/swagger-ui.html` (o `8080` si corres el backend fuera de Docker)
- OpenAPI JSON: `http://localhost:8081/v3/api-docs`

## Endpoints principales

| Método | Ruta | Descripción | Auth |
|---|---|---|---|
| POST | `/api/auth/login` | Login, devuelve JWT | No |
| GET | `/api/products` | Lista productos (`?name=` para buscar) | Sí |
| GET | `/api/products/{id}` | Obtiene un producto | Sí |
| POST | `/api/products` | Crea un producto | Sí |
| PUT | `/api/products/{id}` | Actualiza un producto | Sí |
| DELETE | `/api/products/{id}` | Elimina un producto | Sí |

## Tests

```bash
cd backend
mvn test
```

Incluye tests unitarios del servicio (Mockito) y del controller (MockMvc), cubriendo el flujo CRUD, validaciones y manejo de errores.

## Funcionalidades implementadas

**Frontend**
- Listado de productos en tabla (PrimeNG `p-table`) con paginación.
- Alta y edición de productos mediante formulario reactivo en un diálogo modal.
- Eliminación con diálogo de confirmación.
- Búsqueda por nombre en tiempo real (debounce contra el backend).
- Validaciones de formulario (campos requeridos, longitudes, mínimos numéricos).
- Login con JWT, interceptor HTTP que adjunta el token y guard de ruta.

**Backend**
- API REST completa (GET/POST/PUT/DELETE) sobre `/api/products`.
- Autenticación JWT sobre endpoint de login con credenciales fijas.
- Validaciones de entrada con Bean Validation y manejo global de errores.
- Persistencia en MySQL vía Spring Data JPA.
- Documentación con Swagger/OpenAPI.
