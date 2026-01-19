package com.ecommerce.ecommerce.dto;

import java.math.BigDecimal;

public class EstatisticasPedidosDTO {
    private long totalPedidos;
    private long pedidosHoje;
    private BigDecimal faturamentoTotal;
    private BigDecimal faturamentoMes;
    private long pedidosPendentes;
    private long pedidosProcessando;
    private long pedidosEntregues;

    // Getters and Setters
    public long getTotalPedidos() { return totalPedidos; }
    public void setTotalPedidos(long totalPedidos) { this.totalPedidos = totalPedidos; }

    public long getPedidosHoje() { return pedidosHoje; }
    public void setPedidosHoje(long pedidosHoje) { this.pedidosHoje = pedidosHoje; }

    public BigDecimal getFaturamentoTotal() { return faturamentoTotal; }
    public void setFaturamentoTotal(BigDecimal faturamentoTotal) { this.faturamentoTotal = faturamentoTotal; }

    public BigDecimal getFaturamentoMes() { return faturamentoMes; }
    public void setFaturamentoMes(BigDecimal faturamentoMes) { this.faturamentoMes = faturamentoMes; }

    public long getPedidosPendentes() { return pedidosPendentes; }
    public void setPedidosPendentes(long pedidosPendentes) { this.pedidosPendentes = pedidosPendentes; }

    public long getPedidosProcessando() { return pedidosProcessando; }
    public void setPedidosProcessando(long pedidosProcessando) { this.pedidosProcessando = pedidosProcessando; }

    public long getPedidosEntregues() { return pedidosEntregues; }
    public void setPedidosEntregues(long pedidosEntregues) { this.pedidosEntregues = pedidosEntregues; }
}