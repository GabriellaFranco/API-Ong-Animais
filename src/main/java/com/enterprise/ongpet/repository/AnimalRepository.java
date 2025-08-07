package com.enterprise.ongpet.repository;

import com.enterprise.ongpet.enums.Especie;
import com.enterprise.ongpet.model.entity.Animal;
import com.enterprise.ongpet.model.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

     @Override
     Page<Animal> findAll(Pageable pageable);

    boolean existsByNomeAndEspecieAndResponsavel(String nome, Especie especie, Usuario responsavel);

}
