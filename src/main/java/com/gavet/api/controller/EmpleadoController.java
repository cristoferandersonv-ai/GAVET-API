
package com.gavet.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gavet.api.dto.EmpleadoVista;
import com.gavet.api.entity.Empleado;
import com.gavet.api.repository.EmpleadoRepository;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoController(
            EmpleadoRepository empleadoRepository
    ) {
        this.empleadoRepository = empleadoRepository;
    }

    @GetMapping
    public List<EmpleadoVista> listar() {

        return empleadoRepository.findAll()
                .stream()
                .map(this::convertir)
                .toList();
    }

    private EmpleadoVista convertir(Empleado empleado) {

        return new EmpleadoVista(
                empleado.getIdEmpleado(),
                empleado.getNombreCompleto(),
                empleado.getEmail(),
                empleado.getTelefono(),
                empleado.getRol().name(),
                Boolean.TRUE.equals(empleado.getActivo())
        );
    }
}
