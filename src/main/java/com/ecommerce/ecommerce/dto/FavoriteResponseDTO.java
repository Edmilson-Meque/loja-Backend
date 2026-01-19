package com.ecommerce.ecommerce.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FavoriteResponseDTO {

    private Long id;
    private Long produtoId;
    private String produtoNome;
    private String produtoDescricaoCurta;
    private BigDecimal produtoPreco;
    private BigDecimal produtoPrecoPromocional;
    private Boolean produtoEmPromocao;
    private BigDecimal produtoPrecoAtual;
    private Integer percentualDesconto;
    private LocalDateTime dataAdicao;

    // Constructor
    public FavoriteResponseDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }

    public String getProdutoNome() { return produtoNome; }
    public void setProdutoNome(String produtoNome) { this.produtoNome = produtoNome; }

    public String getProdutoDescricaoCurta() { return produtoDescricaoCurta; }
    public void setProdutoDescricaoCurta(String produtoDescricaoCurta) { this.produtoDescricaoCurta = produtoDescricaoCurta; }

    public BigDecimal getProdutoPreco() { return produtoPreco; }
    public void setProdutoPreco(BigDecimal produtoPreco) { this.produtoPreco = produtoPreco; }

    public BigDecimal getProdutoPrecoPromocional() { return produtoPrecoPromocional; }
    public void setProdutoPrecoPromocional(BigDecimal produtoPrecoPromocional) { this.produtoPrecoPromocional = produtoPrecoPromocional; }

    public Boolean getProdutoEmPromocao() { return produtoEmPromocao; }
    public void setProdutoEmPromocao(Boolean produtoEmPromocao) { this.produtoEmPromocao = produtoEmPromocao; }

    public BigDecimal getProdutoPrecoAtual() { return produtoPrecoAtual; }
    public void setProdutoPrecoAtual(BigDecimal produtoPrecoAtual) { this.produtoPrecoAtual = produtoPrecoAtual; }

    public Integer getPercentualDesconto() { return percentualDesconto; }
    public void setPercentualDesconto(Integer percentualDesconto) { this.percentualDesconto = percentualDesconto; }

    public LocalDateTime getDataAdicao() { return dataAdicao; }
    public void setDataAdicao(LocalDateTime dataAdicao) { this.dataAdicao = dataAdicao; }
}