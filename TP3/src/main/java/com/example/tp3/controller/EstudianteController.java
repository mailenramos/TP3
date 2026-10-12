package com.example.tp3.controller;

import com.example.tp3.dto.EstudianteRequest;
import com.example.tp3.dto.EstudianteResponse;
import com.example.tp3.service.EstudianteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/estudiantes")
public class EstudianteController {

    private final EstudianteService estudianteService;

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EstudianteResponse crear(@RequestBody EstudianteRequest request) {
        return estudianteService.crear(request);
    }

    @GetMapping
    public List<EstudianteResponse> obtenerTodos() {
        return estudianteService.obtenerTodosOrdenados();
    }

    @GetMapping("/libreta/{libretaUniversitaria}")
    public EstudianteResponse obtenerPorLibreta(@PathVariable Integer libretaUniversitaria) {
        return estudianteService.obtenerPorLibreta(libretaUniversitaria);
    }

    @GetMapping("/genero/{genero}")
    public List<EstudianteResponse> obtenerPorGenero(@PathVariable String genero) {
        return estudianteService.obtenerPorGenero(genero);
    }
}