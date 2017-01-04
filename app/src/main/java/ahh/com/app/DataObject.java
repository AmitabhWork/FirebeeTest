package ahh.com.app;

import com.google.firebase.storage.StorageReference;

import ahh.com.app.beans.CoreMember;

/**
 * Created by amitabhs on 04/11/16.
 */
public class DataObject {
    private String name;

    private StorageReference imageUrl;

    DataObject(String text1, StorageReference bgimage) {
        name = text1;

        imageUrl = bgimage;
    }

    public String getName() {
        return name;
    }

    public StorageReference getImageUrl() {
        return imageUrl;
    }
}