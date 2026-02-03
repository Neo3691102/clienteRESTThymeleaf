package com.nestorc.appwebThymeleaf.controller;

import com.nestorc.appwebThymeleaf.dto.ResponseWrapper;
import com.nestorc.appwebThymeleaf.dto.Usuario;
import com.nestorc.appwebThymeleaf.feign.FeignService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class IndexController {

    @Autowired
    FeignService feignApiClient;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Object index(HttpServletRequest request, HttpServletResponse response, Model model){
        log.info("Ingresando a página principal");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");

        ResponseWrapper<List<Usuario>> usersResponse = feignApiClient.getUsers();
        List<Usuario> usuarios = new ArrayList<>();
        if(usersResponse.isSuccess()){
            usuarios = usersResponse.getResponseEntity().getBody();
        }

        model.addAttribute("numUsuarios", usuarios.size());
        model.addAttribute("usuarios", usuarios);

        return modelAndView;
    }
}
