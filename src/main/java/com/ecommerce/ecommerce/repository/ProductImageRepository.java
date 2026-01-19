package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.ProductImage;
import com.ecommerce.ecommerce.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    List<ProductImage> findByProduto(Produto produto);

    @Query("SELECT pi FROM ProductImage pi WHERE pi.produto.id = :produtoId ORDER BY pi.ordem ASC")
    List<ProductImage> findByProdutoId(@Param("produtoId") Long produtoId);

    @Query("SELECT pi FROM ProductImage pi WHERE pi.produto.id = :produtoId AND pi.principal = true")
    List<ProductImage> findByProdutoIdAndPrincipalTrue(@Param("produtoId") Long produtoId);

    @Query("SELECT pi FROM ProductImage pi WHERE pi.produto.id = :produtoId AND pi.principal = true")
    Optional<ProductImage> findPrincipalByProdutoId(@Param("produtoId") Long produtoId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ProductImage pi WHERE pi.produto.id = :produtoId")
    void deleteByProdutoId(@Param("produtoId") Long produtoId);

    @Modifying
    @Transactional
    @Query("UPDATE ProductImage pi SET pi.principal = false WHERE pi.produto.id = :produtoId")
    void clearPrincipalFlag(@Param("produtoId") Long produtoId);

    @Modifying
    @Transactional
    @Query("UPDATE ProductImage pi SET pi.principal = true WHERE pi.id = :imageId")
    void setAsPrincipal(@Param("imageId") Long imageId);

    @Query("SELECT COUNT(pi) FROM ProductImage pi WHERE pi.produto.id = :produtoId")
    long countByProdutoId(@Param("produtoId") Long produtoId);

    @Query("SELECT MAX(pi.ordem) FROM ProductImage pi WHERE pi.produto.id = :produtoId")
    Integer findMaxOrdemByProdutoId(@Param("produtoId") Long produtoId);

    @Query("SELECT pi FROM ProductImage pi WHERE pi.produto.id = :produtoId AND pi.ordem = :ordem")
    Optional<ProductImage> findByProdutoIdAndOrdem(@Param("produtoId") Long produtoId,
                                                   @Param("ordem") Integer ordem);
}