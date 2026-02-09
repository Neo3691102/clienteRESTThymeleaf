package com.nestorc.appwebThymeleaf.controller;

import com.nestorc.appwebThymeleaf.dto.Login;
import com.nestorc.appwebThymeleaf.dto.ResponseWrapper;
import com.nestorc.appwebThymeleaf.feign.FeignService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class LoginController {

    @Autowired
    FeignService feignService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object login(HttpServletRequest request, HttpServletResponse response) throws IOException {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("pagina-login");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            ResponseWrapper<List<Login>> usuariosBD = feignService.getAuthUsers();
            List<Login> listaUsuarios = usuariosBD.getResponseEntity().getBody();

            if (listaUsuarios == null || listaUsuarios.isEmpty()) {
                modelAndView.addObject("error", "No hay usuarios registrados");
                return modelAndView;
            }

            boolean autenticado = listaUsuarios.stream()
                    .anyMatch(user ->
                            user.getUsuarioBD().equals(username) &&
                                    user.getPasswordBD().equals(password)
                    );

            if (autenticado) {
                request.getSession().setAttribute("username", username);
                response.sendRedirect("/");
                return null;
            } else {
                modelAndView.addObject("error", "Usuario o contraseña incorrectos");
                return modelAndView;
            }

        } catch (Exception e) {
            modelAndView.addObject("error", "Error al conectar con el servicio de autenticación");
            return modelAndView;
        }
    }



    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public Object logout(HttpServletRequest request, HttpServletResponse response){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("pagina-login");

        request.getSession().setAttribute("username", null);

        return modelAndView;
    }
}
