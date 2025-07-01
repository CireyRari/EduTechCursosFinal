package com.catalogo.edutechcursos.controller;

import com.catalogo.edutechcursos.assembler.CursoModelAssembler;
import com.catalogo.edutechcursos.dto.CursoDTO;
import com.catalogo.edutechcursos.service.CursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Cursos (v2)", description = "Operaciones con enlaces HATEOAS")
@RestController
@RequestMapping("/api/v2/cursos")
public class CursoControllerV2 {

    @Autowired
    private CursoService cursoService;

    @Autowired
    private CursoModelAssembler assembler;

    @Operation(summary = "Listar cursos (v2)", description = "Obtiene todos los cursos con enlaces HATEOAS")
    @ApiResponse(responseCode = "200", description = "Listado HATEOAS generado")
    @GetMapping
    public CollectionModel<EntityModel<CursoDTO>> listarCursos() {
        List<EntityModel<CursoDTO>> cursos = cursoService.getCursos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(cursos,
                linkTo(methodOn(CursoControllerV2.class).listarCursos()).withSelfRel());
    }

    @Operation(summary = "Buscar curso (v2)", description = "Busca un curso por ID con enlaces HATEOAS")
    @ApiResponse(responseCode = "200", description = "Curso encontrado con enlaces HATEOAS")
    @GetMapping("{id}")
    public EntityModel<CursoDTO> buscarCurso(@Parameter(description = "ID del curso") @PathVariable String id) {
        CursoDTO dto = cursoService.getCursoById(id);
        return assembler.toModel(dto);
    }
}