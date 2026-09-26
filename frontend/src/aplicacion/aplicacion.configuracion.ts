import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideRouter } from '@angular/router';
import { providePrimeNG } from 'primeng/config';
import { MessageService } from 'primeng/api';
import Aura from '@primeng/themes/aura';

import { rutas } from './aplicacion.rutas';
import { interceptorAutenticacion } from './nucleo/interceptores/autenticacion.interceptor';

export const configuracionAplicacion: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(rutas),
    provideHttpClient(withInterceptors([interceptorAutenticacion])),
    provideAnimationsAsync(),
    providePrimeNG({ theme: { preset: Aura, options: { darkModeSelector: false } } }),
    MessageService
  ]
};
