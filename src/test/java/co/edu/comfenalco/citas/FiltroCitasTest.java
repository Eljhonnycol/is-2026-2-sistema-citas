package co.edu.comfenalco.citas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class FiltroCitasTest {

    private final FiltroCitas filtro = new FiltroCitas();

    private final List<String> citas = List.of(
            "cardiologia|2026-10-12",
            "pediatria|2026-10-13",
            "cardiologia|2026-10-14");

    @Test
    void devuelveSoloLasCitasDeLaEspecialidad() {
        List<String> resultado = filtro.filtrarPorEspecialidad(citas, "cardiologia");

        assertEquals(List.of("cardiologia|2026-10-12", "cardiologia|2026-10-14"), resultado);
    }

    @Test
    void devuelveListaVaciaSiNoHayCoincidencias() {
        assertTrue(filtro.filtrarPorEspecialidad(citas, "neurologia").isEmpty());
    }

    @Test
    void lanzaExcepcionSiLaEspecialidadEsNula() {
        assertThrows(IllegalArgumentException.class, () -> filtro.filtrarPorEspecialidad(citas, null));
    }

    @Test
    void lanzaExcepcionSiLaEspecialidadEstaVacia() {
        assertThrows(IllegalArgumentException.class, () -> filtro.filtrarPorEspecialidad(citas, "  "));
    }
}
