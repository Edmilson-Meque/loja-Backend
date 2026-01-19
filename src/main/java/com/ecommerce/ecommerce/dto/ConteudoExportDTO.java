package com.ecommerce.ecommerce.dto;

import com.ecommerce.ecommerce.entity.TipoConteudo;
import java.time.LocalDateTime;
import java.util.List;

public class ConteudoExportDTO {

    private Long id;
    private String titulo;
    private String slug;
    private String conteudo;
    private String descricaoCurta;
    private TipoConteudo tipo;
    private String imagemCapa;
    private LocalDateTime dataPublicacao;
    private LocalDateTime dataAtualizacao;
    private String autor;
    private String metaTitulo;
    private String metaDescricao;
    private String metaKeywords;
    private boolean ativo;
    private boolean destaque;
    private Integer visualizacoes;
    private Integer ordem;
    private String categoriaNome;
    private List<String> tags;

    // Constructor
    public ConteudoExportDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public LocalDateTime getDataPublicacao() { return dataPublicacao; }
    public void setDataPublicacao(LocalDateTime dataPublicacao) { this.dataPublicacao = dataPublicacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

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

    public Integer getVisualizacoes() { return visualizacoes; }
    public void setVisualizacoes(Integer visualizacoes) { this.visualizacoes = visualizacoes; }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }

    public String getCategoriaNome() { return categoriaNome; }
    public void setCategoriaNome(String categoriaNome) { this.categoriaNome = categoriaNome; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
}