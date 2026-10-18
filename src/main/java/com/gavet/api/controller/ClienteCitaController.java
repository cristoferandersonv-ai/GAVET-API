package com.gavet.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gavet.api.dto.CitaDetalleResponse;
import com.gavet.api.service.CitaService;

@RestController
@RequestMapping(
    "/api/clientes/{idCliente}/citas"
)
public class ClienteCitaController {

    private final CitaService citaService;

    public ClienteCitaController(
            CitaService citaService
    ) {
        this.citaService =
                citaService;
    }

    /*
     * GET /api/clientes/1/citas
     */
    @GetMapping
    public List<CitaDetalleResponse>
            listarCitas(

        @PathVariable("idCliente")
        Integer idCliente
    ) {

        return citaService
            .listarPorCliente(
                idCliente
            );
    }

    /*
     * PUT /api/clientes/1/citas/5/cancelar
     */
    @PutMapping("/{idCita}/cancelar")
    public CitaDetalleResponse cancelarCita(

        @PathVariable("idCliente")
        Integer idCliente,

        @PathVariable("idCita")
        Integer idCita
    ) {

        return citaService.cancelarCita(
            idCliente,
            idCita
        );
    }
}