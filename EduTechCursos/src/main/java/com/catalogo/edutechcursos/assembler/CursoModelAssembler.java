package com.catalogo.edutechcursos.assembler;

import com.catalogo.edutechcursos.controller.CursoController;
import com.catalogo.edutechcursos.dto.CursoDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CursoModelAssembler implements RepresentationModelAssembler<CursoDTO, EntityModel<CursoDTO>> {

    @Override
    public EntityModel<CursoDTO> toModel(CursoDTO curso) {
        return EntityModel.of(curso,
                linkTo(methodOn(CursoController.class).buscarCurso(curso.getId())).withSelfRel(),
                linkTo(methodOn(CursoController.class).listarCursos()).withRel("cursos"));
    }
}

