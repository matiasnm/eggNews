package com.egg.noticias.entidades;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.egg.noticias.enumeraciones.Rol;

@Entity
@DiscriminatorValue("A")
public class Admin extends Usuario {
    
    public Admin() {}

    public Admin (String email, String password) {
        super(email, password);
        super.setRol(Rol.ADMIN);
    }
}