package com.egg.noticias.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;


@Entity
public class Noticia implements Serializable {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    private String titulo;
    
    @Column(columnDefinition = "MEDIUMTEXT")
    private String cuerpo;

    @ManyToOne()
    @JoinColumn(name = "periodista_id")
    private Usuario creador;

    private Boolean activo = true;

    public Noticia() {
    }

    public Noticia(Usuario creador, String titulo, String cuerpo) {
        this.creador = creador;
        this.titulo = titulo;
        this.cuerpo = cuerpo;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Usuario getCreador() {
        return creador;
    }
    
}