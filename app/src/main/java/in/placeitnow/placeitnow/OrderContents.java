package in.placeitnow.placeitnow;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Pranav Gupta on 12/25/2016.
 */
//This is the class which will carry attributes of an order
public class OrderContents{

    private String customer;
    private String contactnumber;
    private String address;
    private String time;
    private String date;
    private String amount;
    private String vendor;
    private String progress_order_number;
    private String orderId;
    private String order_payment_status;
    private long epoch;
    private ArrayList<OrderItem> ordercontents;


    //private Map<String,ArrayList<String>> ordercontent;

    //constructor
    public OrderContents(String customer,String contactnumber,String address,String time,String date,String amount,
                         String vendor,String progress_order_number,String orderId,
                         String order_payment_mode,ArrayList<OrderItem> content,long epoch){
        this.customer=customer;
        this.contactnumber=contactnumber;
        this.address=address;
        this.time = time;
        this.date = date;
        this.amount = amount;
        this.vendor = vendor;
        this.progress_order_number = progress_order_number;
        this.orderId = orderId;
        this.order_payment_status = order_payment_mode;
        this.ordercontents = content;
        this.epoch = epoch;
    }

    public OrderContents(String orderID,String username,String progress_order_number ){
        this.orderId = orderID;
        this.customer = username;
        this.progress_order_number = progress_order_number;

    }
    public OrderContents(){

    }

    public long getEpoch() {
        return epoch;
    }

    public void setEpoch(long epoch) {
        this.epoch = epoch;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getProgress_order_number() {
        return progress_order_number;
    }

    public void setProgress_order_number(String progress_order_number) {
        this.progress_order_number = progress_order_number;
    }

    public String getOrder_payment_status() {
        return order_payment_status;
    }

    public void setOrder_payment_status(String order_payment_mode) {
        this.order_payment_status = order_payment_mode;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<OrderItem> getOrdercontents() {
        return ordercontents;
    }

    public void setOrdercontents(ArrayList<OrderItem> ordercontents) {
        this.ordercontents = ordercontents;
    }
}
