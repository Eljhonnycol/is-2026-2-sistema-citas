package co.edu.comfenalco.citas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AgendaCitasTest {

    private static final ZoneId ZONA = ZoneId.of("America/Bogota");
    private static final LocalDate DIA = LocalDate.of(2026, 10, 12);

    private AgendaCitas agenda;

    @BeforeEach
    void preparar() {
        RegistroPacientes registro = new RegistroPacientes();
        registro.registrar("1001", "Ana Pérez", "ana@correo.com");
        registro.registrar("1002", "Luis Mora", "luis@correo.com");
        // "Ahora" fijo: viernes 9 de octubre de 2026, 10:00
        Clock reloj = Clock.fixed(Instant.parse("2026-10-09T15:00:00Z"), ZONA);
        agenda = new AgendaCitas(registro, reloj);
    }

    @Test
    void agendaUnaCitaValidaYLaDejaGuardada() {
        Cita cita = agenda.agendar("1001", "Dr. Gómez", "cardiologia", DIA.atTime(9, 0));

        assertEquals("Ana Pérez", cita.paciente().nombre());
        assertEquals(List.of(cita), agenda.citasDelProfesional("Dr. Gómez", DIA));
    }

    @Test
    void rechazaUnPacienteNoRegistrado() {
        assertThrows(IllegalArgumentException.class,
                () -> agenda.agendar("9999", "Dr. Gómez", "cardiologia", DIA.atTime(9, 0)));
        assertThrows(IllegalArgumentException.class,
                () -> agenda.agendar(null, "Dr. Gómez", "cardiologia", DIA.atTime(9, 0)));
    }

    @Test
    void rechazaDatosObligatoriosVacios() {
        assertThrows(IllegalArgumentException.class,
                () -> agenda.agendar("1001", " ", "cardiologia", DIA.atTime(9, 0)));
        assertThrows(IllegalArgumentException.class,
                () -> agenda.agendar("1001", "Dr. Gómez", null, DIA.atTime(9, 0)));
        assertThrows(IllegalArgumentException.class,
                () -> agenda.agendar("1001", "Dr. Gómez", "cardiologia", null));
    }

    @Test
    void rechazaFechasPasadasOIgualesAAhora() {
        assertThrows(IllegalArgumentException.class, () -> agenda.agendar(
                "1001", "Dr. Gómez", "cardiologia", LocalDateTime.of(2026, 10, 8, 9, 0)));
        assertThrows(IllegalArgumentException.class, () -> agenda.agendar(
                "1001", "Dr. Gómez", "cardiologia", LocalDateTime.of(2026, 10, 9, 10, 0)));
    }

    @Test
    void rechazaHorasFueraDeLasFranjasValidas() {
        LocalTime[] invalidas = {
            LocalTime.of(7, 30), LocalTime.of(18, 0), LocalTime.of(9, 15), LocalTime.of(9, 0, 30)
        };
        for (LocalTime hora : invalidas) {
            assertThrows(IllegalArgumentException.class,
                    () -> agenda.agendar("1001", "Dr. Gómez", "cardiologia", DIA.atTime(hora)),
                    hora.toString());
        }
    }

    @Test
    void aceptaLaPrimeraYLaUltimaFranjaDelDia() {
        agenda.agendar("1001", "Dr. Gómez", "cardiologia", DIA.atTime(8, 0));
        agenda.agendar("1001", "Dr. Gómez", "cardiologia", DIA.atTime(17, 30));

        assertEquals(2, agenda.citasDelProfesional("Dr. Gómez", DIA).size());
    }

    @Test
    void rechazaDosCitasDelMismoProfesionalALaMismaHora() {
        agenda.agendar("1001", "Dr. Gómez", "cardiologia", DIA.atTime(9, 0));

        assertThrows(IllegalStateException.class,
                () -> agenda.agendar("1002", "Dr. Gómez", "cardiologia", DIA.atTime(9, 0)));
        assertEquals(1, agenda.citasDelProfesional("Dr. Gómez", DIA).size());
    }

    @Test
    void permiteLaMismaHoraConOtroProfesionalOElMismoProfesionalEnOtraHora() {
        agenda.agendar("1001", "Dr. Gómez", "cardiologia", DIA.atTime(9, 0));
        agenda.agendar("1002", "Dra. Rojas", "pediatria", DIA.atTime(9, 0));
        agenda.agendar("1002", "Dr. Gómez", "cardiologia", DIA.atTime(9, 30));

        assertEquals(2, agenda.citasDelProfesional("Dr. Gómez", DIA).size());
        assertEquals(1, agenda.citasDelProfesional("Dra. Rojas", DIA).size());
    }

    @Test
    void rechazaMasDelMaximoDeCitasPorDiaParaUnProfesional() {
        LocalTime hora = AgendaCitas.HORA_INICIO;
        for (int i = 0; i < AgendaCitas.MAX_CITAS_POR_DIA; i++) {
            agenda.agendar("1001", "Dr. Gómez", "cardiologia", DIA.atTime(hora));
            hora = hora.plusMinutes(AgendaCitas.MINUTOS_POR_FRANJA);
        }

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> agenda.agendar("1002", "Dr. Gómez", "cardiologia", DIA.atTime(9, 0)));
        assertTrue(error.getMessage().contains("máximo"));

        // El límite es por profesional y por día
        agenda.agendar("1002", "Dra. Rojas", "pediatria", DIA.atTime(9, 0));
        agenda.agendar("1002", "Dr. Gómez", "cardiologia", DIA.plusDays(1).atTime(9, 0));
    }

    @Test
    void elMaximoDeCitasCoincideConElArchivoDeReglas() throws IOException {
        String regla = Files.readString(Path.of("config", "reglas-agenda.md")).trim();

        assertTrue(regla.endsWith(": " + AgendaCitas.MAX_CITAS_POR_DIA), regla);
    }
}
