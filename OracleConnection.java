import java.io.*;
import java.util.*;
import java.sql.*;

class OracleConnection {
    public static void main(String args[]) {
        String url = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db5?autoReconnect=true&useSSL=false";
        String username = "Group5";
        String password = "CSCI3170";

        try {
            // step1 load the driver class
            Class.forName("com.mysql.jdbc.Driver");

            // step2 create the connection object
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Success");

            // step3 create the statement object
            Statement statement = connection.createStatement();

            // step4 execute query
            ResultSet resultString = statement.executeQuery("Select * from part");
            while (resultString.next())
                System.out.println(
                        resultString.getInt(1) + "  " + resultString.getString(2) + "  " + resultString.getString(3));

            // step5 close the connection object
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
