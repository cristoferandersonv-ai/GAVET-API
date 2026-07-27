package com.gavet.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gavet.api.dto.ActualizarClienteRequest;
import com.gavet.api.dto.CambiarPasswordRequest;
import com.gavet.api.dto.ClienteResponse;
import com.gavet.api.dto.MensajeResponse;
import com.gavet.api.service.ClienteService;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(
            ClienteService clienteService
    ) {

        this.clienteService =
                clienteService;
    }

    /*
     * GET /api/clientes/1
     */
    @GetMapping("/{idCliente}")
    public ClienteResponse obtenerCliente(

        @PathVariable("idCliente")
        Integer idCliente
    ) {

        return clienteService
            .obtenerCliente(
                idCliente
            );
    }

    /*
     * PUT /api/clientes/1
     */
    @PutMapping("/{idCliente}")
    public ClienteResponse actualizarCliente(

        @PathVariable("idCliente")
        Integer idCliente,

        @RequestBody
        ActualizarClienteRequest request
    ) {

        return clienteService
            .actualizarCliente(
                idCliente,
                request
            );
    }

    /*
     * PUT /api/clientes/1/password
     */
    @PutMapping("/{idCliente}/password")
    public MensajeResponse cambiarPassword(

        @PathVariable("idCliente")
        Integer idCliente,

        @RequestBody
        CambiarPasswordRequest request
    ) {

        return clienteService
            .cambiarPassword(
                idCliente,
                request
            );
    }

    /*
     * PUT /api/clientes/1/eliminar
     *
     * Realiza una eliminación lógica:
     * activo = false.
     */
    @PutMapping("/{idCliente}/eliminar")
    public MensajeResponse eliminarCuenta(

        @PathVariable("idCliente")
        Integer idCliente
    ) {

        return clienteService
            .eliminarCuenta(
                idCliente
            );
    }
}