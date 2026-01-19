package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findBySlug(String slug);

    Optional<Tag> findByNome(String nome);

    @Query("SELECT t FROM Tag t ORDER BY (SELECT COUNT(c) FROM t.conteudos c WHERE c.ativo = true) DESC")
    List<Tag> findMaisPopulares();

    @Query("SELECT t FROM Tag t WHERE t.id IN (SELECT tg.id FROM Conteudo c JOIN c.tags tg WHERE c.id = :conteudoId)")
    List<Tag> findByConteudoId(@Param("conteudoId") Long conteudoId);

    boolean existsBySlug(String slug);

    boolean existsByNome(String nome);
}