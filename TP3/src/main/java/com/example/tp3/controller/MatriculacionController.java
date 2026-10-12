package com.example.tp3.controller;

import com.example.tp3.dto.MatriculaResponse;
import com.example.tp3.dto.MatriculacionRequest;
import com.example.tp3.service.estudianteCarreraService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/matriculas")
public class MatriculacionController {

    private final estudianteCarreraService estudianteCarreraService;

    public MatriculacionController(estudianteCarreraService estudianteCarreraService) {
        this.estudianteCarreraService = estudianteCarreraService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MatriculaResponse matricular(@RequestBody MatriculacionRequest request) {
        return estudianteCarreraService.matricular(request);
    }
}