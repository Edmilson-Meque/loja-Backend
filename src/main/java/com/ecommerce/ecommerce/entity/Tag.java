package com.ecommerce.ecommerce.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(name = "slug", unique = true, nullable = false)
    private String slug;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "cor")
    private String cor;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private Set<Conteudo> conteudos = new HashSet<>();

    // Constructor
    public Tag() {}

    public Tag(String nome, String slug) {
        this.nome = nome;
        this.slug = slug;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public Set<Conteudo> getConteudos() { return conteudos; }
    public void setConteudos(Set<Conteudo> conteudos) { this.conteudos = conteudos; }
}
