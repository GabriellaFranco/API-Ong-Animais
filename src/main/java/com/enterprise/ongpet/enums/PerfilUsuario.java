package com.enterprise.ongpet.enums;

import com.enterprise.ongpet.exception.ResourceNotFoundException;

public enum PerfilUsuario {
    ADMIN,
    VOLUNTARIO,
    PADRAO;

    private PerfilUsuario parsePerfilUsuario(String perfil) {
        try {
            return PerfilUsuario.valueOf(perfil.toUpperCase());
        }
        catch (Exception exc) {
            throw new ResourceNotFoundException("Perfil de Usuário não encontrado");
        }
    }
}
