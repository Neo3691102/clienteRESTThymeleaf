package com.nestorc.appwebThymeleaf.feign;

import com.nestorc.appwebThymeleaf.dto.ResponseWrapper;
import com.nestorc.appwebThymeleaf.dto.Telefono;
import com.nestorc.appwebThymeleaf.dto.Usuario;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
class FeignServiceTest {

    public static FeignService feignService;

    @BeforeAll
    static void setUp() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("com.nestorc.appwebThymeleaf.feign");
        context.refresh();

        FeignApiClient feignApiClient = (FeignApiClient) context.getBean("feignApiClient");
        String apiUrl = (String) context.getBean("apiUrl");

        feignService = new FeignService(feignApiClient, apiUrl);
    }

    @Test
    public void testGetUsers() {
        feignService.getUsers().getResponseEntity().getBody().forEach(System.out::println);
    }

    @Test
    public void testGetUserById() throws Exception {
        Usuario usuario = feignService.getUserById(9).getResponseEntity().getBody();
        log.info("Usuario: {}", usuario);
    }

    @Test
    public void testCreateUser() throws Exception {
        Usuario usuario = Usuario.builder()
                .nombre("Esteban3")
                .edad(28)
                .build();
        ResponseWrapper<Usuario> responseWrapper = feignService.createUser(usuario);
        log.info("Usuario creado: {}", responseWrapper.getResponseEntity().getBody());
    }

    @Test
    public void testUpdateUserById() throws Exception {
        Usuario usuario = Usuario.builder()
                .nombre("Casimiro")
                .edad(20)
                .build();
        Usuario usuarioActualizado = feignService.updateUser(20, usuario).getResponseEntity().getBody();
        log.info("Usuario actualizado: {}", usuarioActualizado);
    }

    @Test
    public void testDeleteUserById() throws Exception {
        int id = 22;
        feignService.deleteUser(id);
        log.info("Usuario con id {} fue eliminado", id);
    }

    //telefonos
    @Test
    public void testGetPhones() {

        feignService.getPhones().getResponseEntity().getBody().forEach(System.out::println);
    }

    @Test
    public void testGetPhoneById() throws Exception {
        Telefono telefono = feignService.getPhoneById(9).getResponseEntity().getBody();
        log.info("Telefono: {}", telefono);
    }

    @Test
    public void testCreatePhone() throws Exception {
        Telefono telefono = Telefono.builder()

                .tipoTelefono("Celular")
                .lada(999)
                .numero("5547789")
                .build();
        ResponseWrapper<Telefono> responseWrapper = feignService.crearPhone(telefono);
        log.info("Telefono creado: {}", responseWrapper.getResponseEntity().getBody());
    }

    @Test
    public void testUpdatePhoneById() throws Exception {
        Telefono telefono = Telefono.builder()
                .tipoTelefono("Casa")
                .lada(336)
                .numero("1234565")
                .build();
        Telefono telefonoactualizado = feignService.actualizarPhoneXId(4, telefono).getResponseEntity().getBody();
        log.info("Telefono actualizado: {}", telefonoactualizado);
    }

    @Test
    public void testDeletePhonerById() throws Exception {
        int id = 22;
        feignService.deleteUser(id);
        log.info("Telefono con id {} fue eliminado", id);
    }
}