import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DialogModule } from 'primeng/dialog';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { InputTextModule } from 'primeng/inputtext';
import { TableModule } from 'primeng/table';
import { Router } from '@angular/router';
import { Subject, debounceTime, distinctUntilChanged, switchMap, takeUntil } from 'rxjs';
import { ServicioAutenticacion } from '../../../nucleo/servicios/autenticacion.servicio';
import { Producto } from '../../../nucleo/modelos/producto.modelo';
import { ServicioProductos } from '../../../nucleo/servicios/productos.servicio';
import { ComponenteFormularioProducto } from '../formulario-producto/formulario-producto.componente';

@Component({
  selector: 'aplicacion-lista-productos',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    IconFieldModule,
    InputIconModule,
    DialogModule,
    ConfirmDialogModule,
    ComponenteFormularioProducto
  ],
  providers: [ConfirmationService],
  templateUrl: './lista-productos.componente.html',
  styleUrl: './lista-productos.componente.scss'
})
export class ComponenteListaProductos implements OnInit, OnDestroy {
  productos: Producto[] = [];
  cargando = false;
  guardando = false;
  terminoBusqueda = '';

  dialogoVisible = false;
  productoEnEdicion: Producto | null = null;

  private busqueda$ = new Subject<string>();
  private destruccion$ = new Subject<void>();

  constructor(
    private servicioProductos: ServicioProductos,
    private servicioAutenticacion: ServicioAutenticacion,
    private servicioConfirmacion: ConfirmationService,
    private servicioMensajes: MessageService,
    private enrutador: Router
  ) {}

  ngOnInit(): void {
    this.busqueda$
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        switchMap((termino) => this.servicioProductos.listar(termino || undefined)),
        takeUntil(this.destruccion$)
      )
      .subscribe({
        next: (productos) => (this.productos = productos),
        error: () => this.servicioMensajes.add({ severity: 'error', summary: 'Error', detail: 'No se pudo cargar los productos' })
      });

    this.cargarProductos();
  }

  ngOnDestroy(): void {
    this.destruccion$.next();
    this.destruccion$.complete();
  }

  cargarProductos(): void {
    this.cargando = true;
    this.servicioProductos.listar(this.terminoBusqueda || undefined).subscribe({
      next: (productos) => {
        this.productos = productos;
        this.cargando = false;
      },
      error: () => {
        this.cargando = false;
        this.servicioMensajes.add({ severity: 'error', summary: 'Error', detail: 'No se pudo cargar los productos' });
      }
    });
  }

  alCambiarBusqueda(termino: string): void {
    this.terminoBusqueda = termino;
    this.busqueda$.next(termino);
  }

  abrirDialogoCreacion(): void {
    this.productoEnEdicion = null;
    this.dialogoVisible = true;
  }

  abrirDialogoEdicion(producto: Producto): void {
    this.productoEnEdicion = { ...producto };
    this.dialogoVisible = true;
  }

  cerrarDialogo(): void {
    this.dialogoVisible = false;
    this.productoEnEdicion = null;
  }

  guardarProducto(producto: Producto): void {
    this.guardando = true;
    const solicitud$ = producto.id
      ? this.servicioProductos.actualizar(producto.id, producto)
      : this.servicioProductos.crear(producto);

    solicitud$.subscribe({
      next: () => {
        this.guardando = false;
        this.cerrarDialogo();
        this.servicioMensajes.add({
          severity: 'success',
          summary: 'Éxito',
          detail: producto.id ? 'Producto actualizado' : 'Producto agregado'
        });
        this.cargarProductos();
      },
      error: () => {
        this.guardando = false;
        this.servicioMensajes.add({ severity: 'error', summary: 'Error', detail: 'No se pudo guardar el producto' });
      }
    });
  }

  confirmarEliminacion(producto: Producto): void {
    this.servicioConfirmacion.confirm({
      message: `¿Seguro que deseas eliminar "${producto.nombre}"?`,
      header: 'Confirmar eliminación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Eliminar',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => this.eliminarProducto(producto)
    });
  }

  private eliminarProducto(producto: Producto): void {
    if (!producto.id) {
      return;
    }
    this.servicioProductos.eliminar(producto.id).subscribe({
      next: () => {
        this.servicioMensajes.add({ severity: 'success', summary: 'Éxito', detail: 'Producto eliminado' });
        this.cargarProductos();
      },
      error: () => this.servicioMensajes.add({ severity: 'error', summary: 'Error', detail: 'No se pudo eliminar el producto' })
    });
  }

  cerrarSesion(): void {
    this.servicioAutenticacion.cerrarSesion();
    this.enrutador.navigate(['/iniciar-sesion']);
  }
}
