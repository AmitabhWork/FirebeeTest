package ahh.com.app.beans;

public class CoreMember {

    private String firstname;
    private String lastname;
    private String email;
    private String key;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public CoreMember() {
        // Default constructor required for calls to DataSnapshot.getValue(CoreMember.class)
    }

    public CoreMember(String firstname, String lastname, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    @Override
    public String toString() {
        return firstname + "," + lastname + "," + email + "," + key;
    }
}