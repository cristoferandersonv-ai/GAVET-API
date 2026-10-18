package com.gavet.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prueba")
public class PruebaController {

    @GetMapping
    public Map<String, String> probarApi() {

        Map<String, String> respuesta = new HashMap<>();

        respuesta.put("estado", "correcto");
        respuesta.put(
            "mensaje",
            "La API de GAVET está funcionando"
        );

        return respuesta;
    }
}