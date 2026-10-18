package com.gavet.api.dto;

public class MensajeResponse {

    private boolean exito;
    private String mensaje;

    public MensajeResponse(
            boolean exito,
            String mensaje
    ) {
        this.exito = exito;
        this.mensaje = mensaje;
    }

    public boolean isExito() {
        return exito;
    }

    public String getMensaje() {
        return mensaje;
    }
}