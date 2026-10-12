package com.example.tp3.repository;

import com.example.tp3.dto.AnioCantidadDTO;
import com.example.tp3.model.Estudiante;
import com.example.tp3.model.estudianteCarrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface estudianteCarreraRepository extends JpaRepository<estudianteCarrera, Long> {

    @Query("""
            SELECT ec.estudiante
            FROM EstudianteCarrera ec
            WHERE ec.carrera.id = :carreraId
              AND LOWER(ec.estudiante.ciudad) = LOWER(:ciudad)
            ORDER BY ec.estudiante.apellido ASC, ec.estudiante.nombre ASC
            """)
    List<Estudiante> buscarEstudiantesPorCarreraYCiudad(
            @Param("carreraId") Long carreraId,
            @Param("ciudad") String ciudad
    );

    @Query("""
            SELECT new com.example.tp3.dto.AnioCantidadDTO(ec.anioInscripcion, COUNT(ec))
            FROM EstudianteCarrera ec
            WHERE ec.carrera.id = :carreraId
            GROUP BY ec.anioInscripcion
            ORDER BY ec.anioInscripcion ASC
            """)
    List<AnioCantidadDTO> contarInscriptosPorAnio(@Param("carreraId") Long carreraId);

    @Query("""
            SELECT new com.example.tp3.dto.AnioCantidadDTO(ec.anioGraduacion, COUNT(ec))
            FROM EstudianteCarrera ec
            WHERE ec.carrera.id = :carreraId
              AND ec.anioGraduacion IS NOT NULL
            GROUP BY ec.anioGraduacion
            ORDER BY ec.anioGraduacion ASC
            """)
    List<AnioCantidadDTO> contarEgresadosPorAnio(@Param("carreraId") Long carreraId);
}
