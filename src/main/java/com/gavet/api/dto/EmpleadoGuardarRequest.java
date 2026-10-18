
package com.gavet.api.dto;

public record EmpleadoGuardarRequest(
        String nombre,
        String correo,
        String telefono,
        String password
) {
}
