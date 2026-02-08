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

        //traer el usuario y contraseña con feign y luego evaluarlo
        Login usuarioAuth = new Login();
        ResponseWrapper<List<Login>> usuariosBD = feignService.getAuthUsers();
        usuariosBD.getResponseEntity().getBody().forEach(user ->{
            if(user.getUsuarioBD() == username && user.getPasswordBD() == password ){
                usuarioAuth.setUsuarioBD(username);
                usuarioAuth.setPasswordBD(password);
            }else{
                try {
                    throw new Exception("Hubo un error en la autenticacion");
                } catch (Exception e) {
                    throw new RuntimeException("Error en la autenticacion");
                }
            }
        });
        request.getSession().setAttribute("username", usuarioAuth.getUsuarioBD());
        response.sendRedirect("/");


        //Validar el usuario y contraseña contra los de una tabla en base de datos
//        if(username.equals("nestor") && password.equals("333")){
//            request.getSession().setAttribute("username", username);
//            response.sendRedirect("/");
//        }
        //String usuario = feignService crear metodos en el servicio REST
        return modelAndView;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public Object logout(HttpServletRequest request, HttpServletResponse response){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("pagina-login");

        request.getSession().setAttribute("username", null);

        return modelAndView;
    }
}
