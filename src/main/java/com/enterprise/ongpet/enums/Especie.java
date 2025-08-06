package com.enterprise.ongpet.enums;

import com.enterprise.ongpet.exception.ResourceNotFoundException;

public enum Especie {
    CANINO,
    FELINO,
    OUTROS;

    public Especie parseEspecie(String especie) {
        try {
            return Especie.valueOf(especie.toUpperCase());
        }
        catch (IllegalArgumentException exc) {
            throw new ResourceNotFoundException("Espécie não encontrada");
        }
    }
}
