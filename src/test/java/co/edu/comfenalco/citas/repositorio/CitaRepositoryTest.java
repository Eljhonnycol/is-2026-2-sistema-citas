package co.edu.comfenalco.citas.repositorio;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.comfenalco.citas.modelo.Cita;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
class CitaRepositoryTest {

    @Autowired
    CitaRepository repo;

    private static final String PROF = "Dr. Pérez";
    private static final LocalDateTime HORA = LocalDateTime.of(2030, 11, 4, 8, 0);

    @Test
    void debeDetectarHorarioOcupado() {
        repo.save(new Cita(PROF, HORA, "paciente-001"));

        assertThat(repo.existsByProfesionalAndFechaHora(PROF, HORA)).isTrue();
        assertThat(repo.existsByProfesionalAndFechaHora(PROF, HORA.plusHours(1))).isFalse();
        assertThat(repo.existsByProfesionalAndFechaHora("Dra. Gómez", HORA)).isFalse();
    }

    @Test
    void debeContarSoloLasCitasDelDia() {
        repo.save(new Cita(PROF, HORA, "p-1"));
        repo.save(new Cita(PROF, HORA.plusHours(1), "p-2"));
        repo.save(new Cita(PROF, HORA.withHour(23).withMinute(59), "p-3"));
        repo.save(new Cita(PROF, HORA.plusDays(1), "p-4"));

        LocalDate dia = HORA.toLocalDate();
        assertThat(repo.countByProfesionalAndFechaHoraBetween(
                PROF, dia.atStartOfDay(), dia.atTime(LocalTime.MAX))).isEqualTo(3);
    }
}
