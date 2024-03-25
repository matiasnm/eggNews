package com.egg.noticias.excepciones;

public class SinResultadosExcepcion extends RuntimeException {

   private SinResultadosExcepcion(String dato) {
      super(String.format("Sin resultados para '" + dato + "'"));
   }

   public static SinResultadosExcepcion of(String dato) {
      return new SinResultadosExcepcion(dato);
   }
}
