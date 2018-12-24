package server;

import messages.*;

import java.io.File;
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
import java.util.stream.Collectors;

public class ClientHandler {
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private ServerCore serverCore;
    private Socket socket;
    private Message msg;
    private boolean connectIsOn;

    public ClientHandler(Socket socket, ServerCore serverCore) {
        this.serverCore = serverCore;
        this.socket = socket;
        this.connectIsOn = true;

        if (initConnection()){
            new Thread(()-> {
                while (connectIsOn){
                    if (readOISSuccessful()){
                        if (msg instanceof FileMessage){
                            if (((FileMessage) msg).getPurpose().equals("save")){
                                saveFile((FileMessage)msg);
                            }
                            else {
                                try {
                                    oos.writeObject(download((FileMessage) msg));
                                } catch (IOException e) {
                                }
                            }

                        }
                        if (msg instanceof AuthMessage){
                            // выпиливаем сервер авторизации
                            if (serverCore.getDataWorker().checkUser(((AuthMessage) msg).getLogin(), ((AuthMessage) msg).getPass())){
                                ((AuthMessage) msg).setAuthOk(true);
                                if (Files.notExists(Paths.get("received/" + ((AuthMessage) msg).getLogin()))){
                                    try {
                                        Files.createDirectory(Paths.get("received/" + ((AuthMessage) msg).getLogin()));
                                    } catch (IOException e) {
                                    }
                                }
                            }
                            try {
                                oos.writeObject(msg);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (msg instanceof InitMessage){
                            try {
                                oos.writeObject(readDirectory((InitMessage) msg));
                                oos.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (msg instanceof DeleteMessage){
                            try {
                                Files.delete(Paths.get(((DeleteMessage) msg).getDirectory()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (msg instanceof ShutdownMessage){
                            connectIsOn = false;
                            dropConnection();
                        }
                    }
                }
            }).start();
        }
    }

    public boolean readOISSuccessful(){
        try {
            msg = (Message) ois.readObject();
            return true;
        } catch (IOException e) {
            return false;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public boolean initConnection(){
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            return true;
        } catch (IOException ex){
            dropConnection();
            return false;
        }
    }

    public void dropConnection(){
        try {
            ois.close();
            oos.close();
            serverCore.unSubcribeClient(this);
        } catch (IOException ex){

        }
    }

    public void saveFile(FileMessage msg) {
        try {
            System.out.println(msg.getName());
            System.out.println(msg.getLogin());
            Files.write(Paths.get( "received/" + msg.getLogin() + "/" + msg.getName()), msg.getDataBlock(), StandardOpenOption.CREATE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public InitMessage readDirectory(InitMessage initMsg){
        try {
            List<Path> tmpList = Files.list(Paths.get(initMsg.getDirectoryName())).collect(Collectors.toList());
            List<String> res = new ArrayList<>();
            for (Path t : tmpList){
                res.add(t.getFileName().toString());
            }
            initMsg.setPathsList(res);
            return initMsg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return initMsg;
    }

    public FileMessage download(FileMessage fm){
        try {
            FileMessage fileMessageDown = new FileMessage(fm.getName(), Files.readAllBytes(Paths.get("received\\" + fm.getLogin() + "\\" + fm.getName())), fm.getLogin(), "download");
//            fm.setDataBlock(Files.readAllBytes(Paths.get("received\\" + fm.getLogin() + "\\" + fm.getName())));
            return fileMessageDown;
        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

}
