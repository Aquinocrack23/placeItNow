package in.placeitnow.placeitnow;

/**
 * Created by Pranav Gupta on 12/23/2016.
 */

public class Menu {
    private String item_key;
    private String menu_item;
    private int price;
    private String message;
    private String type;
    private int quantity;

    public Menu(String key,String item,int price,String message,String type,int quantity){

        this.item_key=key;
        this.menu_item = item;
        this.price = price;
        this.message = message;
        this.type = type;
        this.quantity = quantity;
    }
    public String getItem_key() {
        return item_key;
    }

    public void setItem_key(String item_key) {
        this.item_key = item_key;
    }

    public void setMenu_item(String menu_item) {
        this.menu_item = menu_item;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getMenu_item() {
        return menu_item;
    }

    public String getMessage() {
        return message;
    }

    public int getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
