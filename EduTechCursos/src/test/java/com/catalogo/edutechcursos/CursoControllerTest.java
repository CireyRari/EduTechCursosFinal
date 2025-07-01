package com.catalogo.edutechcursos;

import com.catalogo.edutechcursos.controller.CursoController;
import com.catalogo.edutechcursos.dto.CursoDTO;
import com.catalogo.edutechcursos.service.CursoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CursoController.class)
public class CursoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CursoService cursoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testListarCursos() throws Exception {
        CursoDTO curso = new CursoDTO();
        curso.setId("JAV1234");
        curso.setNombre("Java");
        curso.setEstadoNombre("Disponible");

        when(cursoService.getCursos()).thenReturn(List.of(curso));

        mockMvc.perform(get("/api/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre", is("Java")));
    }

    @Test
    void testBuscarCursoFound() throws Exception {
        CursoDTO curso = new CursoDTO();
        curso.setId("JAV1234");
        curso.setNombre("Java");

        when(cursoService.getCursoById("JAV1234")).thenReturn(curso);

        mockMvc.perform(get("/api/cursos/JAV1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Java")));
    }

    @Test
    void testBuscarCursoNotFound() throws Exception {
        when(cursoService.getCursoById("NOEXISTE")).thenThrow(new RuntimeException("Curso no encontrado"));

        mockMvc.perform(get("/api/cursos/NOEXISTE"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("no encontrado")));
    }

    @Test
    void testAgregarCursoValido() throws Exception {
        CursoDTO input = new CursoDTO();
        input.setNombre("Java");
        input.setDescripcion("Intro");
        input.setDuracion(20);
        input.setCosto(15000);
        input.setEstadoId(1L);

        CursoDTO creado = new CursoDTO();
        creado.setId("JAV1234");
        creado.setNombre("Java");

        when(cursoService.saveCurso(any())).thenReturn(creado);

        mockMvc.perform(post("/api/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is("JAV1234")));
    }

    @Test
    void testActualizarCurso() throws Exception {
        CursoDTO input = new CursoDTO();
        input.setNombre("Java Avanzado");
        input.setDescripcion("Avanzado");
        input.setDuracion(30);
        input.setCosto(25000);
        input.setEstadoId(1L);

        CursoDTO actualizado = new CursoDTO();
        actualizado.setNombre("Java Avanzado");

        when(cursoService.updateCurso(eq("JAV1234"), any())).thenReturn(actualizado);

        mockMvc.perform(put("/api/cursos/JAV1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Java Avanzado")));
    }

    @Test
    void testEliminarCursoOK() throws Exception {
        when(cursoService.deleteCurso("JAV1234")).thenReturn(true);

        mockMvc.perform(delete("/api/cursos/JAV1234"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testEliminarCursoNotFound() throws Exception {
        when(cursoService.deleteCurso("NOEXISTE")).thenReturn(false);

        mockMvc.perform(delete("/api/cursos/NOEXISTE"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("no encontrado")));
    }
}