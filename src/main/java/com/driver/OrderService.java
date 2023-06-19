package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired OrderRepository orderRepository;
    public void addOrder(Order order){
        orderRepository.addOrder(order);
    }

    public void addPartner(String deliveryPartnerId){
        orderRepository.addPartner(deliveryPartnerId);
    }

    public void assignOrderToPartner(String orderId,String partnerId){
        orderRepository.assignOrderToPartner(orderId,partnerId);
    }

    public Order getOrderById(String orderId){
        return orderRepository.getOrderById(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId){
        return orderRepository.getPartnerById(partnerId);
    }

    public int getOrderCount(String partnerId){
        return orderRepository.getOrderCount(partnerId);
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return orderRepository.getOrdersByPartnerId(partnerId);
    }

    public List<String> getAllOrders() {
        return orderRepository.getAllOrder();
    }

    public Integer getCountOfUnassignedOrders() {
        return orderRepository.getCountOfUnassignedOrders();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time,String partnerId){
        return orderRepository.getOrdersLeftAfterGivenTimeByPartnerId(time,partnerId);
    }

    public String getLastDeliveryTime(String partnerid){
        return orderRepository.getLastDeliveryTime(partnerid);
    }

    public void deletePartner(String partnerId) {
        orderRepository.deletePartner(partnerId);
    }

    public void deleteOrder(String orderId) {
        orderRepository.deleteOrder(orderId);
    }
}
