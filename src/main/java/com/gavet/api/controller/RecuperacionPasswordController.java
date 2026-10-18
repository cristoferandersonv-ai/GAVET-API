package com.gavet.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gavet.api.dto.MensajeResponse;
import com.gavet.api.dto.RecuperarPasswordRequest;
import com.gavet.api.dto.VerificarCorreoRequest;
import com.gavet.api.service.RecuperacionPasswordService;

@RestController
@RequestMapping("/api/auth")
public class RecuperacionPasswordController {

    private final RecuperacionPasswordService
            recuperacionPasswordService;

    public RecuperacionPasswordController(
            RecuperacionPasswordService
                    recuperacionPasswordService
    ) {
        this.recuperacionPasswordService =
                recuperacionPasswordService;
    }

    /*
     * POST /api/auth/verificar-correo
     */
    @PostMapping("/verificar-correo")
    public MensajeResponse verificarCorreo(

        @RequestBody
        VerificarCorreoRequest request
    ) {

        return recuperacionPasswordService
            .verificarCorreo(
                request
            );
    }

    /*
     * PUT /api/auth/recuperar-password
     */
    @PutMapping("/recuperar-password")
    public MensajeResponse recuperarPassword(

        @RequestBody
        RecuperarPasswordRequest request
    ) {

        return recuperacionPasswordService
            .recuperarPassword(
                request
            );
    }
}