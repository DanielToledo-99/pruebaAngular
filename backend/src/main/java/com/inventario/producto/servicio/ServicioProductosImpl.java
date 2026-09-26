package com.inventario.producto.servicio;

import com.inventario.producto.dto.SolicitudProducto;
import com.inventario.producto.dto.RespuestaProducto;
import com.inventario.producto.entidad.Producto;
import com.inventario.producto.excepcion.ExcepcionRecursoNoEncontrado;
import com.inventario.producto.repositorio.RepositorioProductos;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicioProductosImpl implements ServicioProductos {

    private final RepositorioProductos repositorioProductos;

    public ServicioProductosImpl(RepositorioProductos repositorioProductos) {
        this.repositorioProductos = repositorioProductos;
    }

    @Override
    public List<RespuestaProducto> listar(String nombre) {
        List<Producto> productos = StringUtils.hasText(nombre)
                ? repositorioProductos.findByNombreContainingIgnoreCase(nombre)
                : repositorioProductos.findAll();
        return productos.stream().map(RespuestaProducto::desdeEntidad).collect(Collectors.toList());
    }

    @Override
    public RespuestaProducto buscarPorId(Long id) {
        return RespuestaProducto.desdeEntidad(obtenerProductoOError(id));
    }

    @Override
    public RespuestaProducto crear(SolicitudProducto solicitud) {
        Producto producto = new Producto();
        aplicarSolicitud(producto, solicitud);
        return RespuestaProducto.desdeEntidad(repositorioProductos.save(producto));
    }

    @Override
    public RespuestaProducto actualizar(Long id, SolicitudProducto solicitud) {
        Producto producto = obtenerProductoOError(id);
        aplicarSolicitud(producto, solicitud);
        return RespuestaProducto.desdeEntidad(repositorioProductos.save(producto));
    }

    @Override
    public void eliminar(Long id) {
        Producto producto = obtenerProductoOError(id);
        repositorioProductos.delete(producto);
    }

    private Producto obtenerProductoOError(Long id) {
        return repositorioProductos.findById(id)
                .orElseThrow(() -> new ExcepcionRecursoNoEncontrado("Producto no encontrado con id " + id));
    }

    private void aplicarSolicitud(Producto producto, SolicitudProducto solicitud) {
        producto.setNombre(solicitud.getNombre());
        producto.setDescripcion(solicitud.getDescripcion());
        producto.setCantidad(solicitud.getCantidad());
        producto.setPrecio(solicitud.getPrecio());
    }
}
