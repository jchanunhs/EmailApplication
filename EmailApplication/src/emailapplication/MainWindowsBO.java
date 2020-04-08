package emailapplication;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class MainWindowsPanel extends JPanel {

    public MainWindowsPanel(String EM, String PW) {

        InboxPanel inbox = new InboxPanel(EM, PW); //inbox panel has a thread
        inbox.start(); //start thread

        SendEmailPanel sendmail = new SendEmailPanel(EM, PW); //send email
        JTabbedPane mp = new JTabbedPane(); //place them in jtabbed pane

        //add panels to tabs
        mp.add("Inbox", inbox);
        mp.add("Compose", sendmail);

        //center the panel
        setLayout(new BorderLayout());
        add(mp, BorderLayout.CENTER);

    }

}

public class MainWindowsBO extends JFrame {

    private MainWindowsPanel CP_Panel;

    public MainWindowsBO(String EM, String PW) {
        setTitle("Main Windows");
        setSize(840, 840);

        setVisible(true);

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
        CP_Panel = new MainWindowsPanel(EM, PW);
        contentPane.add(CP_Panel);
        show();
    }

}
