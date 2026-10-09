# Sistema de Citas - Proyecto de Aula

Aplicación para agendar y consultar citas médicas por especialidad.

> Este repositorio se usa únicamente para practicar el Taller de Git/GitHub de
> Ingeniería de Software (VI semestre, II periodo académico 2026, Fundación
> Universitaria Tecnológico Comfenalco). No contiene el proyecto de aula real.

## Integrantes
- Eljhonnycol (@Eljhonnycol) — trabajo individual

## Tecnologías
- Java 17
- Maven
- JUnit 5
- Git / GitHub / GitHub Actions

## Requisitos
- Git
- JDK 17
- Maven 3.x

Verifica la instalación con `git --version`, `java -version` y `mvn -version`.

## Clonar y ejecutar las pruebas
```bash
git clone https://github.com/Eljhonnycol/is-2026-2-sistema-citas.git
cd is-2026-2-sistema-citas
mvn verify
```

Por ahora el proyecto no tiene una aplicación ejecutable (no hay método `main`):
solo contiene la clase `FiltroCitas` con sus pruebas, que se validan con `mvn verify`.

## Estructura
```
.github/workflows/ci.yml   Pipeline de GitHub Actions
config/reglas-agenda.md    Regla usada en el ejercicio de conflictos (Nivel 4)
src/main/java/...          Código fuente (FiltroCitas)
src/test/java/...          Pruebas con JUnit 5
pom.xml                    Configuración de Maven
```

## Flujo de trabajo
- La rama `main` está protegida: todo cambio entra por Pull Request.
- Una rama por historia o tarea, con prefijo `feature/`, `fix/`, `docs/` o `test/`
  (por ejemplo `feature/HU03-buscar-citas-especialidad`).
- Commits en formato Conventional Commits: `<tipo>(<alcance>): <descripción>`,
  con tipos `feat`, `fix`, `docs`, `test`, `refactor` y `chore`.
- El workflow `CI` ejecuta `mvn verify` en cada Pull Request hacia `main` y en cada
  push a `main`.

## Nota sobre el trabajo individual
La guía del taller está pensada para equipos de 3 a 4 integrantes, con revisión de
código por un compañero. Este trabajo lo realiza una sola persona, por lo que no hay
revisores independientes y los merges los hace el administrador del repositorio. La
forma de validar esos requisitos se consulta con el docente.
