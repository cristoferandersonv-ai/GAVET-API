package com.gavet.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gavet.api.entity.Mascota;

public interface MascotaRepository
        extends JpaRepository<Mascota, Integer> {

    /*
     * Obtiene las mascotas activas de un cliente.
     */
    List<Mascota>
            findByIdClienteAndActivoTrueOrderByIdMascotaAsc(
                Integer idCliente
            );

    /*
     * Busca una mascota específica que pertenezca
     * al cliente indicado.
     */
    Optional<Mascota>
            findByIdMascotaAndIdClienteAndActivoTrue(
                Integer idMascota,
                Integer idCliente
            );
}