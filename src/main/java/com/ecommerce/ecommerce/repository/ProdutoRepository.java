package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    @Query("SELECT DISTINCT p FROM Produto p LEFT JOIN FETCH p.categoria")
    List<Produto> findAllComCategoria();
    long countByCategoriaId(Long categoriaId);
}
