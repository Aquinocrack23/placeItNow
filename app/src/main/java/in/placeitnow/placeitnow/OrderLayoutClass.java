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

    public Integer getOrders_before_yours() {
        return orders_before_yours;
    }

    public void setOrders_before_yours(Integer orders_before_yours) {
        this.orders_before_yours = orders_before_yours;
    }

    public Integer getProgress_order_number() {
        return progress_order_number;
    }

    public void setProgress_order_number(Integer progress_order_number) {
        this.progress_order_number = progress_order_number;
    }

    public OrderLayoutClass() {

    }

    public String getVendor_name() {
        return vendor_name;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setItems(ArrayList<OrderItem> items) {
        this.items = items;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setTime(Long time) {
        this.time = time;
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

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public OrderLayoutClass(String orderID, String uid, String displayName, ArrayList<OrderItem> orderItems) {

        this.orderID = orderID;
        this.uid = uid;
        this.displayName = displayName;
        this.items = orderItems;
        this.amount = 0.0;
        for (OrderItem orderItem : orderItems)
            amount += orderItem.getItemPrice() * orderItem.getItemQuantity();
        this.orderDone = false;
        this.paymentDone = false;
        this.comment = "";

    }
    String getDisplayName() {
        return this.displayName;
    }

    public Double getAmount() {
        return amount;
    }

    public ArrayList<OrderItem> getItems() {
        return this.items;
    }

    public String getUid() {
        return this.uid;
    }

    public String getOrderKey() {
        return this.orderKey;
    }

    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }

    public Long getTime() {
        return Long.valueOf(this.time);
    }

    String getComment() {
        return this.comment;
    }

    void setComment(String comment) {
        this.comment = comment;
    }
}
