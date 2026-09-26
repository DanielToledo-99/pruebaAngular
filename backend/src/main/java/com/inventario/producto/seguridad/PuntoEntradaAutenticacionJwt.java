package com.inventario.producto.seguridad;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventario.producto.excepcion.RespuestaError;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class PuntoEntradaAutenticacionJwt implements AuthenticationEntryPoint {

    private final ObjectMapper convertidorJson = new ObjectMapper().findAndRegisterModules();

    @Override
    public void commence(HttpServletRequest solicitud, HttpServletResponse respuesta,
                          AuthenticationException excepcionAutenticacion) throws IOException {
        respuesta.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        respuesta.setCharacterEncoding("UTF-8");
        respuesta.setContentType(MediaType.APPLICATION_JSON_VALUE);
        RespuestaError cuerpo = new RespuestaError(HttpServletResponse.SC_UNAUTHORIZED, "No autorizado",
                "Se requiere un token de autenticación válido");
        respuesta.getWriter().write(convertidorJson.writeValueAsString(cuerpo));
    }
}
