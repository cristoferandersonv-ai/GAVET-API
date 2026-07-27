package com.gavet.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gavet.api.dto.MascotaRequest;
import com.gavet.api.dto.MascotaResponse;
import com.gavet.api.service.MascotaService;

@RestController
@RequestMapping(
    "/api/clientes/{idCliente}/mascotas"
)
public class MascotaController {

    private final MascotaService mascotaService;

    public MascotaController(
            MascotaService mascotaService
    ) {
        this.mascotaService =
                mascotaService;
    }

    /*
     * GET /api/clientes/1/mascotas
     */
    @GetMapping
    public List<MascotaResponse>
            listarMascotas(

        @PathVariable("idCliente")
        Integer idCliente
    ) {

        return mascotaService
            .listarPorCliente(
                idCliente
            );
    }

    /*
     * GET /api/clientes/1/mascotas/2
     */
    @GetMapping("/{idMascota}")
    public MascotaResponse obtenerMascota(

        @PathVariable("idCliente")
        Integer idCliente,

        @PathVariable("idMascota")
        Integer idMascota
    ) {

        return mascotaService.obtenerPorId(
            idCliente,
            idMascota
        );
    }

    /*
     * POST /api/clientes/1/mascotas
     */
    @PostMapping
    public MascotaResponse registrarMascota(

        @PathVariable("idCliente")
        Integer idCliente,

        @RequestBody
        MascotaRequest request
    ) {

        return mascotaService.registrar(
            idCliente,
            request
        );
    }

    /*
     * PUT /api/clientes/1/mascotas/2
     */
    @PutMapping("/{idMascota}")
    public MascotaResponse actualizarMascota(

        @PathVariable("idCliente")
        Integer idCliente,

        @PathVariable("idMascota")
        Integer idMascota,

        @RequestBody
        MascotaRequest request
    ) {

        return mascotaService.actualizar(
            idCliente,
            idMascota,
            request
        );
    }
}