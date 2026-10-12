package com.example.tp3.repository;

import com.example.tp3.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    List<Estudiante> findAllByOrderByApellidoAscNombreAsc();

    Optional<Estudiante> findByLibretaUniversitaria(Integer libretaUniversitaria);

    List<Estudiante> findByGeneroIgnoreCase(String genero);
}
