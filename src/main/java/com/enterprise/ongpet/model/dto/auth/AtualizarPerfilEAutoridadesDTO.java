package com.enterprise.ongpet.model.dto.auth;

import com.enterprise.ongpet.enums.PerfilUsuario;

import java.util.List;

public record AtualizarPerfilEAutoridadesDTO(
        PerfilUsuario novoPerfil,
        List<String> novasAutoridades
) {
}
