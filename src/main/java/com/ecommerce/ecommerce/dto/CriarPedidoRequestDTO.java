package com.ecommerce.ecommerce.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.List;

public class CriarPedidoRequestDTO {

    @Valid
    private EnderecoEntregaDTO enderecoEntrega;

    @NotBlank(message = "Telefone para contato é obrigatório")
    @Pattern(regexp = "^\\+?[1-9][0-9]{7,14}$", message = "Número de telefone inválido")
    private String telefoneContato;

    private String observacoes;

    private BigDecimal valorFrete = BigDecimal.ZERO;
    private BigDecimal desconto = BigDecimal.ZERO;

    // Getters and Setters
    public EnderecoEntregaDTO getEnderecoEntrega() { return enderecoEntrega; }
    public void setEnderecoEntrega(EnderecoEntregaDTO enderecoEntrega) { this.enderecoEntrega = enderecoEntrega; }

    public String getTelefoneContato() { return telefoneContato; }
    public void setTelefoneContato(String telefoneContato) { this.telefoneContato = telefoneContato; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public BigDecimal getValorFrete() { return valorFrete; }
    public void setValorFrete(BigDecimal valorFrete) { this.valorFrete = valorFrete; }

    public BigDecimal getDesconto() { return desconto; }
    public void setDesconto(BigDecimal desconto) { this.desconto = desconto; }
}