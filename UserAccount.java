import java.util.HashMap;


public class UserAccount {

    HashMap<String,String> loginfo = new HashMap<String,String>();

    UserAccount(){
        loginfo.put("mangunaycg@students.national-u.edu.ph","Passwordnicyrus");
        loginfo.put("mangunaycg@students.national-u.edu.ph","Passwordnijonald");
        loginfo.put("gabotkb@students.national-u.edu.ph","Passwordnikelly");
        loginfo.put("jondizcs@students.national-u.edu.ph","Passwordnicesar");
        loginfo.put("phcare","123");


    }
    protected HashMap getLoginInfo(){
        return loginfo;
    }
}