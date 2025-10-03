package com.vaadin.starter.bakery.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.repositories.UserRepository;

/**
 * Serviço para gerenciamento de operações CRUD relacionadas a usuários.
 * Implementa a interface {@link FilterableCrudService} para fornecer funcionalidades de criação,
 * leitura, atualização e exclusão de entidades {@link User}, com suporte a filtros e paginação.
 * Inclui validações para impedir a modificação ou exclusão de usuários bloqueados e a exclusão de
 * contas pelo próprio usuário.
 */
@Service
public class UserService implements FilterableCrudService<User> {

    /**
     * Mensagem de erro lançada quando tenta-se modificar ou excluir um usuário bloqueado.
     */
    public static final String MODIFY_LOCKED_USER_NOT_PERMITTED = "User has been locked and cannot be modified or deleted";

    /**
     * Mensagem de erro lançada quando um usuário tenta excluir sua própria conta.
     */
    public static final String DELETING_SELF_NOT_PERMITTED = "You cannot delete your own account";

    /**
     * Repositório para acesso aos dados de usuários no banco de dados.
     */
    private final UserRepository userRepository;

    /**
     * Construtor que injeta o repositório de usuários via dependência.
     *
     * @param userRepository o repositório {@link UserRepository} para operações com usuários
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Busca usuários que correspondem a um filtro opcional, com suporte a paginação.
     * Se um filtro for fornecido, realiza uma busca case-insensitive nos campos de e-mail,
     * nome, sobrenome ou papel do usuário. Caso contrário, retorna todos os usuários com paginação.
     *
     * @param filter   um {@link Optional} contendo o texto do filtro (opcional)
     * @param pageable o objeto {@link Pageable} que define a paginação e ordenação
     * @return uma página de {@link User} que atende aos critérios de busca
     */
    public Page<User> findAnyMatching(Optional<String> filter, Pageable pageable) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return getRepository()
                    .findByEmailLikeIgnoreCaseOrFirstNameLikeIgnoreCaseOrLastNameLikeIgnoreCaseOrRoleLikeIgnoreCase(
                            repositoryFilter, repositoryFilter, repositoryFilter, repositoryFilter, pageable);
        } else {
            return find(pageable);
        }
    }

    /**
     * Conta o número de usuários que correspondem a um filtro opcional.
     * Se um filtro for fornecido, realiza uma contagem case-insensitive baseada nos campos
     * de e-mail, nome, sobrenome ou papel do usuário. Caso contrário, retorna a contagem total
     * de usuários.
     *
     * @param filter um {@link Optional} contendo o texto do filtro (opcional)
     * @return o número de usuários que atendem ao filtro
     */
    @Override
    public long countAnyMatching(Optional<String> filter) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return userRepository.countByEmailLikeIgnoreCaseOrFirstNameLikeIgnoreCaseOrLastNameLikeIgnoreCaseOrRoleLikeIgnoreCase(
                    repositoryFilter, repositoryFilter, repositoryFilter, repositoryFilter);
        } else {
            return count();
        }
    }

    /**
     * Obtém o repositório JPA utilizado para operações com usuários.
     *
     * @return o {@link UserRepository} para entidades {@link User}
     */
    @Override
    public UserRepository getRepository() {
        return userRepository;
    }

    /**
     * Busca usuários com suporte a paginação, sem aplicar filtros.
     *
     * @param pageable o objeto {@link Pageable} que define a paginação e ordenação
     * @return uma página de {@link User} contendo os usuários encontrados
     */
    public Page<User> find(Pageable pageable) {
        return getRepository().findBy(pageable);
    }

    /**
     * Salva uma entidade {@link User} no banco de dados.
     * Verifica se o usuário está bloqueado antes de salvar, lançando uma exceção se for o caso.
     *
     * @param currentUser o usuário atual ({@link User}) que está salvando a entidade
     * @param entity      a entidade {@link User} a ser salva
     * @return a entidade {@link User} salva
     * @throws UserFriendlyDataException se o usuário estiver bloqueado
     */
    @Override
    public User save(User currentUser, User entity) {
        throwIfUserLocked(entity);
        return getRepository().saveAndFlush(entity);
    }

    /**
     * Exclui uma entidade {@link User} do banco de dados.
     * Impede a exclusão de um usuário bloqueado ou a exclusão do próprio usuário logado.
     * A operação é executada em uma transação.
     *
     * @param currentUser   o usuário atual ({@link User}) que está realizando a exclusão
     * @param userToDelete  a entidade {@link User} a ser excluída
     * @throws UserFriendlyDataException se o usuário tentar excluir a si mesmo ou se o usuário
     *                                   a ser excluído estiver bloqueado
     */
    @Override
    @Transactional
    public void delete(User currentUser, User userToDelete) {
        throwIfDeletingSelf(currentUser, userToDelete);
        throwIfUserLocked(userToDelete);
        FilterableCrudService.super.delete(currentUser, userToDelete);
    }

    /**
     * Verifica se o usuário atual está tentando excluir sua própria conta.
     * Lança uma exceção caso isso ocorra.
     *
     * @param currentUser o usuário atual ({@link User}) que está realizando a operação
     * @param user        o usuário ({@link User}) a ser excluído
     * @throws UserFriendlyDataException se o usuário atual for igual ao usuário a ser excluído
     */
    private void throwIfDeletingSelf(User currentUser, User user) {
        if (currentUser.equals(user)) {
            throw new UserFriendlyDataException(DELETING_SELF_NOT_PERMITTED);
        }
    }

    /**
     * Verifica se o usuário está bloqueado.
     * Lança uma exceção se o usuário estiver bloqueado, impedindo modificações ou exclusão.
     *
     * @param entity o usuário ({@link User}) a ser verificado
     * @throws UserFriendlyDataException se o usuário estiver bloqueado
     */
    private void throwIfUserLocked(User entity) {
        if (entity != null && entity.isLocked()) {
            throw new UserFriendlyDataException(MODIFY_LOCKED_USER_NOT_PERMITTED);
        }
    }

    /**
     * Cria uma nova instância de {@link User} para uso em operações de criação.
     *
     * @param currentUser o usuário atual ({@link User}) que está criando a entidade
     * @return uma nova instância de {@link User}
     */
    @Override
    public User createNew(User currentUser) {
        return new User();
    }

}