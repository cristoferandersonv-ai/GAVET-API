
package com.gavet.api.dto;

import java.util.List;

public record ClientePanel(
        Integer id,
        String nombre,
        String correo,
        String telefono,
        Boolean activo,
        List<MascotaPanel> mascotas
) {
}
