package com.gavet.api.dto;

public class AuthResponse {

    private boolean exito;

    private String mensaje;

    private UsuarioResponse usuario;

    private boolean cuentaDesactivada;

    /*
     * Constructor utilizado por las respuestas
     * normales de registro e inicio de sesión.
     */
    public AuthResponse(
            boolean exito,
            String mensaje,
            UsuarioResponse usuario
    ) {

        this(
            exito,
            mensaje,
            usuario,
            false
        );
    }

    /*
     * Constructor utilizado cuando necesitamos
     * indicar que una cuenta está desactivada.
     */
    public AuthResponse(
            boolean exito,
            String mensaje,
            UsuarioResponse usuario,
            boolean cuentaDesactivada
    ) {

        this.exito =
                exito;

        this.mensaje =
                mensaje;

        this.usuario =
                usuario;

        this.cuentaDesactivada =
                cuentaDesactivada;
    }

    public boolean isExito() {

        return exito;
    }

    public String getMensaje() {

        return mensaje;
    }

    public UsuarioResponse getUsuario() {

        return usuario;
    }

    public boolean isCuentaDesactivada() {

        return cuentaDesactivada;
    }
}