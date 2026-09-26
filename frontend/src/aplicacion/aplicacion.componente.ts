import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'aplicacion-raiz',
  standalone: true,
  imports: [RouterOutlet, ToastModule],
  templateUrl: './aplicacion.componente.html'
})
export class ComponenteAplicacion {}
