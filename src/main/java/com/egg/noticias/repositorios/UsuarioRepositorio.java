package com.egg.noticias.repositorios;

import com.egg.noticias.entidades.Periodista;
import com.egg.noticias.entidades.Usuario;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {

    public Optional<Usuario> findByEmail(String email);
    
    @Query("from Periodista")
    List<Periodista> findPeriodistas();

}