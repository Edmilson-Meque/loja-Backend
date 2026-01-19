package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.*;
import com.ecommerce.ecommerce.entity.*;
import com.ecommerce.ecommerce.exception.BadRequestException;
import com.ecommerce.ecommerce.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConteudoService {

    private final ConteudoRepository conteudoRepository;
    private final CategoriaConteudoRepository categoriaConteudoRepository;
    private final TagRepository tagRepository;
    private final ImageService imageService;

    public ConteudoService(
            ConteudoRepository conteudoRepository,
            CategoriaConteudoRepository categoriaConteudoRepository,
            TagRepository tagRepository,
            ImageService imageService
    ) {
        this.conteudoRepository = conteudoRepository;
        this.categoriaConteudoRepository = categoriaConteudoRepository;
        this.tagRepository = tagRepository;
        this.imageService = imageService;
    }

    // ============ CRUD CONTEÚDO ============

    @Transactional
    public ConteudoResponseDTO criar(ConteudoRequestDTO dto) {
        // Validar slug único
        if (conteudoRepository.existsBySlug(dto.getSlug())) {
            throw new BadRequestException("Slug já está em uso: " + dto.getSlug());
        }

        Conteudo conteudo = new Conteudo();
        conteudo.setTitulo(dto.getTitulo());
        conteudo.setSlug(dto.getSlug());
        conteudo.setConteudo(dto.getConteudo());
        conteudo.setDescricaoCurta(dto.getDescricaoCurta());
        conteudo.setTipo(dto.getTipo());
        conteudo.setAutor(dto.getAutor() != null ? dto.getAutor() : "Administrador");
        conteudo.setMetaTitulo(dto.getMetaTitulo());
        conteudo.setMetaDescricao(dto.getMetaDescricao());
        conteudo.setMetaKeywords(dto.getMetaKeywords());
        conteudo.setAtivo(dto.isAtivo());
        conteudo.setDestaque(dto.isDestaque());
        conteudo.setOrdem(dto.getOrdem());

        // Imagem de capa
        if (dto.getImagemCapa() != null) {
            conteudo.setImagemCapa(dto.getImagemCapa());
        }

        // Data de publicação
        if (dto.getDataPublicacao() != null) {
            conteudo.setDataPublicacao(dto.getDataPublicacao());
        } else {
            conteudo.setDataPublicacao(LocalDateTime.now());
        }

        // Categoria
        if (dto.getCategoriaId() != null) {
            CategoriaConteudo categoria = categoriaConteudoRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new BadRequestException("Categoria não encontrada"));
            conteudo.setCategoria(categoria);
        }

        // Tags
        if (dto.getTagIds() != null && !dto.getTagIds().isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(dto.getTagIds());
            conteudo.setTags(new HashSet<>(tags));
        }

        Conteudo salvo = conteudoRepository.save(conteudo);
        return convertToDTO(salvo);
    }

    @Transactional(readOnly = true)
    public ConteudoResponseDTO buscarPorId(Long id) {
        Conteudo conteudo = conteudoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Conteúdo não encontrado"));

        // Incrementar visualizações
        conteudo.incrementarVisualizacoes();
        conteudoRepository.save(conteudo);

        return convertToDTO(conteudo);
    }

    @Transactional(readOnly = true)
    public ConteudoResponseDTO buscarPorSlug(String slug) {
        Conteudo conteudo = conteudoRepository.findBySlug(slug)
                .orElseThrow(() -> new BadRequestException("Conteúdo não encontrado"));

        if (!conteudo.isAtivo()) {
            throw new BadRequestException("Conteúdo não está disponível");
        }

        // Incrementar visualizações
        conteudo.incrementarVisualizacoes();
        conteudoRepository.save(conteudo);

        return convertToDTO(conteudo);
    }

    @Transactional(readOnly = true)
    public List<ConteudoSimplificadoDTO> listarTodos() {
        return conteudoRepository.findAll().stream()
                .map(this::convertToSimplificadoDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PaginatedResponseDTO<ConteudoSimplificadoDTO> buscarConteudos(BuscaConteudoDTO filtros) {
        // Configurar paginação
        Sort sort = Sort.by(filtros.getDirecao().equalsIgnoreCase("ASC") ?
                Sort.Direction.ASC : Sort.Direction.DESC, filtros.getOrdenarPor());
        Pageable pageable = PageRequest.of(filtros.getPagina(), filtros.getTamanho(), sort);

        Page<Conteudo> paginaConteudos;

        // Aplicar filtros
        if (filtros.getTermo() != null && !filtros.getTermo().trim().isEmpty()) {
            // Busca por termo
            paginaConteudos = conteudoRepository.findAll((root, query, cb) -> {
                List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
                predicates.add(cb.like(cb.lower(root.get("titulo")), "%" + filtros.getTermo().toLowerCase() + "%"));

                if (filtros.getTipo() != null) {
                    predicates.add(cb.equal(root.get("tipo"), filtros.getTipo()));
                }
                if (filtros.getAtivo() != null) {
                    predicates.add(cb.equal(root.get("ativo"), filtros.getAtivo()));
                }

                return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
            }, pageable);
        } else {
            // Busca sem termo
            paginaConteudos = conteudoRepository.findAll((root, query, cb) -> {
                List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();

                if (filtros.getTipo() != null) {
                    predicates.add(cb.equal(root.get("tipo"), filtros.getTipo()));
                }
                if (filtros.getAtivo() != null) {
                    predicates.add(cb.equal(root.get("ativo"), filtros.getAtivo()));
                }
                if (filtros.getDestaque() != null) {
                    predicates.add(cb.equal(root.get("destaque"), filtros.getDestaque()));
                }
                if (filtros.getCategoriaId() != null) {
                    predicates.add(cb.equal(root.get("categoria").get("id"), filtros.getCategoriaId()));
                }
                if (filtros.getDataInicio() != null && filtros.getDataFim() != null) {
                    predicates.add(cb.between(root.get("dataPublicacao"),
                            filtros.getDataInicio(), filtros.getDataFim()));
                }

                return predicates.isEmpty() ? null : cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
            }, pageable);
        }

        List<ConteudoSimplificadoDTO> conteudosDTO = paginaConteudos.getContent().stream()
                .map(this::convertToSimplificadoDTO)
                .collect(Collectors.toList());

        return new PaginatedResponseDTO<>(
                conteudosDTO,
                paginaConteudos.getNumber(),
                paginaConteudos.getTotalPages(),
                paginaConteudos.getTotalElements(),
                paginaConteudos.getSize()
        );
    }

    @Transactional
    public ConteudoResponseDTO atualizar(Long id, ConteudoRequestDTO dto) {
        Conteudo conteudo = conteudoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Conteúdo não encontrado"));

        // Verificar se slug foi alterado e se é único
        if (!conteudo.getSlug().equals(dto.getSlug()) &&
                conteudoRepository.existsBySlug(dto.getSlug())) {
            throw new BadRequestException("Slug já está em uso: " + dto.getSlug());
        }

        conteudo.setTitulo(dto.getTitulo());
        conteudo.setSlug(dto.getSlug());
        conteudo.setConteudo(dto.getConteudo());
        conteudo.setDescricaoCurta(dto.getDescricaoCurta());
        conteudo.setTipo(dto.getTipo());
        conteudo.setImagemCapa(dto.getImagemCapa());
        conteudo.setAutor(dto.getAutor());
        conteudo.setMetaTitulo(dto.getMetaTitulo());
        conteudo.setMetaDescricao(dto.getMetaDescricao());
        conteudo.setMetaKeywords(dto.getMetaKeywords());
        conteudo.setAtivo(dto.isAtivo());
        conteudo.setDestaque(dto.isDestaque());
        conteudo.setOrdem(dto.getOrdem());
        conteudo.atualizarData();

        // Atualizar categoria
        if (dto.getCategoriaId() != null) {
            CategoriaConteudo categoria = categoriaConteudoRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new BadRequestException("Categoria não encontrada"));
            conteudo.setCategoria(categoria);
        } else {
            conteudo.setCategoria(null);
        }

        // Atualizar tags
        if (dto.getTagIds() != null) {
            List<Tag> tags = tagRepository.findAllById(dto.getTagIds());
            conteudo.setTags(new HashSet<>(tags));
        }

        Conteudo atualizado = conteudoRepository.save(conteudo);
        return convertToDTO(atualizado);
    }

    @Transactional
    public void deletar(Long id) {
        Conteudo conteudo = conteudoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Conteúdo não encontrado"));

        // Não deletar conteúdos importantes do sistema
        if (conteudo.getTipo() == TipoConteudo.TERMOS ||
                conteudo.getTipo() == TipoConteudo.POLITICA ||
                conteudo.getTipo() == TipoConteudo.SOBRE) {
            throw new BadRequestException("Não é possível deletar conteúdos importantes do sistema");
        }

        conteudoRepository.delete(conteudo);
    }

    // ============ MÉTODOS ESPECÍFICOS ============

    @Transactional(readOnly = true)
    public List<ConteudoSimplificadoDTO> listarPorTipo(TipoConteudo tipo) {
        return conteudoRepository.findByTipo(tipo).stream()
                .filter(Conteudo::isAtivo)
                .map(this::convertToSimplificadoDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ConteudoSimplificadoDTO> listarDestaques() {
        return conteudoRepository.findByDestaqueTrueAndAtivoTrue().stream()
                .map(this::convertToSimplificadoDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ConteudoSimplificadoDTO> listarRecentes(int limite) {
        Pageable pageable = PageRequest.of(0, limite, Sort.by(Sort.Direction.DESC, "dataPublicacao"));
        return conteudoRepository.findAtivosRecentes(LocalDateTime.now(), pageable)
                .stream()
                .map(this::convertToSimplificadoDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ConteudoSimplificadoDTO> listarMaisVistos(int limite) {
        return conteudoRepository.findMaisVistos(limite).stream()
                .map(this::convertToSimplificadoDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ConteudoResponseDTO atualizarStatus(Long id, boolean ativo) {
        Conteudo conteudo = conteudoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Conteúdo não encontrado"));

        conteudo.setAtivo(ativo);
        conteudo.atualizarData();

        Conteudo atualizado = conteudoRepository.save(conteudo);
        return convertToDTO(atualizado);
    }

    @Transactional
    public ConteudoResponseDTO atualizarDestaque(Long id, boolean destaque) {
        Conteudo conteudo = conteudoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Conteúdo não encontrado"));

        conteudo.setDestaque(destaque);
        conteudo.atualizarData();

        Conteudo atualizado = conteudoRepository.save(conteudo);
        return convertToDTO(atualizado);
    }

    @Transactional(readOnly = true)
    public SlugValidationDTO validarSlug(String slug) {
        boolean disponivel = !conteudoRepository.existsBySlug(slug);
        return new SlugValidationDTO(slug, disponivel);
    }

    // ============ CONVERSORES DTO ============

    private ConteudoResponseDTO convertToDTO(Conteudo conteudo) {
        ConteudoResponseDTO dto = new ConteudoResponseDTO();
        dto.setId(conteudo.getId());
        dto.setTitulo(conteudo.getTitulo());
        dto.setSlug(conteudo.getSlug());
        dto.setConteudo(conteudo.getConteudo());
        dto.setDescricaoCurta(conteudo.getDescricaoCurta());
        dto.setTipo(conteudo.getTipo());
        dto.setImagemCapa(conteudo.getImagemCapa());
        dto.setDataPublicacao(conteudo.getDataPublicacao());
        dto.setDataAtualizacao(conteudo.getDataAtualizacao());
        dto.setAutor(conteudo.getAutor());
        dto.setMetaTitulo(conteudo.getMetaTitulo());
        dto.setMetaDescricao(conteudo.getMetaDescricao());
        dto.setMetaKeywords(conteudo.getMetaKeywords());
        dto.setAtivo(conteudo.isAtivo());
        dto.setDestaque(conteudo.isDestaque());
        dto.setVisualizacoes(conteudo.getVisualizacoes());
        dto.setOrdem(conteudo.getOrdem());

        // Categoria
        if (conteudo.getCategoria() != null) {
            CategoriaConteudoDTO categoriaDTO = new CategoriaConteudoDTO();
            categoriaDTO.setId(conteudo.getCategoria().getId());
            categoriaDTO.setNome(conteudo.getCategoria().getNome());
            categoriaDTO.setSlug(conteudo.getCategoria().getSlug());
            categoriaDTO.setDescricao(conteudo.getCategoria().getDescricao());
            categoriaDTO.setIcone(conteudo.getCategoria().getIcone());
            categoriaDTO.setCor(conteudo.getCategoria().getCor());
            dto.setCategoria(categoriaDTO);
        }

        // Tags
        if (conteudo.getTags() != null && !conteudo.getTags().isEmpty()) {
            List<TagDTO> tagsDTO = conteudo.getTags().stream()
                    .map(tag -> {
                        TagDTO tagDTO = new TagDTO();
                        tagDTO.setId(tag.getId());
                        tagDTO.setNome(tag.getNome());
                        tagDTO.setSlug(tag.getSlug());
                        tagDTO.setDescricao(tag.getDescricao());
                        tagDTO.setCor(tag.getCor());
                        tagDTO.setDataCriacao(tag.getDataCriacao());
                        return tagDTO;
                    })
                    .collect(Collectors.toList());
            dto.setTags(tagsDTO);
        }

        return dto;
    }

    private ConteudoSimplificadoDTO convertToSimplificadoDTO(Conteudo conteudo) {
        ConteudoSimplificadoDTO dto = new ConteudoSimplificadoDTO();
        dto.setId(conteudo.getId());
        dto.setTitulo(conteudo.getTitulo());
        dto.setSlug(conteudo.getSlug());
        dto.setDescricaoCurta(conteudo.getDescricaoCurta());
        dto.setTipo(conteudo.getTipo());
        dto.setImagemCapa(conteudo.getImagemCapa());
        dto.setDataPublicacao(conteudo.getDataPublicacao());
        dto.setAutor(conteudo.getAutor());
        dto.setDestaque(conteudo.isDestaque());
        dto.setVisualizacoes(conteudo.getVisualizacoes());

        if (conteudo.getCategoria() != null) {
            dto.setCategoriaNome(conteudo.getCategoria().getNome());
        }

        return dto;
    }

    // ============ ESTATÍSTICAS ============

    @Transactional(readOnly = true)
    public EstatisticasCMSDTO getEstatisticas() {
        EstatisticasCMSDTO estatisticas = new EstatisticasCMSDTO();

        estatisticas.setTotalConteudos(conteudoRepository.count());
        estatisticas.setTotalConteudosAtivos(conteudoRepository.countByAtivoTrue());

        // Conteúdos por tipo
        Map<String, Long> conteudosPorTipo = new HashMap<>();
        for (TipoConteudo tipo : TipoConteudo.values()) {
            long count = conteudoRepository.countByTipo(tipo);
            conteudosPorTipo.put(tipo.getDescricao(), count);
        }
        estatisticas.setConteudosPorTipo(conteudosPorTipo);

        return estatisticas;
    }
}