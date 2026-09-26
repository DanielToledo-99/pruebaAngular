import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { URL_BASE_API } from '../configuracion/api.configuracion';
import { SolicitudInicioSesion, RespuestaInicioSesion } from '../modelos/autenticacion.modelo';

const CLAVE_TOKEN = 'token_autenticacion';

@Injectable({ providedIn: 'root' })
export class ServicioAutenticacion {
  constructor(private http: HttpClient) {}

  iniciarSesion(credenciales: SolicitudInicioSesion): Observable<RespuestaInicioSesion> {
    return this.http
      .post<RespuestaInicioSesion>(`${URL_BASE_API}/autenticacion/iniciar-sesion`, credenciales)
      .pipe(tap((respuesta) => localStorage.setItem(CLAVE_TOKEN, respuesta.token)));
  }

  cerrarSesion(): void {
    localStorage.removeItem(CLAVE_TOKEN);
  }

  obtenerToken(): string | null {
    return localStorage.getItem(CLAVE_TOKEN);
  }

  estaAutenticado(): boolean {
    return !!this.obtenerToken();
  }
}
