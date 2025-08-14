package com.enterprise.ongpet.repository;

import com.enterprise.ongpet.enums.PerfilUsuario;
import com.enterprise.ongpet.model.entity.Usuario;
import jakarta.websocket.server.PathParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Override
    Page<Usuario> findAll(Pageable pageable);

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByPerfil(PerfilUsuario perfil);

    @Query("""
                SELECT u FROM Usuario u
                WHERE (:nome IS NULL OR LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
                AND (:cpf IS NULL OR u.cpf LIKE CONCAT('%', :cpf, '%'))
                AND (:cidade IS NULL OR LOWER(u.cidade) LIKE LOWER(CONCAT('%', :cidade, '%')))
                AND (:perfil IS NULL OR u.perfil = :perfil)
                ORDER BY
                    CASE WHEN :nome IS NOT NULL AND LOWER(u.nome) = LOWER(:nome) THEN 0 ELSE 1 END,
                    CASE WHEN :cpf IS NOT NULL AND u.cpf = :cpf THEN 0 ELSE 1 END,
                    CASE WHEN :cidade IS NOT NULL AND LOWER(u.cidade) = LOWER(:cidade) THEN 0 ELSE 1 END
            """)
    Page<Usuario> findByFilter(@Param("nome") String nome, @Param("cpf") String cpf, @Param("cidade") String cidade,
                               @Param("perfil") PerfilUsuario perfil, Pageable pageable);

}
