package com.inventario.producto.autenticacion.dto;

public class RespuestaInicioSesion {

    private final String token;
    private final String tipoToken = "Bearer";

    public RespuestaInicioSesion(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getTipoToken() {
        return tipoToken;
    }
}
