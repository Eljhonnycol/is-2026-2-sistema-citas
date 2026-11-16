# Sistema de Citas - Proyecto de Aula

Aplicación para agendar y consultar citas médicas. Incluye el taller de **pruebas de software en Spring Boot**
(Ingeniería de Software, VI semestre, II periodo 2026, Fundación Universitaria Tecnológico Comfenalco).

## Integrantes
- Eljhonnycol (@Eljhonnycol) — trabajo individual

## Tecnologías
Java 25 · Spring Boot 4.0.0 · Maven · Spring Data JPA + H2 · JUnit 5 · Mockito · MockMvc · Selenium 4.35 ·
JaCoCo 0.8.15 · GitHub Actions

## Requisitos
- JDK 25 e IntelliJ IDEA con soporte para Java 25
- Google Chrome (solo para la prueba Selenium)

## Ejecutar la aplicación
Ejecutar `CitasApplication` y abrir <http://localhost:8080/citas.html>.

## Ejecutar las pruebas
| Qué | Cómo |
|---|---|
| Una clase | IntelliJ: triángulo verde junto a la clase > Run |
| Suite (excluye `ui`) | `mvn test` |
| Suite + cobertura (mínimo 70 % de líneas) | `mvn verify` y abrir `target/site/jacoco/index.html` |
| Selenium (`@Tag("ui")`) | Ejecutar `ReservaUITest` individualmente desde IntelliJ |

## Pruebas del taller (`src/test/java/.../citas`)
| Clase | Tipo |
|---|---|
| `servicio/AgendaServiceTest` | Unitaria (JUnit + Mockito) |
| `web/CitaControllerTest` | Controlador REST (MockMvc): 201, 400, 409 |
| `repositorio/CitaRepositoryTest` | Persistencia (JPA + H2) |
| `integracion/ReservaIntegracionTest` | Integración HTTP real: 201 y luego 409 |
| `ui/ReservaUITest` | Interfaz (Selenium, Chrome headless) |

Las clases de la actividad anterior (`AgendaCitas`, `FiltroCitas`, `RegistroPacientes`, etc., en el paquete raíz)
se conservan con sus pruebas.

## Reglas de negocio y API
Un profesional no puede tener dos citas en el mismo horario y admite máximo 20 citas diarias.
`POST /api/citas` responde **201** (creada), **409** (horario ocupado o cupo lleno) o **400** (datos inválidos).

## Documentación del taller
- [`docs/matriz-casos-prueba.md`](docs/matriz-casos-prueba.md)
- [`docs/registro-defectos.md`](docs/registro-defectos.md)

## Estructura
```
.github/workflows/ci.yml   Pipeline "Pruebas y cobertura" (mvn -B verify + artefacto reporte-jacoco)
config/reglas-agenda.md    Regla usada por las pruebas de la actividad anterior
docs/                      Matriz de casos y registro de defectos
src/main/java/...          Código fuente (modelo, repositorio, servicio, web)
src/main/resources/static/citas.html   Formulario de reserva
src/test/java/...          Pruebas
pom.xml                    Maven (Spring Boot 4.0.0, Java 25, JaCoCo)
```

## Flujo de trabajo
- La rama `main` está protegida: todo cambio entra por Pull Request.
- Una rama por tarea (`feature/`, `fix/`, `docs/`, `test/`) y commits en formato Conventional Commits.
- El workflow ejecuta `mvn -B verify` en cada `push` y `pull_request`.
