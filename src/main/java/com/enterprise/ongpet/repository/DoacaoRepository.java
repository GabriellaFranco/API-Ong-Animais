package com.enterprise.ongpet.repository;

import com.enterprise.ongpet.model.entity.Doacao;
import com.enterprise.ongpet.model.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface DoacaoRepository extends JpaRepository<Doacao, Long> {

    @Override
    Page<Doacao> findAll(Pageable pageable);

    boolean existsByDoadorAndDataBetween(Usuario usuario, LocalDateTime inicio, LocalDateTime fim);

}
