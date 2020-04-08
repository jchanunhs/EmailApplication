package emailapplication;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class InboxPanel extends JPanel implements ActionListener, Runnable {

    Thread thread = null;//Thread does not start until mainwindows starts it

    Email email;
    private String UserEmail;
    private String Password;
    private JTextField before, after;
    private JButton getlist, signoff;

    DefaultTableModel model = new DefaultTableModel();
    JTable table; //table
    String[] columnName = {"From", "Message", "Date", "Time"}; //column names

    private Socket echoSocket;
    private BufferedReader sin;
    private PrintWriter out;

    public InboxPanel(String EM, String PW) {
        UserEmail = EM;
        Password = PW;
        email = new Email(EM); //Used to get emails to populate inbox

        getlist = new JButton("Populate list"); //button
        getlist.addActionListener(this);
        signoff = new JButton("Sign Off"); //close system
        signoff.addActionListener(this);

        //Account object to get the name of the user
        Account acc = new Account(EM, PW);
        JLabel greetinglabel = new JLabel("Hello " + acc.getName());
        greetinglabel.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel beforelabel = new JLabel("Date from:");
        JLabel afterlabel = new JLabel("Date to:");

        before = new JTextField(15);
        after = new JTextField(15);

        //Adding Buttons, Labels and Textfields in respective panels 
        JPanel greetingpane = new JPanel();
        JPanel beforepane = new JPanel();
        JPanel afterpane = new JPanel();
        JPanel buttonpane = new JPanel();

        greetingpane.add(greetinglabel);

        beforepane.add(beforelabel);
        beforepane.add(before);

        afterpane.add(afterlabel);
        afterpane.add(after);

        buttonpane.add(getlist);
        buttonpane.add(signoff);

        //populate table with all emails as default
        model.setColumnIdentifiers(columnName); //column titles

        //initializing a table and scroll pane
        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setPreferredSize(new Dimension(600, 600));

        //Label for table
        JLabel listLabel = new JLabel("Email List");

        //P1 contains only the label P2 contains the table with the scroll
        JPanel p1 = new JPanel(); //contains the label only
        JPanel p2 = new JPanel();//tables

        p1.add(listLabel);
        p2.add(scroll);

        //make it vertical
        Box b = Box.createVerticalBox();
        //The format: Greeting label, dates textfields, buttons, then table with the scrollpane.
        b.add(greetingpane);
        b.add(beforepane);
        b.add(afterpane);
        b.add(buttonpane);
        //email list
        b.add(p1);
        b.add(p2);
        add(b);

    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        String arg = evt.getActionCommand();
        if (arg.equals("Populate list")) {//open new panel with list of emails based on date
            String BF = before.getText();
            String AR = after.getText();
            if (!BF.equals("") && !AR.equals("")) { //check if inputs are empty
                PopulateListBO PL_PANE = new PopulateListBO(UserEmail, Password, before.getText(), after.getText());
            } else {
                JOptionPane.showMessageDialog(null, "Invalid inputs for dates", "Confirmation", JOptionPane.ERROR_MESSAGE);
            }
        } else if (arg.equals("Sign Off")) {
            System.exit(0);
        }
    }

    //Start the thread
    public void start() {
        if (thread == null) {
            thread = new Thread(this); //start thread
            thread.start();
        }
    }

    public void run() {
        while (thread != null) {

            //populate table with all emails as default
            model.setRowCount(0); //clear the table
            //     System.out.println("Number of threads: " + Thread.activeCount() + " and current thread ID: " + Thread.currentThread().getId() + " and thread name is: " + Thread.currentThread().getName());

            try {
                echoSocket = new Socket("localhost", 1233);
                sin = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
                out = new PrintWriter(echoSocket.getOutputStream(), true);
                System.out.println("Client: Sending Email Information " + UserEmail + " to server!");
                out.println(UserEmail); //Send email to inputstream
                out.println(Password);
                out.println("GET");
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
                System.out.println("Done fetching emails. Connection closed.");
                //close connections
                sin.close();
                out.close();
                echoSocket.close();
                Thread.sleep(5000); //5 second delay for each refresh

            } catch (IOException ex) {
                Logger.getLogger(InboxPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(InboxPanel.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}

class InboxFrame extends JFrame {

    private InboxPanel IB_Panel;

    public InboxFrame(String EM, String PW) {
        setTitle("Your Emails");
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
        IB_Panel = new InboxPanel(EM, PW);

        Container contentPane = getContentPane(); //add a panel to a frame
        contentPane.add(IB_Panel);
        IB_Panel.start();
    }

}
