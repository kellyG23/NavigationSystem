import java.util.HashMap;

public class UserAccount {

    HashMap<String,String> loginfo = new HashMap<String,String>();

    UserAccount(){
        loginfo.put("Cyrus","Passwordnicyrus");
        loginfo.put("Jonald","Passwordnijonald");
        loginfo.put("Kelly","Passwordnikelly");
        loginfo.put("Cesar","Passwordnicesar");
        loginfo.put("phcare","123");


    }
    protected HashMap getLoginInfo(){
        return loginfo;
    }
}