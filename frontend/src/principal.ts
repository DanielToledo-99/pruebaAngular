import { bootstrapApplication } from '@angular/platform-browser';
import { configuracionAplicacion } from './aplicacion/aplicacion.configuracion';
import { ComponenteAplicacion } from './aplicacion/aplicacion.componente';

bootstrapApplication(ComponenteAplicacion, configuracionAplicacion)
  .catch((err) => console.error(err));
