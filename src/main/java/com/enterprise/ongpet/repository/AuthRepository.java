package com.enterprise.ongpet.repository;

import com.enterprise.ongpet.model.entity.Autoridade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Autoridade, Long> {

    Optional<Autoridade> findByName(String nome);

    List<Autoridade> findByNameIn(List<String> nomes);
}
