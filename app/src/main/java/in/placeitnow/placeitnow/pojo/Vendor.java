package in.placeitnow.placeitnow.pojo;

/**
 * Created by Pranav Gupta on 12/19/2016.
 */

public class Vendor {

    private String vid;
    private String name;
    private Boolean status;
    private Long order_current;

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }


    public String getVid() {

        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }
    public Vendor(String name, Boolean s){
        this.name = name;
        this.status = s;
    }

    public Vendor(String vid,String name, Boolean s){
        this.vid=vid;
        this.name = name;
        this.status = s;
    }
    public Vendor(String vid,String name, Boolean s,Long oc){
        this.vid=vid;
        this.name = name;
        this.status = s;
        this.order_current = oc;
    }

    public Boolean getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public Long getOrder_current() {
        return order_current;
    }

    public void setOrder_current(Long order_current) {
        this.order_current = order_current;
    }
}
