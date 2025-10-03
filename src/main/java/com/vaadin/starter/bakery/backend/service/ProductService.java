package com.vaadin.starter.bakery.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.repositories.ProductRepository;

/**
 * Serviço para gerenciamento de operações CRUD relacionadas a produtos.
 * Implementa a interface {@link FilterableCrudService} para fornecer funcionalidades de criação,
 * leitura, atualização e exclusão de entidades {@link Product}, com suporte a filtros e paginação.
 */
@Service
public class ProductService implements FilterableCrudService<Product> {

    /**
     * Repositório para acesso aos dados de produtos no banco de dados.
     */
    private final ProductRepository productRepository;

    /**
     * Construtor que injeta o repositório de produtos via dependência.
     *
     * @param productRepository o repositório {@link ProductRepository} para operações com produtos
     */
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Busca produtos que correspondem a um filtro opcional, com suporte a paginação.
     * Se um filtro for fornecido, realiza uma busca case-insensitive no nome do produto.
     * Caso contrário, retorna todos os produtos com paginação.
     *
     * @param filter   um {@link Optional} contendo o texto do filtro (opcional)
     * @param pageable o objeto {@link Pageable} que define a paginação e ordenação
     * @return uma página de {@link Product} que atende aos critérios de busca
     */
    @Override
    public Page<Product> findAnyMatching(Optional<String> filter, Pageable pageable) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return productRepository.findByNameLikeIgnoreCase(repositoryFilter, pageable);
        } else {
            return find(pageable);
        }
    }

    /**
     * Conta o número de produtos que correspondem a um filtro opcional.
     * Se um filtro for fornecido, realiza uma contagem case-insensitive baseada no nome do produto.
     * Caso contrário, retorna a contagem total de produtos.
     *
     * @param filter um {@link Optional} contendo o texto do filtro (opcional)
     * @return o número de produtos que atendem ao filtro
     */
    @Override
    public long countAnyMatching(Optional<String> filter) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return productRepository.countByNameLikeIgnoreCase(repositoryFilter);
        } else {
            return count();
        }
    }

    /**
     * Busca produtos com suporte a paginação, sem aplicar filtros.
     *
     * @param pageable o objeto {@link Pageable} que define a paginação e ordenação
     * @return uma página de {@link Product} contendo os produtos encontrados
     */
    public Page<Product> find(Pageable pageable) {
        return productRepository.findBy(pageable);
    }

    /**
     * Obtém o repositório JPA utilizado para operações com produtos.
     *
     * @return o {@link JpaRepository} para entidades {@link Product}
     */
    @Override
    public JpaRepository<Product, Long> getRepository() {
        return productRepository;
    }

    /**
     * Cria uma nova instância de {@link Product} para uso em operações de criação.
     *
     * @param currentUser o usuário atual ({@link User}) que está criando o produto
     * @return uma nova instância de {@link Product}
     */
    @Override
    public Product createNew(User currentUser) {
        return new Product();
    }

    /**
     * Salva uma entidade {@link Product} no banco de dados.
     * Lança uma exceção amigável se houver violação de integridade (ex: nome duplicado).
     *
     * @param currentUser o usuário atual ({@link User}) que está salvando o produto
     * @param entity      a entidade {@link Product} a ser salva
     * @return a entidade {@link Product} salva
     * @throws UserFriendlyDataException se já existir um produto com o mesmo nome
     */
    @Override
    public Product save(User currentUser, Product entity) {
        try {
            return FilterableCrudService.super.save(currentUser, entity);
        } catch (DataIntegrityViolationException e) {
            throw new UserFriendlyDataException(
                    "Já existe um produto com esse nome. Por favor, escolha um nome único para o produto.");
        }
    }

}