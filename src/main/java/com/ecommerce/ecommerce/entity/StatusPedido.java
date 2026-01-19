package com.ecommerce.ecommerce.entity;

public enum StatusPedido {
    AGUARDANDO_PAGAMENTO("Aguardando Pagamento"),
    PAGAMENTO_CONFIRMADO("Pagamento Confirmado"),
    PROCESSANDO("Processando"),
    ENVIADO("Enviado"),
    ENTREGUE("Entregue"),
    CANCELADO("Cancelado");

    private final String descricao;

    StatusPedido(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
