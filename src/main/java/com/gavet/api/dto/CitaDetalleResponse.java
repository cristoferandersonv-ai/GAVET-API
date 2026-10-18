package com.gavet.api.dto;

import java.math.BigDecimal;

public class CitaDetalleResponse {

    private Integer id;
    private Integer mascotaId;
    private String nombreMascota;
    private String tipoBano;

    private boolean cortePelo;
    private boolean corteUnas;
    private boolean limpiezaOidos;
    private boolean cepilladoEspecial;
    private boolean corteHigienico;

    private String fecha;
    private String hora;
    private String observaciones;
    private BigDecimal precioTotal;
    private String estado;

    public CitaDetalleResponse(
            Integer id,
            Integer mascotaId,
            String nombreMascota,
            String tipoBano,
            boolean cortePelo,
            boolean corteUnas,
            boolean limpiezaOidos,
            boolean cepilladoEspecial,
            boolean corteHigienico,
            String fecha,
            String hora,
            String observaciones,
            BigDecimal precioTotal,
            String estado
    ) {
        this.id = id;
        this.mascotaId = mascotaId;
        this.nombreMascota = nombreMascota;
        this.tipoBano = tipoBano;
        this.cortePelo = cortePelo;
        this.corteUnas = corteUnas;
        this.limpiezaOidos = limpiezaOidos;
        this.cepilladoEspecial = cepilladoEspecial;
        this.corteHigienico = corteHigienico;
        this.fecha = fecha;
        this.hora = hora;
        this.observaciones = observaciones;
        this.precioTotal = precioTotal;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public Integer getMascotaId() {
        return mascotaId;
    }

    public String getNombreMascota() {
        return nombreMascota;
    }

    public String getTipoBano() {
        return tipoBano;
    }

    public boolean isCortePelo() {
        return cortePelo;
    }

    public boolean isCorteUnas() {
        return corteUnas;
    }

    public boolean isLimpiezaOidos() {
        return limpiezaOidos;
    }

    public boolean isCepilladoEspecial() {
        return cepilladoEspecial;
    }

    public boolean isCorteHigienico() {
        return corteHigienico;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public String getEstado() {
        return estado;
    }
}