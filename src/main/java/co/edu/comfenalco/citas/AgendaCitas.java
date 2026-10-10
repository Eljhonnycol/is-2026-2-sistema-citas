package co.edu.comfenalco.citas;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Agenda en memoria de citas. Cada profesional atiende en franjas de 30 minutos,
 * entre las 08:00 y las 18:00 (la última franja empieza a las 17:30).
 */
public class AgendaCitas {

    public static final LocalTime HORA_INICIO = LocalTime.of(8, 0);
    public static final LocalTime HORA_FIN = LocalTime.of(18, 0);
    public static final int MINUTOS_POR_FRANJA = 30;

    /** Debe coincidir con config/reglas-agenda.md. */
    public static final int MAX_CITAS_POR_DIA = 20;

    private final RegistroPacientes registro;
    private final Clock reloj;
    private final List<Cita> citas = new ArrayList<>();

    public AgendaCitas(RegistroPacientes registro) {
        this(registro, Clock.systemDefaultZone());
    }

    public AgendaCitas(RegistroPacientes registro, Clock reloj) {
        this.registro = Objects.requireNonNull(registro, "El registro de pacientes es obligatorio");
        this.reloj = Objects.requireNonNull(reloj, "El reloj es obligatorio");
    }

    /**
     * Agenda una cita para un paciente registrado.
     *
     * @throws IllegalArgumentException si el paciente no está registrado, falta algún dato,
     *         la fecha no es futura o no corresponde a una franja válida
     * @throws IllegalStateException si el horario ya está ocupado o el profesional
     *         alcanzó el máximo de citas del día
     */
    public Cita agendar(String documentoPaciente, String profesional, String especialidad,
            LocalDateTime fechaHora) {
        Paciente paciente = registro.buscarPorDocumento(documentoPaciente)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El paciente no está registrado: " + documentoPaciente));
        String nombreProfesional = exigirTexto(profesional, "El profesional");
        String nombreEspecialidad = exigirTexto(especialidad, "La especialidad");
        validarFecha(fechaHora);

        List<Cita> delDia = citasDelProfesional(nombreProfesional, fechaHora.toLocalDate());
        if (delDia.size() >= MAX_CITAS_POR_DIA) {
            throw new IllegalStateException("El profesional ya alcanzó el máximo de "
                    + MAX_CITAS_POR_DIA + " citas para el " + fechaHora.toLocalDate());
        }
        if (delDia.stream().anyMatch(c -> c.fechaHora().equals(fechaHora))) {
            throw new IllegalStateException("El profesional ya tiene una cita el " + fechaHora);
        }

        Cita cita = new Cita(paciente, nombreProfesional, nombreEspecialidad, fechaHora);
        citas.add(cita);
        return cita;
    }

    /** Citas de un profesional en un día, ordenadas por hora. */
    public List<Cita> citasDelProfesional(String profesional, LocalDate fecha) {
        String nombre = exigirTexto(profesional, "El profesional");
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha es obligatoria");
        }
        return citas.stream()
                .filter(c -> c.profesional().equals(nombre))
                .filter(c -> c.fechaHora().toLocalDate().equals(fecha))
                .sorted(Comparator.comparing(Cita::fechaHora))
                .toList();
    }

    private void validarFecha(LocalDateTime fechaHora) {
        if (fechaHora == null) {
            throw new IllegalArgumentException("La fecha y hora son obligatorias");
        }
        if (!fechaHora.isAfter(LocalDateTime.now(reloj))) {
            throw new IllegalArgumentException("La cita debe ser en una fecha futura: " + fechaHora);
        }
        if (!esFranjaValida(fechaHora.toLocalTime())) {
            throw new IllegalArgumentException(
                    "La hora debe ser una franja de 30 minutos entre 08:00 y 17:30: " + fechaHora);
        }
    }

    private static boolean esFranjaValida(LocalTime hora) {
        boolean dentroDelHorario = !hora.isBefore(HORA_INICIO) && hora.isBefore(HORA_FIN);
        boolean alineada = hora.getMinute() % MINUTOS_POR_FRANJA == 0
                && hora.getSecond() == 0 && hora.getNano() == 0;
        return dentroDelHorario && alineada;
    }

    private static String exigirTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " es obligatorio");
        }
        return valor.trim();
    }
}
