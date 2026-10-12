package com.example.tp3.repository;

import com.example.tp3.dto.CarreraCantidadDTO;
import com.example.tp3.model.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarreraRepository extends JpaRepository<Carrera, Long> {

    List<Carrera> findAllByOrderByNombreAsc();

    @Query("""
            SELECT new com.example.tp3.dto.CarreraCantidadDTO(ec.carrera.id, ec.carrera.nombre, COUNT(ec))
            FROM EstudianteCarrera ec
            GROUP BY ec.carrera.id, ec.carrera.nombre
            ORDER BY COUNT(ec) DESC, ec.carrera.nombre ASC
            """)
    List<CarreraCantidadDTO> obtenerCarrerasPorCantidadDeInscriptos();
}
