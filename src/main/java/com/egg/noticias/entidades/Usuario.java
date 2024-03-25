package com.egg.noticias.entidades;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.egg.noticias.enumeraciones.Rol;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("U")
public class Usuario {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @Temporal(TemporalType.DATE)
    @NotNull
    private Date alta;

    private String email;
    private String password;

    public Usuario() {}
    
    public Usuario(String email, String password, Rol rol) {
        this.password = password;
        this.email = email;
        this.rol = rol;
        this.alta = Date.from(Instant.now());
    }

    public Usuario(String email, String password) {
        this.password = password;
        this.email = email;
        this.rol = Rol.USER;
        this.alta = Date.from(Instant.now());
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Rol getRol() {
        return rol;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");   
        return "Usuario [id=" + id + "alta=" + dateFormat.format(alta) + ", password=" + password + ", email=" + email + ", rol="
                + rol + "]";
    }
    
    
}
