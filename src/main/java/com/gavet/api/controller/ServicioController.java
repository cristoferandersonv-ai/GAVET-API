package com.gavet.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gavet.api.entity.Servicio;
import com.gavet.api.service.ServicioService;

@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

    private final ServicioService servicioService;

    
    public ServicioController(
            ServicioService servicioService
    ) {
        this.servicioService = servicioService;
    }

    
    @GetMapping
    public List<Servicio> listarServicios() {

        return servicioService.listarServiciosActivos();
    }

    
    @GetMapping("/banos")
    public List<Servicio> listarBanos() {

        return servicioService.listarBanos();
    }

   
    @GetMapping("/adicionales")
    public List<Servicio> listarAdicionales() {

        return servicioService.listarAdicionales();
    }
}