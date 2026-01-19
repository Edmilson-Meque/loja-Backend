package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.Contato;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContatoRepository extends JpaRepository<Contato, Long> {

    List<Contato> findByRespondidoFalse();

    List<Contato> findByLidoFalse();

    List<Contato> findByEmail(String email);

    Page<Contato> findAllByOrderByDataEnvioDesc(Pageable pageable);

    @Query("SELECT c FROM Contato c WHERE c.dataEnvio BETWEEN :inicio AND :fim")
    List<Contato> findByPeriodo(@Param("inicio") LocalDateTime inicio,
                                @Param("fim") LocalDateTime fim);

    @Query("SELECT c FROM Contato c WHERE c.respondido = :respondido ORDER BY c.dataEnvio DESC")
    Page<Contato> findByRespondido(@Param("respondido") boolean respondido, Pageable pageable);

    long countByRespondidoFalse();

    long countByLidoFalse();

    @Query("SELECT COUNT(c) FROM Contato c WHERE c.dataEnvio >= :dataInicio")
    long countRecentes(@Param("dataInicio") LocalDateTime dataInicio);
}