package emailapplication;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Email {

    private String SendFrom, Mail, Date, Time, SendTo;
    private  DBConnection ToDB = null;
    private Connection DBConn = null;
    //send emails
    Email(String EMAddr, String MSG, String Recipent) {
        SendFrom = EMAddr;
        Mail = MSG;
        SendTo = Recipent;
    }

    //get emails
    Email(String EM) {
        SendTo = EM;
    }

    public boolean sendEmail() {
        boolean done = false;

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(System.currentTimeMillis());
            Date = formatter.format(date);
            formatter = new SimpleDateFormat("HH:mm:ss");
            date = new Date(System.currentTimeMillis());
            Time = formatter.format(date);
            
            ToDB = new DBConnection(); //Have a connection to the DB
            DBConn = ToDB.openConn();
            Statement Stmt = DBConn.createStatement();
            String SQL_Command = "SELECT * FROM Account WHERE Email ='" + SendTo + "'"; //Check if email recipent exist
            ResultSet Rslt = Stmt.executeQuery(SQL_Command);
            if (Rslt.next()) {
                SQL_Command = "SELECT * FROM Account WHERE Email ='" + SendFrom + "'"; //Check if email exist
                Rslt = Stmt.executeQuery(SQL_Command);
                if (Rslt.next()) {
                    SQL_Command = "INSERT INTO EMAIL(SendFrom, Mail, Date, Time, SendTo) VALUES ('" + SendFrom + "', '" + Mail + "', '" + Date + "', '" + Time + "', '" + SendTo + "')"; //update password to new password
                    Stmt.executeUpdate(SQL_Command);
                    Stmt.close();
                    DBConn.close();
                    ToDB.closeConn();
                    done = true;
                }
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

    public ResultSet getAllEmails(String EM, String PW) {
        ResultSet Rslt = null;
        try {

            ToDB = new DBConnection(); //Have a connection to the DB
            DBConn = ToDB.openConn();
            Statement Stmt = DBConn.createStatement();
            Account acc = new Account(EM, PW);
            if (acc.isValid()) {
                //SendTo is the recipent so we want to how many emails were sent to this user (the user = From)
                String SQL_Command = "SELECT * FROM EMAIL WHERE SendTo = '" + SendTo + "'"
                        + "ORDER BY 'Date','Time' ASC";
                Rslt = Stmt.executeQuery(SQL_Command); //execute query to get Mails for From address

            } else {
                Rslt = null;
            }

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
        return Rslt; //returns the executed query rows
    }

    public ResultSet getEmails(String before, String after) {
        ResultSet Rslt = null;
        try {

            ToDB = new DBConnection(); //Have a connection to the DB
            DBConn = ToDB.openConn();
            Statement Stmt = DBConn.createStatement(); //Query to get emails organized by dates
            String SQL_Command = "SELECT * FROM EMAIL WHERE SendTo = '" + SendTo + "'"
                    + " AND Date BETWEEN '" + before + "' AND '" + after + "'"
                    + "ORDER BY 'Date','Time' ASC";
            Rslt = Stmt.executeQuery(SQL_Command); //execute query to get transaction based on dates

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
        return Rslt; //returns the executed query rows
    }

    public void closeAllConnection(){
        try {
            DBConn.close();
            ToDB.closeConn();
        } catch (SQLException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

