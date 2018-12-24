package server;

public class Main {

    public static void main(String[] args) {
        ServerCore serverCore = new ServerCore("localhost");
        serverCore.prepare();
        serverCore.waitForConnection();
    }
}
