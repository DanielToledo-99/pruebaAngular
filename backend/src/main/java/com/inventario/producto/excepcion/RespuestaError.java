package com.inventario.producto.excepcion;

import java.time.LocalDateTime;
import java.util.Map;

public class RespuestaError {

    private final LocalDateTime fechaHora = LocalDateTime.now();
    private int estado;
    private String error;
    private String mensaje;
    private Map<String, String> erroresCampos;

    public RespuestaError(int estado, String error, String mensaje) {
        this.estado = estado;
        this.error = error;
        this.mensaje = mensaje;
    }

    public RespuestaError(int estado, String error, String mensaje, Map<String, String> erroresCampos) {
        this(estado, error, mensaje);
        this.erroresCampos = erroresCampos;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public int getEstado() {
        return estado;
    }

    public String getError() {
        return error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Map<String, String> getErroresCampos() {
        return erroresCampos;
    }
}
