package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoriaConteudoRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Slug é obrigatório")
    private String slug;

    private String descricao;
    private String icone;
    private String cor;
    private Integer ordem = 0;
    private boolean ativo = true;

    // Getters and Setters
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
}