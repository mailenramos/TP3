package com.example.tp3.service;

import com.example.tp3.dto.CarreraCantidadDTO;
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

    public Carrera crear(Carrera carrera) {
        return carreraRepository.save(carrera);
    }

    public List<Carrera> obtenerTodas() {
        return carreraRepository.findAllByOrderByNombreAsc();
    }

    public List<CarreraCantidadDTO> obtenerPorCantidadDeInscriptos() {
        return carreraRepository.obtenerCarrerasPorCantidadDeInscriptos();
    }
}
