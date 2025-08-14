package com.enterprise.ongpet.repository;

import com.enterprise.ongpet.enums.StatusAdocao;
import com.enterprise.ongpet.model.entity.Animal;
import com.enterprise.ongpet.model.entity.PedidoAdocao;
import com.enterprise.ongpet.model.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PedidoAdocaoRepository extends JpaRepository<PedidoAdocao, Long> {

    @Override
    Page<PedidoAdocao> findAll(Pageable pageable);

    boolean existsByAdotanteAndAnimalAndStatus(Usuario usuario, Animal animal, StatusAdocao statusAdocao);

    long countByAdotanteAndStatus(Usuario adotante, StatusAdocao statusAdocao);

    long countByVoluntarioResponsavelAndStatus(Usuario voluntario, StatusAdocao statusAdocao);

    @Query("""
                SELECT p FROM PedidoAdocao p
                WHERE (:statusAdocao IS NULL OR p.status = :statusAdocao)
                  AND (:dataPedido IS NULL OR p.dataPedido = :dataPedido)
                  AND (
                       :adotante IS NULL
                       OR LOWER(p.adotante.nome) = LOWER(:adotante)
                       OR LOWER(p.adotante.nome) LIKE LOWER(CONCAT('%', :adotante, '%'))
                  )
                  AND (
                       :voluntarioResponsavel IS NULL
                       OR LOWER(p.voluntarioResponsavel.nome) = LOWER(:voluntarioResponsavel)
                       OR LOWER(p.voluntarioResponsavel.nome) LIKE LOWER(CONCAT('%', :voluntarioResponsavel, '%'))
                  )
                ORDER BY
                    CASE WHEN :adotante IS NOT NULL AND LOWER(p.adotante.nome) = LOWER(:adotante) THEN 0 ELSE 1 END,
                    CASE WHEN :voluntarioResponsavel IS NOT NULL AND LOWER(p.voluntarioResponsavel.nome) = LOWER(:voluntarioResponsavel) THEN 0 ELSE 1 END
            """)
    Page<PedidoAdocao> findByFilters(@Param("status") StatusAdocao statusAdocao,
                                     @Param("data") LocalDate dataPedido,
                                     @Param("adotante") String adotante,
                                     @Param("voluntarioResponsavel") String voluntarioResponsavel,
                                     Pageable pageable);

}
