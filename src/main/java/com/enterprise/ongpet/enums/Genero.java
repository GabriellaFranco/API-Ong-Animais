package com.enterprise.ongpet.enums;

import com.enterprise.ongpet.exception.ResourceNotFoundException;

public enum Genero {
    MASCULINO,
    FEMININO;

    private Genero parseGenero(String genero) {
        try {
            return Genero.valueOf(genero.toUpperCase());
        }
        catch (IllegalArgumentException exc) {
            throw new ResourceNotFoundException("Gênero inválido");
        }
    }
}
