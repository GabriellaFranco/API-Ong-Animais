package com.enterprise.ongpet.model.entity;

import com.enterprise.ongpet.enums.StatusAdocao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "pedidos_adocao")
public class PedidoAdocao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate dataPedido;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusAdocao status;

    @Column(nullable = false)
    private String observacoes;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "id_animal")
    private Animal animal;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "id_adotante")
    private Usuario adotante;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "id_voluntario_responsavel")
    private Usuario voluntarioResponsavel;


}
