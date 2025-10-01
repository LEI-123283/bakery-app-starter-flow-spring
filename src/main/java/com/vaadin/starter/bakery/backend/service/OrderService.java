package com.vaadin.starter.bakery.backend.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.vaadin.starter.bakery.backend.data.DashboardData;
import com.vaadin.starter.bakery.backend.data.DeliveryStats;
import com.vaadin.starter.bakery.backend.data.OrderState;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.OrderSummary;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.repositories.OrderRepository;

/**
 * Serviço de negócios para manipulação de {@link Order}.
 *
 * <p>Responsável por:</p>
 * <ul>
 *     <li>Criar, atualizar e salvar pedidos</li>
 *     <li>Adicionar comentários e histórico</li>
 *     <li>Consultar estatísticas de entregas</li>
 *     <li>Gerar dados para dashboards e relatórios</li>
 * </ul>
 */
@Service
public class OrderService implements CrudService<Order> {

	/** Repositório JPA de pedidos. */
	private final OrderRepository orderRepository;

	/**
	 * Construtor que injeta o repositório de pedidos.
	 *
	 * @param orderRepository instância de {@link OrderRepository}
	 */
	@Autowired
	public OrderService(OrderRepository orderRepository) {
		super();
		this.orderRepository = orderRepository;
	}

	/**
	 * Conjunto de estados considerados "não disponíveis".
	 *
	 * <p>São todos os estados exceto:
	 * <ul>
	 *   <li>{@link OrderState#DELIVERED}</li>
	 *   <li>{@link OrderState#READY}</li>
	 *   <li>{@link OrderState#CANCELLED}</li>
	 * </ul>
	 */
	private static final Set<OrderState> notAvailableStates = Collections.unmodifiableSet(
			EnumSet.complementOf(EnumSet.of(OrderState.DELIVERED, OrderState.READY, OrderState.CANCELLED)));

	/**
	 * Cria ou atualiza um pedido, aplicando lógica customizada via {@code orderFiller}.
	 *
	 * @param currentUser usuário que está salvando o pedido
	 * @param id id do pedido (ou {@code null} para criar novo)
	 * @param orderFiller função que preenche os dados do pedido
	 * @return pedido persistido no banco
	 */
	@Transactional(rollbackOn = Exception.class)
	public Order saveOrder(User currentUser, Long id, BiConsumer<User, Order> orderFiller) {
		Order order;
		if (id == null) {
			order = new Order(currentUser);
		} else {
			order = load(id);
		}
		orderFiller.accept(currentUser, order);
		return orderRepository.save(order);
	}

	/**
	 * Salva diretamente um pedido existente ou novo.
	 *
	 * @param order entidade {@link Order} a ser salva
	 * @return pedido persistido
	 */
	@Transactional(rollbackOn = Exception.class)
	public Order saveOrder(Order order) {
		return orderRepository.save(order);
	}

	/**
	 * Adiciona um comentário ao histórico do pedido.
	 *
	 * @param currentUser usuário que adiciona o comentário
	 * @param order pedido alvo
	 * @param comment texto do comentário
	 * @return pedido atualizado e salvo
	 */
	@Transactional(rollbackOn = Exception.class)
	public Order addComment(User currentUser, Order order, String comment) {
		order.addHistoryItem(currentUser, comment);
		return orderRepository.save(order);
	}

	/**
	 * Consulta pedidos com filtro de nome de cliente e/ou data de entrega posterior a uma data.
	 *
	 * @param optionalFilter filtro opcional pelo nome do cliente
	 * @param optionalFilterDate filtro opcional pela data mínima de entrega
	 * @param pageable informações de paginação
	 * @return página de pedidos encontrados
	 */
	public Page<Order> findAnyMatchingAfterDueDate(Optional<String> optionalFilter,
												   Optional<LocalDate> optionalFilterDate, Pageable pageable) {
		if (optionalFilter.isPresent() && !optionalFilter.get().isEmpty()) {
			if (optionalFilterDate.isPresent()) {
				return orderRepository.findByCustomerFullNameContainingIgnoreCaseAndDueDateAfter(
						optionalFilter.get(), optionalFilterDate.get(), pageable);
			} else {
				return orderRepository.findByCustomerFullNameContainingIgnoreCase(optionalFilter.get(), pageable);
			}
		} else {
			if (optionalFilterDate.isPresent()) {
				return orderRepository.findByDueDateAfter(optionalFilterDate.get(), pageable);
			} else {
				return orderRepository.findAll(pageable);
			}
		}
	}

	/**
	 * Busca pedidos com data de entrega a partir de hoje.
	 *
	 * @return lista de {@link OrderSummary} iniciando na data atual
	 */
	@Transactional
	public List<OrderSummary> findAnyMatchingStartingToday() {
		return orderRepository.findByDueDateGreaterThanEqual(LocalDate.now());
	}

