package com.ecommerce.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class ProductImageResponseDTO {

    private Long id;
    private Long produtoId;
    private String urlImagem;
    private String descricao;
    private Integer ordem;
    private boolean principal;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataUpload;

    private String nomeArquivo;
    private Long tamanhoBytes;
    private String contentType;
    private String urlThumbnail;

    // Constructor
    public ProductImageResponseDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }

    public String getUrlImagem() { return urlImagem; }
    public void setUrlImagem(String urlImagem) { this.urlImagem = urlImagem; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }

    public boolean isPrincipal() { return principal; }
    public void setPrincipal(boolean principal) { this.principal = principal; }

    public LocalDateTime getDataUpload() { return dataUpload; }
    public void setDataUpload(LocalDateTime dataUpload) { this.dataUpload = dataUpload; }

    public String getNomeArquivo() { return nomeArquivo; }
    public void setNomeArquivo(String nomeArquivo) { this.nomeArquivo = nomeArquivo; }

    public Long getTamanhoBytes() { return tamanhoBytes; }
    public void setTamanhoBytes(Long tamanhoBytes) { this.tamanhoBytes = tamanhoBytes; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public String getUrlThumbnail() { return urlThumbnail; }
    public void setUrlThumbnail(String urlThumbnail) { this.urlThumbnail = urlThumbnail; }
}
