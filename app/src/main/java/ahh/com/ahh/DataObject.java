package ahh.com.ahh;

/**
 * Created by amitabhs on 04/11/16.
 */
public class DataObject {
    private String mText1;
    private String mText2;
    private String mbgimage;

    DataObject (String text1, String text2,String bgimage){
        mText1 = text1;
        mText2 = text2;
        mbgimage=bgimage;
    }

    public String getmText1() {
        return mText1;
    }

    public void setmText1(String mText1) {
        this.mText1 = mText1;
    }

    public String getmText2() {
        return mText2;
    }

    public void setmText2(String mText2) {
        this.mText2 = mText2;
    }
    public String getMbgimage(){return  mbgimage;}
    public void setMbgimage(String mbgimage){this.mbgimage=mbgimage;}
}