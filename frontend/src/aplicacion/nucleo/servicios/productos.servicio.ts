import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { URL_BASE_API } from '../configuracion/api.configuracion';
import { Producto } from '../modelos/producto.modelo';

@Injectable({ providedIn: 'root' })
export class ServicioProductos {
  private readonly urlBase = `${URL_BASE_API}/productos`;

  constructor(private http: HttpClient) {}

  listar(nombre?: string): Observable<Producto[]> {
    const opciones = nombre ? { params: { nombre } } : {};
    return this.http.get<Producto[]>(this.urlBase, opciones);
  }

  crear(producto: Producto): Observable<Producto> {
    return this.http.post<Producto>(this.urlBase, producto);
  }

  actualizar(id: number, producto: Producto): Observable<Producto> {
    return this.http.put<Producto>(`${this.urlBase}/${id}`, producto);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.urlBase}/${id}`);
  }
}
