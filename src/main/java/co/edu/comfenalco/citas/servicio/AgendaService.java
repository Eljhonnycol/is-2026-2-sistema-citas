package co.edu.comfenalco.citas.servicio;

import co.edu.comfenalco.citas.modelo.Cita;
import co.edu.comfenalco.citas.repositorio.CitaRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgendaService {

    public static final int MAX_CITAS_DIA = 20;

    private final CitaRepository repo;

    public AgendaService(CitaRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public Cita reservar(String profesional, LocalDateTime fechaHora, String pacienteId) {
        if (repo.existsByProfesionalAndFechaHora(profesional, fechaHora)) {
            throw new HorarioNoDisponibleException("Horario ocupado: " + fechaHora);
        }
        LocalDate dia = fechaHora.toLocalDate();
        long previas = repo.countByProfesionalAndFechaHoraBetween(
                profesional, dia.atStartOfDay(), dia.atTime(LocalTime.MAX));
        if (previas >= MAX_CITAS_DIA) {
            throw new CupoDiarioExcedidoException("Cupo diario lleno: " + dia);
        }
        try {
            return repo.saveAndFlush(new Cita(profesional, fechaHora, pacienteId));
        } catch (DataIntegrityViolationException ex) {
            throw new HorarioNoDisponibleException("Horario ocupado: " + fechaHora);
        }
    }
}
