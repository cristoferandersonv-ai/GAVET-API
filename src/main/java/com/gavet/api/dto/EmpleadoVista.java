
package com.gavet.api.dto;

public record EmpleadoVista(
        Integer id,
        String nombre,
        String correo,
        String telefono,
        String rol,
        boolean activo
) {
}
