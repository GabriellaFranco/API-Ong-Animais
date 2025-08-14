package com.enterprise.ongpet.repository;

import com.enterprise.ongpet.enums.Especie;
import com.enterprise.ongpet.enums.Genero;
import com.enterprise.ongpet.enums.PorteAnimal;
import com.enterprise.ongpet.model.entity.Animal;
import com.enterprise.ongpet.model.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    @Override
    Page<Animal> findAll(Pageable pageable);

    boolean existsByNomeAndEspecieAndResponsavel(String nome, Especie especie, Usuario responsavel);

    @Query("""
                SELECT a FROM Animal a
                WHERE (:nome IS NULL OR LOWER(a.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
                  AND (:especie IS NULL OR a.especie = :especie)
                  AND (:genero IS NULL OR a.genero = :genero)
                  AND (:porte IS NULL OR a.porte = :porte)
                  AND (:disponivel IS NULL OR a.disponivel = :disponivel)
            """)
    Page<Animal> findByFilter(
            @Param("nome") String nome,
            @Param("especie") Especie especie,
            @Param("genero") Genero genero,
            @Param("porte") PorteAnimal porte,
            @Param("disponivel") Boolean disponivel,
            Pageable pageable
    );
}
