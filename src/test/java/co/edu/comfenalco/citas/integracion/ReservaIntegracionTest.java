package co.edu.comfenalco.citas.integracion;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.comfenalco.citas.repositorio.CitaRepository;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservaIntegracionTest {

    @LocalServerPort
    private int puerto;

    @Autowired
    private CitaRepository repo;

    private RestClient cliente;

    @BeforeEach
    void configurar() {
        repo.deleteAll();
        cliente = RestClient.builder().baseUrl("http://localhost:" + puerto).build();
    }

    private Map<String, String> reserva(String paciente) {
        return Map.of("profesional", "Dr. Pérez",
                "fechaHora", "2030-11-04T08:00:00",
                "pacienteId", paciente);
    }

    @Test
    void flujoCompletoDeDobleReserva() {
        ResponseEntity<String> primera = cliente.post().uri("/api/citas")
                .body(reserva("paciente-001")).retrieve().toEntity(String.class);
        assertThat(primera.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<String> segunda = cliente.post().uri("/api/citas")
                .body(reserva("paciente-002"))
                .exchange((request, response) -> ResponseEntity
                        .status(response.getStatusCode())
                        .body(new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8)));
        assertThat(segunda.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(segunda.getBody()).contains("error");

        assertThat(repo.count()).isEqualTo(1);
    }
}
