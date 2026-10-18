
package com.gavet.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.gavet.api.dto.CambiarEstadoRequest;
import com.gavet.api.dto.CitaPanel;
import com.gavet.api.dto.ResumenEstilista;
import com.gavet.api.entity.Empleado;
import com.gavet.api.enums.RolEmpleado;
import com.gavet.api.repository.EmpleadoRepository;
import com.gavet.api.service.PersonalGestionService;

@RestController
@RequestMapping("/api/personal/estilista")
public class PersonalEstilistaController {

    private final PersonalGestionService gestion;
    private final EmpleadoRepository empleadoRepository;

    public PersonalEstilistaController(
            PersonalGestionService gestion,
            EmpleadoRepository empleadoRepository
    ) {
        this.gestion = gestion;
        this.empleadoRepository = empleadoRepository;
    }

    private void validarEstilista(Integer idEmpleado) {

        Empleado empleado = empleadoRepository
                .findById(idEmpleado)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Estilista no encontrado"
                        )
                );

        if (
                empleado.getRol() != RolEmpleado.ESTILISTA
                        || !Boolean.TRUE.equals(empleado.getActivo())
        ) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "El empleado no es un estilista activo"
            );
        }
    }

    // ESTADISTICAS DEL ESTILISTA
    @GetMapping("/{idEmpleado}/resumen")
    public ResumenEstilista resumen(
            @PathVariable Integer idEmpleado
    ) {

        validarEstilista(idEmpleado);

        return gestion.resumenEstilista(idEmpleado);
    }

    // CITAS ASIGNADAS
    @GetMapping("/{idEmpleado}/citas")
    public List<CitaPanel> listarCitas(
            @PathVariable Integer idEmpleado,

            @RequestParam(
                    defaultValue = "TODAS"
            )
            String modo
    ) {

        validarEstilista(idEmpleado);

        return gestion.citasEstilista(
                idEmpleado,
                modo
        );
    }

    // ACTUALIZAR ESTADO DE LA ATENCION
    @PatchMapping("/{idEmpleado}/citas/{idCita}/estado")
    public CitaPanel cambiarEstado(
            @PathVariable Integer idEmpleado,
            @PathVariable Integer idCita,
            @RequestBody CambiarEstadoRequest request
    ) {

        validarEstilista(idEmpleado);

        return gestion.cambiarEstadoEstilista(
                idEmpleado,
                idCita,
                request
        );
    }
}
