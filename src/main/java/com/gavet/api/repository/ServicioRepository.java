package com.gavet.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gavet.api.entity.Servicio;
import com.gavet.api.enums.TipoServicio;

public interface ServicioRepository
        extends JpaRepository<Servicio, Integer> {

    /*
     * Obtiene todos los servicios activos.
     */
    List<Servicio> findByActivoTrueOrderByIdServicioAsc();

    /*
     * Obtiene servicios activos según su tipo:
     * BANO o ADICIONAL.
     */
    List<Servicio> findByTipoAndActivoTrueOrderByIdServicioAsc(
            TipoServicio tipo
    );
}