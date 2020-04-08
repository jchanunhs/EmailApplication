package emailapplication;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class SignUpPanel extends JPanel implements ActionListener {

    private JButton RegisterButton;
    private JTextField EmailField, NameField;
    private JPasswordField PasswordField, PasswordField1;
    private String Email, PsWord, PsWord1, Name;
    private Account Acct;

    public SignUpPanel() {
        RegisterButton = new JButton("Register"); //initializing two button references

        EmailField = new JTextField(15);
        PasswordField = new JPasswordField(15);
        PasswordField1 = new JPasswordField(15);
        NameField = new JTextField(15);

        JLabel EmailLabel = new JLabel("Email: ");
        JLabel PasswordLabel = new JLabel("Password: ");
        JLabel PasswordLabel1 = new JLabel("Re-enter Password");
        JLabel NameLabel = new JLabel("Name");

        JPanel EmailPanel = new JPanel();
        JPanel PasswordPanel = new JPanel();
        JPanel PasswordPanel1 = new JPanel();
        JPanel NamePanel = new JPanel();

        EmailPanel.add(EmailLabel);
        EmailPanel.add(EmailField);
        PasswordPanel.add(PasswordLabel);
        PasswordPanel.add(PasswordField);
        PasswordPanel1.add(PasswordLabel1);
        PasswordPanel1.add(PasswordField1);
        NamePanel.add(NameLabel);
        NamePanel.add(NameField);

        add(EmailPanel);
        add(PasswordPanel);
        add(PasswordPanel1);
        add(NamePanel);

        add(RegisterButton);  //add the two buttons on to this panel
        RegisterButton.addActionListener(this); //event listener registration
    }

    public void actionPerformed(ActionEvent evt) //event handling
    {
        //Object source = evt.getSource(); //get who generates this event
        String arg = evt.getActionCommand();
        if (arg.equals("Register")) { //determine which button is clicked
            Email = EmailField.getText(); //take actions
            PsWord = PasswordField.getText();
            PsWord1 = PasswordField1.getText();
            Name = NameField.getText();

            SignUpControl SU_CTRL = new SignUpControl(Email, PsWord, PsWord1, Name);
        }
    }

}

public class SignUpBO extends JFrame {

    private SignUpPanel SU_Panel;

    public SignUpBO() {
        setTitle("Sign Up");
        setSize(340, 210);

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

        Container contentPane = getContentPane(); //add a panel to a frame
        SU_Panel = new SignUpPanel();
        contentPane.add(SU_Panel);
        show();
    }

    /*public static void main(String [] args)
    { JFrame frame = new SignUpBO(); //initialize a JFrame object
      frame.show(); //display the frame
    }*/
}
