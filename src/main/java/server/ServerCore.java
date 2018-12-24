package server;

import database.DataWorker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ServerCore {
    private Vector<ClientHandler> clients;
    private ServerSocket serverSocket;
    private Socket clientSocket;

    private final int PORT = 8189;
    private String localHost;

    private DataWorker dataWorker;

    public ServerCore(String localHost) {
        this.localHost = localHost;
        clients = new Vector<>();
        dataWorker = new DataWorker();
    }

    public DataWorker getDataWorker() {
        return dataWorker;
    }

    public void prepare(){
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Сервер запущен");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void waitForConnection(){
        try {
            while (true){
                clientSocket = serverSocket.accept();
                subcribeClient(new ClientHandler(clientSocket, this));
                System.out.println("Клиент подключился");
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
            try {
                clientSocket.close();
                serverSocket.close();
            } catch (IOException e) {
                System.out.println("Connection is lost");
                e.printStackTrace();
            }
        }
    }

    public void subcribeClient(ClientHandler client){
        clients.add(client);
    }

    public void unSubcribeClient(ClientHandler client){
        clients.remove(client);
    }
}