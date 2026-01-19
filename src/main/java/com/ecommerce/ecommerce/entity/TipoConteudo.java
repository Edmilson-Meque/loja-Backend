package com.ecommerce.ecommerce.entity;

public enum TipoConteudo {
    PAGINA("Página"),
    POST("Post"),
    NOVIDADE("Novidade"),
    BLOG("Blog"),
    FAQ("FAQ"),
    SOBRE("Sobre"),
    CONTATO("Contato"),
    TERMOS("Termos de Uso"),
    POLITICA("Política de Privacidade"),
    INFORMATIVO("Informativo");

    private final String descricao;

    TipoConteudo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
