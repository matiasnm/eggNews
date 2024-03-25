package com.egg.noticias.entidades;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.egg.noticias.enumeraciones.Rol;

@Entity
@DiscriminatorValue("P")
public class Periodista extends Usuario {

    @OneToMany(mappedBy = "creador")
    private List<Noticia> misNoticias;

    private Float sueldo;
    
    private Boolean activo;

    public Periodista() {}
    
    public Periodista(String email, String password) {
        super(email, password);
        super.setRol(Rol.PERIODISTA);
        this.activo = true;
        this.sueldo = Float.valueOf(0);
    }

    public List<Noticia> getMisNoticias() {
        return misNoticias;
    }

    public Float getSueldo() {
        return sueldo;
    }

    public void setMisNoticias(List<Noticia> misNoticias) {
        this.misNoticias = misNoticias;
    }

    public void setSueldo(Float sueldo) {
        this.sueldo = sueldo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return super.toString() + "Periodista [sueldo=" + sueldo + ", activo=" + activo + "]";
    }

}