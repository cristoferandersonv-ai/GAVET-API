package com.gavet.api.controller;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gavet.api.dto.CitaRequest;
import com.gavet.api.dto.CitaResponse;
import com.gavet.api.dto.DisponibilidadResponse;
import com.gavet.api.service.CitaService;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(
            CitaService citaService
    ) {
        this.citaService = citaService;
    }

    /*
     * GET /api/citas/disponibilidad
     */
    @GetMapping("/disponibilidad")
    public DisponibilidadResponse
            verificarDisponibilidad(

        @RequestParam
        @DateTimeFormat(
            iso = DateTimeFormat.ISO.DATE
        )
        LocalDate fecha,

        @RequestParam
        @DateTimeFormat(
            iso = DateTimeFormat.ISO.TIME
        )
        LocalTime hora,

        @RequestParam(
            name = "duracion_minutos"
        )
        int duracionMinutos
    ) {

        return citaService.verificarDisponibilidad(
            fecha,
            hora,
            duracionMinutos
        );
    }

    /*
     * POST /api/citas
     */
    @PostMapping
    public CitaResponse registrarCita(

        @RequestBody
        CitaRequest request
    ) {

        return citaService.registrarCita(
            request
        );
    }
}