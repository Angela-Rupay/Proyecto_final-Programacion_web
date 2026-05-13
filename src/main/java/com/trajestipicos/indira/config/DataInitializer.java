package com.trajestipicos.indira.config;

import com.trajestipicos.indira.model.Rol;
import com.trajestipicos.indira.repository.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.trajestipicos.indira.model.Modelo;
import com.trajestipicos.indira.repository.ModeloRepository;
import com.trajestipicos.indira.model.FloresColor;
import com.trajestipicos.indira.repository.FloresColorRepository;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initData(
            RolRepository rolRepository,
            ModeloRepository modeloRepository,
            FloresColorRepository floresColorRepository

    ) {
        return args -> {

            // ROLES
            if (rolRepository.findByTipoRol("ADMIN").isEmpty()) {
                Rol admin = new Rol();
                admin.setTipoRol("ADMIN");
                rolRepository.save(admin);
            }

            if (rolRepository.findByTipoRol("CLIENTE").isEmpty()) {
                Rol cliente = new Rol();
                cliente.setTipoRol("CLIENTE");
                rolRepository.save(cliente);
            }

            // MODELOS
            if (modeloRepository.findById("TF").isEmpty()) {
                modeloRepository.save(new Modelo("TF", "Fantasía", null));
            }

            if (modeloRepository.findById("P").isEmpty()) {
                modeloRepository.save(new Modelo("P", "Pintado", null));
            }

            if (modeloRepository.findById("TPRO").isEmpty()) {
                modeloRepository.save(new Modelo("TPRO", "Profesional", null));
            }

            if (modeloRepository.findById("TT").isEmpty()) {
                modeloRepository.save(new Modelo("TT", "Tradicional", null));
            }
            // COLORES DE FLORES
            String[] colores = {
                    "Amarillo", "Rojo", "Naranja", "Ocre", "Azul",
                    "Verde", "Rosado", "Morado", "Negro"
            };

            for (String nombreColor : colores) {
                boolean existe = floresColorRepository.findAll()
                        .stream()
                        .anyMatch(color -> color.getNombreColor().equalsIgnoreCase(nombreColor));

                if (!existe) {
                    FloresColor color = new FloresColor();
                    color.setNombreColor(nombreColor);
                    floresColorRepository.save(color);
                }
            }


            System.out.println("Datos iniciales cargados correctamente");
        };
    }
}