package com.gavet.api.dto;


public class EmpleadoResponse {


    private Integer id;

    private String nombre;

    private String rol;


    public EmpleadoResponse(
            Integer id,
            String nombre,
            String rol){

        this.id=id;
        this.nombre=nombre;
        this.rol=rol;
    }


    public Integer getId(){
        return id;
    }


    public String getNombre(){
        return nombre;
    }


    public String getRol(){
        return rol;
    }

}