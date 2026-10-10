package co.edu.comfenalco.citas;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Registro en memoria de pacientes, identificados por su documento.
 */
public class RegistroPacientes {

    private final Map<String, Paciente> pacientes = new HashMap<>();

    /**
     * Registra un paciente nuevo.
     *
     * @throws IllegalArgumentException si falta algún dato o el correo no es válido
     * @throws IllegalStateException si ya existe un paciente con ese documento
     */
    public Paciente registrar(String documento, String nombre, String correo) {
        Paciente paciente = new Paciente(documento, nombre, correo);
        if (pacientes.containsKey(paciente.documento())) {
            throw new IllegalStateException(
                    "Ya existe un paciente con el documento " + paciente.documento());
        }
        pacientes.put(paciente.documento(), paciente);
        return paciente;
    }

    public Optional<Paciente> buscarPorDocumento(String documento) {
        if (documento == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(pacientes.get(documento.trim()));
    }
}
