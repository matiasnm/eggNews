package com.egg.noticias.controladores;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.egg.noticias.entidades.Noticia;
import com.egg.noticias.entidades.Usuario;
import com.egg.noticias.excepciones.CamposVaciosException;
import com.egg.noticias.excepciones.EmailRegistradoException;
import com.egg.noticias.servicios.NoticiaServicio;
import com.egg.noticias.servicios.UsuarioServicio;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    UsuarioServicio usuarioServicio;

    @Autowired
    NoticiaServicio noticiaServicio;

    @GetMapping("/")
    public String home(HttpSession session, ModelMap modelo) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        List<Noticia> noticias = noticiaServicio.read();
        modelo.addAttribute("noticias", noticias);
        if (usuario != null) {
            if (usuario.getRol().toString().equals("ADMIN")) {
                return "redirect:/admin/dashboard";
            }
            else {
                return "home.html";
            }
        }
        return "login.html";
    }

    @GetMapping("/registrar")
    public String registrarGet() {
        return "register.html";
    }

    @PostMapping("/registrar")
    public String registrarPost(
        HttpSession session,
        @RequestParam String inputEmail,
        @RequestParam String inputPassword1,
        @RequestParam String inputPassword2,
        ModelMap modelo) {
        try {
            usuarioServicio.create(inputEmail, inputPassword1, inputPassword2);
            modelo.put("msg", "Usuario registrado.");
            return loginGet(null, modelo);
        }
        catch(EmailRegistradoException ex) {
            modelo.put("error", "Error al crear usuario: " + ex.getMessage());
            return "register.html";            
        }
        catch(CamposVaciosException ex) {
            modelo.put("error", "Error al crear usuario: " + ex.getMessage());
            return "register.html";
        }
    }

    @GetMapping("/login")
    public String loginGet(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error","Usuario o contraseña inválidos.");
        }
        return "login.html";
    }

    @PostMapping("/buscar")
    public String searchPost(@RequestParam String tituloBuscar, ModelMap modelo) {
        List<Noticia> noticias = noticiaServicio.search(tituloBuscar);
        modelo.addAttribute("noticias", noticias);
        return "home.html";
    }

}