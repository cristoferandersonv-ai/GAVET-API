package com.gavet.api.dto;


public class UsuarioResponse {


    private Integer id;

    private String nombre;

    private String correo;

    private String telefono;

    private String tipoUsuario;

    private String rol;



    public UsuarioResponse(
            Integer id,
            String nombre,
            String correo,
            String telefono,
            String tipoUsuario,
            String rol
    ){

        this.id=id;
        this.nombre=nombre;
        this.correo=correo;
        this.telefono=telefono;
        this.tipoUsuario=tipoUsuario;
        this.rol=rol;

    }



    public Integer getId(){
        return id;
    }


    public String getNombre(){
        return nombre;
    }


    public String getCorreo(){
        return correo;
    }


    public String getTelefono(){
        return telefono;
    }


    public String getTipoUsuario(){
        return tipoUsuario;
    }


    public String getRol(){
        return rol;
    }

}