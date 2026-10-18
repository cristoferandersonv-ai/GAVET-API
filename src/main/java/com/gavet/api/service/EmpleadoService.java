package com.gavet.api.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.gavet.api.entity.Empleado;
import com.gavet.api.repository.EmpleadoRepository;



@Service
public class EmpleadoService {


    private final EmpleadoRepository empleadoRepository;


    public EmpleadoService(
            EmpleadoRepository empleadoRepository){

        this.empleadoRepository=empleadoRepository;
    }



    public List<Empleado> listar(){

        return empleadoRepository.findAll();

    }


}