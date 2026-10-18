package com.gavet.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gavet.api.entity.Servicio;
import com.gavet.api.enums.TipoServicio;
import com.gavet.api.repository.ServicioRepository;

@Service
public class ServicioService {

    private final ServicioRepository servicioRepository;

    /*
     * Spring entrega automáticamente el repositorio
     * mediante este constructor.
     */
    public ServicioService(
            ServicioRepository servicioRepository
    ) {
        this.servicioRepository = servicioRepository;
    }

    /*
     * Lista todos los servicios que están activos.
     */
    public List<Servicio> listarServiciosActivos() {

        return servicioRepository
                .findByActivoTrueOrderByIdServicioAsc();
    }

    /*
     * Lista solamente los baños.
     */
    public List<Servicio> listarBanos() {

        return servicioRepository
                .findByTipoAndActivoTrueOrderByIdServicioAsc(
                        TipoServicio.BANO
                );
    }

    /*
     * Lista solamente los servicios adicionales.
     */
    public List<Servicio> listarAdicionales() {

        return servicioRepository
                .findByTipoAndActivoTrueOrderByIdServicioAsc(
                        TipoServicio.ADICIONAL
                );
    }
}