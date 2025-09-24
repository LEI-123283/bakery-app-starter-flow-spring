package com.vaadin.starter.bakery.backend.data;

/**
 * Representa estatísticas de entrega de uma padaria, rastreando pedidos e seus estados.
 * Esta classe armazena contagens de pedidos entregues hoje, com entrega prevista para hoje,
 * com entrega prevista para amanhã, não disponíveis hoje e novos pedidos.
 */
public class DeliveryStats {

    private int deliveredToday;
    private int dueToday;
    private int dueTomorrow;
    private int notAvailableToday;
    private int newOrders;

    /**
     * Obtém o número de pedidos entregues hoje.
     *
     * @return o número de pedidos entregues hoje
     */
    public int getDeliveredToday() {
        return deliveredToday;
    }

    /**
     * Define o número de pedidos entregues hoje.
     *
     * @param deliveredToday o número de pedidos entregues hoje
     */
    public void setDeliveredToday(int deliveredToday) {
        this.deliveredToday = deliveredToday;
    }

    /**
     * Obtém o número de pedidos com entrega prevista para hoje.
     *
     * @return o número de pedidos com entrega prevista para hoje
     */
    public int getDueToday() {
        return dueToday;
    }

    /**
     * Define o número de pedidos com entrega prevista para hoje.
     *
     * @param dueToday o número de pedidos com entrega prevista para hoje
     */
    public void setDueToday(int dueToday) {
        this.dueToday = dueToday;
    }

    /**
     * Obtém o número de pedidos com entrega prevista para amanhã.
     *
     * @return o número de pedidos com entrega prevista para amanhã
     */
    public int getDueTomorrow() {
        return dueTomorrow;
    }

    /**
     * Define o número de pedidos com entrega prevista para amanhã.
     *
     * @param dueTomorrow o número de pedidos com entrega prevista para amanhã
     */
    public void setDueTomorrow(int dueTomorrow) {
        this.dueTomorrow = dueTomorrow;
    }

    /**
     * Obtém o número de pedidos não disponíveis para entrega hoje.
     *
     * @return o número de pedidos não disponíveis hoje
     */
    public int getNotAvailableToday() {
        return notAvailableToday;
    }

    /**
     * Define o número de pedidos não disponíveis para entrega hoje.
     *
     * @param notAvailableToday o número de pedidos não disponíveis hoje
     */
    public void setNotAvailableToday(int notAvailableToday) {
        this.notAvailableToday = notAvailableToday;
    }

    /**
     * Obtém o número de novos pedidos recebidos.
     *
     * @return o número de novos pedidos
     */
    public int getNewOrders() {
        return newOrders;
    }

    /**
     * Define o número de novos pedidos recebidos.
     *
     * @param newOrders o número de novos pedidos
     */
    public void setNewOrders(int newOrders) {
        this.newOrders = newOrders;
    }

}