package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;

public class TagRequestDTO {

    @NotBlank(message = "Nome da tag é obrigatório")
    private String nome;

    @NotBlank(message = "Slug da tag é obrigatório")
    private String slug;

    private String descricao;
    private String cor;

    // Getters and Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }
}