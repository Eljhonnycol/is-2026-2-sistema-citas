package co.edu.comfenalco.citas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistroPacientesTest {

    private RegistroPacientes registro;

    @BeforeEach
    void preparar() {
        registro = new RegistroPacientes();
    }

    @Test
    void registraUnPacienteValidoYLoEncuentraPorDocumento() {
        registro.registrar("1001", "Ana Pérez", "ana@correo.com");

        Optional<Paciente> encontrado = registro.buscarPorDocumento("1001");

        assertTrue(encontrado.isPresent());
        assertEquals("Ana Pérez", encontrado.get().nombre());
        assertEquals("ana@correo.com", encontrado.get().correo());
    }

    @Test
    void rechazaDocumentoNuloOVacio() {
        assertThrows(IllegalArgumentException.class,
                () -> registro.registrar(null, "Ana", "ana@correo.com"));
        assertThrows(IllegalArgumentException.class,
                () -> registro.registrar("  ", "Ana", "ana@correo.com"));
    }

    @Test
    void rechazaNombreNuloOVacio() {
        assertThrows(IllegalArgumentException.class,
                () -> registro.registrar("1001", null, "ana@correo.com"));
        assertThrows(IllegalArgumentException.class,
                () -> registro.registrar("1001", "", "ana@correo.com"));
    }

    @Test
    void rechazaCorreoNuloOVacio() {
        assertThrows(IllegalArgumentException.class,
                () -> registro.registrar("1001", "Ana", null));
        assertThrows(IllegalArgumentException.class,
                () -> registro.registrar("1001", "Ana", " "));
    }

    @Test
    void rechazaCorreosSinFormatoValido() {
        for (String correo : new String[] {"sin-arroba", "ana@dominio", "@correo.com", "ana@.com"}) {
            assertThrows(IllegalArgumentException.class,
                    () -> registro.registrar("1001", "Ana", correo), correo);
        }
    }

    @Test
    void rechazaDocumentoRepetidoSinSobrescribirAlPacienteExistente() {
        registro.registrar("1001", "Ana Pérez", "ana@correo.com");

        assertThrows(IllegalStateException.class,
                () -> registro.registrar("1001", "Otra Persona", "otra@correo.com"));

        assertEquals("Ana Pérez", registro.buscarPorDocumento("1001").orElseThrow().nombre());
    }

    @Test
    void buscarUnDocumentoInexistenteDevuelveVacio() {
        assertTrue(registro.buscarPorDocumento("9999").isEmpty());
        assertTrue(registro.buscarPorDocumento(null).isEmpty());
    }
}
