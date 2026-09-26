package com.inventario.producto.excepcion;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    @ExceptionHandler(ExcepcionRecursoNoEncontrado.class)
    public ResponseEntity<RespuestaError> manejarNoEncontrado(ExcepcionRecursoNoEncontrado ex) {
        RespuestaError cuerpo = new RespuestaError(HttpStatus.NOT_FOUND.value(), "No encontrado", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cuerpo);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<RespuestaError> manejarCredencialesInvalidas(BadCredentialsException ex) {
        RespuestaError cuerpo = new RespuestaError(HttpStatus.UNAUTHORIZED.value(), "No autorizado", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(cuerpo);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespuestaError> manejarValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> erroresCampos = new HashMap<>();
        for (FieldError errorCampo : ex.getBindingResult().getFieldErrors()) {
            erroresCampos.put(errorCampo.getField(), errorCampo.getDefaultMessage());
        }
        RespuestaError cuerpo = new RespuestaError(HttpStatus.BAD_REQUEST.value(), "Validación fallida",
                "Uno o más campos no son válidos", erroresCampos);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cuerpo);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RespuestaError> manejarErrorGeneral(Exception ex) {
        RespuestaError cuerpo = new RespuestaError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error interno del servidor",
                "Ocurrió un error inesperado");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(cuerpo);
    }
}
