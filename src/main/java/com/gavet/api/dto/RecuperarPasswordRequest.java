package com.gavet.api.dto;

public class RecuperarPasswordRequest {

    private String correo;
    private String passwordNueva;

    public RecuperarPasswordRequest() {
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(
            String correo
    ) {
        this.correo = correo;
    }

    public String getPasswordNueva() {
        return passwordNueva;
    }

    public void setPasswordNueva(
            String passwordNueva
    ) {
        this.passwordNueva = passwordNueva;
    }
}