package com.nestorc.appwebThymeleaf.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ResponseEntity<T> {
    Map<String, Object> headers; //object para evitar problemas de deserializacion con los headers
    T body;
    String statusCode;
    int statusCodeValue;
}
