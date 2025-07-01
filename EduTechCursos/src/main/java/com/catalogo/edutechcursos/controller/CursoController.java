package com.catalogo.edutechcursos.controller;

import com.catalogo.edutechcursos.dto.CursoDTO;
import com.catalogo.edutechcursos.service.CursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.catalogo.edutechcursos.dto.CrearTicketRequest;
import org.springframework.web.client.RestTemplate;



import java.util.List;
import java.util.Map;

@Tag(name = "Cursos", description = "Operaciones relacionadas con los cursos")
@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @Operation(summary = "Listar todos los cursos", description = "Obtiene una lista de todos los cursos disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cursos listados correctamente")
    })
    @GetMapping
    public List<CursoDTO> listarCursos() {
        return cursoService.getCursos();
    }

    @Operation(summary = "Buscar curso por ID", description = "Busca un curso específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso encontrado"),
            @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    })
    @GetMapping("{id}")
    public ResponseEntity<?> buscarCurso(@Parameter(description = "ID del curso") @PathVariable String id) {
        try {
            CursoDTO curso = cursoService.getCursoById(id);
            return ResponseEntity.ok(curso);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Curso con id " + id + " no encontrado");
        }
    }

    @Operation(summary = "Crear nuevo curso", description = "Agrega un nuevo curso al sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Curso creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> agregarCurso(@Validated @RequestBody CursoDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Body incompleto o inválido");
        }

        CursoDTO creado = cursoService.saveCurso(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Actualizar curso existente", description = "Actualiza la información de un curso existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    })
    @PutMapping("{id}")
    public ResponseEntity<?> actualizarCurso(@Parameter(description = "ID del curso") @PathVariable String id,
                                             @Validated @RequestBody CursoDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Body incompleto o inválido");
        }

        try {
            CursoDTO actualizado = cursoService.updateCurso(id, dto);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Curso con id " + id + " no encontrado");
        }
    }

    @Operation(summary = "Eliminar curso", description = "Elimina un curso específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<?> eliminarCurso(@Parameter(description = "ID del curso") @PathVariable String id) {
        boolean eliminado = cursoService.deleteCurso(id);
        if (eliminado) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Curso con id " + id + " no encontrado");
        }
    }

//Ruta para generar un ticket desde un curso especifico via su ip
    @Autowired
    private RestTemplate restTemplate;
    @PostMapping("/{idCurso}/ticket")
    public ResponseEntity<String> crearTicketDesdeCurso(
        @PathVariable String idCurso,
        @RequestBody Map<String, String> body
    ) {
        CrearTicketRequest ticketRequest = new CrearTicketRequest(
                body.get("nombre"),
                body.get("descripcion"),
                body.get("tipoProblema"),
                body.get("estadoTicket"),
                idCurso
        );

        String url = "http://soporte:8082/api/tickets"; // usar 'tsoporte' si estás en Docker

        try {
            restTemplate.postForEntity(url, ticketRequest, String.class);
            return ResponseEntity.ok("Ticket creado correctamente para el curso con ID: " + idCurso);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al crear el ticket: " + e.getMessage());
        }
    }
}


    