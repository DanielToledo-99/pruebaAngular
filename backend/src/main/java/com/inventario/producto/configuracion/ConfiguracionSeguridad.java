package com.inventario.producto.configuracion;

import com.inventario.producto.seguridad.PuntoEntradaAutenticacionJwt;
import com.inventario.producto.seguridad.FiltroAutenticacionJwt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class ConfiguracionSeguridad {

    private final FiltroAutenticacionJwt filtroAutenticacionJwt;
    private final PuntoEntradaAutenticacionJwt puntoEntradaAutenticacionJwt;
    private final List<String> origenesPermitidos;

    public ConfiguracionSeguridad(FiltroAutenticacionJwt filtroAutenticacionJwt,
                           PuntoEntradaAutenticacionJwt puntoEntradaAutenticacionJwt,
                           @Value("${aplicacion.cors.origenes-permitidos}") String origenesPermitidos) {
        this.filtroAutenticacionJwt = filtroAutenticacionJwt;
        this.puntoEntradaAutenticacionJwt = puntoEntradaAutenticacionJwt;
        this.origenesPermitidos = Arrays.asList(origenesPermitidos.split(","));
    }

    private static final String[] RUTAS_PUBLICAS = {
            "/api/autenticacion/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**"
    };

    @Bean
    public SecurityFilterChain cadenaFiltros(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors().configurationSource(fuenteConfiguracionCors()).and()
                .exceptionHandling().authenticationEntryPoint(puntoEntradaAutenticacionJwt).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(RUTAS_PUBLICAS).permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(filtroAutenticacionJwt, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource fuenteConfiguracionCors() {
        CorsConfiguration configuracion = new CorsConfiguration();
        configuracion.setAllowedOrigins(origenesPermitidos);
        configuracion.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuracion.setAllowedHeaders(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource fuente = new UrlBasedCorsConfigurationSource();
        fuente.registerCorsConfiguration("/**", configuracion);
        return fuente;
    }
}
