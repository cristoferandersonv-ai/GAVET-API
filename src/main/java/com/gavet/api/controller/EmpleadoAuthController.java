
package com.gavet.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gavet.api.dto.EmpleadoAuthResponse;
import com.gavet.api.dto.LoginEmpleadoRequest;
import com.gavet.api.service.EmpleadoAuthService;

@RestController
@RequestMapping("/api/auth/empleados")
public class EmpleadoAuthController {

    private final EmpleadoAuthService empleadoAuthService;

    public EmpleadoAuthController(
            EmpleadoAuthService empleadoAuthService) {

        this.empleadoAuthService = empleadoAuthService;
    }

    @PostMapping("/login")
    public EmpleadoAuthResponse iniciarSesion(
            @RequestBody LoginEmpleadoRequest request) {

        return empleadoAuthService.iniciarSesion(request);
    }
}
