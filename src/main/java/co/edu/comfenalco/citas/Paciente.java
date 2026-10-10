package co.edu.comfenalco.citas;

import java.util.regex.Pattern;

/**
 * Paciente del sistema de citas. Todos los campos son obligatorios.
 */
public record Paciente(String documento, String nombre, String correo) {

    private static final Pattern FORMATO_CORREO = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    public Paciente {
        documento = exigirTexto(documento, "El documento");
        nombre = exigirTexto(nombre, "El nombre");
        correo = exigirTexto(correo, "El correo");
        if (!FORMATO_CORREO.matcher(correo).matches()) {
            throw new IllegalArgumentException("El correo no tiene un formato válido: " + correo);
        }
    }

    private static String exigirTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " es obligatorio");
        }
        return valor.trim();
    }
}
