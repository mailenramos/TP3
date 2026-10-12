package com.example.tp3.dto;

public record EstudianteRequest (
        Long dni,
        String nombre,
        String apellido,
        Integer edad,
        String genero,
        String ciudad,
        Integer libretaUniversitaria
) {
}
