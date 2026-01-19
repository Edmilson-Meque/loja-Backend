package com.ecommerce.ecommerce.dto;

public class SlugValidationDTO {

    private String slug;
    private boolean disponivel;

    // Constructor
    public SlugValidationDTO() {}

    public SlugValidationDTO(String slug, boolean disponivel) {
        this.slug = slug;
        this.disponivel = disponivel;
    }

    // Getters and Setters
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }
}