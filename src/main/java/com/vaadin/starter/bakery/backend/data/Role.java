package com.vaadin.starter.bakery.backend.data;

/**
 * Classe utilitária que define os diferentes papéis (roles) disponíveis no sistema.
 *
 * <p>
 * Os papéis são usados para controlar permissões de acesso às funcionalidades
 * e às views da aplicação. Cada papel é representado por uma constante {@code String}.
 * </p>
 *
 * <ul>
 *   <li>{@link #BARISTA} - Usuário com permissões relacionadas ao preparo de bebidas.</li>
 *   <li>{@link #BAKER} - Usuário com permissões relacionadas ao preparo de alimentos.</li>
 *   <li>{@link #ADMIN} - Usuário administrador, com acesso a todas as views e permissões.</li>
 * </ul>
 *
 * A classe não pode ser instanciada.
 */
public class Role {

	/** Papel de usuário com permissões de barista. */
	public static final String BARISTA = "barista";

	/** Papel de usuário com permissões de padeiro. */
	public static final String BAKER = "baker";

	/**
	 * Papel de usuário administrador.
	 * <p>
	 * Este papel implicitamente concede acesso a todas as views e funcionalidades do sistema.
	 * </p>
	 */
	public static final String ADMIN = "admin";

	/**
	 * Construtor privado para evitar instanciação.
	 * A classe deve ser usada apenas de forma estática.
	 */
	private Role() {
		// Static methods and fields only
	}

	/**
	 * Retorna todos os papéis disponíveis no sistema.
	 *
	 * @return um array contendo {@link #BARISTA}, {@link #BAKER} e {@link #ADMIN}
	 */
	public static String[] getAllRoles() {
		return new String[] { BARISTA, BAKER, ADMIN };
	}

}
