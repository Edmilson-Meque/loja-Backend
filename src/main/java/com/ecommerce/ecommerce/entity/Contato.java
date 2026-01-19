package com.ecommerce.ecommerce.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contatos")
public class Contato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String telefone;

    @Column(nullable = false)
    private String assunto;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensagem;

    @Column(name = "data_envio")
    private LocalDateTime dataEnvio = LocalDateTime.now();

    @Column(name = "lido")
    private boolean lido = false;

    @Column(name = "respondido")
    private boolean respondido = false;

    @Column(name = "resposta")
    private String resposta;

    @Column(name = "data_resposta")
    private LocalDateTime dataResposta;

    @Column(name = "ip_cliente")
    private String ipCliente;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "tipo_contato")
    @Enumerated(EnumType.STRING)
    private TipoContato tipoContato = TipoContato.GERAL;

    // Constructor
    public Contato() {}

    public Contato(String nome, String email, String telefone, String assunto, String mensagem) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.assunto = assunto;
        this.mensagem = mensagem;
    }

    // Business methods
    public void marcarComoLido() {
        this.lido = true;
    }

    public void responder(String resposta) {
        this.resposta = resposta;
        this.respondido = true;
        this.dataResposta = LocalDateTime.now();
    }

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

    public TipoContato getTipoContato() { return tipoContato; }
    public void setTipoContato(TipoContato tipoContato) { this.tipoContato = tipoContato; }
}