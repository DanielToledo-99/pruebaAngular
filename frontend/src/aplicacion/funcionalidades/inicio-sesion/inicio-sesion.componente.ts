import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { finalize } from 'rxjs';
import { ServicioAutenticacion } from '../../nucleo/servicios/autenticacion.servicio';

@Component({
  selector: 'aplicacion-inicio-sesion',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ButtonModule, InputTextModule, PasswordModule],
  templateUrl: './inicio-sesion.componente.html',
  styleUrl: './inicio-sesion.componente.scss'
})
export class ComponenteInicioSesion {
  private constructorFormulario = inject(FormBuilder);
  private servicioAutenticacion = inject(ServicioAutenticacion);
  private enrutador = inject(Router);

  formulario = this.constructorFormulario.group({
    usuario: ['', Validators.required],
    contrasena: ['', Validators.required]
  });

  cargando = false;
  errorInicioSesion = false;

  enviar(): void {
    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }

    this.errorInicioSesion = false;
    this.cargando = true;

    this.servicioAutenticacion
      .iniciarSesion(this.formulario.getRawValue() as { usuario: string; contrasena: string })
      .pipe(finalize(() => (this.cargando = false)))
      .subscribe({
        next: () => this.enrutador.navigate(['/productos']),
        error: () => (this.errorInicioSesion = true)
      });
  }
}
