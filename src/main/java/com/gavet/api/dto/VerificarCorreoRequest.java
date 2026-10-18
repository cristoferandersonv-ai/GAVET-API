package com.gavet.api.dto;

public class VerificarCorreoRequest {

    private String correo;

    public VerificarCorreoRequest() {
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(
            String correo
    ) {
        this.correo = correo;
    }
}