package com.ecommerce.ecommerce.dto;

import java.util.Map;

public class EstatisticasCMSDTO {

    private long totalConteudos;
    private long totalConteudosAtivos;
    private long totalConteudosPorTipo;
    private long totalCategorias;
    private long totalTags;
    private long totalContatos;
    private long totalContatosNaoLidos;
    private long totalContatosNaoRespondidos;
    private Map<String, Long> conteudosPorTipo;
    private Map<String, Long> contatosPorTipo;

    // Constructor
    public EstatisticasCMSDTO() {}

    // Getters and Setters
    public long getTotalConteudos() { return totalConteudos; }
    public void setTotalConteudos(long totalConteudos) { this.totalConteudos = totalConteudos; }

    public long getTotalConteudosAtivos() { return totalConteudosAtivos; }
    public void setTotalConteudosAtivos(long totalConteudosAtivos) { this.totalConteudosAtivos = totalConteudosAtivos; }

    public long getTotalConteudosPorTipo() { return totalConteudosPorTipo; }
    public void setTotalConteudosPorTipo(long totalConteudosPorTipo) { this.totalConteudosPorTipo = totalConteudosPorTipo; }

    public long getTotalCategorias() { return totalCategorias; }
    public void setTotalCategorias(long totalCategorias) { this.totalCategorias = totalCategorias; }

    public long getTotalTags() { return totalTags; }
    public void setTotalTags(long totalTags) { this.totalTags = totalTags; }

    public long getTotalContatos() { return totalContatos; }
    public void setTotalContatos(long totalContatos) { this.totalContatos = totalContatos; }

    public long getTotalContatosNaoLidos() { return totalContatosNaoLidos; }
    public void setTotalContatosNaoLidos(long totalContatosNaoLidos) { this.totalContatosNaoLidos = totalContatosNaoLidos; }

    public long getTotalContatosNaoRespondidos() { return totalContatosNaoRespondidos; }
    public void setTotalContatosNaoRespondidos(long totalContatosNaoRespondidos) { this.totalContatosNaoRespondidos = totalContatosNaoRespondidos; }

    public Map<String, Long> getConteudosPorTipo() { return conteudosPorTipo; }
    public void setConteudosPorTipo(Map<String, Long> conteudosPorTipo) { this.conteudosPorTipo = conteudosPorTipo; }

    public Map<String, Long> getContatosPorTipo() { return contatosPorTipo; }
    public void setContatosPorTipo(Map<String, Long> contatosPorTipo) { this.contatosPorTipo = contatosPorTipo; }
}