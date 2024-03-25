package com.egg.noticias.servicios;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.egg.noticias.entidades.Periodista;
import com.egg.noticias.entidades.Usuario;
import com.egg.noticias.enumeraciones.Rol;
import com.egg.noticias.excepciones.CamposVaciosException;
import com.egg.noticias.excepciones.EmailRegistradoException;
import com.egg.noticias.excepciones.SinResultadosExcepcion;
import com.egg.noticias.repositorios.UsuarioRepositorio;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio uRepositorio;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws SinResultadosExcepcion {
        
        Usuario usuario = uRepositorio.findByEmail(email)
            .orElseThrow(() -> SinResultadosExcepcion.of(email));

        List<GrantedAuthority> permisos = new ArrayList<>();
        GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
        permisos.add(p);

        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attrs.getRequest().getSession(true);
        sesion.setAttribute("usuario", usuario);
    
        return new User(usuario.getEmail(), usuario.getPassword(), permisos);
    }


    @Transactional
    public void create(String email, String password1, String password2) throws CamposVaciosException, EmailRegistradoException {
        validar(email, password1, password2);
        String password = new BCryptPasswordEncoder().encode(password1);
        Usuario usuario = new Usuario(email, password, Rol.USER);
        uRepositorio.save(usuario);
    }
    
    @Transactional
    public List<Periodista> readPeriodistas() {
        List<Periodista> periodistas = uRepositorio.findPeriodistas();
        return periodistas;
    }

    @Transactional
    public Periodista readPeriodista(String id) {
        Periodista periodista = (Periodista) uRepositorio.findById(id)
            .orElseThrow(() -> SinResultadosExcepcion.of(id));
        return periodista;
    }

    @Transactional
    public void update(Periodista periodista) {
        uRepositorio.save(periodista);
    }

    @Transactional
    public void unactive(String id) {
        Periodista periodista = readPeriodista(id);
        periodista.setActivo(false);
        uRepositorio.save(periodista);
    }

    @Transactional
    public void active(String id) {
        Periodista periodista = readPeriodista(id);
        periodista.setActivo(true);
        uRepositorio.save(periodista);
    }

    private void validar(String email, String password1, String password2) throws CamposVaciosException {
        if (email.isEmpty() || email == null) {
            throw new CamposVaciosException("El campo email no puede estar vacío.");
        }
        if (password1.isEmpty() || password1 == null) {
            throw new CamposVaciosException("El campo password no puede estar vacío.");
        }
        if (password1.length() < 5) {
            throw new CamposVaciosException("El campo password debe tener al menos 5 caracteres.");
        }
        if (!password1.equals(password2)) {
            throw new CamposVaciosException("Las contraseñas no coinciden.");
        }
        if (uRepositorio.findByEmail(email).isPresent()) {
            throw EmailRegistradoException.of(email);
        }
    }

}