package com.nestorc.appwebThymeleaf.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Login {
    private int idUsuario;
    private String usuarioBD;
    private String passwordBD;
}
