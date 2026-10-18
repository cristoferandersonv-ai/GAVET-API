package com.gavet.api.dto;


public class LoginEmpleadoRequest {


    private String correo;

    private String password;


    public LoginEmpleadoRequest(){

    }


    public String getCorreo(){
        return correo;
    }


    public void setCorreo(String correo){
        this.correo = correo;
    }


    public String getPassword(){
        return password;
    }


    public void setPassword(String password){
        this.password=password;
    }

}