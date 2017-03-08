package in.placeitnow.placeitnow;

import java.util.ArrayList;

/**
 * Created by swapsha96 on 1/21/2017.
 */

public class OrderLayoutClass {

    private String orderKey;
    private String uid;
    private String displayName;
    private ArrayList<OrderItem> items;
    private Double amount;
    private Long time;
    private String comment;
    private boolean orderDone;
    private boolean paymentDone;
    private String orderID;
    private Integer progress_order_number;
    private String vendor_name;
    private Integer orders_before_yours;

    public OrderLayoutClass() {

    }
    public OrderLayoutClass(String orderID, String displayName, String uid, ArrayList<OrderItem> orderItems,
                            String vendor_name, Integer order_number, Long epoch) {

        this.orderID = orderID;
        this.uid = uid;
        this.items = orderItems;
        this.displayName = displayName;
        this.amount = 0.0;
        for (OrderItem orderItem : orderItems)
            amount += orderItem.getItemPrice() * orderItem.getItemQuantity();
        this.orderDone = false;
        this.paymentDone = false;
        this.comment = "";
        this.vendor_name = vendor_name;
        this.progress_order_number = order_number;
        this.time = epoch;
        this.orders_before_yours = order_number-1;


    }
    public ArrayList<OrderItem> getItems() {
        return items;
    }

    public Double getAmount() {
        return amount;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getDisplayName() {

        return displayName;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public Long getTime() {

        return time;
    }

    public Integer getProgress_order_number() {
        return progress_order_number;
    }

    public String getOrderKey() {

        return orderKey;
    }

    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }

    public boolean isOrderDone() {
        return orderDone;
    }

    public void setOrderDone(boolean orderDone) {
        this.orderDone = orderDone;
    }

    public boolean isPaymentDone() {
        return paymentDone;
    }

    public void setPaymentDone(boolean paymentDone) {
        this.paymentDone = paymentDone;
    }

    public Integer getOrders_before_yours() {
        return orders_before_yours;
    }

    public void setOrders_before_yours(Integer orders_before_yours) {
        this.orders_before_yours = orders_before_yours;
    }
    public void setProgress_order_number(Integer progress_order_number) {
        this.progress_order_number = progress_order_number;
    }

}
