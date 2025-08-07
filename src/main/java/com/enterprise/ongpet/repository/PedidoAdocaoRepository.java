package com.enterprise.ongpet.repository;

import com.enterprise.ongpet.enums.StatusAdocao;
import com.enterprise.ongpet.model.entity.Animal;
import com.enterprise.ongpet.model.entity.PedidoAdocao;
import com.enterprise.ongpet.model.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoAdocaoRepository extends JpaRepository<PedidoAdocao, Long> {

    @Override
    Page<PedidoAdocao> findAll(Pageable pageable);

    boolean existsByAdotanteAndAnimalAndStatus(Usuario usuario, Animal animal, StatusAdocao statusAdocao);

    long countByAdotanteAndStatus(Usuario adotante, StatusAdocao statusAdocao);

    long countByVoluntarioResponsavelAndStatus(Usuario voluntario, StatusAdocao statusAdocao);


}
