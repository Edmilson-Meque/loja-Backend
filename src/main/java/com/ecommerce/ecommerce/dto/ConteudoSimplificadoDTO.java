package com.ecommerce.ecommerce.dto;

import com.ecommerce.ecommerce.entity.TipoConteudo;
import java.time.LocalDateTime;

public class ConteudoSimplificadoDTO {

    private Long id;
    private String titulo;
    private String slug;
    private String descricaoCurta;
    private TipoConteudo tipo;
    private String imagemCapa;
    private LocalDateTime dataPublicacao;
    private String autor;
    private boolean destaque;
    private Integer visualizacoes;
    private String categoriaNome;

    // Constructor
    public ConteudoSimplificadoDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getDescricaoCurta() { return descricaoCurta; }
    public void setDescricaoCurta(String descricaoCurta) { this.descricaoCurta = descricaoCurta; }

    public TipoConteudo getTipo() { return tipo; }
    public void setTipo(TipoConteudo tipo) { this.tipo = tipo; }

    public String getImagemCapa() { return imagemCapa; }
    public void setImagemCapa(String imagemCapa) { this.imagemCapa = imagemCapa; }

    public LocalDateTime getDataPublicacao() { return dataPublicacao; }
    public void setDataPublicacao(LocalDateTime dataPublicacao) { this.dataPublicacao = dataPublicacao; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public boolean isDestaque() { return destaque; }
    public void setDestaque(boolean destaque) { this.destaque = destaque; }

    public Integer getVisualizacoes() { return visualizacoes; }
    public void setVisualizacoes(Integer visualizacoes) { this.visualizacoes = visualizacoes; }

    public String getCategoriaNome() { return categoriaNome; }
    public void setCategoriaNome(String categoriaNome) { this.categoriaNome = categoriaNome; }
}