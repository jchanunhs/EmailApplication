package emailapplication;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class PopulateListPanel extends JPanel {

    private String UserEmail, Password;
    private JTextField before, after;

    DefaultTableModel model = new DefaultTableModel();
    JTable table; //table
    String[] columnName = {"From", "Message", "Date", "Time"};

    public PopulateListPanel(String EM, String PW, String BF, String AR) {
        UserEmail = EM;
        Password = PW;

        //JLabels set as uneditable
        JLabel beforelabel = new JLabel("Date from:");
        JLabel afterlabel = new JLabel("Date to:");

        before = new JTextField(15);
        before.setText(BF);
        before.setEditable(false);
        after = new JTextField(15);
        after.setText(AR);
        after.setEditable(false);

        //date panels
        JPanel beforepane = new JPanel();
        JPanel afterpane = new JPanel();
        beforepane.add(beforelabel);
        beforepane.add(before);
        afterpane.add(afterlabel);
        afterpane.add(after);

        //populate table with all emails as default
        model.setColumnIdentifiers(columnName); //column titles

        try {
            Socket echoSocket = new Socket("localhost", 1233); //connect to server
            BufferedReader sin = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
            System.out.println("Client: Sending Email Information " + UserEmail + " to server!");
            out.println(UserEmail); //Send email and password to server
            out.println(Password);
            out.println("Populate"); //populate list
            out.println(BF);
            out.println(AR);
            System.out.println("Client: Waiting for response. Please Wait!");

            while (!sin.ready()) { //wait for reply

            }
            String line = sin.readLine(); //first line of server tells us if it recieved email
            System.out.println("Server Message to Client: " + line);
            //get the first row of email information and add to model if string values are not done
            String SF = sin.readLine();
            String MSG = sin.readLine();
            String TDate = sin.readLine();
            String TTime = sin.readLine();
            if (!SF.equals("Done")) {
                model.addRow(new Object[]{SF, MSG, TDate, TTime});
            }

            //keep running loop until server tells us its done
            while (!SF.equals("Done")) {
                SF = sin.readLine();
                MSG = sin.readLine();
                TDate = sin.readLine();
                TTime = sin.readLine();

                if (!SF.equals("Done")) {
                    model.addRow(new Object[]{SF, MSG, TDate, TTime});
                }
            }
            //close connections
            sin.close();
            out.close();
            echoSocket.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Server is offline! Please try again later!", "Confirmation", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        //initializing a table and scroll pane
        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setPreferredSize(new Dimension(600, 600));

        JLabel listLabel = new JLabel("Email List");

        JPanel p1 = new JPanel(); //contains the label only
        JPanel p2 = new JPanel();//tables

        p1.add(listLabel);
        p2.add(scroll);

        Box b = Box.createVerticalBox();
        //dates
        b.add(beforepane);
        b.add(afterpane);
        //email list
        b.add(p1);
        b.add(p2);
        add(b);

    }
}

class PopulateListBO extends JFrame {

    private PopulateListPanel PL_Panel;

    public PopulateListBO(String EM, String PW, String BF, String AR) {
        setTitle("Your Emails from " + BF + " to " + AR);
        setSize(840, 840);
        //get screen size and set the location of the frame
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
        int screenHeight = d.height;
        int screenWidth = d.width;
        setLocation(screenWidth / 2 - 180, screenHeight / 4);

        addWindowListener(new WindowAdapter() //handle window closing event
        {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        Container contentPane = getContentPane(); //add a panel to a frame
        PL_Panel = new PopulateListPanel(EM, PW, BF, AR);
        contentPane.add(PL_Panel);
        show();
    }

}
