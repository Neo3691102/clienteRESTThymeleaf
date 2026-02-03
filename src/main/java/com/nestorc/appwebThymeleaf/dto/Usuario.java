package com.nestorc.appwebThymeleaf.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Usuario {
    private int idUsuario;
    private String nombre;
    private int edad;
    private List<Telefono> telefonos = new ArrayList<>();
}
