import java.io.*;
import java.util.*;
import java.sql.*;

class OracleConnection {

    public static void mainMenu() throws IOException {
        System.out.println("Welcome to sales system!\n");
        System.out.println("-----Main menu-----");
        System.out.println("What kinds of operation would you like to perform?");
        System.out.println("1. Operations for administrator");
        System.out.println("2. Operations for salesperson");
        System.out.println("3. Operations for manager");
        System.out.println("Exit the program");
        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(System.in));
        String input = bufferReader.readLine();
        System.out.println(input);
    }

    public static void runSql() throws SQLException {
        String SQLStatement = "";
    }

    public static void main(String args[]) {
        String url = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db5?autoReconnect=true&useSSL=false";
        String username = "Group5";
        String password = "CSCI3170";

        try {
            // step1 load the driver class
            Class.forName("com.mysql.jdbc.Driver");

            // step2 create the connection object
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Successfully connect with the database!");

            // step3 create the statement object
            Statement statement = connection.createStatement();
            System.out.println("Successfull create statement");

            // Main Program Start from now on
            InputStream file= new FileInputStream("./scriptfile.sql");
            int x=file.read();

            statement.executeQuery(sql)


            /*
             * // step4 execute query
             * ResultSet resultString = statement.executeQuery("SHOW DATABASES");
             * while (resultString.next())
             * System.out.println(resultString);
             */

            // Start
            mainMenu();
            /*
             * code for input
             * BufferedReader bufferReader = new BufferedReader(new
             * InputStreamReader(System.in));
             * String input = bufferReader.readLine();
             * System.out.println(input);
             */

            // step5 close the connection object
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
