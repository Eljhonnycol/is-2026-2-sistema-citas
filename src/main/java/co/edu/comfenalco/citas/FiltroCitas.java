package co.edu.comfenalco.citas;

import java.util.List;
import java.util.stream.Collectors;

public class FiltroCitas {

    public List<String> filtrarPorEspecialidad(List<String> citas, String especialidad) {
        if (especialidad == null || especialidad.isBlank()) {
            throw new IllegalArgumentException("La especialidad es obligatoria");
        }
        return citas.stream()
                .filter(cita -> cita.contains(especialidad))
                .collect(Collectors.toList());
    }
}
