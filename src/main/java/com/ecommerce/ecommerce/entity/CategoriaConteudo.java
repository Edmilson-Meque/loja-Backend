package com.ecommerce.ecommerce.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categorias_conteudo")
public class CategoriaConteudo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "slug", unique = true, nullable = false)
    private String slug;

    @Column(name = "icone")
    private String icone;

    @Column(name = "cor")
    private String cor;

    @Column(name = "ordem")
    private Integer ordem = 0;

    @Column(name = "ativo")
    private boolean ativo = true;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao = LocalDateTime.now();

    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    private List<Conteudo> conteudos = new ArrayList<>();

    // Constructor
    public CategoriaConteudo() {}

    public CategoriaConteudo(String nome, String slug, String descricao) {
        this.nome = nome;
        this.slug = slug;
        this.descricao = descricao;
    }

    // Business methods
    public void atualizarData() {
        this.dataAtualizacao = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getIcone() { return icone; }
    public void setIcone(String icone) { this.icone = icone; }

    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    public List<Conteudo> getConteudos() { return conteudos; }
    public void setConteudos(List<Conteudo> conteudos) { this.conteudos = conteudos; }
}
