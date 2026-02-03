package com.nestorc.appwebThymeleaf.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class Telefono {
    private int idTelefono;
    private String tipoTelefono;
    private int lada;
    private String numero;
    private Usuario usuario;
}
