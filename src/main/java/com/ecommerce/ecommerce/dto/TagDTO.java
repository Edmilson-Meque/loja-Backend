package com.ecommerce.ecommerce.dto;

import java.time.LocalDateTime;

public class TagDTO {

    private Long id;
    private String nome;
    private String slug;
    private String descricao;
    private String cor;
    private LocalDateTime dataCriacao;
    private Integer totalConteudos;

    // Constructor
    public TagDTO() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Integer getTotalConteudos() {
        return totalConteudos;
    }

    public void setTotalConteudos(Integer totalConteudos) {
        this.totalConteudos = totalConteudos;
    }
}