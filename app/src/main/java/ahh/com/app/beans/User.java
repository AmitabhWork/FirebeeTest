package ahh.com.app.beans;

/**
 * Created by amitabhs on 29/12/16.
 */

public class User {
    public String displayName;
    public String UID;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(CoreMember.class)
    }

    public User(String firstname, String UID, String email) {
        this.displayName = firstname;
        this.UID = UID;
        this.email = email;
    }
}
