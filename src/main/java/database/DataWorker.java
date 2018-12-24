package database;

import java.sql.*;

public class DataWorker {
    private Connection connection;

    private PreparedStatement prepInsert;
    private PreparedStatement prepSearch;
    private Statement statement;

    private ResultSet resSet;

    public DataWorker() {
        try {
            connect();
            init();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            dropConnect();
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
            dropConnect();
        } catch (InstantiationException e) {
            System.out.println(e.getMessage());
            dropConnect();
        }
        catch (SQLException e){
            System.out.println(e.getSQLState() + " " + e.getErrorCode() + " " + e.getMessage());
            dropConnect();
        }
    }

    // why we need to use .newInstance? +2 ex found
    public void connect() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException{
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/postBlock?autoReconnect=true&useSSL=false&maxReconnects=15", "root",
                                                        "MnOo@!90");
    }

    public void init(){
        try {
            statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                                            "id INT NOT NULL AUTO_INCREMENT," +
                                            "login VARCHAR(20) NOT NULL," +
                                            "pass VARCHAR(15) NOT NULL," +
                                            "PRIMARY KEY(id));");
            prepInsert = connection.prepareStatement("INSERT INTO users (login, pass) VALUES (?, ?)");
            prepSearch = connection.prepareStatement("SELECT login, pass FROM users WHERE login = ?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addUser(String login, String pass) throws SQLException {
        prepInsert.setString(1, login);
        prepInsert.setString(2, pass);
        prepInsert.executeUpdate();
    }

    public boolean checkUser(String login, String pass){
        try {
            prepSearch.setString(1, login);
            resSet = prepSearch.executeQuery();
            if (resSet.next()){
                if (resSet.getString(2).equals(pass)){
                    return true;
                }
                return false;
            }
            addUser(login, pass);
            return true;
        } catch (SQLException e) {
            e.getMessage();
            return false;
        }
    }

    public void dropConnect(){
        try {
            connection.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
