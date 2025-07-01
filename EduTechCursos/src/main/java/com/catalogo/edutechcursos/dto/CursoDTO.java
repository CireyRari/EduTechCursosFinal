package com.catalogo.edutechcursos.dto;

import lombok.Data;

@Data
public class CursoDTO {
    private String id; // Cambiado de Long a String
    private String nombre;
    private String descripcion;
    private Integer duracion;
    private Integer costo;
    private Long estadoId;         // Para recibir el ID del estado
    private String estadoNombre;   // Para mostrar el nombre del estado al cliente
}