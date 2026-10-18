package com.gavet.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gavet.api.entity.CitaAdicional;

public interface CitaAdicionalRepository
        extends JpaRepository<CitaAdicional, Integer> {

    List<CitaAdicional>
            findByIdCitaOrderByIdCitaAdicionalAsc(
                Integer idCita
            );
}