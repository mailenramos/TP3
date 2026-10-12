package com.example.tp3.service;

import com.example.tp3.dto.EstudianteRequest;
import com.example.tp3.dto.EstudianteResponse;
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

    // Crea un estudiante a partir de los datos recibidos y lo guarda en la base de
    // datos.
    public EstudianteResponse crear(EstudianteRequest request) {
        Estudiante estudiante = new Estudiante();
        estudiante.setDni(request.dni());
        estudiante.setNombre(request.nombre());
        estudiante.setApellido(request.apellido());
        estudiante.setEdad(request.edad());
        estudiante.setGenero(request.genero());
        estudiante.setCiudad(request.ciudad());
        estudiante.setLibretaUniversitaria(request.libretaUniversitaria());

        return EstudianteResponse.from(estudianteRepository.save(estudiante));
    }

    // Obtiene todos los estudiantes ordenados por apellido y nombre.
    public List<EstudianteResponse> obtenerTodosOrdenados() {
        return estudianteRepository.findAllByOrderByApellidoAscNombreAsc()
                .stream()
                .map(EstudianteResponse::from)
                .toList();
    }

    // Busca un estudiante por su número de libreta universitaria.
    // Si no existe, devuelve un error 404.
    public EstudianteResponse obtenerPorLibreta(Integer libretaUniversitaria) {
        return estudianteRepository.findByLibretaUniversitaria(libretaUniversitaria)
                .map(EstudianteResponse::from)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe un estudiante con libreta universitaria " + libretaUniversitaria));
    }

    // Busca estudiantes según su género, sin distinguir mayúsculas y minúsculas.
    public List<EstudianteResponse> obtenerPorGenero(String genero) {
        return estudianteRepository.findByGeneroIgnoreCase(genero)
                .stream()
                .map(EstudianteResponse::from)
                .toList();
    }
}