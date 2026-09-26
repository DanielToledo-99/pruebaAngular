import { Routes } from '@angular/router';
import { guardiaAutenticacion } from './nucleo/guardias/autenticacion.guardia';
import { ComponenteInicioSesion } from './funcionalidades/inicio-sesion/inicio-sesion.componente';
import { ComponenteListaProductos } from './funcionalidades/productos/lista-productos/lista-productos.componente';

export const rutas: Routes = [
  { path: 'iniciar-sesion', component: ComponenteInicioSesion },
  { path: 'productos', component: ComponenteListaProductos, canActivate: [guardiaAutenticacion] },
  { path: '', redirectTo: 'productos', pathMatch: 'full' },
  { path: '**', redirectTo: 'productos' }
];
