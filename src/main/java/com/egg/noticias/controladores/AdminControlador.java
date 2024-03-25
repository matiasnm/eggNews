package com.egg.noticias.controladores;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.egg.noticias.entidades.Noticia;
import com.egg.noticias.entidades.Periodista;
import com.egg.noticias.entidades.Usuario;
import com.egg.noticias.excepciones.CamposVaciosException;
import com.egg.noticias.servicios.NoticiaServicio;
import com.egg.noticias.servicios.UsuarioServicio;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

//Admin 12345
    @Autowired
    NoticiaServicio noticiaServicio;
    
    @Autowired
    UsuarioServicio usuarioServicio;

    @GetMapping("/dashboard")
    public String dashboard(ModelMap modelo) {
        List<Noticia> noticias = noticiaServicio.read();
        modelo.addAttribute("noticias", noticias);
        return "dashboard.html";
    }

        
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PERIODISTA')")
    @PostMapping("/crear")
    public String createPost(
        HttpSession session, 
        @RequestParam String tituloCrear, 
        @RequestParam String cuerpoCrear, 
        ModelMap modelo) {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            noticiaServicio.create(usuario, tituloCrear, cuerpoCrear);
            return "redirect:/";
        } 
        catch (CamposVaciosException ex) {
            modelo.put("alerta", ex.getMessage());
            modelo.put("crear", true);
            return dashboard(modelo);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PERIODISTA')")
    @GetMapping("/editar")
    public String updateGet(@RequestParam String idEditar, ModelMap modelo) {
        modelo.put("editar", noticiaServicio.read(idEditar));     
        return dashboard(modelo);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PERIODISTA')")
    @PostMapping("/editar")
    public String updatePost(@RequestParam String tituloEditar, @RequestParam String cuerpoEditar, @RequestParam String idEditar, ModelMap modelo) {
        try {
            noticiaServicio.update(idEditar, tituloEditar, cuerpoEditar);
            return "redirect:/";
        } catch (CamposVaciosException ex) {
            modelo.put("alerta", ex.getMessage());
            modelo.put("editar", noticiaServicio.read(idEditar));
            return dashboard(modelo);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/desactivar")
    public String unactive(@RequestParam String idDesactivar, ModelMap modelo) {
        noticiaServicio.unactive(idDesactivar);
        return "redirect:/admin/dashboard";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping("/borrar")
    public String delete(@RequestParam String idBorrar, ModelMap modelo) {
        noticiaServicio.delete(idBorrar);
        return "redirect:/admin/dashboard";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PERIODISTA')")
    @GetMapping("/misnoticias")
    public String misNoticiasGet(HttpSession session, ModelMap modelo) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        List<Noticia> noticias;
        if (usuario.getRol().toString().equals("ADMIN")) {
            noticias = noticiaServicio.readByCreador(usuario);
        }
        else {
            Periodista periodista = usuarioServicio.readPeriodista(usuario.getId());
            noticias = periodista.getMisNoticias();
        }
        modelo.addAttribute("noticias", noticias);
        return "dashboard.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/inactivas" )
    public String inactivasGet(ModelMap modelo) {
        List<Noticia> noticias = noticiaServicio.readByUnactive();
        modelo.addAttribute("noticias", noticias);
        return "dashboard.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/periodistas")
    public String periodistasGet(ModelMap modelo) {
        List<Periodista> periodistas = usuarioServicio.readPeriodistas();
        modelo.addAttribute("periodistas", periodistas);
        return "ver.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/periodistas/sueldo")
    public String periodistasSueldoPost(ModelMap modelo, String id, Float sueldo) {
        Periodista periodista = usuarioServicio.readPeriodista(id);

        if (sueldo.isNaN() || sueldo == null) {
            sueldo = Float.valueOf("0.0");
        }
        periodista.setSueldo(sueldo);
        usuarioServicio.update(periodista);
        return "redirect:/admin/periodistas";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/periodistas/desactivar")
    public String periodistasActive(ModelMap modelo, String id) {
        usuarioServicio.unactive(id);
        return "redirect:/admin/periodistas";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/periodistas/activar")
    public String periodistasUnactive(ModelMap modelo, String id) {
        usuarioServicio.active(id);
        return "redirect:/admin/periodistas";
    }
    
}