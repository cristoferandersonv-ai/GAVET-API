
package com.gavet.api.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gavet.api.dto.EmpleadoAuthResponse;
import com.gavet.api.dto.EmpleadoUsuarioResponse;
import com.gavet.api.dto.LoginEmpleadoRequest;
import com.gavet.api.entity.Empleado;
import com.gavet.api.repository.EmpleadoRepository;

@Service
public class EmpleadoAuthService {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoAuthService(
            EmpleadoRepository empleadoRepository) {

        this.empleadoRepository = empleadoRepository;
    }

    public EmpleadoAuthResponse iniciarSesion(
            LoginEmpleadoRequest request) {

        if (request == null ||
                request.getCorreo() == null ||
                request.getPassword() == null) {

            return new EmpleadoAuthResponse(
                    false,
                    "Ingrese correo y contraseña",
                    null
            );
        }

        String correo = request.getCorreo().trim();
        String password = request.getPassword();

        if (correo.isEmpty() || password.isEmpty()) {

            return new EmpleadoAuthResponse(
                    false,
                    "Ingrese correo y contraseña",
                    null
            );
        }

        Optional<Empleado> resultado =
                empleadoRepository.findByEmailIgnoreCase(correo);

        if (resultado.isEmpty()) {

            return new EmpleadoAuthResponse(
                    false,
                    "Credenciales incorrectas",
                    null
            );
        }

        Empleado empleado = resultado.get();

        if (!Boolean.TRUE.equals(empleado.getActivo())) {

            return new EmpleadoAuthResponse(
                    false,
                    "La cuenta se encuentra desactivada",
                    null
            );
        }

        if (empleado.getPassword() == null ||
                !empleado.getPassword().equals(password)) {

            return new EmpleadoAuthResponse(
                    false,
                    "Credenciales incorrectas",
                    null
            );
        }

        if (empleado.getRol() == null) {

            return new EmpleadoAuthResponse(
                    false,
                    "El empleado no tiene un rol asignado",
                    null
            );
        }

        EmpleadoUsuarioResponse usuario =
                new EmpleadoUsuarioResponse(
                        empleado.getIdEmpleado(),
                        empleado.getNombreCompleto(),
                        empleado.getEmail(),
                        empleado.getRol().name()
                );

        return new EmpleadoAuthResponse(
                true,
                "Bienvenido a GAVET",
                usuario
        );
    }
}
