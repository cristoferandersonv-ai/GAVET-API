
package com.gavet.api.dto;

import java.math.BigDecimal;

public record ReporteAtenciones(
        String desde,
        String hasta,
        long total,
        long proximas,
        long confirmadas,
        long enAtencion,
        long completadas,
        long canceladas,
        BigDecimal ingresosCompletados
) {
}
