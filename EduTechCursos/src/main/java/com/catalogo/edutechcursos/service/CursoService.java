package com.catalogo.edutechcursos.service;

import com.catalogo.edutechcursos.model.Curso;
import com.catalogo.edutechcursos.model.Estado;
import com.catalogo.edutechcursos.repository.CursoRepository;
import com.catalogo.edutechcursos.repository.EstadoRepository;
import com.catalogo.edutechcursos.dto.CursoDTO;
import com.catalogo.edutechcursos.dto.CursoMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    // Obtener todos los cursos como DTOs
    public List<CursoDTO> getCursos() {
        return cursoRepository.findAll().stream()
                .map(CursoMapper::toDTO) // Convertimos cada Curso a CursoDTO
                .toList();
    }

    // Buscar un curso por ID y devolverlo como DTO
    public CursoDTO getCursoById(String id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + id));
        return CursoMapper.toDTO(curso);
    }

    // Guardar un nuevo curso (conversión desde DTO a entidad)
    public CursoDTO saveCurso(CursoDTO dto) {
        Estado estado = estadoRepository.findById(dto.getEstadoId())
                .orElseThrow(() -> new RuntimeException("Estado no encontrado con ID: " + dto.getEstadoId()));

        Curso curso = CursoMapper.toEntity(dto, estado);
        Curso guardado = cursoRepository.save(curso);
        return CursoMapper.toDTO(guardado);
    }

    // Actualizar un curso existente
    public CursoDTO updateCurso(String id, CursoDTO dto) {
        Curso cursoExistente = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + id));

        Estado estado = estadoRepository.findById(dto.getEstadoId())
                .orElseThrow(() -> new RuntimeException("Estado no encontrado con ID: " + dto.getEstadoId()));

        // Se actualizan solo los campos necesarios
        cursoExistente.setNombre(dto.getNombre());
        cursoExistente.setDescripcion(dto.getDescripcion());
        cursoExistente.setDuracion(dto.getDuracion());
        cursoExistente.setCosto(dto.getCosto());
        cursoExistente.setEstado(estado);

        Curso actualizado = cursoRepository.save(cursoExistente);
        return CursoMapper.toDTO(actualizado);
    }

    // Eliminar un curso por ID
    public Boolean deleteCurso(String id) {
        if (cursoRepository.existsById(id)) {
            cursoRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public List<CursoDTO> getCursosPorEstado(String nombreEstado) {
        return cursoRepository.findByEstadoNombre(nombreEstado).stream()
                .map(CursoMapper::toDTO)
                .toList();
    }

    public List<CursoDTO> getCursosPorDuracionMayorA(int duracion) {
        return cursoRepository.findByDuracionGreaterThan(duracion).stream()
                .map(CursoMapper::toDTO)
                .toList();
    }

    public List<CursoDTO> getCursosPorCostoMenorA(int costo) {
        return cursoRepository.findByCostoLessThan(costo).stream()
                .map(CursoMapper::toDTO)
                .toList();
    }

    public List<CursoDTO> getCursosOrdenadosPorNombre() {
        return cursoRepository.findAllByOrderByNombreAsc().stream()
                .map(CursoMapper::toDTO)
                .toList();
    }

    public Long contarCursosPorEstado(String estado) {
        return cursoRepository.countByEstadoNombre(estado);
    }

}
