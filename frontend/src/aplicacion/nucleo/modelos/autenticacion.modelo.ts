export interface SolicitudInicioSesion {
  usuario: string;
  contrasena: string;
}

export interface RespuestaInicioSesion {
  token: string;
  tipoToken: string;
}
