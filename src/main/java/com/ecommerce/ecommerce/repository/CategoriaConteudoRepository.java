package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.CategoriaConteudo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaConteudoRepository extends JpaRepository<CategoriaConteudo, Long> {

    Optional<CategoriaConteudo> findBySlug(String slug);

    Optional<CategoriaConteudo> findByNome(String nome);

    List<CategoriaConteudo> findByAtivoTrue();

    List<CategoriaConteudo> findByAtivoTrueOrderByOrdemAsc();

    @Query("SELECT cc FROM CategoriaConteudo cc WHERE cc.ativo = true AND cc.id IN (SELECT DISTINCT c.categoria.id FROM Conteudo c WHERE c.ativo = true)")
    List<CategoriaConteudo> findCategoriasComConteudo();

    boolean existsBySlug(String slug);

    boolean existsByNome(String nome);
}