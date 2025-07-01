package com.catalogo.edutechcursos.dataloader;

import com.catalogo.edutechcursos.model.Curso;
import com.catalogo.edutechcursos.model.Estado;
import com.catalogo.edutechcursos.repository.CursoRepository;
import com.catalogo.edutechcursos.repository.EstadoRepository;

import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
public class CursoDataSeeder {

    @Bean
    public CommandLineRunner seedCursos(CursoRepository cursoRepository, EstadoRepository estadoRepository) {
        return args -> {

            if (cursoRepository.count() == 0) {

                Faker faker = new Faker(new Random());

                Estado disponible = estadoRepository.findById(1L).orElse(null);
                Estado noDisponible = estadoRepository.findById(2L).orElse(null);

                // Si no existen los estados, los creamos
                if (disponible == null) {
                    disponible = estadoRepository.save(new Estado(null, "Disponible"));
                }
                if (noDisponible == null) {
                    noDisponible = estadoRepository.save(new Estado(null, "No disponible"));
                }

                List<Curso> cursos = new ArrayList<>();

                for (int i = 0; i < 10; i++) {
                    Curso curso = new Curso();
                    curso.setNombre(faker.educator().course());
                    curso.setDescripcion(faker.lorem().sentence(10));
                    curso.setDuracion(faker.number().numberBetween(10, 100));
                    curso.setCosto(faker.number().numberBetween(10000, 100000));
                    curso.setEstado(faker.bool().bool() ? disponible : noDisponible);
                    cursos.add(curso);
                }

                cursoRepository.saveAll(cursos);
                System.out.println("✅ 10 cursos falsos generados");
            }
        };
    }
}
