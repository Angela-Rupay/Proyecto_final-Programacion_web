package com.trajestipicos.indira.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Locale;

/**
 * Configuración web relacionada con la internacionalización de la aplicación.
 * <p>
 * Esta clase define el idioma por defecto del sistema y permite cambiarlo
 * mediante el parámetro {@code lang} en la URL. De esta manera, las vistas
 * pueden conservar una navegación multilenguaje sin afectar la lógica principal
 * del backend.
 *  @author Angela Sofía Rupay Aros
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     * Define el resolvedor de idioma usado por Spring MVC.
     * <p>
     * El idioma por defecto de la aplicación se establece en español, ya que el
     * sistema está orientado inicialmente a usuarios de la región de Neiva y del
     * contexto colombiano.
     *
     * @return resolvedor de idioma basado en sesión.
     *  @author Angela Sofía Rupay Aros
     */
    @Bean(name = "localeResolver")
    public LocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.forLanguageTag("es"));
        return resolver;
    }
    /**
     * Crea el interceptor encargado de detectar cambios de idioma.
     * <p>
     * El idioma se cambia a partir del parámetro {@code lang}, por ejemplo:
     * {@code ?lang=es}, {@code ?lang=en} o {@code ?lang=pt}. Si el parámetro
     * recibido no es válido, se ignora para evitar errores de navegación.
     *
     * @return interceptor de cambio de idioma.
     *  @author Angela Sofía Rupay Aros
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        interceptor.setIgnoreInvalidLocale(true);
        return interceptor;
    }
    /**
     * Registra el interceptor de idioma dentro del flujo de peticiones MVC.
     *
     * @param registry registro de interceptores de Spring MVC.
     *  @author Angela Sofía Rupay Aros
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}