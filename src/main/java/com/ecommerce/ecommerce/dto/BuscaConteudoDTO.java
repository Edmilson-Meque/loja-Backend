package com.ecommerce.ecommerce.dto;

import com.ecommerce.ecommerce.entity.TipoConteudo;
import java.time.LocalDateTime;

public class BuscaConteudoDTO {

    private String termo;
    private TipoConteudo tipo;
    private Long categoriaId;
    private Long tagId;
    private Boolean ativo;
    private Boolean destaque;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private String autor;
    private String ordenarPor = "dataPublicacao"; // dataPublicacao, visualizacoes, titulo
    private String direcao = "DESC"; // ASC, DESC
    private Integer pagina = 0;
    private Integer tamanho = 20;

    // Getters and Setters
    public String getTermo() { return termo; }
    public void setTermo(String termo) { this.termo = termo; }

    public TipoConteudo getTipo() { return tipo; }
    public void setTipo(TipoConteudo tipo) { this.tipo = tipo; }

    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }

    public Long getTagId() { return tagId; }
    public void setTagId(Long tagId) { this.tagId = tagId; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public Boolean getDestaque() { return destaque; }
    public void setDestaque(Boolean destaque) { this.destaque = destaque; }

    public LocalDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }

    public LocalDateTime getDataFim() { return dataFim; }
    public void setDataFim(LocalDateTime dataFim) { this.dataFim = dataFim; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getOrdenarPor() { return ordenarPor; }
    public void setOrdenarPor(String ordenarPor) { this.ordenarPor = ordenarPor; }

    public String getDirecao() { return direcao; }
    public void setDirecao(String direcao) { this.direcao = direcao; }

    public Integer getPagina() { return pagina; }
    public void setPagina(Integer pagina) { this.pagina = pagina; }

    public Integer getTamanho() { return tamanho; }
    public void setTamanho(Integer tamanho) { this.tamanho = tamanho; }
}