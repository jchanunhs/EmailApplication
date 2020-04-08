package emailapplication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

    SocketServer() {

    }

    public static void main(String args[]) throws IOException {
        ServerSocket socket = new ServerSocket(1233); //port 1233
        while (true) { //continously accept connections and pass them to emailhandler to handle email connections
            Socket echoSocket = socket.accept();
            EmailHandler handler = new EmailHandler(echoSocket);
            handler.start(); //start thread

        }
    }
}
