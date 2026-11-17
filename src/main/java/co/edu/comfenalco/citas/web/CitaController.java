package co.edu.comfenalco.citas.web;

import co.edu.comfenalco.citas.modelo.Cita;
import co.edu.comfenalco.citas.servicio.AgendaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final AgendaService agenda;

    public CitaController(AgendaService agenda) {
        this.agenda = agenda;
    }

    @PostMapping
    public ResponseEntity<CitaResponse> reservar(@Valid @RequestBody ReservaRequest req) {
        Cita cita = agenda.reservar(req.profesional(), req.fechaHora(), req.pacienteId());
        return ResponseEntity.status(HttpStatus.CREATED).body(CitaResponse.de(cita));
    }
}
