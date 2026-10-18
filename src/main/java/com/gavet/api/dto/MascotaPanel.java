
package com.gavet.api.dto;

public record MascotaPanel(
        Integer id,
        String nombre,
        String especie,
        String raza,
        Integer edadAnios,
        String notas,
        Boolean activo
) {
}
