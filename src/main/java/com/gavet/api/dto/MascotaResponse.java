package com.gavet.api.dto;

import java.math.BigDecimal;

public class MascotaResponse {

    private Integer id;
    private String nombre;
    private String especie;
    private String raza;
    private Integer edad;
    private String sexo;
    private BigDecimal peso;

    public MascotaResponse(
            Integer id,
            String nombre,
            String especie,
            String raza,
            Integer edad,
            String sexo,
            BigDecimal peso
    ) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.edad = edad;
        this.sexo = sexo;
        this.peso = peso;
    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public String getRaza() {
        return raza;
    }

    public Integer getEdad() {
        return edad;
    }

    public String getSexo() {
        return sexo;
    }

    public BigDecimal getPeso() {
        return peso;
    }
}