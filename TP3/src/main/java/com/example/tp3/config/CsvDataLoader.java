package com.example.tp3.config;

import com.example.tp3.model.Carrera;
import com.example.tp3.model.Estudiante;
import com.example.tp3.model.estudianteCarrera;
import com.example.tp3.repository.CarreraRepository;
import com.example.tp3.repository.EstudianteRepository;
import com.example.tp3.repository.estudianteCarreraRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Carga los datos de ejemplo (carreras, estudiantes y matriculaciones) desde los
 * CSV de src/main/resources/csv al iniciar la aplicación.
 */
@Component
public class CsvDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CsvDataLoader.class);

    private final CarreraRepository carreraRepository;
    private final EstudianteRepository estudianteRepository;
    private final estudianteCarreraRepository estudianteCarreraRepository;

    public CsvDataLoader(
            CarreraRepository carreraRepository,
            EstudianteRepository estudianteRepository,
            estudianteCarreraRepository estudianteCarreraRepository
    ) {
        this.carreraRepository = carreraRepository;
        this.estudianteRepository = estudianteRepository;
        this.estudianteCarreraRepository = estudianteCarreraRepository;
    }

    @Override
    public void run(String... args) throws IOException {
        if (carreraRepository.count() > 0 || estudianteRepository.count() > 0) {
            log.info("La base ya tiene datos, no se cargan los CSV.");
            return;
        }

        Map<Long, Carrera> carreras = cargarCarreras();
        Map<Long, Estudiante> estudiantes = cargarEstudiantes();
        int matriculas = cargarMatriculas(carreras, estudiantes);

        log.info("CSV cargados: {} carreras, {} estudiantes, {} matriculaciones.",
                carreras.size(), estudiantes.size(), matriculas);
    }

    // carreras.csv: id_carrera,carrera,duracion
    private Map<Long, Carrera> cargarCarreras() throws IOException {
        Map<Long, Carrera> porIdCsv = new HashMap<>();
        for (String[] c : leerCsv("csv/carreras.csv")) {
            Carrera carrera = new Carrera();
            carrera.setNombre(c[1]);
            carrera.setDuracion(Integer.valueOf(c[2]));
            porIdCsv.put(Long.valueOf(c[0]), carreraRepository.save(carrera));
        }
        return porIdCsv;
    }

    // estudiantes.csv: DNI,nombre,apellido,edad,genero,ciudad,LU
    private Map<Long, Estudiante> cargarEstudiantes() throws IOException {
        Map<Long, Estudiante> porDni = new HashMap<>();
        List<Estudiante> lista = new ArrayList<>();
        for (String[] c : leerCsv("csv/estudiantes.csv")) {
            Estudiante e = new Estudiante();
            e.setDni(Long.valueOf(c[0]));
            e.setNombre(c[1]);
            e.setApellido(c[2]);
            e.setEdad(Integer.valueOf(c[3]));
            e.setGenero(c[4]);
            e.setCiudad(c[5]);
            e.setLibretaUniversitaria(Integer.valueOf(c[6]));
            lista.add(e);
        }
        for (Estudiante guardado : estudianteRepository.saveAll(lista)) {
            porDni.put(guardado.getDni(), guardado);
        }
        return porDni;
    }

    // estudianteCarrera.csv: id,id_estudiante,id_carrera,inscripcion,graduacion,antiguedad
    // id_estudiante es el DNI. graduacion = 0 significa "no se graduó".
    private int cargarMatriculas(Map<Long, Carrera> carreras, Map<Long, Estudiante> estudiantes)
            throws IOException {
        List<estudianteCarrera> lista = new ArrayList<>();
        for (String[] c : leerCsv("csv/estudianteCarrera.csv")) {
            Estudiante estudiante = estudiantes.get(Long.valueOf(c[1]));
            Carrera carrera = carreras.get(Long.valueOf(c[2]));
            if (estudiante == null || carrera == null) {
                log.warn("Matriculación ignorada (estudiante o carrera inexistente): {}", String.join(",", c));
                continue;
            }
            int graduacion = Integer.parseInt(c[4]);

            estudianteCarrera m = new estudianteCarrera();
            m.setEstudiante(estudiante);
            m.setCarrera(carrera);
            m.setAnioInscripcion(Integer.valueOf(c[3]));
            m.setAnioGraduacion(graduacion == 0 ? null : graduacion);
            m.setAntiguedad(Integer.valueOf(c[5]));
            lista.add(m);
        }
        estudianteCarreraRepository.saveAll(lista);
        return lista.size();
    }

    /** Lee un CSV del classpath, saltea el encabezado y las líneas vacías. */
    private List<String[]> leerCsv(String ruta) throws IOException {
        byte[] bytes = new ClassPathResource(ruta).getInputStream().readAllBytes();
        List<String[]> filas = new ArrayList<>();
        boolean encabezado = true;
        for (String linea : new String(bytes, StandardCharsets.ISO_8859_1).split("\\r?\\n")) {
            // se re-decodifica cada línea por separado (ver decodificar)
            if (encabezado) {
                encabezado = false;
                continue;
            }
            if (linea.isBlank()) {
                continue;
            }
            filas.add(decodificar(linea).trim().split(",", -1));
        }
        return filas;
    }

    /**
     * Los CSV mezclan UTF-8 con algún byte suelto en Latin-1. Se intenta UTF-8
     * estricto por línea y, si falla, se interpreta la línea como ISO-8859-1.
     * La línea llega como ISO-8859-1 (1 char = 1 byte), así se recuperan los bytes originales.
     */
    private static String decodificar(String lineaComoLatin1) {
        byte[] original = lineaComoLatin1.getBytes(StandardCharsets.ISO_8859_1);
        try {
            return StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT)
                    .decode(ByteBuffer.wrap(original))
                    .toString()
                    .replace("\uFEFF", "");
        } catch (CharacterCodingException e) {
            return lineaComoLatin1;
        }
    }
}