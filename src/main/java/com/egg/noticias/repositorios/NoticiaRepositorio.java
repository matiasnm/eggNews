package com.egg.noticias.repositorios;

import com.egg.noticias.entidades.Noticia;
import com.egg.noticias.entidades.Usuario;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface NoticiaRepositorio extends JpaRepository<Noticia, String> {

    public List<Noticia> findByTituloContainsAndActivoIsTrue(String titulo);
    
    public List<Noticia> findByActivoIsTrue();

    public List<Noticia> findByActivoIsFalse();
    
    public List<Noticia> findByCreadorAndActivoIsTrue(Usuario creador);

    public Optional<Noticia> findByIdAndActivoIsTrue(String id);
}