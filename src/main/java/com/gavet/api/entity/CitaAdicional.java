package com.gavet.api.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cita_adicionales")
public class CitaAdicional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita_adicional")
    private Integer idCitaAdicional;

    @Column(
        name = "id_cita",
        nullable = false
    )
    private Integer idCita;

    @Column(
        name = "id_servicio",
        nullable = false
    )
    private Integer idServicio;

    @Column(
        name = "precio_aplicado",
        nullable = false,
        precision = 10,
        scale = 2
    )
    private BigDecimal precioAplicado;

    @Column(
        name = "duracion_aplicada_minutos",
        nullable = false
    )
    private Integer duracionAplicadaMinutos;

    /*
     * Constructor vacío requerido por JPA.
     */
    public CitaAdicional() {
    }

    public Integer getIdCitaAdicional() {
        return idCitaAdicional;
    }

    public void setIdCitaAdicional(
            Integer idCitaAdicional
    ) {
        this.idCitaAdicional = idCitaAdicional;
    }

    public Integer getIdCita() {
        return idCita;
    }

    public void setIdCita(Integer idCita) {
        this.idCita = idCita;
    }

    public Integer getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(
            Integer idServicio
    ) {
        this.idServicio = idServicio;
    }

    public BigDecimal getPrecioAplicado() {
        return precioAplicado;
    }

    public void setPrecioAplicado(
            BigDecimal precioAplicado
    ) {
        this.precioAplicado = precioAplicado;
    }

    public Integer getDuracionAplicadaMinutos() {
        return duracionAplicadaMinutos;
    }

    public void setDuracionAplicadaMinutos(
            Integer duracionAplicadaMinutos
    ) {
        this.duracionAplicadaMinutos =
                duracionAplicadaMinutos;
    }
}