	/**
	 * Conta o total de pedidos com filtros opcionais.
	 *
	 * @param optionalFilter filtro opcional pelo nome do cliente
	 * @param optionalFilterDate filtro opcional pela data mínima de entrega
	 * @return quantidade de pedidos encontrados
	 */
	public long countAnyMatchingAfterDueDate(Optional<String> optionalFilter, Optional<LocalDate> optionalFilterDate) {
		if (optionalFilter.isPresent() && optionalFilterDate.isPresent()) {
			return orderRepository.countByCustomerFullNameContainingIgnoreCaseAndDueDateAfter(optionalFilter.get(),
					optionalFilterDate.get());
		} else if (optionalFilter.isPresent()) {
			return orderRepository.countByCustomerFullNameContainingIgnoreCase(optionalFilter.get());
		} else if (optionalFilterDate.isPresent()) {
			return orderRepository.countByDueDateAfter(optionalFilterDate.get());
		} else {
			return orderRepository.count();
		}
	}

	/**
	 * Calcula estatísticas de entregas do dia atual.
	 *
	 * @return objeto {@link DeliveryStats} com os dados
	 */
	private DeliveryStats getDeliveryStats() {
		DeliveryStats stats = new DeliveryStats();
		LocalDate today = LocalDate.now();
		stats.setDueToday((int) orderRepository.countByDueDate(today));
		stats.setDueTomorrow((int) orderRepository.countByDueDate(today.plusDays(1)));
		stats.setDeliveredToday((int) orderRepository.countByDueDateAndStateIn(today,
				Collections.singleton(OrderState.DELIVERED)));

		stats.setNotAvailableToday((int) orderRepository.countByDueDateAndStateIn(today, notAvailableStates));
		stats.setNewOrders((int) orderRepository.countByState(OrderState.NEW));

		return stats;
	}

	/**
	 * Gera dados de dashboard, incluindo estatísticas de entrega,
	 * vendas por mês/ano e entregas por produto.
	 *
	 * @param month mês de referência
	 * @param year ano de referência
	 * @return objeto {@link DashboardData} preenchido
	 */
	public DashboardData getDashboardData(int month, int year) {
		DashboardData data = new DashboardData();
		data.setDeliveryStats(getDeliveryStats());
		data.setDeliveriesThisMonth(getDeliveriesPerDay(month, year));
		data.setDeliveriesThisYear(getDeliveriesPerMonth(year));

		Number[][] salesPerMonth = new Number[3][12];
		data.setSalesPerMonth(salesPerMonth);
		List<Object[]> sales = orderRepository.sumPerMonthLastThreeYears(OrderState.DELIVERED, year);

		for (Object[] salesData : sales) {
			// year, month, deliveries
			int y = year - (int) salesData[0];
			int m = (int) salesData[1] - 1;
			if (y == 0 && m == month - 1) {
				// ignora mês atual, pois contém dados incompletos
				continue;
			}
			long count = (long) salesData[2];
			salesPerMonth[y][m] = count;
		}

		LinkedHashMap<Product, Integer> productDeliveries = new LinkedHashMap<>();
		data.setProductDeliveries(productDeliveries);
		for (Object[] result : orderRepository.countPerProduct(OrderState.DELIVERED, year, month)) {
			int sum = ((Long) result[0]).intValue();
			Product p = (Product) result[1];
			productDeliveries.put(p, sum);
		}

		return data;
	}

	/**
	 * Recupera o número de entregas por dia em um determinado mês.
	 *
	 * @param month mês desejado
	 * @param year ano desejado
	 * @return lista de contagens de entregas por dia
	 */
	private List<Number> getDeliveriesPerDay(int month, int year) {
		int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
		return flattenAndReplaceMissingWithNull(daysInMonth,
				orderRepository.countPerDay(OrderState.DELIVERED, year, month));
	}

	/**
	 * Recupera o número de entregas por mês em um ano.
	 *
	 * @param year ano desejado
	 * @return lista de contagens de entregas por mês
	 */
	private List<Number> getDeliveriesPerMonth(int year) {
		return flattenAndReplaceMissingWithNull(12, orderRepository.countPerMonth(OrderState.DELIVERED, year));
	}

	/**
	 * Normaliza resultados de consultas de agregação,
	 * preenchendo índices vazios com {@code null}.
	 *
	 * @param length tamanho esperado da lista
	 * @param list resultados de agregação no formato [índice, valor]
	 * @return lista normalizada
	 */
	private List<Number> flattenAndReplaceMissingWithNull(int length, List<Object[]> list) {
		List<Number> counts = new ArrayList<>();
		for (int i = 0; i < length; i++) {
			counts.add(null);
		}

		for (Object[] result : list) {
			counts.set((Integer) result[0] - 1, (Number) result[1]);
		}
		return counts;
	}

	/** {@inheritDoc} */
	@Override
	public JpaRepository<Order, Long> getRepository() {
		return orderRepository;
	}

	/**
	 * Cria uma nova instância de pedido com valores padrão.
	 *
	 * <p>O pedido criado:</p>
	 * <ul>
	 *   <li>É associado ao {@code currentUser}</li>
	 *   <li>Possui horário de entrega padrão às 16h</li>
	 *   <li>Data de entrega definida para hoje</li>
	 * </ul>
	 *
	 * @param currentUser usuário que cria o pedido
	 * @return novo pedido inicializado
	 */
	@Override
	@Transactional
	public Order createNew(User currentUser) {
		Order order = new Order(currentUser);
		order.setDueTime(LocalTime.of(16, 0));
		order.setDueDate(LocalDate.now());
		return order;
	}

}
