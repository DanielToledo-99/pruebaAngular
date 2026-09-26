package com.inventario.producto.servicio;

import com.inventario.producto.dto.SolicitudProducto;
import com.inventario.producto.dto.RespuestaProducto;

import java.util.List;

public interface ServicioProductos {

    List<RespuestaProducto> listar(String nombre);

    RespuestaProducto buscarPorId(Long id);

    RespuestaProducto crear(SolicitudProducto solicitud);

    RespuestaProducto actualizar(Long id, SolicitudProducto solicitud);

    void eliminar(Long id);
}
