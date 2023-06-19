package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {
    HashMap<String,Order> orderHashMap = new HashMap<>();
    HashMap<String,DeliveryPartner> deliveryPartnerHashMap = new HashMap<>();

    HashMap<String, List<Order>> listOfOrderHashMap = new HashMap<>();
    public void addOrder(Order order){
        orderHashMap.put(order.getId(),order);
    }

    public void addPartner(String deliveryPartnerId){
        DeliveryPartner deliveryPartner = new DeliveryPartner(deliveryPartnerId);
        deliveryPartnerHashMap.put(deliveryPartner.getId(),deliveryPartner);
    }

    public void assignOrderToPartner(String orderId,String partnerId){
        Order order = orderHashMap.get(orderId);
        DeliveryPartner deliveryPartner = deliveryPartnerHashMap.get(partnerId);
        deliveryPartner.setNumberOfOrders(deliveryPartner.getNumberOfOrders()+1);

        if(listOfOrderHashMap.containsKey(deliveryPartner.getId())){
            List<Order> orderList = listOfOrderHashMap.get(deliveryPartner.getId());
            orderList.add(order);
            listOfOrderHashMap.put(deliveryPartner.getId(),orderList);
        }
        else{
            List<Order> list = new ArrayList<>();
            list.add(order);
            listOfOrderHashMap.put(deliveryPartner.getId(),list);
        }

    }

    public Order getOrderById(String orderId){
        return orderHashMap.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId){
        return deliveryPartnerHashMap.get(partnerId);
    }

    public int getOrderCount(String partnerId){
        DeliveryPartner deliveryPartner = deliveryPartnerHashMap.get(partnerId);
        return deliveryPartner.getNumberOfOrders();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        List<Order> orderList = listOfOrderHashMap.get(partnerId);

        List<String > orderIdList = new ArrayList<>();

        for(Order order : orderList){
            orderIdList.add(order.getId());
        }

        return orderIdList;
    }

    public List<String> getAllOrder() {
        Collection<Order> orderCollection = orderHashMap.values();

        List<String> list = new ArrayList<>();

        for(Order order : orderCollection){
            list.add(order.getId());
        }

        return list;
    }

    public Integer getCountOfUnassignedOrders() {
        Collection<Order> orderCollection = orderHashMap.values();
        int totalOrder = orderHashMap.size();

        int totalAssignedOrder = 0;

        for(List<Order> orderList : listOfOrderHashMap.values()){
            totalAssignedOrder += orderList.size();
        }

        return totalOrder - totalAssignedOrder;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time,String partnerId){
        DeliveryPartner deliveryPartner = deliveryPartnerHashMap.get(partnerId);
        int passTime = Integer.parseInt(time.substring(0,2))*60 + Integer.parseInt(time.substring(3));

        int ordersLeft = 0;

        for(Order order : listOfOrderHashMap.get(deliveryPartner.getId())){
            int deliveryTime  = order.getDeliveryTime();
            if(passTime > deliveryTime)
                ordersLeft++;
        }

        return ordersLeft;
    }

    public String getLastDeliveryTime(String partnerid){
        DeliveryPartner deliveryPartner = deliveryPartnerHashMap.get(partnerid);

        Order lastOrder = listOfOrderHashMap.get(partnerid).get(listOfOrderHashMap.get(partnerid).size()-1);

        int lastOrderTime = lastOrder.getDeliveryTime();

        int HHtime = lastOrderTime/60;
        int MMtime = lastOrderTime%60;

        String time = "";

        if(HHtime< 10) time = "0"+HHtime+":";
        else time = ""+HHtime+":";

        if(MMtime <10) time += "0"+MMtime;
        else time += MMtime;

        return time;
    }

    public void deletePartner(String partnerId) {

        if(listOfOrderHashMap.containsKey(partnerId)){
            listOfOrderHashMap.remove(partnerId);
            deliveryPartnerHashMap.remove(partnerId);
        }
    }

    public void deleteOrder(String orderId) {
        DeliveryPartner deliveryPartner = null;
        boolean flag = false;
        for(String partnerId : deliveryPartnerHashMap.keySet()){
            deliveryPartner = deliveryPartnerHashMap.get(partnerId);
            if(listOfOrderHashMap.containsKey(partnerId)){
                for(Order order : listOfOrderHashMap.get(partnerId)){
                    if(order.getId().equals(orderId)){
                        int newNumberOfOrder = deliveryPartner.getNumberOfOrders()-1;
                        deliveryPartner.setNumberOfOrders(newNumberOfOrder);
                        orderHashMap.remove(orderId);
                        flag = true;
                        break;
                    }
                }
            }
            if(flag) break;
        }
        if(deliveryPartner!=null)
        {
            Iterator<Order> listIterator = listOfOrderHashMap.get(deliveryPartner.getId()).listIterator();
            for (Iterator<Order> it = listIterator; it.hasNext(); ) {
                Order order = it.next();
                if (order.getId().equals(orderId)) {
                    listOfOrderHashMap.get(deliveryPartner.getId()).remove(order);
                    break;
                }
            }
        }

    }
}
