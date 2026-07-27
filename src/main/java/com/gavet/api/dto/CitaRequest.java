package com.gavet.api.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CitaRequest {

    @JsonProperty("id_mascota")
    private Integer idMascota;

    @JsonProperty("id_servicio_principal")
    private Integer idServicioPrincipal;

    @JsonProperty("servicios_adicionales")
    private List<Integer> serviciosAdicionales;

    private LocalDate fecha;

    private LocalTime hora;

    private String observaciones;

    /*
     * Constructor vacío requerido para recibir el JSON.
     */
    public CitaRequest() {
    }

    public Integer getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(Integer idMascota) {
        this.idMascota = idMascota;
    }

    public Integer getIdServicioPrincipal() {
        return idServicioPrincipal;
    }

    public void setIdServicioPrincipal(
            Integer idServicioPrincipal
    ) {
        this.idServicioPrincipal = idServicioPrincipal;
    }

    public List<Integer> getServiciosAdicionales() {
        return serviciosAdicionales;
    }

    public void setServiciosAdicionales(
            List<Integer> serviciosAdicionales
    ) {
        this.serviciosAdicionales = serviciosAdicionales;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(
            String observaciones
    ) {
        this.observaciones = observaciones;
    }
}