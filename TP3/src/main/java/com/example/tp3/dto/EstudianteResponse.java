package com.example.tp3.dto;

import com.example.tp3.model.Estudiante;

public record EstudianteResponse (
        Long dni,
        String nombre,
        String apellido,
        Integer edad,
        String genero,
        String ciudad,
        Integer libretaUniversitaria
) {
    public static EstudianteResponse from(Estudiante e) {
        return new EstudianteResponse(
                e.getDni(),
                e.getNombre(),
                e.getApellido(),
                e.getEdad(),
                e.getGenero(),
                e.getCiudad(),
                e.getLibretaUniversitaria()
        );
    }
}
