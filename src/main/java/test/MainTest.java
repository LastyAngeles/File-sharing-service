package test;

import database.DataWorker;

import java.sql.SQLException;

public class MainTest {
    public static void main(String[] args) {
        DataWorker dw = new DataWorker();
        try {
            dw.addUser("Norman", "passwordN");
            dw.dropConnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
