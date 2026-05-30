package com.trajestipicos.indira.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración encargada de exponer recursos estáticos externos al directorio
 * tradicional de la aplicación.
 * <p>
 * En este proyecto se utiliza para mostrar las imágenes de los vestidos que son
 * cargadas por el administrador y almacenadas en la carpeta local
 * {@code uploads/vestidos}.
 *  @author Angela Sofía Rupay Aros
 */

@Configuration
public class WebResourceConfig implements WebMvcConfigurer {

    /**
     * Registra la ruta pública desde la cual se pueden consultar las imágenes
     * subidas de los vestidos.
     * <p>
     * La ruta {@code /images/vestidos/**} se asocia con la carpeta física
     * {@code uploads/vestidos/}, permitiendo que el frontend pueda mostrar las
     * imágenes cargadas desde el panel administrativo.
     *
     * @param registry registro de manejadores de recursos estáticos.
     *
     *  @author Angela Sofía Rupay Aros
     */

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/vestidos/**")
                .addResourceLocations("file:uploads/vestidos/");
    }
}
