package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCarrinhoIdAndProdutoId(Long carrinhoId, Long produtoId);

    List<CartItem> findByCarrinhoId(Long carrinhoId);

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.carrinho.id = :carrinhoId AND ci.produto.id = :produtoId")
    void deleteByCarrinhoIdAndProdutoId(@Param("carrinhoId") Long carrinhoId,
                                        @Param("produtoId") Long produtoId);

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.carrinho.id = :carrinhoId")
    void deleteByCarrinhoId(@Param("carrinhoId") Long carrinhoId);

    long countByCarrinhoId(Long carrinhoId);
}