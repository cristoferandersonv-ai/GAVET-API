package com.gavet.api.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CitaResponse {

    private boolean exito;

    private String mensaje;

    @JsonProperty("id_cita")
    private Integer idCita;

    @JsonProperty("precio_total")
    private BigDecimal precioTotal;

    @JsonProperty("duracion_total_minutos")
    private Integer duracionTotalMinutos;

    public CitaResponse(
            boolean exito,
            String mensaje,
            Integer idCita,
            BigDecimal precioTotal,
            Integer duracionTotalMinutos
    ) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.idCita = idCita;
        this.precioTotal = precioTotal;
        this.duracionTotalMinutos =
                duracionTotalMinutos;
    }

    public boolean isExito() {
        return exito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Integer getIdCita() {
        return idCita;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public Integer getDuracionTotalMinutos() {
        return duracionTotalMinutos;
    }
}