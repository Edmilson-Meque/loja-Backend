package com.ecommerce.ecommerce.dto;

import com.ecommerce.ecommerce.entity.TipoContato;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ContatoRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
    private String email;

    @NotBlank(message = "Telefone é obrigatório")
    @Size(min = 8, max = 20, message = "Telefone deve ter entre 8 e 20 caracteres")
    private String telefone;

    @NotBlank(message = "Assunto é obrigatório")
    @Size(min = 2, max = 200, message = "Assunto deve ter entre 2 e 200 caracteres")
    private String assunto;

    @NotBlank(message = "Mensagem é obrigatória")
    @Size(min = 10, max = 5000, message = "Mensagem deve ter entre 10 e 5000 caracteres")
    private String mensagem;

    private TipoContato tipoContato = TipoContato.GERAL;

    // Getters and Setters
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
    public void setTipoContato(TipoContato tipoContato) { this.tipoContato = tipoContato; }
}