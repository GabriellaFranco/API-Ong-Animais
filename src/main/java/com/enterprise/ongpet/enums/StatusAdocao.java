package com.enterprise.ongpet.enums;

import com.enterprise.ongpet.exception.ResourceNotFoundException;

public enum StatusAdocao {
    SOLICITADA,
    APROVADA,
    REPROVADA;

    private StatusAdocao parseStatusAdocao(String status) {
        try {
            return StatusAdocao.valueOf(status.toUpperCase());
        }
        catch (IllegalArgumentException exc) {
            throw new ResourceNotFoundException("Status inválido");
        }
    }
}
