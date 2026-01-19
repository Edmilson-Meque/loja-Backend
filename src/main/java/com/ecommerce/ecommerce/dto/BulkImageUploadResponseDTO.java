package com.ecommerce.ecommerce.dto;

import java.util.List;

public class BulkImageUploadResponseDTO {

    private Long produtoId;
    private int totalEnviadas;
    private int totalProcessadas;
    private List<ProductImageResponseDTO> imagens;
    private List<String> erros;

    // Constructor
    public BulkImageUploadResponseDTO() {}

    // Getters and Setters
    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }

    public int getTotalEnviadas() { return totalEnviadas; }
    public void setTotalEnviadas(int totalEnviadas) { this.totalEnviadas = totalEnviadas; }

    public int getTotalProcessadas() { return totalProcessadas; }
    public void setTotalProcessadas(int totalProcessadas) { this.totalProcessadas = totalProcessadas; }

    public List<ProductImageResponseDTO> getImagens() { return imagens; }
    public void setImagens(List<ProductImageResponseDTO> imagens) { this.imagens = imagens; }

    public List<String> getErros() { return erros; }
    public void setErros(List<String> erros) { this.erros = erros; }
}