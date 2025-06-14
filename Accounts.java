import java.util.HashMap;

public class Accounts {

    HashMap<String, String> userLogins = new HashMap<>();
    HashMap<String, String> adminLogins = new HashMap<>();

    public Accounts() {
        // Populate user accounts
        userLogins.put("Kelly", "Passwordnikelly");
        userLogins.put("Cesar", "Passwordnicesar");
        userLogins.put("phcare", "123");

        // Populate admin accounts
        adminLogins.put("Cyrus", "Passwordnicyrus");
        adminLogins.put("Jonald", "Passwordnijonald");
    }

    public HashMap<String, String> getUserLogins() {
        return userLogins;
    }

    public HashMap<String, String> getAdminLogins() {
        return adminLogins;
    }
}
