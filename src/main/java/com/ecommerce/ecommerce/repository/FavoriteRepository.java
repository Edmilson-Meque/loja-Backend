package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.Favorite;
import com.ecommerce.ecommerce.entity.User;
import com.ecommerce.ecommerce.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Query("SELECT f FROM Favorite f JOIN FETCH f.produto WHERE f.usuario.id = :usuarioId ORDER BY f.dataAdicao DESC")
    List<Favorite> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    Optional<Favorite> findByUsuarioAndProduto(User usuario, Produto produto);

    boolean existsByUsuarioAndProduto(User usuario, Produto produto);

    void deleteByUsuarioAndProduto(User usuario, Produto produto);

    long countByUsuarioId(Long usuarioId);

    @Query("SELECT COUNT(f) > 0 FROM Favorite f WHERE f.usuario.id = :usuarioId AND f.produto.id = :produtoId")
    boolean existsByUsuarioIdAndProdutoId(@Param("usuarioId") Long usuarioId, @Param("produtoId") Long produtoId);
}