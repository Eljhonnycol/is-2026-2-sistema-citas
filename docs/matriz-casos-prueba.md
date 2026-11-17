# Matriz de casos de prueba

Técnicas: partición de equivalencia y valores límite (cupo diario: 0, 19, 20 citas previas).
La columna **Resultado obtenido** se completa tras ejecutar las pruebas en IntelliJ.

| Caso | Escenario | Resultado esperado | Prueba que lo cubre | Resultado obtenido |
|---|---|---|---|---|
| CP-01 | Horario libre; 0 citas previas | Reserva creada | `AgendaServiceTest.debeReservarHorarioDisponible` | ☐ Aprobó ☐ Falló |
| CP-02 | 19 citas previas (límite inferior del cupo; también 0 y 10) | Reserva creada | `AgendaServiceTest.debeAceptarMientrasHayaCupo` (parametrizada: 0, 10, 19) | ☐ Aprobó ☐ Falló |
| CP-03 | 20 citas previas (cupo lleno) | `CupoDiarioExcedidoException` | `AgendaServiceTest.debeRechazarCuandoElCupoEstaLleno` | ☐ Aprobó ☐ Falló |
| CP-04 | Horario ocupado | `HorarioNoDisponibleException` | `AgendaServiceTest.noDebePermitirDobleReserva` | ☐ Aprobó ☐ Falló |
| CP-05 | Profesional vacío | HTTP 400 | `CitaControllerTest.debeResponder400SiFaltanDatos` | ☐ Aprobó ☐ Falló |
| CP-06 | Fecha pasada | HTTP 400 | `CitaControllerTest.debeResponder400SiLaFechaEsPasada` | ☐ Aprobó ☐ Falló |
| CP-07 | Horario ocupado vía REST | HTTP 409 | `CitaControllerTest.debeResponder409SiElHorarioEstaOcupado` | ☐ Aprobó ☐ Falló |
| CP-08 | Dos reservas iguales vía HTTP | 201, luego 409; una sola fila | `ReservaIntegracionTest.flujoCompletoDeDobleReserva` | ☐ Aprobó ☐ Falló |

## Pruebas complementarias (no numeradas en la guía)
| Escenario | Prueba |
|---|---|
| Detecta horario ocupado (otro horario y otro profesional no cuentan) | `CitaRepositoryTest.debeDetectarHorarioOcupado` |
| Cuenta solo las citas del día (incluye 23:59, excluye el día siguiente) | `CitaRepositoryTest.debeContarSoloLasCitasDelDia` |
| El formulario muestra "Cita reservada" | `ReservaUITest.debeConfirmarLaReservaAlPaciente` |
| El formulario muestra "Horario no disponible" ante duplicado | `ReservaUITest.debeRechazarDobleReserva` |

## Resumen de ejecución (completar)
| Clase | Ejecutadas | Aprobadas | Falladas | Duración |
|---|---|---|---|---|
| AgendaServiceTest | | | | |
| CitaControllerTest | | | | |
| CitaRepositoryTest | | | | |
| ReservaIntegracionTest | | | | |
| ReservaUITest | | | | |
| `mvn verify` (BUILD) | | | | |
| Cobertura JaCoCo (líneas, mínimo 70 %) | | | | |
