package co.edu.comfenalco.citas.web;

import co.edu.comfenalco.citas.servicio.CupoDiarioExcedidoException;
import co.edu.comfenalco.citas.servicio.HorarioNoDisponibleException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ManejadorErrores {

    @ExceptionHandler({HorarioNoDisponibleException.class, CupoDiarioExcedidoException.class})
    public ResponseEntity<Map<String, String>> conflicto(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
    }
}
