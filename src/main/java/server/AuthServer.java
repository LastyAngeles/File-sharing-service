package server;
import messages.AuthMessage;
import messages.Message;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;

public class AuthServer {
    private Hashtable<String, String> dataClients;
    //private Hashtable<String, Boolean> clientOnline;
    public AuthServer() {
        this.dataClients = new Hashtable<>();
    }

    public AuthMessage tryAuth(AuthMessage msg){
        if (dataClients.containsKey(msg.getLogin())){
            if (dataClients.get(msg.getLogin()).equals(msg.getPass())){
                msg.setAuthOk(true);
                return msg;
            }
            msg.setAuthOk(false);
            return msg;
        }
        else {
            dataClients.put(msg.getLogin(), msg.getPass());
            msg.setAuthOk(true);
            try {
                if (Files.notExists(Paths.get("received/" + msg.getLogin())))
                    Files.createDirectory(Paths.get("received/" + msg.getLogin()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return msg;
        }
    }
}
