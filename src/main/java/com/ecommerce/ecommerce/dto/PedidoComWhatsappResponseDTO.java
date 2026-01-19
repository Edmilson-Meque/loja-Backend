package com.ecommerce.ecommerce.dto;

import java.util.Map;

public class PedidoComWhatsappResponseDTO {
    private PedidoResponseDTO pedido;
    private String linkWhatsapp;
    private String instrucoes;
    private Map<String, String> dadosContato;

    public PedidoComWhatsappResponseDTO() {}

    public PedidoComWhatsappResponseDTO(PedidoResponseDTO pedido, String linkWhatsapp) {
        this.pedido = pedido;
        this.linkWhatsapp = linkWhatsapp;
        this.instrucoes = "Clique no link para abrir o WhatsApp e enviar sua mensagem de confirmação de pedido.";

        this.dadosContato = Map.of(
                "empresa", "Nossa Equipe de Vendas",
                "telefone", pedido.getTelefoneContato(),
                "email", "vendas@minhaloja.com"
        );
    }

    // Getters and Setters
    public PedidoResponseDTO getPedido() { return pedido; }
    public void setPedido(PedidoResponseDTO pedido) { this.pedido = pedido; }

    public String getLinkWhatsapp() { return linkWhatsapp; }
    public void setLinkWhatsapp(String linkWhatsapp) { this.linkWhatsapp = linkWhatsapp; }

    public String getInstrucoes() { return instrucoes; }
    public void setInstrucoes(String instrucoes) { this.instrucoes = instrucoes; }

    public Map<String, String> getDadosContato() { return dadosContato; }
    public void setDadosContato(Map<String, String> dadosContato) { this.dadosContato = dadosContato; }
}