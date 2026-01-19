package com.ecommerce.ecommerce.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "conteudos")
public class Conteudo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(unique = true, nullable = false)
    private String slug;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String conteudo;

    @Column(name = "descricao_curta", length = 500)
    private String descricaoCurta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoConteudo tipo = TipoConteudo.INFORMATIVO;

    @Column(name = "imagem_capa")
    private String imagemCapa;

    @Column(name = "data_publicacao")
    private LocalDateTime dataPublicacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao = LocalDateTime.now();

    @Column(name = "autor")
    private String autor;

    @Column(name = "meta_titulo")
    private String metaTitulo;

    @Column(name = "meta_descricao")
    private String metaDescricao;

    @Column(name = "meta_keywords")
    private String metaKeywords;

    @Column(name = "ativo")
    private boolean ativo = true;

    @Column(name = "destaque")
    private boolean destaque = false;

    @Column(name = "visualizacoes")
    private Integer visualizacoes = 0;

    @Column(name = "ordem")
    private Integer ordem = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_conteudo_id")
    private CategoriaConteudo categoria;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "conteudo_tags",
            joinColumns = @JoinColumn(name = "conteudo_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    // Constructor
    public Conteudo() {}

    public Conteudo(String titulo, String slug, String conteudo, TipoConteudo tipo) {
        this.titulo = titulo;
        this.slug = slug;
        this.conteudo = conteudo;
        this.tipo = tipo;
        this.dataPublicacao = LocalDateTime.now();
    }

    // Business methods
    public void incrementarVisualizacoes() {
        this.visualizacoes++;
    }

    public void atualizarData() {
        this.dataAtualizacao = LocalDateTime.now();
    }

    public void adicionarTag(Tag tag) {
        this.tags.add(tag);
    }

    public void removerTag(Tag tag) {
        this.tags.remove(tag);
    }

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

    public CategoriaConteudo getCategoria() { return categoria; }
    public void setCategoria(CategoriaConteudo categoria) { this.categoria = categoria; }

    public Set<Tag> getTags() { return tags; }
    public void setTags(Set<Tag> tags) { this.tags = tags; }
}