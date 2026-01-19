package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.CartItemRequestDTO;
import com.ecommerce.ecommerce.dto.CartResponseDTO;
import com.ecommerce.ecommerce.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/carrinho")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public CartResponseDTO buscarCarrinho() {
        return cartService.buscarCarrinho();
    }

    @PostMapping("/itens")
    @ResponseStatus(HttpStatus.CREATED)
    public CartResponseDTO adicionarItem(@Valid @RequestBody CartItemRequestDTO dto) {
        return cartService.adicionarItem(dto);
    }

    @PutMapping("/itens/{produtoId}")
    public CartResponseDTO atualizarQuantidade(
            @PathVariable Long produtoId,
            @RequestParam Integer quantidade) {
        return cartService.atualizarQuantidade(produtoId, quantidade);
    }

    @DeleteMapping("/itens/{produtoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerItem(@PathVariable Long produtoId) {
        cartService.removerItem(produtoId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void limparCarrinho() {
        cartService.limparCarrinho();
    }

    @GetMapping("/contagem")
    public Integer contarItens() {
        return cartService.contarItens();
    }

    @GetMapping("/total")
    public BigDecimal calcularTotal() {
        return cartService.calcularTotal();
    }
}