
package com.gavet.api.dto;

import java.math.BigDecimal;

public record CitaPanel(
        Integer id,
        String fecha,
        String hora,
        String estado,
        String mascota,
        String cliente,
        String servicio,
        Integer idEstilista,
        String estilista,
        String observaciones,
        BigDecimal precioTotal,
        Integer duracionMinutos
) {
}
