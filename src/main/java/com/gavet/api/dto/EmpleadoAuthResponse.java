
package com.gavet.api.dto;

public class EmpleadoAuthResponse {

    private boolean exito;
    private String mensaje;
    private EmpleadoUsuarioResponse empleado;

    public EmpleadoAuthResponse(
            boolean exito,
            String mensaje,
            EmpleadoUsuarioResponse empleado) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.empleado = empleado;
    }

    public boolean isExito() {
        return exito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public EmpleadoUsuarioResponse getEmpleado() {
        return empleado;
    }
}
