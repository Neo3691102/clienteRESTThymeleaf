package com.nestorc.appwebThymeleaf.feign;

import com.nestorc.appwebThymeleaf.dto.Login;
import com.nestorc.appwebThymeleaf.dto.ResponseWrapper;
import com.nestorc.appwebThymeleaf.dto.Telefono;
import com.nestorc.appwebThymeleaf.dto.Usuario;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

public interface FeignApiClient {

    //Usuarios
    @RequestLine("GET /usuarios")
    ResponseWrapper<List<Usuario>> getUsers();

    @RequestLine("GET /usuarios/{id}")
    ResponseWrapper<Usuario> getUserById(@Param("id") int id);

    @RequestLine("POST /usuarios")
    @Headers({"Content-Type: application/json"})
    ResponseWrapper<Usuario> createUser(Usuario usuario);

    @RequestLine("PUT /usuarios/{id}")
    @Headers({"Content-Type: application/json"})
    ResponseWrapper<Usuario> updateUser(Usuario usuario, @Param("id") int id);

    @RequestLine("DELETE /usuarios/{id}")
    void deleteUser(@Param("id") int id);

    //Telefonos
    @RequestLine("GET /telefonos")
    ResponseWrapper<List<Telefono>> getPhones();

    @RequestLine("GET /telefonos/{id}")
    ResponseWrapper<Telefono> getPhoneById(@Param("id") int id);

    @RequestLine("POST /telefonos")
    @Headers({"Content-Type: application/json"})
    ResponseWrapper<Telefono> createPhone(Telefono telefono);

    @RequestLine("PUT /telefonos/{id}")
    @Headers({"Content-Type: application/json"})
    ResponseWrapper<Telefono> updatePhone(Telefono telefono, @Param("id") int id);

    @RequestLine("DELETE /telefonos/{id}")
    void deletePhone(@Param("id") int id);

    //login
    @RequestLine("GET /autenticacion")
    ResponseWrapper<List<Login>> authUsers();

    @RequestLine("GET /autenticacion/{id}")
    ResponseWrapper<Login> authUserById(@Param("id") int id);
}
