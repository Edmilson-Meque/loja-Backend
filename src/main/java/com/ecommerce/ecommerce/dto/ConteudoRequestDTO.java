package com.ecommerce.ecommerce.dto;

import com.ecommerce.ecommerce.entity.TipoConteudo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class ConteudoRequestDTO {

    @NotBlank(message = "Título é obrigatório")
    private String titulo;

    @NotBlank(message = "Slug é obrigatório")
    private String slug;

    @NotBlank(message = "Conteúdo é obrigatório")
    private String conteudo;

    private String descricaoCurta;

    @NotNull(message = "Tipo de conteúdo é obrigatório")
    private TipoConteudo tipo;

    private String imagemCapa;
    private String autor;
    private String metaTitulo;
    private String metaDescricao;
    private String metaKeywords;
    private boolean ativo = true;
    private boolean destaque = false;
    private Integer ordem = 0;
    private LocalDateTime dataPublicacao;
    private Long categoriaId;
    private List<Long> tagIds;

    // Getters and Setters
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }

    public String getDescricaoCurta() { return descricaoCurta; }
    public void setDescricaoCurta(String descricaoCurta) { this.descricaoCurta = descricaoCurta; }

    public TipoConteudo getTipo() { return tipo; }
    public void setTipo(TipoConteudo tipo) { this.tipo = tipo; }

    public String getImagemCapa() { return imagemCapa; }
    public void setImagemCapa(String imagemCapa) { this.imagemCapa = imagemCapa; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getMetaTitulo() { return metaTitulo; }
    public void setMetaTitulo(String metaTitulo) { this.metaTitulo = metaTitulo; }

    public String getMetaDescricao() { return metaDescricao; }
    public void setMetaDescricao(String metaDescricao) { this.metaDescricao = metaDescricao; }

    public String getMetaKeywords() { return metaKeywords; }
    public void setMetaKeywords(String metaKeywords) { this.metaKeywords = metaKeywords; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public boolean isDestaque() { return destaque; }
    public void setDestaque(boolean destaque) { this.destaque = destaque; }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }

    public LocalDateTime getDataPublicacao() { return dataPublicacao; }
    public void setDataPublicacao(LocalDateTime dataPublicacao) { this.dataPublicacao = dataPublicacao; }

    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }

    public List<Long> getTagIds() { return tagIds; }
    public void setTagIds(List<Long> tagIds) { this.tagIds = tagIds; }
}