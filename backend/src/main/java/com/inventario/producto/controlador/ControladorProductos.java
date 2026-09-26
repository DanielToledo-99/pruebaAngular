package com.inventario.producto.controlador;

import com.inventario.producto.dto.SolicitudProducto;
import com.inventario.producto.dto.RespuestaProducto;
import com.inventario.producto.servicio.ServicioProductos;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Operaciones CRUD sobre el inventario de productos")
public class ControladorProductos {

    private final ServicioProductos servicioProductos;

    public ControladorProductos(ServicioProductos servicioProductos) {
        this.servicioProductos = servicioProductos;
    }

    @GetMapping
    @Operation(summary = "Lista los productos, opcionalmente filtrados por nombre")
    public List<RespuestaProducto> listar(@RequestParam(required = false) String nombre) {
        return servicioProductos.listar(nombre);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un producto por id")
    public RespuestaProducto buscarPorId(@PathVariable Long id) {
        return servicioProductos.buscarPorId(id);
    }

    @PostMapping
    @Operation(summary = "Crea un nuevo producto")
    public ResponseEntity<RespuestaProducto> crear(@Valid @RequestBody SolicitudProducto solicitud) {
        RespuestaProducto creado = servicioProductos.crear(solicitud);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un producto existente")
    public RespuestaProducto actualizar(@PathVariable Long id, @Valid @RequestBody SolicitudProducto solicitud) {
        return servicioProductos.actualizar(id, solicitud);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un producto")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicioProductos.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
