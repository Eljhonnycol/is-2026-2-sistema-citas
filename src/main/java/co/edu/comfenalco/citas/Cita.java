package co.edu.comfenalco.citas;

import java.time.LocalDateTime;

/**
 * Cita médica agendada para un paciente con un profesional.
 */
public record Cita(Paciente paciente, String profesional, String especialidad, LocalDateTime fechaHora) {
}
