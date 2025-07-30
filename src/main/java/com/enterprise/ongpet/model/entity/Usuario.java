package com.enterprise.ongpet.model.entity;

import com.enterprise.ongpet.enums.PerfilUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String cep;
    private String cidade;
    private String bairro;
    private String rua;
    private String numEndereco;

    @Enumerated(EnumType.STRING)
    private PerfilUsuario perfil;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "autoridades_usuario",
            joinColumns = @JoinColumn(name = "id_usuario", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_autoridade", referencedColumnName = "id")
    )
    private List<Autoridade> autoridades = new ArrayList<>();
}
