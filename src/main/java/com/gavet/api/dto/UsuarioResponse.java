package com.gavet.api.dto;

public class UsuarioResponse {

    private Integer id;
    private String nombre;
    private String correo;
    private String telefono;

    public UsuarioResponse(
            Integer id,
            String nombre,
            String correo,
            String telefono
    ) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getTelefono() {
        return telefono;
    }
}