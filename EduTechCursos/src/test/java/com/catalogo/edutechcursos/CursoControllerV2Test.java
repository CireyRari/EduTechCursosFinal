package com.catalogo.edutechcursos;

import com.catalogo.edutechcursos.assembler.CursoModelAssembler;
import com.catalogo.edutechcursos.controller.CursoControllerV2;
import com.catalogo.edutechcursos.dto.CursoDTO;
import com.catalogo.edutechcursos.service.CursoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasKey;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CursoControllerV2.class)
public class CursoControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CursoService cursoService;

    @MockBean
    private CursoModelAssembler assembler;

    @Test
    void testListarCursosConLinks() throws Exception {
        CursoDTO dto = new CursoDTO();
        dto.setId("JAV1234");
        dto.setNombre("Java");

        EntityModel<CursoDTO> model = EntityModel.of(dto);
        model.add(Link.of("/api/v2/cursos/JAV1234").withSelfRel());
        model.add(Link.of("/api/v2/cursos").withRel("cursos"));

        when(cursoService.getCursos()).thenReturn(List.of(dto));
        when(assembler.toModel(dto)).thenReturn(model);

        mockMvc.perform(get("/api/v2/cursos").accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.cursoDTOList[0]._links", hasKey("self")))
                .andExpect(jsonPath("_embedded.cursoDTOList[0].id").value("JAV1234"))
                .andExpect(jsonPath("_links.self.href").exists());
    }

    @Test
    void testBuscarCursoConLink() throws Exception {
        CursoDTO dto = new CursoDTO();
        dto.setId("JAV1234");
        dto.setNombre("Java");

        EntityModel<CursoDTO> model = EntityModel.of(dto);
        model.add(Link.of("/api/v2/cursos/JAV1234").withSelfRel());

        when(cursoService.getCursoById("JAV1234")).thenReturn(dto);
        when(assembler.toModel(dto)).thenReturn(model);

        mockMvc.perform(get("/api/v2/cursos/JAV1234").accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.self.href").value("/api/v2/cursos/JAV1234"));
    }
}