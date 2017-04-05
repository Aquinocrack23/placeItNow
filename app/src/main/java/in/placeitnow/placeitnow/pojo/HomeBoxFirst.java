package in.placeitnow.placeitnow.pojo;

/**
 * Created by Pranav Gupta on 1/7/2017.
 */

public class HomeBoxFirst {
    private String mName;
    private String mDes;
    private int mThumbnail;

    public  HomeBoxFirst(String name,String des,int thumbnail){
        this.mName=name;
        this.mDes=des;
        this.mThumbnail=thumbnail;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getDes() {
        return mDes;
    }

    public void setDes(String des) {
        this.mDes = des;
    }

    public int getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.mThumbnail = thumbnail;
    }

}
