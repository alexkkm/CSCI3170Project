import java.util.*;
import java.sql.*;
import java.io.*;

public class CSCI3170Project {

    public static String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db5?autoReconnect=true&useSSL=false";
    public static String dbUsername = "Group5";
    public static String dbPassword = "CSCI3170";

    public static Connection connectToOracle() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
        } catch (ClassNotFoundException e) {
            System.out.println("[Error]: Java MySQL DB Driver not found!!");
            System.exit(0);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return connection;
    }

    // Create the tables
    public static void createTables(Connection mySQLDB) throws SQLException {

        String categorySQL = "CREATE TABLE category (";
        categorySQL += "cID INTEGER NOT NULL PRIMARY KEY,";
        categorySQL += "cName VARCHAR(20) NOT NULL)";

        String manufacturerSQL = "CREATE TABLE manufacturer (";
        manufacturerSQL += "mID INTEGER NOT NULL PRIMARY KEY,";
        manufacturerSQL += "mName VARCHAR(20) NOT NULL,";
        manufacturerSQL += "mAddress VARCHAR(50) NOT NULL,";
        manufacturerSQL += "mPhoneNumber INTEGER NOT NULL)";

        String partSQL = "CREATE TABLE part (";
        partSQL += "pID INTEGER NOT NULL PRIMARY KEY,";
        partSQL += "pName VARCHAR(20) NOT NULL,";
        partSQL += "pPrice INTEGER NOT NULL,";
        partSQL += "mID INTEGER NOT NULL,";
        partSQL += "cID INTEGER NOT NULL,";
        partSQL += "pWarrantyPeriod INTEGER NOT NULL,";
        partSQL += "pAvailableQuantity INTEGER NOT NULL)";

        String salespersonSQL = "CREATE TABLE salesperson (";
        salespersonSQL += "sID INTEGER NOT NULL PRIMARY KEY,";
        salespersonSQL += "sName VARCHAR(20) NOT NULL,";
        salespersonSQL += "sAddress VARCHAR(50) NOT NULL,";
        salespersonSQL += "sPhoneNumber INTEGER NOT NULL,";
        salespersonSQL += "sExperience INTEGER NOT NULL)";

        String transactionSQL = "CREATE TABLE transaction (";
        transactionSQL += "tID INTEGER NOT NULL PRIMARY KEY,";
        transactionSQL += "pID INTEGER NOT NULL,";
        transactionSQL += "sID INTEGER NOT NULL,";
        transactionSQL += "tDate DATE NOT NULL)";

        Statement stmt = mySQLDB.createStatement();
        System.out.println("Processing...");

        stmt.execute("DROP TABLE category");
        stmt.execute("DROP TABLE manufacturer");
        stmt.execute("DROP TABLE part");
        stmt.execute("DROP TABLE salesperson");
        stmt.execute("DROP TABLE transaction");

        System.err.println("Creating Category Table.");
        stmt.execute(categorySQL);

        System.err.println("Creating Manufacturer Table.");
        stmt.execute(manufacturerSQL);

        System.err.println("Creating Part Table.");
        stmt.execute(partSQL);

        System.err.println("Creating Salesperson Table.");
        stmt.execute(salespersonSQL);

        System.err.println("Creating Transaction Table.");
        stmt.execute(transactionSQL);

        System.out.println("Done! Database is initialized!");
        stmt.close();
    }

    public static void deleteTables(Connection mySQLDB) throws SQLException {
        Statement stmt = mySQLDB.createStatement();
        System.out.println("Processing...");
        stmt.execute("SET FOREIGN_KEY_CHECKS = 0;");
        stmt.execute("DROP TABLE category");
        stmt.execute("DROP TABLE manufacturer");
        stmt.execute("DROP TABLE part");
        stmt.execute("DROP TABLE salesperson");
        stmt.execute("DROP TABLE transaction");
        stmt.execute("SET FOREIGN_KEY_CHECKS = 1;");
        System.out.println("Done! Database is removed!");
        stmt.close();
    }

    public static void loadTables(Scanner menuAns, Connection mySQLDB) throws SQLException {

        String categorySQL = "INSERT INTO Category (CategoryID, CategoryName) VALUES (?,?)";
        String manufacturerSQL = "INSERT INTO Manufacturer (ManufacturerID, ManufacturerName, ManufacturerAddress, ManufacturerPhoneNumber) VALUES (?,?,?,?)";
        String partSQL = "INSERT INTO Part (PartID, PartName, PartPrice, PartManufacturerID, PartCategoryID, PartWarranty, PartAvailableQuantity) VALUES (?,?,?,?,?,?,?)";
        String salespersonSQL = "INSERT INTO Salesperson (SalespersonID, SalespersonName, SalespersonAddress, SalespersonPhoneNumber, SalespersonExperience) VALUES (?,?,?,?,?)";
        String transactionSQL = "INSERT INTO Transaction (TransactionID, PartID, SalespersonID, TransactionDate) VALUES (?,?,?,STR_TO_DATE(?,'%d-%m-%Y'))";

        String filePath = "";
        String targetTable = "";

        while (true) {
            System.out.println("");
            System.out.print("Type in the Source Data Folder Path: ");
            filePath = menuAns.nextLine();
            if ((new File(filePath)).isDirectory())
                break;
        }

        System.out.println("Processing...");
        System.err.println("Loading category.txt");
        try {
            PreparedStatement stmt = mySQLDB.prepareStatement(categorySQL);
            String line = null;
            BufferedReader dataReader = new BufferedReader(new FileReader(filePath + "/category.txt"));
            line = dataReader.readLine();

            while ((line = dataReader.readLine()) != null) {
                String[] dataFields = line.split("\t");

                stmt.setInt(1, Integer.parseInt(dataFields[0]));
                stmt.setString(2, dataFields[1]);
                stmt.addBatch();
            }
            stmt.executeBatch();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        System.err.println("Loading manufacturer.txt");
        try {
            PreparedStatement stmt = mySQLDB.prepareStatement(manufacturerSQL);
            String line = null;
            BufferedReader dataReader = new BufferedReader(new FileReader(filePath + "/manufacturer.txt"));
            line = dataReader.readLine();

            while ((line = dataReader.readLine()) != null) {
                String[] dataFields = line.split("\t");

                stmt.setInt(1, Integer.parseInt(dataFields[0]));
                stmt.setString(2, dataFields[1]);
                stmt.setString(3, dataFields[2]);
                stmt.setInt(4, Integer.parseInt(dataFields[3]));

                stmt.addBatch();
            }
            stmt.executeBatch();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        System.err.println("Loading part.txt");
        try {
            PreparedStatement stmt = mySQLDB.prepareStatement(partSQL);
            String line = null;
            BufferedReader dataReader = new BufferedReader(new FileReader(filePath + "/part.txt"));
            line = dataReader.readLine();

            while ((line = dataReader.readLine()) != null) {
                String[] dataFields = line.split("\t");

                stmt.setInt(1, Integer.parseInt(dataFields[0]));
                stmt.setString(2, dataFields[1]);
                stmt.setInt(3, Integer.parseInt(dataFields[2]));
                stmt.setInt(4, Integer.parseInt(dataFields[3]));
                stmt.setInt(5, Integer.parseInt(dataFields[4]));
                stmt.setInt(6, Integer.parseInt(dataFields[5]));
                stmt.setInt(7, Integer.parseInt(dataFields[6]));
                stmt.addBatch();
            }

            stmt.executeBatch();

            stmt.close();

        } catch (Exception e) {
            System.out.println(e);
        }

        System.err.println("Loading salesperson.txt");
        try {
            PreparedStatement stmt = mySQLDB.prepareStatement(salespersonSQL);
            String line = null;
            BufferedReader dataReader = new BufferedReader(new FileReader(filePath + "/salesperson.txt"));
            line = dataReader.readLine();

            while ((line = dataReader.readLine()) != null) {
                String[] dataFields = line.split("\t");

                stmt.setInt(1, Integer.parseInt(dataFields[0]));
                stmt.setString(2, dataFields[1]);
                stmt.setString(3, dataFields[2]);
                stmt.setInt(4, Integer.parseInt(dataFields[3]));
                stmt.setInt(5, Integer.parseInt(dataFields[4]));

                stmt.addBatch();
            }
            stmt.executeBatch();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        System.err.println("Loading transaction.txt");
        try {
            PreparedStatement stmt = mySQLDB.prepareStatement(transactionSQL);
            String line = null;
            BufferedReader dataReader = new BufferedReader(new FileReader(filePath + "/transaction.txt"));
            line = dataReader.readLine();

            while ((line = dataReader.readLine()) != null) {
                String[] dataFields = line.split("\t");

                stmt.setInt(1, Integer.parseInt(dataFields[0]));
                stmt.setInt(2, Integer.parseInt(dataFields[1]));
                stmt.setInt(3, Integer.parseInt(dataFields[2]));
                stmt.setString(4, dataFields[3]);

                stmt.addBatch();
            }
            stmt.executeBatch();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println("Done! Data is inputted to the database!");
    }

    public static void showTables(Scanner menuAns, Connection mySQLDB) throws SQLException {

        String[] table_name = { "category", "manufacturer", "part", "salesperson", "transaction" };

        System.out.println("Content of:" + table_name + "\n");
        for (int i = 0; i < 5; i++) {
            Statement stmt = mySQLDB.createStatement();
            ResultSet rs = stmt.executeQuery("select * from " + table_name[i]);

            rs.next();
            System.out.println(table_name[i] + ": " + rs.getString(1));
            rs.close();
            stmt.close();
        }
    }

    public static void adminMenu(Scanner menuAns, Connection mySQLDB) throws SQLException {
        String answer = null;

        while (true) {
            System.out.println();
            System.out.println("-----Operations for administrator menu-----");
            System.out.println("What kinds of operation would you like to perform?");
            System.out.println("1. Create all tables");
            System.out.println("2. Delete all tables");
            System.out.println("3. Load from a dataset");
            System.out.println("4. Show content of a table");
            System.out.println("0. Return to the main menu");
            System.out.print("Enter Your Choice: ");
            answer = menuAns.nextLine();

            if (answer.equals("1") || answer.equals("2") || answer.equals("3") || answer.equals("4")
                    || answer.equals("0"))
                break;
            System.out.println("[Error]: Wrong Input, Type in again!!!");
        }

        if (answer.equals("1")) {
            createTables(mySQLDB);
        } else if (answer.equals("2")) {
            deleteTables(mySQLDB);
        } else if (answer.equals("3")) {
            loadTables(menuAns, mySQLDB);
        } else if (answer.equals("4")) {
            showTables(menuAns, mySQLDB);
        }
    }

    /*
     * 
     * 
     * 
     */

    // Function for Salesperson

    public static void salespersonMenu(Scanner menuAns, Connection mySQLDB) throws SQLException {
        String answer = "";

        while (true) {
            System.out.println();
            System.out.println("-----Operations for salesperson menu-----");
            System.out.println("What kinds of operation would you like to perform?");
            System.out.println("1. Search for parts");
            System.out.println("2. Sell a part");
            System.out.println("0. Return to the main menu");
            System.out.print("Enter Your Choice: ");
            answer = menuAns.nextLine();

            if (answer.equals("1") || answer.equals("2") || answer.equals("0"))
                break;
            System.out.println("[Error]: Wrong Input, Type in again!!!");
        }

        if (answer.equals("1")) {
            sdListSalesperson(menuAns, mySQLDB);
        } else if (answer.equals("2")) {
            searchSpacecrafts(menuAns, mySQLDB);
        
    }

    /*
     * 
     * 
     * 
     */

    // Functions for Manager

    // Man 1
    public static void ListSalesperson(Scanner menuAns, Connection mySQLDB) throws SQLException {
        String selectSQL = "Select S.sID,S.sName,S.sPhoneNumber,S.sExperience from salesperson S order by S.sExperience %s";
        String ordering = null;

        while (true) {
            System.out.print("Choose ordering:");
            System.out.print("1. By ascending order");
            System.out.print("2. By descending order");
            System.out.print("Choose the list ordering");
            ordering = menuAns.nextLine();
            if (!ordering.isEmpty())
                break;
        }

        PreparedStatement stmt = mySQLDB.prepareStatement(selectSQL);
        stmt.setString(1, ordering);

        ResultSet resultSet = stmt.executeQuery();

        System.out.println("List of the unreturned spacecraft:");

        System.out.println("| ID | Name | Mobile Phone | Year of Experience |");
        while (resultSet.next()) {
            for (int i = 1; i <= 4; i++) {
                System.out.print("| " + resultSet.getString(i) + " ");
            }
            System.out.println("|");
        }
        System.out.println("...");
        System.out.println("End of Query");
        stmt.close();
    }

    // Man2
    public static void countTransaction(Scanner menuAns, Connection mySQLDB) throws SQLException {
        String selectSQL = "select S.sID,S.sName,S.sExperience,Count(*) as from salesperson S, transaction T where S.sID=T.sID AND S.sExperience>=%d AND S.sExperience<=%d GROUP BY S.sID,S.sName,S.sExperience HAVING COUNT(*) > 1 ORDER BY S.sID DESC";
        String start = null, end = null;

        while (true) {
            System.out.print("Type in the lower bound for years of experience: ");
            start = menuAns.nextLine();
            if (!start.isEmpty())
                break;
        }

        while (true) {
            System.out.print("Type in the lower bound for years of experience:  ");
            end = menuAns.nextLine();
            if (!end.isEmpty())
                break;
        }

        PreparedStatement stmt = mySQLDB.prepareStatement(selectSQL);
        stmt.setString(1, start);
        stmt.setString(2, end);

        ResultSet resultSet = stmt.executeQuery();

        System.out.println("Transaction Record");

        System.out.println("| Agency | MID | SNum | Checkout Date |");
        while (resultSet.next()) {
            for (int i = 1; i <= 4; i++) {
                System.out.print("| " + resultSet.getString(i) + " ");
            }
            System.out.println("|");
        }
        System.out.println("...");
        System.out.println("End of Query");
        stmt.close();
    }

    // Man3
    public static void showSalesValue(Scanner menuAns, Connection mySQLDB) throws SQLException {
        String selectSQL = "select M.mID, M.mName, SUM(P.pPrice) from manufacturer M,part P where M.mID=P.mID Group by M.mID, M.mName Order by SUM(P.pPrice) DESC";

        PreparedStatement stmt = mySQLDB.prepareStatement(selectSQL);

        ResultSet resultSet = stmt.executeQuery();

        System.out.println("| Manufacturer ID | Manufacturer Name | Total Sales Value |");
        while (resultSet.next()) {
            for (int i = 1; i <= 3; i++) {
                System.out.print("| " + resultSet.getString(i) + " ");
            }
            System.out.println("|");
        }
        System.out.println("...");
        System.out.println("End of Query");
        stmt.close();
    }

    public static void managerMenu(Scanner menuAns, Connection mySQLDB) throws SQLException {
        String answer = "";

        while (true) {
            System.out.println();
            System.out.println("-----Operations for spacecraft rental staff-----");
            System.out.println("What kinds of operation would you like to perform?");
            System.out.println("1. List all salespersons");
            System.out.println(
                    "2. Count the no. of sales record of each salesperson under a specific range on uears of experience");
            System.out.println("3. Show the total sales value of each manufacturer");
            System.out.println("4. Show the N most popular part");
            System.out.println("0. Return to the main menu");
            System.out.print("Enter Your Choice: ");
            answer = menuAns.nextLine();

            if (answer.equals("1") || answer.equals("2") || answer.equals("3") || answer.equals("4")
                    || answer.equals("0"))
                break;
            System.out.println("[Error]: Wrong Input, Type in again!!!");
        }

        if (answer.equals("1")) {
            ListSalesperson(menuAns, mySQLDB);
        } else if (answer.equals("2")) {
            countTransaction(menuAns, mySQLDB);
        } else if (answer.equals("3")) {
            showSalesValue(menuAns, mySQLDB);
        } else if (answer.equals("4")) {
            showRentedOutAgency(menuAns, mySQLDB);
        }
    }

    public static void main(String[] args) {
        Scanner menuAns = new Scanner(System.in);
        System.out.println("Welcome to sales system!");

        while (true) {
            try {
                Connection mySQLDB = connectToOracle();
                System.out.println();
                System.out.println("-----Main menu-----");
                System.out.println("What kinds of operation would you like to perform?");
                System.out.println("1. Operations for administrator");
                System.out.println("2. Operations for salesperson");
                System.out.println("3. Operations for manager");
                System.out.println("0. Exit this program");
                System.out.print("Enter Your Choice: ");

                String answer = menuAns.nextLine();

                if (answer.equals("1")) {
                    adminMenu(menuAns, mySQLDB);
                } else if (answer.equals("2")) {
                    salespersonMenu(menuAns, mySQLDB);
                } else if (answer.equals("3")) {
                    managerMenu(menuAns, mySQLDB);
                } else if (answer.equals("0")) {
                    break;
                } else {
                    System.out.println("[Error]: Wrong Input, Type in again!!!");
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        }

        menuAns.close();
        System.exit(0);
    }
}