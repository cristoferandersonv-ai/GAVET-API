package com.gavet.api.dto;

import java.math.BigDecimal;

public class MascotaRequest {

    private String nombre;
    private String especie;
    private String raza;
    private Integer edad;
    private String sexo;
    private BigDecimal peso;

    public MascotaRequest() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(
            String nombre
    ) {
        this.nombre = nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(
            String especie
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

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(
            Integer edad
    ) {
        this.edad = edad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(
            String sexo
    ) {
        this.sexo = sexo;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(
            BigDecimal peso
    ) {
        this.peso = peso;
    }
}