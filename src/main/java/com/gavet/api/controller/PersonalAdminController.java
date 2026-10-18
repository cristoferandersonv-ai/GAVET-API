
package com.gavet.api.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import com.gavet.api.dto.*;
import com.gavet.api.entity.Servicio;
import com.gavet.api.service.PersonalGestionService;

@RestController
@RequestMapping("/api/personal/admin")
public class PersonalAdminController {

    private final PersonalGestionService gestion;

    public PersonalAdminController(
            PersonalGestionService gestion
    ) {
        this.gestion = gestion;
    }

    // DASHBOARD
    @GetMapping("/resumen")
    public ResumenAdmin resumen() {
        return gestion.resumenAdmin();
    }

    // LISTAR ESTILISTAS
    @GetMapping("/estilistas")
    public List<EmpleadoVista> listarEstilistas() {
        return gestion.listarEstilistas();
    }

    // REGISTRAR ESTILISTA
    @PostMapping("/estilistas")
    public EmpleadoVista crearEstilista(
            @RequestBody EmpleadoGuardarRequest request
    ) {
        return gestion.crearEstilista(request);
    }

    // EDITAR ESTILISTA
    @PutMapping("/estilistas/{id}")
    public EmpleadoVista editarEstilista(
            @PathVariable Integer id,
            @RequestBody EmpleadoGuardarRequest request
    ) {
        return gestion.editarEstilista(id, request);
    }

    // ACTIVAR O DESACTIVAR ESTILISTA
    @PatchMapping("/estilistas/{id}/activo")
    public EmpleadoVista cambiarActivoEstilista(
            @PathVariable Integer id,
            @RequestBody ActivoRequest request
    ) {
        return gestion.cambiarActivoEstilista(id, request);
    }

    // LISTAR TODAS LAS CITAS
    @GetMapping("/citas")
    public List<CitaPanel> listarCitas() {
        return gestion.listarTodasCitas();
    }

    // ASIGNAR ESTILISTA A UNA CITA
    @PatchMapping("/citas/{id}/asignar")
    public CitaPanel asignarEstilista(
            @PathVariable Integer id,
            @RequestBody AsignarCitaRequest request
    ) {
        return gestion.asignarCita(id, request);
    }

    // CAMBIAR ESTADO DE CITA
    @PatchMapping("/citas/{id}/estado")
    public CitaPanel cambiarEstadoCita(
            @PathVariable Integer id,
            @RequestBody CambiarEstadoRequest request
    ) {
        return gestion.cambiarEstadoAdmin(id, request);
    }

    // LISTAR SERVICIOS
    @GetMapping("/servicios")
    public List<Servicio> listarServicios() {
        return gestion.listarServicios();
    }

    // REGISTRAR SERVICIO
    @PostMapping("/servicios")
    public Servicio crearServicio(
            @RequestBody ServicioGuardarRequest request
    ) {
        return gestion.guardarServicio(null, request);
    }

    // EDITAR SERVICIO
    @PutMapping("/servicios/{id}")
    public Servicio editarServicio(
            @PathVariable Integer id,
            @RequestBody ServicioGuardarRequest request
    ) {
        return gestion.guardarServicio(id, request);
    }

    // ACTIVAR O DESACTIVAR SERVICIO
    @PatchMapping("/servicios/{id}/activo")
    public Servicio cambiarActivoServicio(
            @PathVariable Integer id,
            @RequestBody ActivoRequest request
    ) {
        return gestion.cambiarActivoServicio(id, request);
    }

    // CONSULTAR CLIENTES Y MASCOTAS
    @GetMapping("/clientes")
    public List<ClientePanel> listarClientes() {
        return gestion.listarClientes();
    }

    // REPORTES POR FECHAS
    @GetMapping("/reportes")
    public ReporteAtenciones reporte(
            @RequestParam
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate desde,

            @RequestParam
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate hasta
    ) {
        return gestion.reporte(desde, hasta);
    }
}
