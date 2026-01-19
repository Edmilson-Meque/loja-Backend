package com.ecommerce.ecommerce.dto;

import java.util.List;

public class PaginatedResponseDTO<T> {

    private List<T> content;
    private int paginaAtual;
    private int totalPaginas;
    private long totalElementos;
    private int tamanhoPagina;
    private boolean primeiraPagina;
    private boolean ultimaPagina;

    // Constructor
    public PaginatedResponseDTO() {}

    public PaginatedResponseDTO(List<T> content, int paginaAtual, int totalPaginas,
                                long totalElementos, int tamanhoPagina) {
        this.content = content;
        this.paginaAtual = paginaAtual;
        this.totalPaginas = totalPaginas;
        this.totalElementos = totalElementos;
        this.tamanhoPagina = tamanhoPagina;
        this.primeiraPagina = paginaAtual == 0;
        this.ultimaPagina = paginaAtual == totalPaginas - 1;
    }

    // Getters and Setters
    public List<T> getContent() { return content; }
    public void setContent(List<T> content) { this.content = content; }

    public int getPaginaAtual() { return paginaAtual; }
    public void setPaginaAtual(int paginaAtual) { this.paginaAtual = paginaAtual; }

    public int getTotalPaginas() { return totalPaginas; }
    public void setTotalPaginas(int totalPaginas) { this.totalPaginas = totalPaginas; }

    public long getTotalElementos() { return totalElementos; }
    public void setTotalElementos(long totalElementos) { this.totalElementos = totalElementos; }

    public int getTamanhoPagina() { return tamanhoPagina; }
    public void setTamanhoPagina(int tamanhoPagina) { this.tamanhoPagina = tamanhoPagina; }

    public boolean isPrimeiraPagina() { return primeiraPagina; }
    public void setPrimeiraPagina(boolean primeiraPagina) { this.primeiraPagina = primeiraPagina; }

    public boolean isUltimaPagina() { return ultimaPagina; }
    public void setUltimaPagina(boolean ultimaPagina) { this.ultimaPagina = ultimaPagina; }
}