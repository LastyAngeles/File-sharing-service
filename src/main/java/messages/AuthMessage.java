package messages;

import java.io.Serializable;

public class AuthMessage extends Message implements Serializable{
    private String login;
    private String pass;
    private boolean isAuthOk;

    public AuthMessage(String login, String pass) {
        this.login = login;
        this.pass = pass;
        this.isAuthOk = false;
    }

    public boolean isAuthOk() {
        return isAuthOk;
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public void setAuthOk(boolean set) {
        isAuthOk = set;
    }
}
