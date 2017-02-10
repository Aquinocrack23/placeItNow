package in.placeitnow.placeitnow;

/**
 * Created by Pranav Gupta on 12/28/2016.
 */

public class OrderedItemContents implements java.io.Serializable {

    private String menu_item;
    private String price;
    private String quantity;

    public OrderedItemContents(String menu_item, String price, String quantity) {
        this.menu_item = menu_item;
        this.price = price;
        this.quantity = quantity;
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
