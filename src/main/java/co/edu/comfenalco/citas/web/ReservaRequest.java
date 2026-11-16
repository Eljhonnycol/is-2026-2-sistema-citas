package co.edu.comfenalco.citas.web;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record ReservaRequest(
        @NotBlank String profesional,
        @NotNull @Future LocalDateTime fechaHora,
        @NotBlank String pacienteId) {
}
