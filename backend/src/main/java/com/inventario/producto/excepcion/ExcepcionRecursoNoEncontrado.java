package com.inventario.producto.excepcion;

public class ExcepcionRecursoNoEncontrado extends RuntimeException {

    public ExcepcionRecursoNoEncontrado(String mensaje) {
        super(mensaje);
    }
}
