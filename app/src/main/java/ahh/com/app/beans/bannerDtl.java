package ahh.com.app.beans;

/**
 * Created by amitabhs on 03/01/17.
 */

public class BannerDtl {
    private int priority;
    private String name;

    @Override
    public String toString() {
        return "BannerDtl{" +
                "priority=" + priority +
                ", name='" + name + '\'' +
                '}';
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
