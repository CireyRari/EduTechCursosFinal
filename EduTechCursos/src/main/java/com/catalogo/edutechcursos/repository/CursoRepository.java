package com.catalogo.edutechcursos.repository;

import com.catalogo.edutechcursos.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, String> {
    List<Curso> findByEstadoNombre(String nombre);
    List<Curso> findByDuracionGreaterThan(Integer duracion);
    List<Curso> findByCostoLessThan(Integer costo);
    List<Curso> findAllByOrderByNombreAsc();
    Long countByEstadoNombre(String nombre);
}
