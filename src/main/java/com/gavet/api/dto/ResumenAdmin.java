
package com.gavet.api.dto;

public record ResumenAdmin(
        long citasHoy,
        long pendientes,
        long enAtencion,
        long completadas
) {
}
