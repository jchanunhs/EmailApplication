package emailapplication;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LoginBO extends JFrame implements ActionListener // Implementing ActionListener is for event handling.
{

    private JButton SignUpButton, LoginButton;  //Instance variables
    private JTextField EmailField;
    private JPasswordField PasswordField;

    public LoginBO() {
        setTitle("Login");
        setSize(300, 200);

        //get screen size and set the location of the frame
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
        int screenHeight = d.height;
        int screenWidth = d.width;
        setLocation(screenWidth / 3, screenHeight / 4);

        addWindowListener(new WindowAdapter() //handle window event
        {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        //user can choose to make new account or login with existing account
        SignUpButton = new JButton("Sign Up");
        LoginButton = new JButton("Login");

        EmailField = new JTextField(15);
        PasswordField = new JPasswordField(15);
        PasswordField.setActionCommand("Login");

        //labels
        JLabel FirstTimeUserLabel = new JLabel("First time user? Click Sign Up to register!");
        JLabel EmailLabel = new JLabel("Email: ");
        JLabel PasswordLabel = new JLabel("Password: ");
        //panels
        JPanel EmailPanel = new JPanel();
        JPanel PasswordPanel = new JPanel();

        EmailPanel.add(EmailLabel);
        EmailPanel.add(EmailField);
        PasswordPanel.add(PasswordLabel);
        PasswordPanel.add(PasswordField);

        JPanel LoginPanel = new JPanel();
        LoginPanel.add(EmailPanel);
        LoginPanel.add(PasswordPanel);

        LoginPanel.add(LoginButton);
        LoginPanel.add(FirstTimeUserLabel);
        LoginPanel.add(SignUpButton);

        SignUpButton.addActionListener(this);
        LoginButton.addActionListener(this);
        PasswordField.addActionListener(this);

        Container contentPane = getContentPane(); //add a panel to a frame
        contentPane.add(LoginPanel);

    }

    public void actionPerformed(ActionEvent evt) //event handling
    {
        //Object source = evt.getSource(); //get who generates this event
        String arg = evt.getActionCommand();

        if (arg.equals("Sign Up")) { //determine which button is clicked

            SignUpBO SUC = new SignUpBO();
        }
        if (arg.equals("Login")) {
            //System.out.println("Name: "+arg);
            String Email = EmailField.getText();
            String Password = PasswordField.getText();
            LoginControl LoginC = new LoginControl(Email, Password);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new LoginBO(); //initialize a JFrame object
        frame.show(); //display the frame

    }
}
