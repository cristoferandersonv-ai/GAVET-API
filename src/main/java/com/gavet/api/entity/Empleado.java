package com.gavet.api.entity;


import java.time.LocalDateTime;

import com.gavet.api.enums.RolEmpleado;

import jakarta.persistence.*;


@Entity
@Table(name = "empleados")
public class Empleado {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empleado")
    private Integer idEmpleado;



    @Column(name="nombre_completo",
            nullable=false,
            length=150)
    private String nombreCompleto;



    @Enumerated(EnumType.STRING)
    @Column(name="rol",
            nullable=false)
    private RolEmpleado rol;



    @Column(name="telefono",
            length=9)
    private String telefono;



    @Column(name="email",
            unique=true)
    private String email;



    @Column(name="password")
    private String password;



    @Column(name="activo")
    private Boolean activo = true;



    @Column(name="fecha_registro",
            insertable=false,
            updatable=false)
    private LocalDateTime fechaRegistro;



    public Empleado(){
    }



    public Integer getIdEmpleado(){
        return idEmpleado;
    }


    public void setIdEmpleado(Integer idEmpleado){
        this.idEmpleado=idEmpleado;
    }



    public String getNombreCompleto(){
        return nombreCompleto;
    }


    public void setNombreCompleto(String nombreCompleto){
        this.nombreCompleto=nombreCompleto;
    }



    public RolEmpleado getRol(){
        return rol;
    }


    public void setRol(RolEmpleado rol){
        this.rol=rol;
    }



    public String getTelefono(){
        return telefono;
    }


    public void setTelefono(String telefono){
        this.telefono=telefono;
    }



    public String getEmail(){
        return email;
    }


    public void setEmail(String email){
        this.email=email;
    }



    public String getPassword(){
        return password;
    }


    public void setPassword(String password){
        this.password=password;
    }



    public Boolean getActivo(){
        return activo;
    }


    public void setActivo(Boolean activo){
        this.activo=activo;
    }



    public LocalDateTime getFechaRegistro(){
        return fechaRegistro;
    }


    public void setFechaRegistro(LocalDateTime fechaRegistro){
        this.fechaRegistro=fechaRegistro;
    }

}