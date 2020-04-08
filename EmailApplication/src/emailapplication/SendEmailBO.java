package emailapplication;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class SendEmailPanel extends JPanel implements ActionListener {

    private JButton OpenButton;
    private JTextField EmailField, MessageField, SendToField;
    private String Email, Message, SendTo, Password;

    public SendEmailPanel(String EM, String PW) {
        OpenButton = new JButton("Submit"); //submit button

        Email = EM;
        Password = PW;

        //Show user their email address
        EmailField = new JTextField(15);
        EmailField.setText(Email);
        EmailField.setEditable(false);

        MessageField = new JTextField(15);
        SendToField = new JTextField(15);

        //Labels 
        JLabel EmailLabel = new JLabel("Your Email: ");
        JLabel MessageLabel = new JLabel("Message: ");
        JLabel SendToLabel = new JLabel("Send To: ");

        //Panels
        JPanel EmailPanel = new JPanel();
        JPanel MessagePanel = new JPanel();
        JPanel SendToPanel = new JPanel();

        //Add TextFields and Labels to panels
        SendToPanel.add(SendToLabel);
        SendToPanel.add(SendToField);
        EmailPanel.add(EmailLabel);
        EmailPanel.add(EmailField);
        MessagePanel.add(MessageLabel);
        MessagePanel.add(MessageField);

        OpenButton.addActionListener(this); //event listener registration

        JPanel CenterPanel = new JPanel();
        CenterPanel.add(EmailPanel);
        CenterPanel.add(SendToPanel);
        CenterPanel.add(MessagePanel);
        CenterPanel.add(OpenButton);
        setLayout(new BorderLayout());
        add(CenterPanel, BorderLayout.CENTER);
        //add(OpenButton, BorderLayout.SOUTH);//add the one button on to this panel
    }

    public void actionPerformed(ActionEvent evt) //event handling
    {
        //Object source = evt.getSource(); //get who generates this event
        String arg = evt.getActionCommand();
        if (arg.equals("Submit")) { //determine which button is clicked
            //get inputs and pass to SendEmailControl
            Email = EmailField.getText();
            Message = MessageField.getText();
            SendTo = SendToField.getText();
            SendEmailControl SE_CTRL = new SendEmailControl(Email, Password, Message, SendTo);

        }
    }

}

public class SendEmailBO extends JFrame {

    private SendEmailPanel CP_Panel;

    public SendEmailBO(String EM, String PW) {
        setTitle("Send Emails");
        setSize(720, 720);

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
        CP_Panel = new SendEmailPanel(EM, PW);
        contentPane.add(CP_Panel);
        show();
    }
}
