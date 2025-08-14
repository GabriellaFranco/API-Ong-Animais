package com.enterprise.ongpet.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.User;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "doacoes")
public class Doacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private BigDecimal valor;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "id_doador")
    private Usuario doador;
}
