package in.placeitnow.placeitnow;

import java.util.ArrayList;

/**
 * Created by swapsha96 on 1/21/2017.
 */

public class OrderLayout {
    private String orderKey;
    private String uid;
    private String displayName;
    private ArrayList<OrderItem> items;
    private Double amount;
    private Long time;
    private String comment;
    private boolean orderDone;
    private boolean paymentDone;


    public OrderLayout() {

    }

    public OrderLayout(String uid, String displayName, ArrayList<OrderItem> orderItems) {
        this.uid = uid;
        this.displayName = displayName;
        this.items = orderItems;
        this.amount = 0.0;
        for (OrderItem orderItem : orderItems)
            amount += orderItem.getItemPrice() * orderItem.getItemQuantity();
        this.orderDone = false;
        this.paymentDone = false;
        this.time = System.currentTimeMillis() / 1000L;
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
