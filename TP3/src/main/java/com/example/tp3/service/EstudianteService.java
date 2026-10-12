package com.example.tp3.service;

import com.example.tp3.model.Estudiante;
import com.example.tp3.repository.EstudianteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EstudianteService {

    private final EstudianteRepository estudianteRepository;

    public EstudianteService(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    public Estudiante crear(Estudiante estudiante) {
        return estudianteRepository.save(estudiante);
    }

    public List<Estudiante> obtenerTodosOrdenados() {
        return estudianteRepository.findAllByOrderByApellidoAscNombreAsc();
    }

    public Estudiante obtenerPorLibreta(Integer libretaUniversitaria) {
        return estudianteRepository.findByLibretaUniversitaria(libretaUniversitaria)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe un estudiante con libreta universitaria " + libretaUniversitaria
                ));
    }

    public List<Estudiante> obtenerPorGenero(String genero) {
        return estudianteRepository.findByGeneroIgnoreCase(genero);
    }
}
