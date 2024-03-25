package com.egg.noticias.excepciones;

public class EmailRegistradoException extends RuntimeException {

    private EmailRegistradoException(String email) {
        super(String.format("El email '" + email +"' ya se encuentra registrado."));
     }
  
     public static EmailRegistradoException of(String email) {
        return new EmailRegistradoException(email);
     }
}
