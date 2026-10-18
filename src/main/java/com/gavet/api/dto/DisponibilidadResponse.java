package com.gavet.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DisponibilidadResponse {

    private boolean disponible;

    private String mensaje;

    @JsonProperty("citas_en_conflicto")
    private long citasEnConflicto;

    public DisponibilidadResponse(
            boolean disponible,
            String mensaje,
            long citasEnConflicto
    ) {
        this.disponible = disponible;
        this.mensaje = mensaje;
        this.citasEnConflicto = citasEnConflicto;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public String getMensaje() {
        return mensaje;
    }

    public long getCitasEnConflicto() {
        return citasEnConflicto;
    }
}