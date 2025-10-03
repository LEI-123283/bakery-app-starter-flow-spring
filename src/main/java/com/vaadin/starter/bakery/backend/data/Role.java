package com.vaadin.starter.bakery.backend.data;


/**
 * Classe utilitária que define os papéis (roles) de utilizadores na aplicação.
 *
 * <p>
 * Cada papel representa um conjunto de permissões e acesso a diferentes áreas
 * da aplicação. Os papéis estão representados como constantes do tipo
 * {@code String}.
 * </p>
 *
 * <ul>
 *   <li>{@link #BARISTA} – Papel para utilizadores com permissões de barista.</li>
 *   <li>{@link #BAKER} – Papel para utilizadores com permissões de padeiro.</li>
 *   <li>{@link #ADMIN} – Papel de administrador, que implicitamente concede acesso
 *       a todas as vistas da aplicação.</li>
 * </ul>
 *
 * <p>
 * Esta classe não pode ser instanciada. Use os métodos estáticos disponibilizados.
 * </p>
 */

  /**
 * comenatrio
 * */

public class Role {

	/** Papel atribuído a utilizadores com permissões de barista. */
	public static final String BARISTA = "barista";

	/** Papel atribuído a utilizadores com permissões de padeiro. */
	public static final String BAKER = "baker";

	/**
	 * Papel de administrador.
	 * <p>
	 * Este papel concede acesso a todas as vistas e funcionalidades da aplicação.
	 * </p>
	 */
	public static final String ADMIN = "admin";

	/**
	 * Construtor privado para evitar instanciação.
	 * <p>
	 * Esta classe deve ser usada apenas de forma estática.
	 * </p>
	 */
	private Role() {
		// Apenas métodos e campos estáticos
	}

	/**
	 * Devolve um array contendo todos os papéis definidos na aplicação.
	 *
	 * @return um array de {@code String} com todos os papéis possíveis
	 */
	public static String[] getAllRoles() {
		return new String[] { BARISTA, BAKER, ADMIN };
	}

}
