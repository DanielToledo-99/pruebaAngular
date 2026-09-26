package com.inventario.producto.repositorio;

import com.inventario.producto.entidad.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositorioProductos extends JpaRepository<Producto, Long> {

    List<Producto> findByNombreContainingIgnoreCase(String nombre);
}
