package com.example.tp3.service;

import com.example.tp3.dto.CarreraCantidadDTO;
import com.example.tp3.dto.CarreraRequest;
import com.example.tp3.dto.CarreraResponse;
import com.example.tp3.model.Carrera;
import com.example.tp3.repository.CarreraRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarreraService {

    private final CarreraRepository carreraRepository;

    public CarreraService(CarreraRepository carreraRepository) {
        this.carreraRepository = carreraRepository;
    }

    // Crea una carrera a partir de los datos recibidos y la guarda en la base de
    // datos.
    public CarreraResponse crear(CarreraRequest request) {
        Carrera carrera = new Carrera();
        carrera.setNombre(request.nombre());
        carrera.setDuracion(request.duracion());

        return CarreraResponse.from(carreraRepository.save(carrera));
    }

    // Obtiene todas las carreras ordenadas alfabéticamente por nombre.
    public List<CarreraResponse> obtenerTodas() {
        return carreraRepository.findAllByOrderByNombreAsc()
                .stream()
                .map(CarreraResponse::from)
                .toList();
    }

    // Obtiene las carreras ordenadas por cantidad de inscriptos, de mayor a menor.
    public List<CarreraCantidadDTO> obtenerPorCantidadDeInscriptos() {
        return carreraRepository.obtenerCarrerasPorCantidadDeInscriptos();
    }
}