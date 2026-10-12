package com.example.tp3.service;

import com.example.tp3.dto.MatriculacionRequest;
import com.example.tp3.dto.ReporteCarreraDTO;
import com.example.tp3.model.Carrera;
import com.example.tp3.model.Estudiante;
import com.example.tp3.model.estudianteCarrera;
import com.example.tp3.repository.CarreraRepository;
import com.example.tp3.repository.EstudianteRepository;
import com.example.tp3.repository.estudianteCarreraRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class estudianteCarreraService {

    private final estudianteCarreraRepository estudianteCarreraRepository;
    private final EstudianteRepository estudianteRepository;
    private final CarreraRepository carreraRepository;

    public estudianteCarreraService(
            estudianteCarreraRepository estudianteCarreraRepository,
            EstudianteRepository estudianteRepository,
            CarreraRepository carreraRepository
    ) {
        this.estudianteCarreraRepository = estudianteCarreraRepository;
        this.estudianteRepository = estudianteRepository;
        this.carreraRepository = carreraRepository;
    }

    public estudianteCarrera matricular(MatriculacionRequest request) {
        Estudiante estudiante = estudianteRepository
                .findByLibretaUniversitaria(request.libretaUniversitaria())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe un estudiante con libreta universitaria " + request.libretaUniversitaria()
                ));

        Carrera carrera = carreraRepository.findById(request.carreraId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe la carrera con id " + request.carreraId()
                ));

        estudianteCarrera matricula = new estudianteCarrera();
        matricula.setEstudiante(estudiante);
        matricula.setCarrera(carrera);
        matricula.setAnioInscripcion(request.anioInscripcion());
        matricula.setAntiguedad(request.antiguedad());
        matricula.setAnioGraduacion(request.anioGraduacion());

        return estudianteCarreraRepository.save(matricula);
    }

    public List<Estudiante> obtenerEstudiantesPorCarreraYCiudad(Long carreraId, String ciudad) {
        if (!carreraRepository.existsById(carreraId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la carrera con id " + carreraId);
        }
        return estudianteCarreraRepository.buscarEstudiantesPorCarreraYCiudad(carreraId, ciudad);
    }

    public List<ReporteCarreraDTO> generarReporte() {
        return carreraRepository.findAllByOrderByNombreAsc().stream()
                .map(carrera -> new ReporteCarreraDTO(
                        carrera.getNombre(),
                        estudianteCarreraRepository.contarInscriptosPorAnio(carrera.getId()),
                        estudianteCarreraRepository.contarEgresadosPorAnio(carrera.getId())
                ))
                .toList();
    }
}
