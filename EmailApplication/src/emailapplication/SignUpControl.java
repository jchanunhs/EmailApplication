package emailapplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JOptionPane;

public class SignUpControl {

    private Account Acct;

    public SignUpControl() {

    }

    SignUpControl(String EM, String PsWord, String PsWord1, String Name) {

        try {
            Socket echoSocket = new Socket("localhost", 1233);
            BufferedReader sin = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
            System.out.println("Client: Sending Email Information " + EM + " to server!");
            out.println(EM); //Send email and password to server
            out.println(PsWord);
            out.println("Sign Up"); // tell server we're signing up 
            System.out.println("Client: Waiting for response. Please Wait!");
            out.println(PsWord1); // sendinng additional information to server to create the account
            out.println(Name);
            while (!sin.ready()) { //wait for reply

            }
            String SV_MSG = sin.readLine();
            System.out.println("Client: Server sent message: " + SV_MSG); //show server message
            String Result = sin.readLine(); //get results from server
            if (Result.equals("Success")) {
                JOptionPane.showMessageDialog(null, "Account has been created!", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
            } else if (Result.equals("Taken")) {
                JOptionPane.showMessageDialog(null, "Email is taken!", "Confirmation", JOptionPane.ERROR_MESSAGE);
            } else if (Result.equals("Error")) {
                JOptionPane.showMessageDialog(null, "Account creation failed due to unmatched passwords", "Confirmation", JOptionPane.ERROR_MESSAGE);
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
