package com.vaadin.starter.bakery.backend.data;

/**
 * Classe utilitária que define constantes para os papéis (roles) de usuário no sistema.
 * Contém papéis predefinidos para barista, padeiro e administrador, usados para controle de acesso.
 * Esta classe não deve ser instanciada, pois contém apenas membros estáticos.
 */
public class Role {

    public static final String BARISTA = "barista";

    /**
     * Constante que representa o papel de padeiro no sistema.
     * Usada para identificar usuários com permissões de padeiro.
     */
    public static final String BAKER = "baker";

    /**
     * Constante que representa o papel de administrador no sistema.
     * Este papel concede acesso implícito a todas as visualizações do sistema.
     */
    public static final String ADMIN = "admin";

    /**
     * Construtor privado para impedir a instânciação da classe.
     * Esta classe é projetada para conter apenas métodos e campos estáticos.
     */
    private Role() {
        // Static methods and fields only
    }

    /**
     * Obtém um array contendo todos os papéis definidos no sistema.
     *
     * @return um array de {@link String} com os papéis {@link #BARISTA}, {@link #BAKER} e {@link #ADMIN}
     */
    public static String[] getAllRoles() {
        return new String[] { BARISTA, BAKER, ADMIN };
    }

}