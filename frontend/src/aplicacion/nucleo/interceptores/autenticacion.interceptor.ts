import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { ServicioAutenticacion } from '../servicios/autenticacion.servicio';

export const interceptorAutenticacion: HttpInterceptorFn = (solicitud, next) => {
  const servicioAutenticacion = inject(ServicioAutenticacion);
  const enrutador = inject(Router);

  const token = servicioAutenticacion.obtenerToken();
  const solicitudAutorizada = token
    ? solicitud.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : solicitud;

  return next(solicitudAutorizada).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        servicioAutenticacion.cerrarSesion();
        enrutador.navigate(['/iniciar-sesion']);
      }
      return throwError(() => error);
    })
  );
};
