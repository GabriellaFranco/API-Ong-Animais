package com.enterprise.ongpet.enums;

import com.enterprise.ongpet.exception.ResourceNotFoundException;

public enum PorteAnimal {
    PEQUENO,
    MEDIO,
    GRANDE;

    private PorteAnimal parsePorteAnimal(String porte) {
        try {
            return PorteAnimal.valueOf(porte.toUpperCase());
        }
        catch (IllegalArgumentException exc) {
            throw new ResourceNotFoundException("Porte inválido");
        }
    }
}
