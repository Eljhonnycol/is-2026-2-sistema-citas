package co.edu.comfenalco.citas.repositorio;

import co.edu.comfenalco.citas.modelo.Cita;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    boolean existsByProfesionalAndFechaHora(String profesional, LocalDateTime fechaHora);

    long countByProfesionalAndFechaHoraBetween(String profesional, LocalDateTime inicio, LocalDateTime fin);
}
