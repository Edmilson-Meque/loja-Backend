package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.ContatoRequestDTO;
import com.ecommerce.ecommerce.dto.ContatoResponseDTO;
import com.ecommerce.ecommerce.dto.RespostaContatoDTO;
import com.ecommerce.ecommerce.entity.Contato;
import com.ecommerce.ecommerce.exception.BadRequestException;
import com.ecommerce.ecommerce.repository.ContatoRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContatoService {

    private final ContatoRepository contatoRepository;
    private final EmailService emailService;

    public ContatoService(
            ContatoRepository contatoRepository,
            EmailService emailService
    ) {
        this.contatoRepository = contatoRepository;
        this.emailService = emailService;
    }

    @Transactional
    public ContatoResponseDTO criar(ContatoRequestDTO dto, HttpServletRequest request) {
        Contato contato = new Contato();
        contato.setNome(dto.getNome());
        contato.setEmail(dto.getEmail());
        contato.setTelefone(dto.getTelefone());
        contato.setAssunto(dto.getAssunto());
        contato.setMensagem(dto.getMensagem());
        contato.setTipoContato(dto.getTipoContato());

        // Capturar informações da requisição
        contato.setIpCliente(getClientIP(request));
        contato.setUserAgent(request.getHeader("User-Agent"));

        Contato salvo = contatoRepository.save(contato);

        // Enviar email de confirmação (opcional)
        try {
            emailService.enviarEmailContato(salvo);
        } catch (Exception e) {
            // Log do erro, mas não falha a operação
            System.err.println("Erro ao enviar email de contato: " + e.getMessage());
        }

        return convertToDTO(salvo);
    }

    @Transactional(readOnly = true)
    public ContatoResponseDTO buscarPorId(Long id) {
        Contato contato = contatoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Contato não encontrado"));
        return convertToDTO(contato);
    }

    @Transactional(readOnly = true)
    public List<ContatoResponseDTO> listarNaoLidos() {
        return contatoRepository.findByLidoFalse().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ContatoResponseDTO> listarNaoRespondidos() {
        return contatoRepository.findByRespondidoFalse().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ContatoResponseDTO> listarTodosPaginado(int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho, Sort.by(Sort.Direction.DESC, "dataEnvio"));
        return contatoRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    @Transactional
    public ContatoResponseDTO marcarComoLido(Long id) {
        Contato contato = contatoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Contato não encontrado"));

        contato.marcarComoLido();
        Contato atualizado = contatoRepository.save(contato);

        return convertToDTO(atualizado);
    }

    @Transactional
    public ContatoResponseDTO responder(Long id, RespostaContatoDTO dto) {
        Contato contato = contatoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Contato não encontrado"));

        if (contato.isRespondido()) {
            throw new BadRequestException("Este contato já foi respondido");
        }

        contato.responder(dto.getResposta());
        Contato atualizado = contatoRepository.save(contato);

        // Enviar email de resposta
        try {
            emailService.enviarEmailResposta(contato, dto.getResposta());
        } catch (Exception e) {
            // Log do erro, mas não falha a operação
            System.err.println("Erro ao enviar email de resposta: " + e.getMessage());
        }

        return convertToDTO(atualizado);
    }

    @Transactional
    public void deletar(Long id) {
        Contato contato = contatoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Contato não encontrado"));

        contatoRepository.delete(contato);
    }

    @Transactional(readOnly = true)
    public long contarNaoLidos() {
        return contatoRepository.countByLidoFalse();
    }

    @Transactional(readOnly = true)
    public long contarNaoRespondidos() {
        return contatoRepository.countByRespondidoFalse();
    }

    private ContatoResponseDTO convertToDTO(Contato contato) {
        ContatoResponseDTO dto = new ContatoResponseDTO();
        dto.setId(contato.getId());
        dto.setNome(contato.getNome());
        dto.setEmail(contato.getEmail());
        dto.setTelefone(contato.getTelefone());
        dto.setAssunto(contato.getAssunto());
        dto.setMensagem(contato.getMensagem());
        dto.setTipoContato(contato.getTipoContato());
        dto.setDataEnvio(contato.getDataEnvio());
        dto.setLido(contato.isLido());
        dto.setRespondido(contato.isRespondido());
        dto.setResposta(contato.getResposta());
        dto.setDataResposta(contato.getDataResposta());
        dto.setIpCliente(contato.getIpCliente());
        dto.setUserAgent(contato.getUserAgent());
        return dto;
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}