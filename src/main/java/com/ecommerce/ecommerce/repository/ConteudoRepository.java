package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.Conteudo;
import com.ecommerce.ecommerce.entity.TipoConteudo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConteudoRepository extends JpaRepository<Conteudo, Long>, JpaSpecificationExecutor<Conteudo> {

    Optional<Conteudo> findBySlug(String slug);

    List<Conteudo> findByTipo(TipoConteudo tipo);

    List<Conteudo> findByAtivoTrue();

    List<Conteudo> findByDestaqueTrueAndAtivoTrue();

    @Query("SELECT c FROM Conteudo c WHERE c.tipo = :tipo AND c.ativo = true ORDER BY c.dataPublicacao DESC")
    List<Conteudo> findAtivosByTipo(@Param("tipo") TipoConteudo tipo);

    @Query("SELECT c FROM Conteudo c WHERE c.ativo = true AND (c.titulo LIKE %:termo% OR c.descricaoCurta LIKE %:termo% OR c.conteudo LIKE %:termo%)")
    List<Conteudo> buscarPorTermo(@Param("termo") String termo);

    @Query("SELECT c FROM Conteudo c WHERE c.ativo = true AND c.dataPublicacao <= :dataAtual ORDER BY c.dataPublicacao DESC")
    Page<Conteudo> findAtivosRecentes(@Param("dataAtual") LocalDateTime dataAtual, Pageable pageable);

    @Query("SELECT c FROM Conteudo c WHERE c.categoria.id = :categoriaId AND c.ativo = true")
    Page<Conteudo> findByCategoriaId(@Param("categoriaId") Long categoriaId, Pageable pageable);

    @Query("SELECT c FROM Conteudo c WHERE c.ativo = true ORDER BY c.visualizacoes DESC")
    List<Conteudo> findMaisVistos(int limit);

    boolean existsBySlug(String slug);

    long countByAtivoTrue();

    long countByTipo(TipoConteudo tipo);

    @Query("SELECT COUNT(c) FROM Conteudo c WHERE c.ativo = true AND c.tipo = 'POST' AND c.dataPublicacao >= :dataInicio")
    long countPostsRecentes(@Param("dataInicio") LocalDateTime dataInicio);
}