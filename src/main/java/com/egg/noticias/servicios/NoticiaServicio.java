package com.egg.noticias.servicios;

import com.egg.noticias.entidades.Noticia;
import com.egg.noticias.entidades.Usuario;
import com.egg.noticias.excepciones.CamposVaciosException;
import com.egg.noticias.excepciones.SinResultadosExcepcion;
import com.egg.noticias.repositorios.NoticiaRepositorio;
import com.egg.noticias.repositorios.UsuarioRepositorio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NoticiaServicio {

    @Autowired
    private NoticiaRepositorio noticiaRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public void create(Usuario usuario, String titulo, String cuerpo) throws CamposVaciosException, SinResultadosExcepcion {
        validar(titulo, cuerpo);
        Usuario usuarioDb = usuarioRepositorio.findById(usuario.getId())
            .orElseThrow(() -> SinResultadosExcepcion.of(usuario.getEmail()));

        Noticia noticia = new Noticia(usuarioDb, titulo, cuerpo);
        noticiaRepositorio.save(noticia);
    }
    
    @Transactional(readOnly = true)
    public List<Noticia> read() {
        List<Noticia> noticias;
        //.findAll(Sort.by(Sort.Direction.DESC, "id"));
        noticias = noticiaRepositorio.findByActivoIsTrue();
        return noticias;
    }
    
    @Transactional(readOnly = true)
    public List<Noticia> readByCreador(Usuario usuario) throws SinResultadosExcepcion{
        List<Noticia> noticias = noticiaRepositorio.findByCreadorAndActivoIsTrue(usuario);
        return noticias;
    }

    @Transactional(readOnly = true)
    public List<Noticia> readByUnactive() {
        List<Noticia> noticias = noticiaRepositorio.findByActivoIsFalse();
        return noticias;
    }

    @Transactional()
    public Noticia read(String id) throws SinResultadosExcepcion{
        Noticia noticia = noticiaRepositorio.findByIdAndActivoIsTrue(id)
            .orElseThrow(() -> SinResultadosExcepcion.of(id));
        return noticia;
    }
    
    @Transactional(readOnly = true)
    public List<Noticia> search(String titulo) {
        List<Noticia> noticias;
        noticias = noticiaRepositorio.findByTituloContainsAndActivoIsTrue(titulo);
        return noticias;
    }

    @Transactional
    public void update(String id, String titulo, String cuerpo) throws CamposVaciosException, SinResultadosExcepcion {
        validar(titulo, cuerpo);

        Noticia noticia = noticiaRepositorio.findByIdAndActivoIsTrue(id)
            .orElseThrow(() -> SinResultadosExcepcion.of(id));
         
        noticia.setTitulo(titulo);
        noticia.setCuerpo(cuerpo);
        noticiaRepositorio.save(noticia);
    }
    
    @Transactional
    public void delete(String id) {
        noticiaRepositorio.deleteById(id);
    }

    private void validar(String titulo, String cuerpo) throws CamposVaciosException{
        if(titulo.isEmpty() || titulo == null){
            throw new CamposVaciosException("El título de la Noticia no puede estar vacío.");
        }
        if(cuerpo.isEmpty() || cuerpo == null){
            throw new CamposVaciosException("El cuerpo de la Noticia no puede estar vacío.");
        }
    }

    @Transactional
    public void unactive(String id) {
        Noticia noticia = read(id);
        noticia.setActivo(false);
        noticiaRepositorio.save(noticia);
    }

    
}