package in.placeitnow.placeitnow;

/**
 * Created by Pranav Gupta on 12/28/2016.
 */

/**In order to send it through intent the class must be serializable and as this is our custom class so we have to implement
 * java.io.serializable while the other classes such as Integer etc internally implement serializable
 * */
public class OrderedItemContents implements java.io.Serializable {

    private String item_key;
    private String menu_item;
    private String price;
    private String quantity;

    public OrderedItemContents(String item_key, String menu_item, String price, String quantity) {
        this.item_key = item_key;
        this.menu_item = menu_item;
        this.price = price;
        this.quantity = quantity;
    }
    public String getItem_key() {
        return item_key;
    }

    public void setItem_key(String item_key) {
        this.item_key = item_key;
    }

    public String getMenu_item() {
        return menu_item;
    }

    public void setMenu_item(String menu_item) {
        this.menu_item = menu_item;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
