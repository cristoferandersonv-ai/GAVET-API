package com.gavet.api.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gavet.api.entity.Empleado;



public interface EmpleadoRepository
        extends JpaRepository<Empleado,Integer>{


    Optional<Empleado> findByEmailIgnoreCase(String email);


    boolean existsByEmailIgnoreCase(String email);


}