package client;
import messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class ClientCore {

    private Socket socket;
    private int portNumber;
    private String localHost;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private String login;
    private List<String> currentPathList;
    private boolean flagForThread;
    private Path endPath;

    public static class ClientCoreHelper{
        private static final ClientCore INSTANCE = new ClientCore();
    }

    private ClientCore() {
        this.localHost = "localhost";
        this.portNumber = 8189;
        this.login = "";
        this.currentPathList = new ArrayList<>();
        this.flagForThread = true;
    }

    public static ClientCore getInstance(){
        return ClientCoreHelper.INSTANCE;
    }

    public void setFlagForThread(boolean flagForThread) {
        this.flagForThread = flagForThread;
    }

    public Path getEndPath() {
        return endPath;
    }

    public void setEndPath(Path endPath) {
        this.endPath = endPath;
    }

    public boolean connect(){
        boolean connectSuccess = false;
        try {
            this.socket = new Socket(localHost, portNumber);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            connectSuccess = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connectSuccess;
    }

    public void sendFiles(Path path){
        try {
            FileMessage fileMessage = new FileMessage(path.getFileName().toString(), Files.readAllBytes(path), login, "save");
            oos.writeObject(fileMessage);
            oos.flush();
            preInit(login);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendFiles(DeleteMessage msg){
        try {
            msg.setDirectory("received\\" + login + "\\" + msg.getDirectory());
            oos.writeObject(msg);
            oos.flush();
            preInit(login);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadFile(String fileName, Path endPath){
        try {
            FileMessage fm = new FileMessage(fileName, null, login, "download");
            this.endPath = endPath;
            oos.writeObject(fm);
            oos.flush();
        }
        catch (IOException ex){

        }
    }

    public boolean tryAuth(String login, String pass){
        AuthMessage auth = new AuthMessage(login, pass);
        try {
            oos.writeObject(auth);
            oos.flush();
            while (true){
                Message smgTmp = null;
                try {
                    smgTmp = (Message) ois.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (smgTmp instanceof AuthMessage){
                    if (((AuthMessage) smgTmp).isAuthOk()){
                        startListen();
                        preInit(login);
                        this.login = ((AuthMessage) smgTmp).getLogin();
                        return true;
                    }
                    else return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void startListen(){
        new Thread(()-> {
            flagForThread = true;
            while (flagForThread){
                try {
                    Message msg = (Message) ois.readObject();
                    if (msg instanceof InitMessage){
                        currentPathList = ((InitMessage) msg).getPathsList();
                    }
                    if (msg instanceof FileMessage && ((FileMessage) msg).getPurpose().equals("download")){
                        Files.write(Paths.get(endPath.toString() + "\\" + ((FileMessage) msg).getName()), ((FileMessage) msg).getDataBlock(), StandardOpenOption.CREATE);
                    }
                } catch (IOException e) {
                } catch (ClassNotFoundException e) {
                }
            }
        }).start();
    }

    public void preInit(String directory){
        InitMessage preInit = new InitMessage(directory);
        try {
            oos.writeObject(preInit);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getCurrentPathList(){
        return currentPathList;
    }

    public void saveClose(){
        try {
            oos.writeObject(new ShutdownMessage());
            oos.close();
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
