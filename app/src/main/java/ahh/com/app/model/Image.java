package ahh.com.app.model;

import com.google.firebase.storage.StorageReference;

import java.io.Serializable;

/**
 * Created by Lincoln on 04/04/16.
 */
public class Image implements Serializable {
    private String name;
    private StorageReference small, medium, large;
    private String timestamp;

    public Image() {
    }

    public Image(String name, StorageReference small, StorageReference medium, StorageReference large, String timestamp) {
        this.name = name;
        this.small = small;
        this.medium = medium;
        this.large = large;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StorageReference getSmall() {
        return small;
    }

    public void setSmall(StorageReference small) {
        this.small = small;
    }

    public StorageReference getMedium() {
        return medium;
    }

    public void setMedium(StorageReference medium) {
        this.medium = medium;
    }

    public StorageReference getLarge() {
        return large;
    }

    public void setLarge(StorageReference large) {
        this.large = large;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
