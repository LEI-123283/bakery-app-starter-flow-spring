package com.vaadin.starter.bakery.backend.data;

import java.util.LinkedHashMap;
import java.util.List;

import com.vaadin.starter.bakery.backend.data.entity.Product;

/**
 * {@code DashboardData} é um DTO usado para transportar informações
 * agregadas exibidas no painel de controle da aplicação.
 *
 * <p>Contém estatísticas de entregas, vendas mensais e anuais,
 * além de dados relacionados a produtos mais entregues.</p>
 */
public class DashboardData {

	/** Estatísticas gerais de entregas. */
	private DeliveryStats deliveryStats;

	/** Número de entregas por dia no mês atual. */
	private List<Number> deliveriesThisMonth;

	/** Número de entregas por mês no ano atual. */
	private List<Number> deliveriesThisYear;

	/**
	 * Vendas por mês ao longo do tempo.
	 * Cada linha representa um mês e contém números (ex.: total de vendas, receita).
	 */
	private Number[][] salesPerMonth;

	/** Mapeamento de produtos e a quantidade de entregas realizadas. */
	private LinkedHashMap<Product, Integer> productDeliveries;

	/**
	 * Obtém as estatísticas de entrega.
	 *
	 * @return estatísticas de entrega
	 */
	public DeliveryStats getDeliveryStats() {
		return deliveryStats;
	}

	/**
	 * Define as estatísticas de entrega.
	 *
	 * @param deliveryStats estatísticas a serem atribuídas
	 */
	public void setDeliveryStats(DeliveryStats deliveryStats) {
		this.deliveryStats = deliveryStats;
	}

	/**
	 * Obtém os números de entregas deste mês.
	 *
	 * @return lista com número de entregas por dia do mês atual
	 */
	public List<Number> getDeliveriesThisMonth() {
		return deliveriesThisMonth;
	}

	/**
	 * Define os números de entregas deste mês.
	 *
	 * @param deliveriesThisMonth lista com número de entregas por dia
	 */
	public void setDeliveriesThisMonth(List<Number> deliveriesThisMonth) {
		this.deliveriesThisMonth = deliveriesThisMonth;
	}

	/**
	 * Obtém os números de entregas deste ano.
	 *
	 * @return lista com número de entregas por mês do ano atual
	 */
	public List<Number> getDeliveriesThisYear() {
		return deliveriesThisYear;
	}

	/**
	 * Define os números de entregas deste ano.
	 *
	 * @param deliveriesThisYear lista com número de entregas por mês
	 */
	public void setDeliveriesThisYear(List<Number> deliveriesThisYear) {
		this.deliveriesThisYear = deliveriesThisYear;
	}

	/**
	 * Define a matriz de vendas por mês.
	 *
	 * @param salesPerMonth matriz onde cada linha representa um mês
	 */
	public void setSalesPerMonth(Number[][] salesPerMonth) {
		this.salesPerMonth = salesPerMonth;
	}

	/**
	 * Obtém os dados de vendas para um mês específico.
	 *
	 * @param i índice do mês (0 = janeiro, 11 = dezembro)
	 * @return vetor de valores de vendas do mês informado
	 */
	public Number[] getSalesPerMonth(int i) {
		return salesPerMonth[i];
	}

	/**
	 * Obtém o mapeamento de produtos e suas quantidades de entregas.
	 *
	 * @return mapa de {@link Product} para quantidade de entregas
	 */
	public LinkedHashMap<Product, Integer> getProductDeliveries() {
		return productDeliveries;
	}

	/**
	 * Define o mapeamento de produtos e suas quantidades de entregas.
	 *
	 * @param productDeliveries mapa de {@link Product} para quantidade de entregas
	 */
	public void setProductDeliveries(LinkedHashMap<Product, Integer> productDeliveries) {
		this.productDeliveries = productDeliveries;
	}
}
