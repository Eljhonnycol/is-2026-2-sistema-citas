package co.edu.comfenalco.citas;

import java.util.List;
import java.util.stream.Collectors;

public class FiltroCitas {

    public List<String> filtrar(List<String> citas, String e) {
        return citas.stream()
               .filter(c -> c.contains(e))
               .collect(Collectors.toList());
    }
}
