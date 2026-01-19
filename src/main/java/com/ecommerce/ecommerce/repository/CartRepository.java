package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.Cart;
import com.ecommerce.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUsuarioId(Long usuarioId);

    Optional<Cart> findByUsuario(User usuario);

    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.itens i LEFT JOIN FETCH i.produto WHERE c.usuario.id = :usuarioId")
    Optional<Cart> findByUsuarioIdWithItems(@Param("usuarioId") Long usuarioId);

    boolean existsByUsuarioId(Long usuarioId);

    void deleteByUsuarioId(Long usuarioId);
}