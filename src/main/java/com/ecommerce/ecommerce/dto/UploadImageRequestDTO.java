package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.NotNull;

public class UploadImageRequestDTO {

    @NotNull(message = "Produto ID é obrigatório")
    private Long produtoId;

    private boolean principal = false;
    private String descricao;
    private Integer ordem;

    // Getters and Setters
    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }

    public boolean isPrincipal() { return principal; }
    public void setPrincipal(boolean principal) { this.principal = principal; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }
}
