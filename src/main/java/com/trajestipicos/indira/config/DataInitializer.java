package com.trajestipicos.indira.config;

import com.trajestipicos.indira.model.Rol;
import com.trajestipicos.indira.repository.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.trajestipicos.indira.model.Modelo;
import com.trajestipicos.indira.repository.ModeloRepository;

/**
 * Clase de configuración encargada de cargar datos iniciales necesarios para el
 * funcionamiento básico del sistema.
 * <p>
 * Al iniciar la aplicación, se verifican y crean los roles principales del
 * sistema y los modelos de vestidos disponibles en el catálogo. Esto evita que
 * el sistema dependa de una carga manual inicial en la base de datos.
 * @author Angela Sofía Rupay Aros
 */

@Configuration
public class DataInitializer {

    /**
     * Inicializa datos base de roles y modelos al arrancar la aplicación.
     * <p>
     * El metodo verifica si cada registro existe antes de crearlo, evitando
     * duplicados en reinicios posteriores. Los datos creados incluyen los roles
     * {@code ADMIN} y {@code CLIENTE}, además de los modelos de vestido:
     * Fantasía, Pintado, Profesional y Tradicional.
     *
     * @param rolRepository repositorio utilizado para consultar y guardar roles.
     * @param modeloRepository repositorio utilizado para consultar y guardar modelos de vestido.
     * @return tarea ejecutada automáticamente al iniciar Spring Boot.
     *  @author Angela Sofía Rupay Aros
     */
    @Bean
    CommandLineRunner initData(
            RolRepository rolRepository,
            ModeloRepository modeloRepository

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


            System.out.println("Datos iniciales cargados correctamente");
        };
    }
}