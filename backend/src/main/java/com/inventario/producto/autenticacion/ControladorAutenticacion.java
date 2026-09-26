package com.inventario.producto.autenticacion;

import com.inventario.producto.autenticacion.dto.SolicitudInicioSesion;
import com.inventario.producto.autenticacion.dto.RespuestaInicioSesion;
import com.inventario.producto.seguridad.UtilidadesJwt;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/autenticacion")
@Tag(name = "Autenticación", description = "Inicio de sesión con credenciales fijas para obtener un JWT")
public class ControladorAutenticacion {

    private final UtilidadesJwt utilidadesJwt;
    private final String usuarioConfigurado;
    private final String contrasenaConfigurada;

    public ControladorAutenticacion(UtilidadesJwt utilidadesJwt,
                           @Value("${autenticacion.usuario}") String usuarioConfigurado,
                           @Value("${autenticacion.contrasena}") String contrasenaConfigurada) {
        this.utilidadesJwt = utilidadesJwt;
        this.usuarioConfigurado = usuarioConfigurado;
        this.contrasenaConfigurada = contrasenaConfigurada;
    }

    @PostMapping("/iniciar-sesion")
    @Operation(summary = "Autentica con usuario/contraseña fijos y devuelve un JWT")
    public RespuestaInicioSesion iniciarSesion(@Valid @RequestBody SolicitudInicioSesion solicitud) {
        if (!usuarioConfigurado.equals(solicitud.getUsuario()) || !contrasenaConfigurada.equals(solicitud.getContrasena())) {
            throw new BadCredentialsException("Usuario o contraseña incorrectos");
        }
        String token = utilidadesJwt.generarToken(solicitud.getUsuario());
        return new RespuestaInicioSesion(token);
    }
}
