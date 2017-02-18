package in.placeitnow.placeitnow;

/**
 * Created by swapsha96 on 1/21/2017.
 */

/**
 *  it is important to make it serializable to pass it through intents*/

public class OrderItem implements java.io.Serializable {
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

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public Integer getItemQuantity() {
        return this.itemQuantity;
    }

    public String getItemName() {
        return this.itemName;
    }
}
