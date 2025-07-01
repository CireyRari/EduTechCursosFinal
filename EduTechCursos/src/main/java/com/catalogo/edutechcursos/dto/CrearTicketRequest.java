
package com.catalogo.edutechcursos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearTicketRequest {
    private String nombre;
    private String descripcion;
    private String tipoProblema;
    private String estadoTicket;
    private String idCurso;
}
