package com.catalogo.edutechcursos.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Curso {

    @Id
    private String id;

    @NotEmpty(message = "Body incompleto o inválido")
    @Schema(description = "Nombre del curso", example = "Introducción a Java")
    private String nombre;

    @NotEmpty(message = "Body incompleto o inválido")
    @Schema(description = "Descripción del curso", example = "Curso de API con Java")
    private String descripcion;

    @NotNull(message = "Body incompleto o inválido")
    @Min(value = 1, message = "La duración debe ser mayor a cero")
    @Schema(description = "Duración del curso", example = "5")
    private Integer duracion;

    @NotNull(message = "Body incompleto o inválido")
    @Min(value = 1, message = "La duración debe ser mayor a cero")
    @Schema(description = "Costo del curso", example = "35000")
    private Integer costo;

    @ManyToOne
    @JoinColumn(name = "estado_id")
    private Estado estado;

    @PrePersist
    private void generarId() {
        if (this.nombre != null && (this.id == null || this.id.isEmpty())) {
            String prefijo = this.nombre.replaceAll("\\s+", "").substring(0, Math.min(3, this.nombre.length())).toUpperCase();
            int random = (int) (Math.random() * 9000) + 1000;
            this.id = prefijo + random;
        }
    }
}