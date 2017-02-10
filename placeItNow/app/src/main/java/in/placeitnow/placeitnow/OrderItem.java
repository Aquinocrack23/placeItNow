package in.placeitnow.placeitnow;

/**
 * Created by swapsha96 on 1/21/2017.
 */

public class OrderItem {
    String itemKey;
    String itemName;
    Double itemPrice;
    Integer itemQuantity;

    public OrderItem() {

    }
    public OrderItem(String itemKey, String itemName, Double itemPrice, Integer itemQuantity) {
        this.itemKey = itemKey;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
    }

    public Double getItemPrice() {
        return this.itemPrice;
    }

    public Integer getItemQuantity() {
        return this.itemQuantity;
    }

    public String getItemName() {
        return this.itemName;
    }
}
