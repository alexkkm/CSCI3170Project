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

    public static void createTables(Connection mySQLDB) throws SQLException {
        // Create the tables

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

        // Create the statement
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

    // Function for Salesperson

    public static void searchNEAs(Scanner menuAns, Connection mySQLDB) throws SQLException {
        String ans = null, keyword = null, method = null, ordering = null;
        String searchSQL = "";
        PreparedStatement stmt = null;

        searchSQL += "SELECT N.NID, N.Distance, N.Family, N.Duration, N.Energy, C.RType ";
        searchSQL += "FROM NEA N, Contain C ";
        searchSQL += "WHERE N.NID = C.NID";

        while (true) {
            System.out.println("Choose the Search criterion:");
            System.out.println("1. ID");
            System.out.println("2. Family");
            System.out.println("3. Resource Type");
            System.out.print("My criterion: ");
            ans = menuAns.nextLine();
            if (ans.equals("1") || ans.equals("2") || ans.equals("3"))
                break;
        }
        method = ans;

        while (true) {
            System.out.print("Type in the Search Keyword: ");
            ans = menuAns.nextLine();
            if (!ans.isEmpty())
                break;
        }
        keyword = ans;

        if (method.equals("1")) {
            searchSQL += " AND N.NID = ? ";
        } else if (method.equals("2")) {
            searchSQL += " AND N.Family LIKE ? ";
        } else if (method.equals("3")) {
            searchSQL += " AND C.RType LIKE ? ";
        }

        stmt = mySQLDB.prepareStatement(searchSQL);
        stmt.setString(1, "%" + keyword + "%");

        if (method.equals("1")) {
            stmt.setString(1, keyword);
        } else if (method.equals("2")) {
            stmt.setString(1, "%" + keyword + "%");
        } else if (method.equals("3")) {
            stmt.setString(1, "%" + keyword + "%");
        }

        String[] field_name = { "ID", "Distance", "Family", "Duration", "Energy", "Resources" };
        for (int i = 0; i < 6; i++) {
            System.out.print("| " + field_name[i] + " ");
        }
        System.out.println("|");

        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            for (int i = 1; i <= 6; i++) {
                System.out.print("| " + resultSet.getString(i) + " ");
            }
            System.out.println("|");
        }
        System.out.println("End of Query");
        resultSet.close();
        stmt.close();
    }

    public static void searchSpacecrafts(Scanner menuAns, Connection mySQLDB) throws SQLException {
        String ans = null, keyword = null, method = null, ordering = null;
        String searchSQL = "";
        PreparedStatement stmt = null;

        searchSQL += "SELECT * FROM (SELECT S.Agency, S.MID, S.Num, IF(A.Capacity IS NULL, 'E', 'A') AS Type, S.Energy, S.Duration, A.Capacity, S.Charge ";
        searchSQL += "FROM SpacecraftModel S ";
        searchSQL += "LEFT JOIN A_Model A ";
        searchSQL += "ON S.Agency = A.Agency AND S.MID = A.MID) AS TEMP WHERE";

        while (true) {
            System.out.println("Choose the Search criterion:");
            System.out.println("1. Agency Name");
            System.out.println("2. Type");
            System.out.println("3. Least energy [km/s]");
            System.out.println("4. Least working time [days]");
            System.out.println("5. Least capacity [m^3]");
            System.out.print("My criterion: ");
            ans = menuAns.nextLine();
            if (ans.equals("1") || ans.equals("2") || ans.equals("3") || ans.equals("4") || ans.equals("5"))
                break;
        }
        method = ans;

        while (true) {
            System.out.print("Type in the Search Keyword: ");
            ans = menuAns.nextLine();
            if (!ans.isEmpty())
                break;
        }
        keyword = ans;

        if (method.equals("1")) {
            searchSQL += " Agency = ? ";
        } else if (method.equals("2")) {
            searchSQL += " Type = ? ";
        } else if (method.equals("3")) {
            searchSQL += " Energy > ? ";
        } else if (method.equals("4")) {
            searchSQL += " Duration > ? ";
        } else if (method.equals("5")) {
            searchSQL += " Capacity > ? ";
        }

        stmt = mySQLDB.prepareStatement(searchSQL);
        if (method.equals("1")) {
            stmt.setString(1, keyword);
        } else if (method.equals("2")) {
            stmt.setString(1, keyword);
        } else if (method.equals("3")) {
            stmt.setDouble(1, Double.parseDouble(keyword));
        } else if (method.equals("4")) {
            stmt.setInt(1, Integer.parseInt(keyword));
        } else if (method.equals("5")) {
            stmt.setInt(1, Integer.parseInt(keyword));
        }

        String[] field_name = { "Agency", "MID", "SNum", "Type", "Energy", "T", "Capacity", "Charge" };
        for (int i = 0; i < 8; i++) {
            System.out.print("| " + field_name[i] + " ");
        }
        System.out.println("|");

        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            for (int i = 1; i <= 8; i++) {
                System.out.print("| " + resultSet.getString(i) + " ");
            }
            System.out.println("|");
        }
        System.out.println("End of Query");
        resultSet.close();
        stmt.close();
    }

    public static void NEAExploration(Scanner menuAns, Connection mySQLDB) throws SQLException {
        String ans = null, keyword = null, ordering = null;
        String searchSQL = "";
        PreparedStatement stmt = null;

        Statement prestmt = mySQLDB.createStatement();
        prestmt.execute(
                "CREATE OR REPLACE VIEW T1 AS SELECT A.Agency, A.MID, A.Num, R.SNum, R.CheckoutDate, R.ReturnDate, A.Charge, A.Duration, A.Energy, A.Capacity FROM RentalRecord R, A_Model A WHERE (R.ReturnDate IS NOT NULL OR (R.CheckoutDate IS NULL AND R.ReturnDate IS NULL)) AND R.Agency=A.Agency AND R.MID=A.MID");
        prestmt.execute(
                "CREATE OR REPLACE VIEW T2 AS SELECT N.NID, N.Distance, N.Family, N.Duration, N.Energy, C.Rtype FROM NEA N, Contain C WHERE N.NID=C.NID");
        prestmt.execute(
                "CREATE OR REPLACE VIEW T3 AS SELECT T2.*, R.Density, R.Value FROM T2 LEFT JOIN Resource R ON T2.RType=R.RType");

        searchSQL = "SELECT T1.Agency, T1.MID, T1.SNum ,(T1.Charge * T3.Duration)AS Cost, ((IF(T3.Value IS NULL,0,T3.Value)*IF(T3.Density IS NULL,0,T3.Density)*T1.Capacity)-(T1.Charge * T3.Duration))AS Benefit FROM T1, T3 WHERE T1.Energy>T3.Energy AND T1.Duration>T3.Duration AND T3.NID=?";

        while (true) {
            System.out.print("Type in the Search Keyword: ");
            ans = menuAns.nextLine();
            if (!ans.isEmpty())
                break;
        }
        keyword = ans;

        searchSQL += " ORDER BY Benefit DESC";

        stmt = mySQLDB.prepareStatement(searchSQL);
        stmt.setString(1, keyword);

        System.out.println("All possible solutions:");

        String[] field_name = { "Agency", "MID", "SNum", "Cost", "Benefit" };
        for (int i = 0; i < 5; i++) {
            System.out.print("| " + field_name[i] + " ");
        }
        System.out.println("|");

        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            for (int i = 1; i <= 5; i++) {
                System.out.print("| " + resultSet.getString(i) + " ");
            }
            System.out.println("|");
        }
        System.out.println("End of Query");
        resultSet.close();
        stmt.close();
        prestmt.execute("DROP VIEW T1");
        prestmt.execute("DROP VIEW T2");
        prestmt.execute("DROP VIEW T3");
        prestmt.close();
    }

    public static void BeneficialExploration(Scanner menuAns, Connection mySQLDB) throws SQLException {
        String ans = null, budget = null, rtype = null, ordering = null;
        String searchSQL = "";
        PreparedStatement stmt = null;

        Statement prestmt = mySQLDB.createStatement();
        prestmt.execute(
                "CREATE OR REPLACE VIEW T1 AS SELECT A.Agency, A.MID, A.Num, R.SNum, R.CheckoutDate, R.ReturnDate, A.Charge, A.Duration, A.Energy, A.Capacity FROM RentalRecord R, A_Model A WHERE (R.ReturnDate IS NOT NULL OR (R.CheckoutDate IS NULL AND R.ReturnDate IS NULL)) AND R.Agency=A.Agency AND R.MID=A.MID");
        prestmt.execute(
                "CREATE OR REPLACE VIEW T2 AS SELECT N.NID, N.Distance, N.Family, N.Duration, N.Energy, C.Rtype FROM NEA N, Contain C WHERE N.NID=C.NID");
        prestmt.execute(
                "CREATE OR REPLACE VIEW T3 AS SELECT T2.*, R.Density, R.Value FROM T2 LEFT JOIN Resource R ON T2.RType=R.RType");
        prestmt.execute(
                "CREATE OR REPLACE VIEW T4 AS SELECT T3.NID, T3.Family, T1.Agency, T1.MID, T1.SNum , T3.Duration,(T1.Charge * T3.Duration)AS Cost, ((IF(T3.Value IS NULL,0,T3.Value)*IF(T3.Density IS NULL,0,T3.Density)*T1.Capacity)-(T1.Charge * T3.Duration))AS Benefit, T3.RType FROM T1, T3 WHERE T1.Energy>T3.Energy AND T1.Duration>T3.Duration");

        searchSQL = "SELECT * FROM T4,(SELECT MAX(Benefit)AS MAXBenefit FROM T4 WHERE Cost<=? AND RType=?)AS T5 WHERE T4.Benefit=T5.MAXBenefit AND T4.Cost<=? AND T4.RType=?";

        while (true) {
            System.out.print("Type in the your budget [$]: ");
            ans = menuAns.nextLine();
            if (!ans.isEmpty())
                break;
        }
        budget = ans;

        while (true) {
            System.out.print("Type in the resource type: ");
            ans = menuAns.nextLine();
            if (!ans.isEmpty())
                break;
        }
        rtype = ans;

        stmt = mySQLDB.prepareStatement(searchSQL);
        stmt.setString(1, budget);
        stmt.setString(2, rtype);
        stmt.setString(3, budget);
        stmt.setString(4, rtype);

        System.out.println("The most beneficial mission is:");

        String[] field_name = { "NEA ID", "Family", "Agency", "MID", "SNum", "Duration", "Cost", "Benefit" };
        for (int i = 0; i < 8; i++) {
            System.out.print("| " + field_name[i] + " ");
        }
        System.out.println("|");

        ResultSet resultSet = stmt.executeQuery();
        resultSet.next();
        for (int i = 1; i <= 8; i++) {
            System.out.print("| " + resultSet.getString(i) + " ");
        }
        System.out.println("|");
        System.out.println("End of Query");
        resultSet.close();
        stmt.close();
        prestmt.execute("DROP VIEW T1");
        prestmt.execute("DROP VIEW T2");
        prestmt.execute("DROP VIEW T3");
        prestmt.execute("DROP VIEW T4");
        prestmt.close();
    }

    public static void customersMenu(Scanner menuAns, Connection mySQLDB) throws SQLException {
        String answer = "";

        while (true) {
            System.out.println();
            System.out.println("-----Operations for explorational companies (rental customers)-----");
            System.out.println("What kinds of operation would you like to perform?");
            System.out.println("1. Search for NEAs based on some criteria");
            System.out.println("2. Search for spacecrafts based on some criteria");
            System.out.println("3. A certain NEA exploration mission design");
            System.out.println("4. The most beneficial NEA exploration mission design");
            System.out.println("0. Return to the main menu");
            System.out.print("Enter Your Choice: ");
            answer = menuAns.nextLine();

            if (answer.equals("1") || answer.equals("2") || answer.equals("3") || answer.equals("4")
                    || answer.equals("0"))
                break;
            System.out.println("[Error]: Wrong Input, Type in again!!!");
        }

        if (answer.equals("1")) {
            searchNEAs(menuAns, mySQLDB);
        } else if (answer.equals("2")) {
            searchSpacecrafts(menuAns, mySQLDB);
        } else if (answer.equals("3")) {
            NEAExploration(menuAns, mySQLDB);
        } else if (answer.equals("4")) {
            BeneficialExploration(menuAns, mySQLDB);
        }
    }

    // Functions for Manager
    public static void rentSpacecraft(Scanner menuAns, Connection mySQLDB) throws SQLException {
        String selectSQL = "SELECT COUNT(*) FROM RentalRecord R WHERE R.Agency=? AND R.MID=? AND R.SNum=? AND (R.ReturnDate IS NOT NULL OR (R.CheckoutDate IS NULL AND R.ReturnDate IS NULL))";
        String updateSQL = "UPDATE RentalRecord R set R.CheckoutDate = ?, R.ReturnDate = NULL WHERE R.Agency=? AND R.MID=? AND R.SNum=?";
        Calendar calendar = Calendar.getInstance();
        java.sql.Date DateObject = new java.sql.Date(calendar.getTime().getTime());

        String Agency = null, MID = null, SNum = null;

        while (true) {
            System.out.print("Enter the space agency name: ");
            Agency = menuAns.nextLine();
            if (!Agency.isEmpty())
                break;
        }

        while (true) {
            System.out.print("Enter the MID: ");
            MID = menuAns.nextLine();
            if (!MID.isEmpty())
                break;
        }

        while (true) {
            System.out.print("Enter the SNum: ");
            SNum = menuAns.nextLine();
            if (!SNum.isEmpty())
                break;
        }

        PreparedStatement stmt = mySQLDB.prepareStatement(selectSQL);
        stmt.setString(1, Agency);
        stmt.setString(2, MID);
        stmt.setString(3, SNum);

        ResultSet resultSet = stmt.executeQuery();
        resultSet.next();
        if (resultSet.getInt(1) != 1) {
            System.err.println("[Error]: This spacecraft is not available to be rented");
            resultSet.close();
            stmt.close();
            return;
        }

        resultSet.close();
        stmt.close();

        System.out.println(DateObject);

        PreparedStatement stmt2 = mySQLDB.prepareStatement(updateSQL);
        stmt2.setDate(1, DateObject);
        stmt2.setString(2, Agency);
        stmt2.setString(3, MID);
        stmt2.setString(4, SNum);
        stmt2.executeUpdate();
        stmt2.close();

        System.out.println("Spacecraft rented successfully!");

    }

    public static void returnSpacecraft(Scanner menuAns, Connection mySQLDB) throws SQLException {
        String selectSQL = "SELECT COUNT(*) FROM RentalRecord R WHERE R.Agency=? AND R.MID=? AND R.SNum=? AND (R.ReturnDate IS NULL)";
        String updateSQL = "UPDATE RentalRecord R set R.ReturnDate = ? WHERE R.Agency=? AND R.MID=? AND R.SNum=?";
        Calendar calendar = Calendar.getInstance();
        java.sql.Date DateObject = new java.sql.Date(calendar.getTime().getTime());

        String Agency = null, MID = null, SNum = null;

        while (true) {
            System.out.print("Enter the space agency name: ");
            Agency = menuAns.nextLine();
            if (!Agency.isEmpty())
                break;
        }

        while (true) {
            System.out.print("Enter the MID: ");
            MID = menuAns.nextLine();
            if (!MID.isEmpty())
                break;
        }

        while (true) {
            System.out.print("Enter the SNum: ");
            SNum = menuAns.nextLine();
            if (!SNum.isEmpty())
                break;
        }

        PreparedStatement stmt = mySQLDB.prepareStatement(selectSQL);
        stmt.setString(1, Agency);
        stmt.setString(2, MID);
        stmt.setString(3, SNum);

        ResultSet resultSet = stmt.executeQuery();
        resultSet.next();
        if (resultSet.getInt(1) != 1) {
            System.err.println("[Error]: This spacecraft is not available to be returned");
            resultSet.close();
            stmt.close();
            return;
        }

        resultSet.close();

        stmt.close();

        System.out.println(DateObject);

        PreparedStatement stmt2 = mySQLDB.prepareStatement(updateSQL);
        stmt2.setDate(1, DateObject);
        stmt2.setString(2, Agency);
        stmt2.setString(3, MID);
        stmt2.setString(4, SNum);
        stmt2.executeUpdate();
        stmt2.close();

        System.out.println("Spacecraft returned successfully!");

    }

    public static void showRentedOut(Scanner menuAns, Connection mySQLDB) throws SQLException {
        String selectSQL = "SELECT R.Agency, R.MID, R.SNum, DATE_FORMAT(R.CheckoutDate, '%d-%m-%Y')AS Checkout FROM RentalRecord R WHERE (R.ReturnDate IS NULL) AND R.CheckoutDate>=STR_TO_DATE(?,'%d-%m-%Y') AND R.CheckoutDate<=STR_TO_DATE(?,'%d-%m-%Y') ORDER BY Checkout DESC";
        String start = null, end = null;

        while (true) {
            System.out.print("Typing in the starting date [DD-MM-YYYY]: ");
            start = menuAns.nextLine();
            if (!start.isEmpty())
                break;
        }

        while (true) {
            System.out.print("Typing in the ending date [DD-MM-YYYY]: ");
            end = menuAns.nextLine();
            if (!end.isEmpty())
                break;
        }

        PreparedStatement stmt = mySQLDB.prepareStatement(selectSQL);
        stmt.setString(1, start);
        stmt.setString(2, end);

        ResultSet resultSet = stmt.executeQuery();

        System.out.println("List of the unreturned spacecraft:");

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

    public static void showRentedOutAgency(Scanner menuAns, Connection mySQLDB) throws SQLException {
        String selectSQL = "SELECT Agency, COUNT(*) FROM RentalRecord R WHERE R.ReturnDate IS NULL GROUP BY Agency";

        PreparedStatement stmt = mySQLDB.prepareStatement(selectSQL);

        ResultSet resultSet = stmt.executeQuery();

        System.out.println("| Agency | Number |");
        while (resultSet.next()) {
            for (int i = 1; i <= 2; i++) {
                System.out.print("| " + resultSet.getString(i) + " ");
            }
            System.out.println("|");
        }
        System.out.println("...");
        System.out.println("End of Query");
        stmt.close();
    }

    public static void staffMenu(Scanner menuAns, Connection mySQLDB) throws SQLException {
        String answer = "";

        while (true) {
            System.out.println();
            System.out.println("-----Operations for spacecraft rental staff-----");
            System.out.println("What kinds of operation would you like to perform?");
            System.out.println("1. Rent a spacecraft");
            System.out.println("2. Return a spacecraft");
            System.out.println("3. List all spacecrafts currently rented out (on a mission) for a certain period");
            System.out.println("4. List the number of spacecrafts currently rented out by each agency");
            System.out.println("0. Return to the main menu");
            System.out.print("Enter Your Choice: ");
            answer = menuAns.nextLine();

            if (answer.equals("1") || answer.equals("2") || answer.equals("3") || answer.equals("4")
                    || answer.equals("0"))
                break;
            System.out.println("[Error]: Wrong Input, Type in again!!!");
        }

        if (answer.equals("1")) {
            rentSpacecraft(menuAns, mySQLDB);
        } else if (answer.equals("2")) {
            returnSpacecraft(menuAns, mySQLDB);
        } else if (answer.equals("3")) {
            showRentedOut(menuAns, mySQLDB);
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
                    customersMenu(menuAns, mySQLDB);
                } else if (answer.equals("3")) {
                    staffMenu(menuAns, mySQLDB);
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