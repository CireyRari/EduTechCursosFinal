package com.catalogo.edutechcursos.dto;

import com.catalogo.edutechcursos.model.Curso;
import com.catalogo.edutechcursos.model.Estado;

public class CursoMapper {

    public static CursoDTO toDTO(Curso curso) {
        CursoDTO dto = new CursoDTO();
        dto.setId(curso.getId());
        dto.setNombre(curso.getNombre());
        dto.setDescripcion(curso.getDescripcion());
        dto.setDuracion(curso.getDuracion());
        dto.setCosto(curso.getCosto());

        if (curso.getEstado() != null) {
            dto.setEstadoId(curso.getEstado().getId());
            dto.setEstadoNombre(curso.getEstado().getNombre());
        }

        return dto;
    }

    public static Curso toEntity(CursoDTO dto, Estado estado) {
        Curso curso = new Curso();
        curso.setId(dto.getId());
        curso.setNombre(dto.getNombre());
        curso.setDescripcion(dto.getDescripcion());
        curso.setDuracion(dto.getDuracion());
        curso.setCosto(dto.getCosto());
        curso.setEstado(estado);
        return curso;
    }
}

