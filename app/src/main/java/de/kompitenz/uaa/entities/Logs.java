package de.kompitenz.uaa.entities;

/**
 * Created by User on 04-Oct-17.
 */
public class Logs {

    String contract_no;
    String action;
    String user;


    public String getLogContract() {
        return contract_no;
    }
    public String getAction() {
        return action;
    }
    public String getUserLogged() {
        return user;
    }


    public void setLogContract(String contract_no) {
        this.contract_no = contract_no;
    }
    public void setAction(String action) { this.action = action; }
    public void setUserLogged(String user) { this.user = user; }
}
