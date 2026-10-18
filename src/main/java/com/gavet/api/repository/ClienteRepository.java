package com.gavet.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gavet.api.entity.Cliente;

public interface ClienteRepository
        extends JpaRepository<Cliente, Integer> {

    /*
     * Busca solamente clientes activos.
     */
    Optional<Cliente>
            findByEmailIgnoreCaseAndActivoTrue(
                String email
            );

    /*
     * Busca el cliente sin importar si se
     * encuentra activo o desactivado.
     *
     * Este método permite recuperar cuentas.
     */
    Optional<Cliente>
            findByEmailIgnoreCase(
                String email
            );

    /*
     * Comprueba si un correo ya existe.
     *
     * Una cuenta desactivada también ocupa
     * su correo y debe recuperarse, no duplicarse.
     */
    boolean existsByEmailIgnoreCase(
            String email
    );

    /*
     * Comprueba si un teléfono ya existe.
     */
    boolean existsByTelefono(
            String telefono
    );

    /*
     * Comprueba si otro cliente utiliza el correo.
     */
    boolean
            existsByEmailIgnoreCaseAndIdClienteNot(
                String email,
                Integer idCliente
            );

    /*
     * Comprueba si otro cliente utiliza el teléfono.
     */
    boolean
            existsByTelefonoAndIdClienteNot(
                String telefono,
                Integer idCliente
            );
}