package Model;


import java.util.Date;

public class UserModel {

    private long Userid;
    private String first_name;
    private String last_name;
    private String email;
    private String User_type;



    public UserModel(long userid, String first_name, String last_name, String email, String user_type,Date creationDate) {
        Userid = userid;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.User_type = user_type;

    }

    public UserModel() {

    }

    public long getUserid() {
        return Userid;
    }

    public void setUserid(long userid) {
        Userid = userid;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_type() {
        return User_type;
    }

    public void setUser_type(String user_type) {
        User_type = user_type;
    }
}
