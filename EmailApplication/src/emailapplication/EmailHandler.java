package emailapplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

class EmailHandler implements Runnable {

    Thread thread;
    private Socket echoSocket;
    private BufferedReader sin;
    private PrintWriter out;
    private String UserEmail;
    private String Password;
    private Email em;

    //Handler contains the sockets and sets up printwriter and bufferedreader for communication to client
    EmailHandler(Socket skt) throws IOException {
        echoSocket = skt;
        sin = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        out = new PrintWriter(echoSocket.getOutputStream(), true);
    }

    //Start thread
    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();

        }
    }

    @Override
    public void run() {
        while (thread != null) {
            try {
                System.out.println("Connection from: " + echoSocket.getInetAddress()); //get connection address
                //if echosocket inputstream not ready, do nothing and wait!            
                while (!sin.ready()) { //wait for reply

                }

          //      System.out.println("Number of threads: " + Thread.activeCount() + " and current thread ID: " + Thread.currentThread().getId() + " and thread name is: " + Thread.currentThread().getName());

                UserEmail = sin.readLine(); //get user information along with their action
                Password = sin.readLine();
                String Action = sin.readLine();
                out.println("Email Recieved: " + UserEmail + " with action: " + Action); //let client know we recieved the email

                System.out.println("Server: Email Recieved: " + UserEmail + " with password: " + Password + " action: " + Action);  //server also shows information about recieved email

                if (Action.equals("GET")) { //getting emails
                    Account acct = new Account(UserEmail, Password);
                    if (acct.isValid()) { //used in case users try to connect via putty.
                        //Create email object and use it to get the results from DB
                        em = new Email(UserEmail);
                        ResultSet Rslt = em.getAllEmails(UserEmail, Password); //gets all emails from the user

                        //for each row, we get the Email Information
                        while (Rslt.next()) {
                            String SF = Rslt.getString("SendFrom");
                            String MSG = Rslt.getString("Mail");
                            String TDate = Rslt.getString("Date");
                            String TTime = Rslt.getString("Time");
                            //output the email information to outputstream for client
                            out.println(SF);
                            out.println(MSG);
                            out.println(TDate);
                            out.println(TTime);
                            //Server display information from the emails (used for error checking)
              //              System.out.println("Server: Data sent to client: " + SF + " " + MSG + " " + TDate + " " + TTime);
                        }

                        //Notify client no more emails
                        out.println("Done");

                        System.out.println("Server: Done fetching data for email: " + UserEmail);

                        Rslt.close();
                    } else {
                        out.println("Wrong Information!");
                    }
                } else if (Action.equals("Populate")) { //getting emails based on dates
                    Account acct = new Account(UserEmail, Password);
                    if (acct.isValid()) { //used in case users try to connect via putty.
                        //Create email object and use it to get the results from DB
                        em = new Email(UserEmail);
                        //read client input for dates BF = Before, AR = After
                        String BF = sin.readLine();
                        String AR = sin.readLine();
                        ResultSet Rslt = em.getEmails(BF, AR); //Get emails based on dates

                        //for each row, we get the Email Information
                        while (Rslt.next()) {
                            String SF = Rslt.getString("SendFrom");
                            String MSG = Rslt.getString("Mail");
                            String TDate = Rslt.getString("Date");
                            String TTime = Rslt.getString("Time");
                            //output the email information to outputstream for client
                            out.println(SF);
                            out.println(MSG);
                            out.println(TDate);
                            out.println(TTime);
                            //Server display information from the emails (used for error checking)
    //                        System.out.println("Server: Data sent to client: " + SF + " " + MSG + " " + TDate + " " + TTime);
                        }

                        //Notify client no more emails
                        out.println("Done");

                        System.out.println("Server: Done fetching data for email: " + UserEmail);

                        Rslt.close();
                    } else {
                        out.println("Wrong Information!");
                    }
                } else if (Action.equals("POST")) {  //sending email
                    Account acct = new Account(UserEmail, Password);
                    if (acct.isValid()) { //get message and recipent from client
                        String Message = sin.readLine();
                        String Recipent = sin.readLine();
                        em = new Email(UserEmail, Message, Recipent);

                        if (em.sendEmail()) { //attempt to send email
                            out.println("Success");
                            System.out.println("Server: Email sent successfully!");
                        } else {
                            out.println("Failed");
                            System.out.println("Server: Email failed to send to recipent: " + Recipent);
                        }

                    } else {
                        out.println("Wrong information!");
                    }
                } else if (Action.equals("Login")) {//logging in
                    Account acct = new Account(UserEmail, Password);
                    if (acct.isValid()) {
                        if (acct.signIn()) {
                            out.println("Success");
                            System.out.println("Login Success!");
                        } else {
                            out.println("Failed");
                            System.out.println("Error logging in!");
                        }
                    } else {
                        out.println("Wrong information!");
                        System.out.println("Invalid account information!");
                    }

                } else if (Action.equals("Sign Up")) {//Signing up for account
                    String pw1 = sin.readLine();
                    String name = sin.readLine();
                    Account acct = new Account(UserEmail, Password, pw1, name);
                    if (acct.signUp() && Password.equals(pw1)) {
                        out.println("Success");
                        System.out.println("Sign up success!");
                    } else if (acct.EmailTaken()) {
                        out.println("Taken");
                        System.out.println("Email Taken!");
                    } else {
                        out.println("Error");
                        System.out.println("Mismatch passwords");
                    }

                }
                System.out.println("Closing connection from: " + echoSocket.getInetAddress());
                //close connnections
                sin.close();
                out.close();
                echoSocket.close();
                Thread.currentThread().stop();
            } catch (IOException ex) {
                Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
                //      System.out.println("Server: Client with Email: " + UserEmail + " has disconnected!");
            } catch (SQLException ex) {
                Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            /*catch (InterruptedException ex) {
                Logger.getLogger(EmailHandler.class.getName()).log(Level.SEVERE, null, ex);
            }*/

        }
    }

}
