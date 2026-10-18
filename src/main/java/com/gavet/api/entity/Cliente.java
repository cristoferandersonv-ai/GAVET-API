package com.gavet.api.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer idCliente;

    @Column(
        name = "nombre_completo",
        nullable = false,
        length = 150
    )
    private String nombreCompleto;

    @Column(
        name = "telefono",
        nullable = false,
        unique = true,
        length = 9
    )
    private String telefono;

    @Column(
        name = "email",
        nullable = false,
        unique = true,
        length = 150
    )
    private String email;

    /*
     * Para este proyecto académico,
     * la contraseña se guarda directamente.
     */
    @Column(
        name = "password",
        nullable = false,
        length = 255
    )
    private String password;

    @Column(
        name = "direccion",
        length = 255
    )
    private String direccion;

    @Column(
        name = "activo",
        nullable = false
    )
    private Boolean activo = true;

    @Column(
        name = "fecha_registro",
        insertable = false,
        updatable = false
    )
    private LocalDateTime fechaRegistro;

    /*
     * Constructor vacío requerido por JPA.
     */
    public Cliente() {
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(
            Integer idCliente
    ) {
        this.idCliente = idCliente;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(
            String nombreCompleto
    ) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(
            String telefono
    ) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(
            String email
    ) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(
            String password
    ) {
        this.password = password;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(
            String direccion
    ) {
        this.direccion = direccion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(
            Boolean activo
    ) {
        this.activo = activo;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(
            LocalDateTime fechaRegistro
    ) {
        this.fechaRegistro = fechaRegistro;
    }
}