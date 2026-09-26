import { CommonModule } from '@angular/common';
import { Component, EventEmitter, inject, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputNumberModule } from 'primeng/inputnumber';
import { InputTextModule } from 'primeng/inputtext';
import { InputTextarea } from 'primeng/inputtextarea';
import { Producto } from '../../../nucleo/modelos/producto.modelo';

@Component({
  selector: 'aplicacion-formulario-producto',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ButtonModule, InputTextModule, InputTextarea, InputNumberModule],
  templateUrl: './formulario-producto.componente.html',
  styleUrl: './formulario-producto.componente.scss'
})
export class ComponenteFormularioProducto implements OnChanges {
  private constructorFormulario = inject(FormBuilder);

  @Input() producto: Producto | null = null;
  @Input() guardando = false;
  @Output() guardar = new EventEmitter<Producto>();
  @Output() cancelar = new EventEmitter<void>();

  formulario = this.constructorFormulario.group({
    nombre: ['', [Validators.required, Validators.maxLength(150)]],
    descripcion: ['', Validators.maxLength(1000)],
    cantidad: [null as number | null, [Validators.required, Validators.min(0)]],
    precio: [null as number | null, [Validators.required, Validators.min(0.01)]]
  });

  ngOnChanges(cambios: SimpleChanges): void {
    if (cambios['producto']) {
      this.formulario.reset({
        nombre: this.producto?.nombre ?? '',
        descripcion: this.producto?.descripcion ?? '',
        cantidad: this.producto?.cantidad ?? null,
        precio: this.producto?.precio ?? null
      });
    }
  }

  enviar(): void {
    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }

    const valor = this.formulario.getRawValue();
    this.guardar.emit({
      ...(this.producto?.id ? { id: this.producto.id } : {}),
      nombre: valor.nombre!,
      descripcion: valor.descripcion ?? '',
      cantidad: valor.cantidad!,
      precio: valor.precio!
    });
  }
}
