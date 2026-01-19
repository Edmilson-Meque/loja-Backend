package com.ecommerce.ecommerce.entity;

public enum TipoContato {
    GERAL("Geral"),
    DUVIDA("Dúvida"),
    RECLAMACAO("Reclamação"),
    SUGESTAO("Sugestão"),
    ORCAMENTO("Orçamento"),
    SUPORTE("Suporte Técnico"),
    PARCERIA("Parceria");

    private final String descricao;

    TipoContato(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}