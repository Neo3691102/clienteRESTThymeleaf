package com.nestorc.appwebThymeleaf.controller;

import com.nestorc.appwebThymeleaf.dto.ResponseWrapper;
import com.nestorc.appwebThymeleaf.dto.Telefono;
import com.nestorc.appwebThymeleaf.dto.Usuario;
import com.nestorc.appwebThymeleaf.feign.FeignService;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
public class UsuarioController {

    @Autowired
    FeignService feignUserService;

    @RequestMapping(value = "/usuario", method = RequestMethod.GET)
    public Object informacionUsuario(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String idUsuario = request.getParameter("idUsuario");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("usuario");
        Usuario usuario = Usuario.crearUsuarioVacio();

        if (!Objects.isNull(idUsuario) && !idUsuario.isEmpty()) {
            ResponseWrapper<Usuario> usuarioResponse = feignUserService.getUserById(Integer.parseInt(idUsuario));
            if (usuarioResponse.isSuccess()) {
                usuario = usuarioResponse.getResponseEntity().getBody();
            }
        }

        model.addAttribute("usuario", usuario);
        return modelAndView;
    }

    @RequestMapping(value = "/formulario-usuario", method = RequestMethod.GET)
    public Object formularioUsuario(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String idUsuario = request.getParameter("idUsuario");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("formulario-usuario");

        Usuario usuario = Usuario.crearUsuarioVacio();
        Telefono telefono = usuario.getTelefonos().get(0);
        model.addAttribute("propositoFormulario", "Crear usuario");

        if (!Objects.isNull(idUsuario) && !idUsuario.isEmpty()) {
            ResponseWrapper<Usuario> usuarioResponse = feignUserService.getUserById(Integer.parseInt(idUsuario));
            if (usuarioResponse.isSuccess()) {
                usuario = usuarioResponse.getResponseEntity().getBody();
                // Solo extraemos el primer telefono (En caso de tener)
                // Actividad sugerida: Generar funcionalidad para retornar todos los telefonos en caso de tener mas de 1 y generar el formulario correspondiente
                if (usuario.getTelefonos().size() > 0) {
                    telefono = usuario.getTelefonos().get(0);
                }
                model.addAttribute("propositoFormulario", "Actualizar usuario");
            }
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("telefono", telefono);
        return modelAndView;
    }

    @RequestMapping(value = "/guardar-usuario", method = RequestMethod.POST)
    public ResponseEntity<Response> saveUserConfiguration(HttpServletRequest request) {
        log.info("Guardando información de Walmart para usuario {}", request.getParameter("GPSServerUser"));
        HttpStatus statusCode = HttpStatus.OK;
        Response response = null;


        try {
            String usuarioId = request.getParameter("FormUsuarioId");
            String usuarioNombre = request.getParameter("FormUsuarioNombre");
            String usuarioEdad = request.getParameter("FormUsuarioEdad");
            String telefonoTipo = request.getParameter("FormTelefonoTipo");
            String telefonoLada = request.getParameter("FormTelefonoLada");
            String telefonoNumero = request.getParameter("FormTelefonoNumero");

            // Validar informacion recibida

            ErrorResponse errorResponse = new ErrorResponse();
            boolean hasErrors = false;

            // Validar ID (si viene distinto de 0 debe ser numérico)
            if (usuarioId == null || usuarioId.isBlank()) {
                errorResponse.addMessage("El id del usuario es obligatorio");
                hasErrors = true;
            } else {
                try {
                    Integer.parseInt(usuarioId);
                } catch (NumberFormatException e) {
                    errorResponse.addMessage("El id del usuario debe ser numérico");
                    hasErrors = true;
                }
            }

            // Validar nombre
            if (usuarioNombre == null || usuarioNombre.isBlank()) {
                errorResponse.addMessage("El nombre del usuario es obligatorio");
                hasErrors = true;
            } else if (usuarioNombre.length() < 3) {
                errorResponse.addMessage("El nombre debe tener al menos 3 caracteres");
                hasErrors = true;
            }

            // Validar edad
            if (usuarioEdad == null || usuarioEdad.isBlank()) {
                errorResponse.addMessage("La edad es obligatoria");
                hasErrors = true;
            } else {
                try {
                    int edad = Integer.parseInt(usuarioEdad);
                    if (edad <= 0 || edad > 120) {
                        errorResponse.addMessage("La edad debe estar entre 1 y 120 años");
                        hasErrors = true;
                    }
                } catch (NumberFormatException e) {
                    errorResponse.addMessage("La edad debe ser numérica");
                    hasErrors = true;
                }
            }

            // Validar tipo de teléfono
            if (telefonoTipo == null || telefonoTipo.isBlank()) {
                errorResponse.addMessage("El tipo de teléfono es obligatorio");
                hasErrors = true;
            }

            // Validar lada
            if (telefonoLada == null || telefonoLada.isBlank()) {
                errorResponse.addMessage("La lada es obligatoria");
                hasErrors = true;
            } else {
                try {
                    Integer.parseInt(telefonoLada);
                } catch (NumberFormatException e) {
                    errorResponse.addMessage("La lada debe ser numérica");
                    hasErrors = true;
                }
            }

            // Validar número
            if (telefonoNumero == null || telefonoNumero.isBlank()) {
                errorResponse.addMessage("El número telefónico es obligatorio");
                hasErrors = true;
            } else if (!telefonoNumero.matches("\\d{7,15}")) {
                errorResponse.addMessage("El número telefónico debe contener solo dígitos (7 a 15 caracteres)");
                hasErrors = true;
            }

            // Si hay errores, detener ejecución
            if (hasErrors) {
                response = errorResponse;
                statusCode = HttpStatus.BAD_REQUEST;
                return new ResponseEntity<>(response, statusCode);
            }


            // Si el usuarioId tiene informacion se trata de una actualizacion por lo tanto seteamos el id recibido
            Usuario.UsuarioBuilder usuarioBuilder = Usuario.builder();
            if (!usuarioId.equals("0")) {
                usuarioBuilder = usuarioBuilder.idUsuario(Integer.parseInt(usuarioId));
            }

            // En caso de haber agregado la funcionalidad para editar mas de 1 telefono, esta seccion se debera adecuar
            // para recibir todos los telefonos y realizar el guardado/actualizacion

            //Generamos el telefono recibido
            Telefono telefono = Telefono.builder()
                    .tipoTelefono(telefonoTipo)
                    .lada(Integer.parseInt(telefonoLada))
                    .numero(telefonoNumero)
                    .build();
            //Generamos el usuario
            Usuario usuario = usuarioBuilder
                    .nombre(usuarioNombre)
                    .edad(Integer.parseInt(usuarioEdad))
                    .telefonos(List.of(telefono))
                    .build();

            ResponseWrapper<Usuario> user = feignUserService.createUser(usuario);
            if (!user.isSuccess()) {


                errorResponse.addMessage(user.getMessage());
                response = errorResponse;

                statusCode = HttpStatus.resolve(user.getResponseEntity().getStatusCodeValue());
            }
        } catch(Exception e) {
            statusCode = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(response, statusCode);
    }
}