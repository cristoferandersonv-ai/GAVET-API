package com.gavet.api.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.gavet.api.enums.EstadoCita;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    private Integer idCita;

    @Column(
        name = "id_mascota",
        nullable = false
    )
    private Integer idMascota;

    @Column(
        name = "id_servicio_principal",
        nullable = false
    )
    private Integer idServicioPrincipal;

    @Column(name = "id_empleado")
    private Integer idEmpleado;

    @Column(
        name = "fecha",
        nullable = false
    )
    private LocalDate fecha;

    @Column(
        name = "hora",
        nullable = false
    )
    private LocalTime hora;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "estado",
        nullable = false
    )
    private EstadoCita estado;

    @Column(
        name = "observaciones",
        length = 300
    )
    private String observaciones;

    @Column(
        name = "precio_servicio_principal",
        nullable = false,
        precision = 10,
        scale = 2
    )
    private BigDecimal precioServicioPrincipal;

    @Column(
        name = "precio_total",
        nullable = false,
        precision = 10,
        scale = 2
    )
    private BigDecimal precioTotal;

    @Column(
        name = "duracion_total_minutos",
        nullable = false
    )
    private Integer duracionTotalMinutos;

    @Column(
        name = "fecha_creacion",
        insertable = false,
        updatable = false
    )
    private LocalDateTime fechaCreacion;

    @Column(
        name = "fecha_modificacion",
        insertable = false,
        updatable = false
    )
    private LocalDateTime fechaModificacion;

    /*
     * Constructor vacío obligatorio para JPA.
     */
    public Cita() {
    }

    public Integer getIdCita() {
        return idCita;
    }

    public void setIdCita(Integer idCita) {
        this.idCita = idCita;
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
        this.idServicioPrincipal =
                idServicioPrincipal;
    }

    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
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

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(
            String observaciones
    ) {
        this.observaciones = observaciones;
    }

    public BigDecimal getPrecioServicioPrincipal() {
        return precioServicioPrincipal;
    }

    public void setPrecioServicioPrincipal(
            BigDecimal precioServicioPrincipal
    ) {
        this.precioServicioPrincipal =
                precioServicioPrincipal;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(
            BigDecimal precioTotal
    ) {
        this.precioTotal = precioTotal;
    }

    public Integer getDuracionTotalMinutos() {
        return duracionTotalMinutos;
    }

    public void setDuracionTotalMinutos(
            Integer duracionTotalMinutos
    ) {
        this.duracionTotalMinutos =
                duracionTotalMinutos;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(
            LocalDateTime fechaCreacion
    ) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(
            LocalDateTime fechaModificacion
    ) {
        this.fechaModificacion =
                fechaModificacion;
    }
}