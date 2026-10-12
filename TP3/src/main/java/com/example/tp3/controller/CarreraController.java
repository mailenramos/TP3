package com.example.tp3.controller;

import com.example.tp3.dto.CarreraCantidadDTO;
import com.example.tp3.dto.CarreraRequest;
import com.example.tp3.dto.CarreraResponse;
import com.example.tp3.dto.EstudianteResponse;
import com.example.tp3.dto.ReporteCarreraDTO;
import com.example.tp3.service.CarreraService;
import com.example.tp3.service.estudianteCarreraService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/carreras")
public class CarreraController {

    private final CarreraService carreraService;
    private final estudianteCarreraService estudianteCarreraService;

    public CarreraController(CarreraService carreraService, estudianteCarreraService estudianteCarreraService) {
        this.carreraService = carreraService;
        this.estudianteCarreraService = estudianteCarreraService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarreraResponse crear(@RequestBody CarreraRequest request) {
        return carreraService.crear(request);
    }

    @GetMapping
    public List<CarreraResponse> obtenerTodas() {
        return carreraService.obtenerTodas();
    }

    @GetMapping("/inscriptos")
    public List<CarreraCantidadDTO> obtenerPorCantidadDeInscriptos() {
        return carreraService.obtenerPorCantidadDeInscriptos();
    }

    @GetMapping("/reporte")
    public List<ReporteCarreraDTO> generarReporte() {
        return estudianteCarreraService.generarReporte();
    }

    @GetMapping("/{carreraId}/estudiantes")
    public List<EstudianteResponse> obtenerEstudiantesPorCiudad(
            @PathVariable Long carreraId,
            @RequestParam String ciudad
    ) {
        return estudianteCarreraService.obtenerEstudiantesPorCarreraYCiudad(carreraId, ciudad);
    }
}