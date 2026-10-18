
package com.gavet.api.dto;

import java.math.BigDecimal;

public record ServicioGuardarRequest(
        String nombre,
        String descripcion,
        BigDecimal precio,
        Integer duracionMinutos,
        String tipo
) {
}
