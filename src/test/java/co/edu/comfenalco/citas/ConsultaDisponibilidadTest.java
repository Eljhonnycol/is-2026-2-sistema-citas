package co.edu.comfenalco.citas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConsultaDisponibilidadTest {

    private static final ZoneId ZONA = ZoneId.of("America/Bogota");
    private static final LocalDate DIA = LocalDate.of(2026, 10, 12);

    private AgendaCitas agenda;
    private ConsultaDisponibilidad consulta;

    @BeforeEach
    void preparar() {
        RegistroPacientes registro = new RegistroPacientes();
        registro.registrar("1001", "Ana Pérez", "ana@correo.com");
        // "Ahora" fijo: viernes 9 de octubre de 2026, 10:00
        Clock reloj = Clock.fixed(Instant.parse("2026-10-09T15:00:00Z"), ZONA);
        agenda = new AgendaCitas(registro, reloj);
        consulta = new ConsultaDisponibilidad(agenda);
    }

    @Test
    void unDiaSinCitasDevuelveTodasLasFranjas() {
        List<LocalDateTime> libres = consulta.franjasLibres("Dr. Gómez", DIA);

        assertEquals(AgendaCitas.MAX_CITAS_POR_DIA, libres.size());
        assertEquals(DIA.atTime(8, 0), libres.get(0));
        assertEquals(DIA.atTime(17, 30), libres.get(libres.size() - 1));
    }

    @Test
    void unaCitaAgendadaOcupaSuFranjaYDejaDeAparecer() {
        agenda.agendar("1001", "Dr. Gómez", "cardiologia", DIA.atTime(9, 0));

        List<LocalDateTime> libres = consulta.franjasLibres("Dr. Gómez", DIA);

        assertEquals(AgendaCitas.MAX_CITAS_POR_DIA - 1, libres.size());
        assertTrue(!libres.contains(DIA.atTime(9, 0)));
        assertTrue(libres.contains(DIA.atTime(8, 30)));
        assertTrue(libres.contains(DIA.atTime(9, 30)));
    }

    @Test
    void lasCitasDeOtroProfesionalOUnDiaDistintoNoAfectanElResultado() {
        agenda.agendar("1001", "Dr. Gómez", "cardiologia", DIA.atTime(9, 0));

        assertEquals(AgendaCitas.MAX_CITAS_POR_DIA,
                consulta.franjasLibres("Dra. Rojas", DIA).size());
        assertEquals(AgendaCitas.MAX_CITAS_POR_DIA,
                consulta.franjasLibres("Dr. Gómez", DIA.plusDays(1)).size());
    }

    @Test
    void unDiaCompletoDevuelveUnaListaVacia() {
        LocalTime hora = AgendaCitas.HORA_INICIO;
        for (int i = 0; i < AgendaCitas.MAX_CITAS_POR_DIA; i++) {
            agenda.agendar("1001", "Dr. Gómez", "cardiologia", DIA.atTime(hora));
            hora = hora.plusMinutes(AgendaCitas.MINUTOS_POR_FRANJA);
        }

        assertTrue(consulta.franjasLibres("Dr. Gómez", DIA).isEmpty());
    }

    @Test
    void rechazaProfesionalVacioOFechaNula() {
        assertThrows(IllegalArgumentException.class, () -> consulta.franjasLibres(null, DIA));
        assertThrows(IllegalArgumentException.class, () -> consulta.franjasLibres("  ", DIA));
        assertThrows(IllegalArgumentException.class, () -> consulta.franjasLibres("Dr. Gómez", null));
    }

    @Test
    void cadaFranjaLibreSePuedeAgendar() {
        agenda.agendar("1001", "Dr. Gómez", "cardiologia", DIA.atTime(8, 0));

        for (LocalDateTime franja : consulta.franjasLibres("Dr. Gómez", DIA)) {
            agenda.agendar("1001", "Dr. Gómez", "cardiologia", franja);
        }

        assertTrue(consulta.franjasLibres("Dr. Gómez", DIA).isEmpty());
    }
}
