package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.*;
import com.ecommerce.ecommerce.entity.StatusPedido;
import com.ecommerce.ecommerce.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> criarPedido(@Valid @RequestBody CriarPedidoRequestDTO dto) {
        PedidoResponseDTO pedido = pedidoService.criarPedido(dto);

        Map<String, Object> response = new HashMap<>();
        response.put("pedido", pedido);
        response.put("mensagem", "Pedido criado com sucesso!");
        response.put("instrucoes", "Para finalizar o pagamento, clique no link do WhatsApp abaixo e envie a mensagem para nossa equipe.");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public PedidoResponseDTO buscarPedido(@PathVariable Long id) {
        return pedidoService.buscarPedido(id);
    }

    @PostMapping("/{id}/reenviar-whatsapp")
    public ResponseEntity<?> reenviarWhatsapp(@PathVariable Long id) {
        PedidoResponseDTO pedido = pedidoService.reenviarWhatsapp(id);

        Map<String, Object> response = new HashMap<>();
        response.put("pedido", pedido);
        response.put("mensagem", "Link do WhatsApp regenerado com sucesso!");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/mensagem-whatsapp")
    public ResponseEntity<?> obterMensagemWhatsapp(@PathVariable Long id) {
        PedidoResponseDTO pedido = pedidoService.buscarPedido(id);

        if (pedido.getLinkWhatsapp() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "Link do WhatsApp não disponível para este pedido"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("linkWhatsapp", pedido.getLinkWhatsapp());
        response.put("codigoPedido", pedido.getCodigoPedido());
        response.put("instrucoes", "Clique no link para abrir o WhatsApp com a mensagem do pedido pré-preparada");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/cancelar")
    public PedidoResponseDTO cancelarPedido(@PathVariable Long id) {
        return pedidoService.cancelarPedido(id);
    }

    // ENDPOINTS ADMIN
    @PutMapping("/admin/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public PedidoResponseDTO atualizarStatusPedido(
            @PathVariable Long id,
            @RequestParam StatusPedido status) {
        return pedidoService.atualizarStatusPedido(id, status);
    }

    @GetMapping("/admin/instrucoes-whatsapp")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getInstrucoesWhatsapp() {
        Map<String, String> instrucoes = new HashMap<>();
        instrucoes.put("mensagem", "Como configurar o WhatsApp para pedidos:");
        instrucoes.put("passo1", "1. Configure o número da empresa no arquivo application.properties");
        instrucoes.put("passo2", "2. Exemplo: app.whatsapp.numero=5511999999999 (com código do país, sem espaços)");
        instrucoes.put("passo3", "3. A mensagem será automaticamente formatada com os detalhes do pedido");
        instrucoes.put("passo4", "4. O cliente será redirecionado para o WhatsApp com a mensagem pronta");
        instrucoes.put("passo5", "5. A equipe de vendas deve receber a mensagem e confirmar o pagamento");

        return ResponseEntity.ok(instrucoes);
    }
}