package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import com.ecommerce.ecommerce.validation.StrongPassword;
import jakarta.validation.constraints.Size;

public class RegisterRequestDTO {

    @NotBlank
    @Size(min = 2, max = 100)
    private String nome;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @StrongPassword
    private String senha;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}