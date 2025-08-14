package com.enterprise.ongpet.repository;

import com.enterprise.ongpet.model.entity.Doacao;
import com.enterprise.ongpet.model.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface DoacaoRepository extends JpaRepository<Doacao, Long> {

    @Override
    Page<Doacao> findAll(Pageable pageable);

    boolean existsByDoadorAndDataBetween(Usuario usuario, LocalDateTime inicio, LocalDateTime fim);

    @Query("""
            SELECT d
            FROM Doacao d
            WHERE (:doador IS NULL OR d.doador = :doador)
              AND (:data IS NULL OR d.data = :data)
              AND (:valor IS NULL OR d.valor = :valor)
            """)
    Page<Doacao> findByFilters(@Param("doador") String doador,
                               @Param("data") LocalDate data,
                               @Param("valor") BigDecimal valor,
                               Pageable pageable);

}
