package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;

public class RespostaContatoDTO {

    @NotBlank(message = "Resposta é obrigatória")
    private String resposta;

    // Getters and Setters
    public String getResposta() { return resposta; }
    public void setResposta(String resposta) { this.resposta = resposta; }
}