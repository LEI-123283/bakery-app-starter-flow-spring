package com.vaadin.starter.bakery.backend.data;

import java.util.Locale;
import com.vaadin.flow.shared.util.SharedUtil;

/**
 * Enumeração que representa os diferentes estados de uma encomenda no sistema.
 * <p>
 * Os estados possíveis são:
 * <ul>
 *     <li>{@link #NEW} - Nova encomenda, ainda não confirmada.</li>
 *     <li>{@link #CONFIRMED} - Encomenda confirmada.</li>
 *     <li>{@link #READY} - Encomenda pronta para entrega.</li>
 *     <li>{@link #DELIVERED} - Encomenda entregue ao cliente.</li>
 *     <li>{@link #PROBLEM} - Encomenda com algum problema.</li>
 *     <li>{@link #CANCELLED} - Encomenda cancelada.</li>
 * </ul>
 * </p>
 * <p>
 * Este enum também fornece um método utilitário para obter o nome do estado em um formato legível para humanos.
 * </p>
 *
 * @author
 */
public enum OrderState {
	NEW, CONFIRMED, READY, DELIVERED, PROBLEM, CANCELLED;

	/**
	 * Retorna o nome do estado de forma amigável e legível para humanos.
	 * <p>
	 * Por exemplo, {@code NEW} será convertido para {@code "New"}.
	 * </p>
	 *
	 * @return uma versão amigável e capitalizada do identificador do enum
	 */
	public String getDisplayName() {
		return SharedUtil.capitalize(name().toLowerCase(Locale.ENGLISH));
	}
}

