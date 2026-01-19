package com.ecommerce.ecommerce.dto;

import com.ecommerce.ecommerce.entity.TipoContato;
import java.time.LocalDateTime;

public class ContatoResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String assunto;
    private String mensagem;
    private TipoContato tipoContato;
    private String tipoContatoDescricao;
    private LocalDateTime dataEnvio;
    private boolean lido;
    private boolean respondido;
    private String resposta;
    private LocalDateTime dataResposta;
    private String ipCliente;
    private String userAgent;

    // Constructor
    public ContatoResponseDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getAssunto() { return assunto; }
    public void setAssunto(String assunto) { this.assunto = assunto; }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }

    public TipoContato getTipoContato() { return tipoContato; }
    public void setTipoContato(TipoContato tipoContato) {
        this.tipoContato = tipoContato;
        this.tipoContatoDescricao = tipoContato != null ? tipoContato.getDescricao() : null;
    }

    public String getTipoContatoDescricao() { return tipoContatoDescricao; }
    public void setTipoContatoDescricao(String tipoContatoDescricao) { this.tipoContatoDescricao = tipoContatoDescricao; }

    public LocalDateTime getDataEnvio() { return dataEnvio; }
    public void setDataEnvio(LocalDateTime dataEnvio) { this.dataEnvio = dataEnvio; }

    public boolean isLido() { return lido; }
    public void setLido(boolean lido) { this.lido = lido; }

    public boolean isRespondido() { return respondido; }
    public void setRespondido(boolean respondido) { this.respondido = respondido; }

    public String getResposta() { return resposta; }
    public void setResposta(String resposta) { this.resposta = resposta; }

    public LocalDateTime getDataResposta() { return dataResposta; }
    public void setDataResposta(LocalDateTime dataResposta) { this.dataResposta = dataResposta; }

    public String getIpCliente() { return ipCliente; }
    public void setIpCliente(String ipCliente) { this.ipCliente = ipCliente; }

    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
}