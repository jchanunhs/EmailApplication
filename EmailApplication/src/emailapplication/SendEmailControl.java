package emailapplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;

public class SendEmailControl {

    public SendEmailControl(String EM, String PW, String MSG, String Recipent) {
        try {
            Email email = new Email(EM, MSG, Recipent);

            Socket echoSocket = new Socket("localhost", 1233); //connect to server
            BufferedReader sin = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
            System.out.println("Client: Sending Email Information " + EM + " to server!");
            out.println(EM); //Send email and password to server
            out.println(PW);
            out.println("POST"); //tell server we're sending emails
            out.println(MSG); //send message and recipent email address
            out.println(Recipent);
            System.out.println("Client: Waiting for response. Please Wait!");

            while (!sin.ready()) { //wait for reply

            }
            String SV_MSG = sin.readLine(); //get message from server
            System.out.println("Client: Server sent message: " + SV_MSG);
            System.out.println(SV_MSG);
            String Result = sin.readLine(); //get result from server 

            if (Result.equals("Success")) {
                JOptionPane.showMessageDialog(null, "Email Sent Successfully!", "Confirmation", JOptionPane.INFORMATION_MESSAGE);

            } else if (Result.equals("Failed")) {
                JOptionPane.showMessageDialog(null, "Email did not send successfully! Recipent Email does not exist!", "Confirmation", JOptionPane.ERROR_MESSAGE);
            }
            sin.close();
            out.close();
            echoSocket.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Server is offline! Please try again later!", "Confirmation", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

    }
}
