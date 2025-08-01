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

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String cpf;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private String cep;

    @Column(nullable = false)
    private String cidade;

    @Column(nullable = false)
    private String bairro;

    @Column(nullable = false)
    private String rua;

    @Column(nullable = false)
    private Long numEndereco;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PerfilUsuario perfil;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "autoridades_usuario",
            joinColumns = @JoinColumn(name = "id_usuario", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_autoridade", referencedColumnName = "id")
    )
    private List<Autoridade> autoridades = new ArrayList<>();

    @OneToMany(mappedBy = "responsavel", cascade = CascadeType.ALL)
    private List<Animal> animaisCadastrados = new ArrayList<>();

    @OneToMany(mappedBy = "adotante", cascade = CascadeType.ALL)
    private List<PedidoAdocao> pedidoAdocao = new ArrayList<>();

    @OneToMany(mappedBy = "voluntarioResponsavel", cascade = CascadeType.ALL)
    private List<PedidoAdocao> pedidosAnalisados = new ArrayList<>();

    @OneToMany(mappedBy = "doador", cascade = CascadeType.ALL)
    private List<Doacao> doacoes = new ArrayList<>();
}
