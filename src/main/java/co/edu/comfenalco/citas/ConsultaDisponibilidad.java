package co.edu.comfenalco.citas;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Consulta los horarios libres de un profesional en un día, a partir de la agenda.
 */
public class ConsultaDisponibilidad {

    private final AgendaCitas agenda;

    public ConsultaDisponibilidad(AgendaCitas agenda) {
        this.agenda = Objects.requireNonNull(agenda, "La agenda es obligatoria");
    }

    /**
     * Franjas libres de un profesional en un día, ordenadas por hora.
     *
     * @throws IllegalArgumentException si el profesional está vacío o la fecha es nula
     */
    public List<LocalDateTime> franjasLibres(String profesional, LocalDate fecha) {
        Set<LocalDateTime> ocupadas = agenda.citasDelProfesional(profesional, fecha).stream()
                .map(Cita::fechaHora)
                .collect(Collectors.toSet());

        List<LocalDateTime> libres = new ArrayList<>();
        for (LocalTime hora = AgendaCitas.HORA_INICIO;
                hora.isBefore(AgendaCitas.HORA_FIN);
                hora = hora.plusMinutes(AgendaCitas.MINUTOS_POR_FRANJA)) {
            LocalDateTime franja = fecha.atTime(hora);
            if (!ocupadas.contains(franja)) {
                libres.add(franja);
            }
        }
        return List.copyOf(libres);
    }
}
