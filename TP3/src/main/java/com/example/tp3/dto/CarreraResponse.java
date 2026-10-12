package com.example.tp3.dto;

import com.example.tp3.model.Carrera;

public record CarreraResponse(
        Long id,
        String nombre,
        Integer duracion
) {
    public static CarreraResponse from(Carrera c) {
        return new CarreraResponse(c.getId(), c.getNombre(), c.getDuracion());
    }
}