package com.example.tp3.dto;

public record MatriculacionRequest(
        Integer libretaUniversitaria,
        Long carreraId,
        Integer anioInscripcion,
        Integer antiguedad,
        Integer anioGraduacion
) {
}
