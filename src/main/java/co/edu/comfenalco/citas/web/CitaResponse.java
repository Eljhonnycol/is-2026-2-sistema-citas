package co.edu.comfenalco.citas.web;

import co.edu.comfenalco.citas.modelo.Cita;
import java.time.LocalDateTime;

public record CitaResponse(Long id, String profesional, LocalDateTime fechaHora) {

    public static CitaResponse de(Cita cita) {
        return new CitaResponse(cita.getId(), cita.getProfesional(), cita.getFechaHora());
    }
}
