package com.ecommerce.ecommerce.dto;

import java.time.LocalDateTime;

public class CategoriaConteudoDTO {

    private Long id;
    private String nome;
    private String slug;
    private String descricao;
    private String icone;
    private String cor;
    private Integer ordem;
    private boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private Integer totalConteudos;

    // Constructor
    public CategoriaConteudoDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getIcone() { return icone; }
    public void setIcone(String icone) { this.icone = icone; }

    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    public Integer getTotalConteudos() { return totalConteudos; }
    public void setTotalConteudos(Integer totalConteudos) { this.totalConteudos = totalConteudos; }
}
