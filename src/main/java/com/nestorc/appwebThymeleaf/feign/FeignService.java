package com.nestorc.appwebThymeleaf.feign;

import com.nestorc.appwebThymeleaf.dto.ResponseWrapper;
import com.nestorc.appwebThymeleaf.dto.Telefono;
import com.nestorc.appwebThymeleaf.dto.Usuario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class FeignService {

    public final FeignApiClient feignApiClient;
    public final String apiUrl;

    @Autowired
    public FeignService(FeignApiClient FeignApiClient, String apiUrl) {
        this.feignApiClient = FeignApiClient;
        this.apiUrl = apiUrl;
    }

    public ResponseWrapper<List<Usuario>> getUsers() {
        log.info("Obteniendo usuarios desde la api {}", apiUrl);
        return feignApiClient.getUsers();
    }

    public ResponseWrapper<Usuario> getUserById(int id) throws Exception {
        if(id <= 0){
            throw new Exception("El id debe ser mayor a cero");
        }

        log.info("Obteniendo el usuario con el id#{}", id);
        ResponseWrapper<Usuario> usuario = feignApiClient.getUserById(id);

        if(usuario.getResponseEntity().getBody() == null){
            log.warn("Usuario con el id {} no fue encontrado", id);
            throw new Exception("Usuario no encontrado");
        }

        return usuario;

    }


    public ResponseWrapper<Usuario> createUser(Usuario user) throws Exception {
        if(user == null){
            log.warn("Introduce un usuario válido");
            throw new Exception("Introduce un usuario valido");
        }
        log.info("Creando usuario {}", user);
        return feignApiClient.createUser(user);
    }

    public ResponseWrapper<Usuario> updateUser(int id, Usuario user) throws Exception {
        if(id <= 0){
            log.warn("El id debe ser mayor a cero");
            throw new Exception("El id debe ser mayor a cero");
        }
        if(user == null){
            log.warn("Porfavor introduce un suario válido");
            throw new Exception("Usuario no puede ir vacío");
        }
        log.info("Actualizando usuario {}", user.getNombre());
        return feignApiClient.updateUser(user, id);
    }

    public void deleteUser(int id) throws Exception {
        if(id <= 0){
            log.warn("El id debe ser mayor a cero");
            throw new Exception("Id invalido");
        }
        log.info("Eliminando usuario con id {}", id);
        feignApiClient.deleteUser(id);
    }

    public ResponseWrapper<List<Telefono>> getPhones(){
        return feignApiClient.getPhones();
    }

    public ResponseWrapper<Telefono> getPhoneById(int id) throws Exception {
        if(id <= 0){
            throw new Exception("El id debe ser mayor a cero");
        }

        log.info("Obteniendo telefono con el id#{}", id);

        ResponseWrapper<Telefono> telefono = feignApiClient.getPhoneById(id);

        if(telefono.getResponseEntity().getBody() == null){
            log.warn("Telefono con el id#{} no fue encontrado", id);
            throw new Exception("Telefono no encontrado");
        }

        return telefono;
    }

    public ResponseWrapper<Telefono> crearPhone(Telefono telefono) throws Exception {
        if(telefono == null){
            throw new Exception("Ningun campo del telefono debe ir vacio");
        }

        log.info("El telefono ha sido creado", telefono);
        return feignApiClient.createPhone(telefono);

    }

    public ResponseWrapper<Telefono> actualizarPhoneXId(int id, Telefono telefono) throws Exception {
        if(id <= 0 || telefono == null){
            throw new Exception("Por favor introduce un telefono con un id válido y todos sus campos...");
        }
        log.info("El telefono con el id#{} ha sido actulizado", id);
        return feignApiClient.updatePhone(telefono, id);
    }

    public void deletePhoneXId(int id) throws Exception {
        if(id <= 0){
            throw new Exception("El id debe ser mayor a cero");
        }
        ResponseWrapper telefono = feignApiClient.getPhoneById(id);
        if(telefono.getResponseEntity().getBody() == null){
            log.warn("El telefono con el id#{} no fue encontrado...", id);
            throw new Exception("El telefono no fue encontrado");
        }
        feignApiClient.deletePhone(id);
    }
}
