package com.inventario.producto.seguridad;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class FiltroAutenticacionJwt extends OncePerRequestFilter {

    private static final String PREFIJO_BEARER = "Bearer ";

    private final UtilidadesJwt utilidadesJwt;

    public FiltroAutenticacionJwt(UtilidadesJwt utilidadesJwt) {
        this.utilidadesJwt = utilidadesJwt;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest solicitud, @NonNull HttpServletResponse respuesta,
                                     @NonNull FilterChain cadenaFiltros) throws ServletException, IOException {
        String cabeceraAutenticacion = solicitud.getHeader("Authorization");

        if (StringUtils.hasText(cabeceraAutenticacion) && cabeceraAutenticacion.startsWith(PREFIJO_BEARER)) {
            String token = cabeceraAutenticacion.substring(PREFIJO_BEARER.length());
            if (utilidadesJwt.esTokenValido(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
                String usuario = utilidadesJwt.extraerUsuario(token);
                UsernamePasswordAuthenticationToken autenticacion =
                        new UsernamePasswordAuthenticationToken(usuario, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(autenticacion);
            }
        }

        cadenaFiltros.doFilter(solicitud, respuesta);
    }
}
