package emailapplication;

import java.sql.*;

public class Account {

    private String Email, Password, Password1, Name;

    //Create account
    public Account(String EM, String PassW, String PassW1, String NM) {
        Email = EM;
        Password = PassW;
        Password1 = PassW1;
        Name = NM;
    }

    //Login
    public Account(String EM, String PassW) {
        Email = EM;
        Password = PassW;
    }

    //change account information based on CID
    public Account(String EM) {
        Email = EM;
    }

    //default constructor
    public Account() {

    }

    public String getName() {
        String Name = "";
        try {
            DBConnection ToDB = new DBConnection();
            Connection DBConn = ToDB.openConn();
            Statement Stmt = DBConn.createStatement();
            String SQL_Command = "SELECT * FROM Account WHERE Email ='" + Email + "' AND Password ='" + Password + "'"; //SQL query command
            ResultSet Rslt = Stmt.executeQuery(SQL_Command); // Execute
            Rslt.next();
            Name = Rslt.getString("Name"); //Fetch name row
        } catch (java.sql.SQLException e) {

            System.out.println("SQLException: " + e);
            while (e != null) {
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("Message: " + e.getMessage());
                System.out.println("Vendor: " + e.getErrorCode());
                e = e.getNextException();
                System.out.println("");
            }
        } catch (java.lang.Exception e) {

            System.out.println("Exception: " + e);
            e.printStackTrace();
        }
        return Name;
    }

    //Sign up for an account
    public boolean signUp() {
        boolean done = !Email.equals("") && !Password.equals("") && !Password1.equals("") && Password.equals(Password1);
        try {
            if (done) {
                DBConnection ToDB = new DBConnection(); //Have a connection to the DB
                Connection DBConn = ToDB.openConn();
                Statement Stmt = DBConn.createStatement();
                String SQL_Command = "SELECT Email FROM Account WHERE Email ='" + Email + "'"; //SQL query command to check if username already taken
                ResultSet Rslt = Stmt.executeQuery(SQL_Command);
                done = done && !Rslt.next();
                if (done) { //if username not taken, insert into db
                    SQL_Command = "INSERT INTO Account(Email, Password, Name) VALUES ('" + Email + "','" + Password + "','" + Name + "')"; //Save the username, password and Name
                    Stmt.executeUpdate(SQL_Command);
                    //Send a greetings message 
                    Email email = new Email("Jason Chan", "Welcome to your new email!", Email);
                    email.sendEmail();
                }

                Stmt.close();
                ToDB.closeConn();

            }
        } catch (java.sql.SQLException e) {
            done = false;
            System.out.println("SQLException: " + e);
            while (e != null) {
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("Message: " + e.getMessage());
                System.out.println("Vendor: " + e.getErrorCode());
                e = e.getNextException();
                System.out.println("");
            }
        } catch (java.lang.Exception e) {
            done = false;
            System.out.println("Exception: " + e);
            e.printStackTrace();
        }
        return done;
    }

    public boolean EmailTaken() { //Check if email is taken
        boolean done = !Email.equals("") && !Password.equals("") && !Password1.equals("") && Password.equals(Password1);
        try {
            if (done) {
                DBConnection ToDB = new DBConnection(); //Have a connection to the DB
                Connection DBConn = ToDB.openConn();
                Statement Stmt = DBConn.createStatement();
                String SQL_Command = "SELECT Email FROM Account WHERE Email ='" + Email + "'"; //SQL query command to check if email already taken
                ResultSet Rslt = Stmt.executeQuery(SQL_Command);
                done = Rslt.next();

                Stmt.close();
                ToDB.closeConn();
            }
        } catch (java.sql.SQLException e) {
            done = false;
            System.out.println("SQLException: " + e);
            while (e != null) {
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("Message: " + e.getMessage());
                System.out.println("Vendor: " + e.getErrorCode());
                e = e.getNextException();
                System.out.println("");
            }
        } catch (java.lang.Exception e) {
            done = false;
            System.out.println("Exception: " + e);
            e.printStackTrace();
        }
        return done;
    }

    public boolean changePassword(String NewPassword) {
        boolean done = false;
        try {
            DBConnection ToDB = new DBConnection(); //Have a connection to the DB
            Connection DBConn = ToDB.openConn();
            Statement Stmt = DBConn.createStatement();
            String SQL_Command = "SELECT * FROM Account WHERE Email ='" + Email + "'"; //Check if customer id exist
            ResultSet Rslt = Stmt.executeQuery(SQL_Command);
            if (Rslt.next()) {
                SQL_Command = "UPDATE Account SET Password='" + NewPassword + "' WHERE Email ='" + Email + "'"; //update password to new password
                Stmt.executeUpdate(SQL_Command);
                Stmt.close();
                ToDB.closeConn();
                done = true;
            }
        } catch (java.sql.SQLException e) {
            done = false;
            System.out.println("SQLException: " + e);
            while (e != null) {
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("Message: " + e.getMessage());
                System.out.println("Vendor: " + e.getErrorCode());
                e = e.getNextException();
                System.out.println("");
            }
        } catch (java.lang.Exception e) {
            done = false;
            System.out.println("Exception: " + e);
            e.printStackTrace();
        }
        return done;
    }

    public boolean signIn() {
        boolean done = false;
        try {
            DBConnection ToDB = new DBConnection();
            Connection DBConn = ToDB.openConn();
            Statement Stmt = DBConn.createStatement();
            String SQL_Command = "SELECT * FROM Account WHERE Email ='" + Email + "' AND Password ='" + Password + "'"; //SQL query command
            ResultSet Rslt = Stmt.executeQuery(SQL_Command);
            done = Rslt.next();
            if (done) {                         //If there is a row, user typed in valid username and password and return true
                done = true;
            }
        } catch (java.sql.SQLException e) {
            done = false;
            System.out.println("SQLException: " + e);
            while (e != null) {
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("Message: " + e.getMessage());
                System.out.println("Vendor: " + e.getErrorCode());
                e = e.getNextException();
                System.out.println("");
            }
        } catch (java.lang.Exception e) {
            done = false;
            System.out.println("Exception: " + e);
            e.printStackTrace();
        }
        return done;
    }

    //Check if account is valid
    public boolean isValid() {
        boolean done = false;
        try {
            DBConnection ToDB = new DBConnection();
            Connection DBConn = ToDB.openConn();
            Statement Stmt = DBConn.createStatement();
            String SQL_Command = "SELECT * FROM Account WHERE Email ='" + Email + "' AND Password ='" + Password + "'"; //SQL query command
            ResultSet Rslt = Stmt.executeQuery(SQL_Command);
            done = Rslt.next();
            if (done) {                         //If there is a row, user typed in valid username and password and return true
                done = true;
            }
        } catch (java.sql.SQLException e) {
            done = false;
            System.out.println("SQLException: " + e);
            while (e != null) {
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("Message: " + e.getMessage());
                System.out.println("Vendor: " + e.getErrorCode());
                e = e.getNextException();
                System.out.println("");
            }
        } catch (java.lang.Exception e) {
            done = false;
            System.out.println("Exception: " + e);
            e.printStackTrace();
        }
        return done;
    }

}
