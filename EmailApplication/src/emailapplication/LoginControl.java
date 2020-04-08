package emailapplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;

public class LoginControl {

    public LoginControl(String Email, String Password) {
        Account Acct = new Account(Email, Password); //Account with email and password

        try {
            Socket echoSocket = new Socket("localhost", 1233); // connect to server
            BufferedReader sin = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);

            System.out.println("Client: Sending Email Information " + Email + " to server!");
            out.println(Email); //Send email and password to server
            out.println(Password);
            out.println("Login"); // tell server we are trying to login
            System.out.println("Client: Waiting for response. Please Wait!");

            while (!sin.ready()) { //wait for reply

            }
            String SV_MSG = sin.readLine();
            System.out.println("Client: Server sent message: " + SV_MSG); //server sends message
            String Result = sin.readLine(); //get server result 
            if (Result.equals("Success")) {
                System.out.println("Login Success! Opening MainWindows!");
                MainWindowsBO main = new MainWindowsBO(Email, Password);
            }
            else{
                 JOptionPane.showMessageDialog(null, "Error: Either invalid account or password!", "Confirmation", JOptionPane.ERROR_MESSAGE);
            }
            sin.close();
            out.close();
            echoSocket.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Server is offline! Please try again later!", "Confirmation", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}
