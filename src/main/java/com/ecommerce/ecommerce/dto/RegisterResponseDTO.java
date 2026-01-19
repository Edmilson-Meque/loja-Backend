package com.ecommerce.ecommerce.dto;

public class RegisterResponseDTO {

    private Long id;
    private String email;

    public RegisterResponseDTO(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}