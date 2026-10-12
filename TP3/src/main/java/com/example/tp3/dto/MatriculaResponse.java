package com.example.tp3.dto;

import com.example.tp3.model.estudianteCarrera;

public record MatriculaResponse(
        Long id,
        Integer libretaUniversitaria,
        String estudiante,
        Long carreraId,
        String carrera,
        Integer anioInscripcion,
        Integer anioGraduacion,
        Integer antiguedad
) {
    public static MatriculaResponse from(estudianteCarrera m) {
        return new MatriculaResponse(
                m.getId(),
                m.getEstudiante().getLibretaUniversitaria(),
                m.getEstudiante().getApellido() + ", " + m.getEstudiante().getNombre(),
                m.getCarrera().getId(),
                m.getCarrera().getNombre(),
                m.getAnioInscripcion(),
                m.getAnioGraduacion(),
                m.getAntiguedad()
        );
    }
}