package com.gavet.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gavet.api.dto.AuthResponse;
import com.gavet.api.dto.LoginClienteRequest;
import com.gavet.api.dto.RegistroClienteRequest;
import com.gavet.api.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(
            AuthService authService
    ) {

        this.authService =
                authService;
    }

    /*
     * POST /api/auth/registro
     */
    @PostMapping("/registro")
    public AuthResponse registrar(

        @RequestBody
        RegistroClienteRequest request
    ) {

        return authService
            .registrar(
                request
            );
    }

    /*
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public AuthResponse iniciarSesion(

        @RequestBody
        LoginClienteRequest request
    ) {

        return authService
            .iniciarSesion(
                request
            );
    }

    /*
     * PUT /api/auth/reactivar-cuenta
     *
     * Reactiva una cuenta eliminada lógicamente.
     */
    @PutMapping("/reactivar-cuenta")
    public AuthResponse reactivarCuenta(

        @RequestBody
        LoginClienteRequest request
    ) {

        return authService
            .reactivarCuenta(
                request
            );
    }
}