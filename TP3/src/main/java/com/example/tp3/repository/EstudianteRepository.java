package com.example.tp3.repository;

import com.example.tp3.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    // Obtiene todos los estudiantes ordenados por apellido y nombre.
    List<Estudiante> findAllByOrderByApellidoAscNombreAsc();

    // Busca un estudiante por su número de libreta universitaria.
    Optional<Estudiante> findByLibretaUniversitaria(Integer libretaUniversitaria);

    // Busca estudiantes por género, sin distinguir mayúsculas y minúsculas.
    List<Estudiante> findByGeneroIgnoreCase(String genero);
}
