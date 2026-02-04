package com.enterprise.ongpet.model.dto.auth;

import com.enterprise.ongpet.enums.PerfilUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AtualizarPerfilEAutoridadesDTO(

        @NotNull
        PerfilUsuario novoPerfil,

        @NotBlank
        List<String> novasAutoridades
) {
}
