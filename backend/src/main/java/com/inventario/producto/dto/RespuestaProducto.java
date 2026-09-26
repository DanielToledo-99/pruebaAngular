package com.inventario.producto.dto;

import com.inventario.producto.entidad.Producto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RespuestaProducto {

    private Long id;
    private String nombre;
    private String descripcion;
    private Integer cantidad;
    private BigDecimal precio;
    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;

    public static RespuestaProducto desdeEntidad(Producto producto) {
        RespuestaProducto respuesta = new RespuestaProducto();
        respuesta.id = producto.getId();
        respuesta.nombre = producto.getNombre();
        respuesta.descripcion = producto.getDescripcion();
        respuesta.cantidad = producto.getCantidad();
        respuesta.precio = producto.getPrecio();
        respuesta.creadoEn = producto.getCreadoEn();
        respuesta.actualizadoEn = producto.getActualizadoEn();
        return respuesta;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public LocalDateTime getActualizadoEn() {
        return actualizadoEn;
    }
}
