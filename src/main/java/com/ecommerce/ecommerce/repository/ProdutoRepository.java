package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
