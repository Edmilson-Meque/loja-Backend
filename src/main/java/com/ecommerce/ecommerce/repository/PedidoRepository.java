package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.Pedido;
import com.ecommerce.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.itens WHERE p.usuario.id = :usuarioId ORDER BY p.dataCriacao DESC")
    List<Pedido> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.itens WHERE p.codigoPedido = :codigo")
    Optional<Pedido> findByCodigoPedido(@Param("codigo") String codigo);

    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.itens WHERE p.usuario = :usuario AND p.id = :pedidoId")
    Optional<Pedido> findByUsuarioAndId(@Param("usuario") User usuario, @Param("pedidoId") Long pedidoId);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.usuario.id = :usuarioId")
    long countByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.itens ORDER BY p.dataCriacao DESC")
    List<Pedido> findAllWithItems();
}
