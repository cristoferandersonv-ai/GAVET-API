package com.gavet.api.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.gavet.api.enums.EspecieMascota;
import com.gavet.api.enums.SexoMascota;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "mascotas")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mascota")
    private Integer idMascota;

    @Column(
        name = "id_cliente",
        nullable = false
    )
    private Integer idCliente;

    @Column(
        name = "nombre",
        nullable = false,
        length = 80
    )
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "especie",
        nullable = false
    )
    private EspecieMascota especie;

    @Column(
        name = "raza",
        nullable = false,
        length = 100
    )
    private String raza;

    @Column(
        name = "edad_anios",
        nullable = false
    )
    private Integer edadAnios;

    @Column(
        name = "peso",
        nullable = false,
        precision = 5,
        scale = 2
    )
    private BigDecimal peso;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "sexo",
        nullable = false
    )
    private SexoMascota sexo;

    @Column(
        name = "notas",
        length = 300
    )
    private String notas;

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
    public Mascota() {
    }

    public Integer getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(
            Integer idMascota
    ) {
        this.idMascota = idMascota;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(
            Integer idCliente
    ) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(
            String nombre
    ) {
        this.nombre = nombre;
    }

    public EspecieMascota getEspecie() {
        return especie;
    }

    public void setEspecie(
            EspecieMascota especie
    ) {
        this.especie = especie;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(
            String raza
    ) {
        this.raza = raza;
    }

    public Integer getEdadAnios() {
        return edadAnios;
    }

    public void setEdadAnios(
            Integer edadAnios
    ) {
        this.edadAnios = edadAnios;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(
            BigDecimal peso
    ) {
        this.peso = peso;
    }

    public SexoMascota getSexo() {
        return sexo;
    }

    public void setSexo(
            SexoMascota sexo
    ) {
        this.sexo = sexo;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(
            String notas
    ) {
        this.notas = notas;
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