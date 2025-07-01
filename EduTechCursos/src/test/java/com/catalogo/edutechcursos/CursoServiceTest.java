package com.catalogo.edutechcursos;

import com.catalogo.edutechcursos.dto.CursoDTO;
import com.catalogo.edutechcursos.dto.CursoMapper;
import com.catalogo.edutechcursos.model.Curso;
import com.catalogo.edutechcursos.model.Estado;
import com.catalogo.edutechcursos.repository.CursoRepository;
import com.catalogo.edutechcursos.repository.EstadoRepository;
import com.catalogo.edutechcursos.service.CursoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private EstadoRepository estadoRepository;

    @InjectMocks
    private CursoService cursoService;

    private Curso curso;
    private Estado estado;
    private final String cursoId = "JAV1234";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        estado = new Estado(1L, "Disponible");
        curso = new Curso(cursoId, "Java", "Intro", 10, 20000, estado);
    }

    @Test
    void testGetCursoById() {
        when(cursoRepository.findById(cursoId)).thenReturn(Optional.of(curso));

        CursoDTO dto = cursoService.getCursoById(cursoId);

        assertEquals("Java", dto.getNombre());
        assertEquals("Disponible", dto.getEstadoNombre());
    }

    @Test
    void testSaveCurso() {
        CursoDTO input = CursoMapper.toDTO(curso);
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estado));
        when(cursoRepository.save(any())).thenReturn(curso);

        CursoDTO resultado = cursoService.saveCurso(input);

        assertEquals(input.getNombre(), resultado.getNombre());
        verify(cursoRepository, times(1)).save(any());
    }

    @Test
    void testUpdateCurso() {
        CursoDTO actualizado = CursoMapper.toDTO(curso);
        actualizado.setNombre("Java Avanzado");

        when(cursoRepository.findById(cursoId)).thenReturn(Optional.of(curso));
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estado));
        when(cursoRepository.save(any())).thenReturn(curso);

        CursoDTO resultado = cursoService.updateCurso(cursoId, actualizado);

        assertEquals("Java Avanzado", resultado.getNombre());
    }

    @Test
    void testDeleteCursoExiste() {
        when(cursoRepository.existsById(cursoId)).thenReturn(true);

        boolean eliminado = cursoService.deleteCurso(cursoId);

        assertTrue(eliminado);
        verify(cursoRepository).deleteById(cursoId);
    }

    @Test
    void testDeleteCursoNoExiste() {
        String idInexistente = "ABC9999";
        when(cursoRepository.existsById(idInexistente)).thenReturn(false);

        boolean eliminado = cursoService.deleteCurso(idInexistente);

        assertFalse(eliminado);
        verify(cursoRepository, never()).deleteById(anyString());
    }
}