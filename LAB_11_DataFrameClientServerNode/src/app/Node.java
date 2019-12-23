package app;

import files.*;

import java.net.*;
import java.io.*;
import java.util.*;

// E:\Java\GIT\LAB_11_DataFrameClientServerNode\out\production\LAB_11_DataFrameClientServerNode

// java -cp E:\Java\GIT\LAB_9_DataFrameThread\out\artifacts\LAB_9_DataFrameThread_jar\LAB_9_DataFrameThread.jar; app.Node
public class Node {
    // for I/O
    private ObjectInputStream sInput;       // to read from the socket
    private ObjectOutputStream sOutput;     // to write on the socket
    private Socket socket;

    // the server, the port
    private DataFrame dataframe;
    private String server;
    private int port;

    Node(String server, int port) {
        this.server = server;
        this.port = port;
    }

    /**
     * To start the Client in console mode use one of the following command
     * > java Client
     * > java Client portNumber
     * > java Client portNumber serverAddress
     * at the console prompt
     * If the portNumber is not specified 1500 is used
     * If the serverAddress is not specified "localHost" is used
     */

    public static void main(String[] args) {
        // default values
        int portNumber = 1500;
        String serverAddress = "localhost";

        switch (args.length) {
            // > javac Client username portNumber serverAddr
            case 2:
                serverAddress = args[1];
                // > javac Client username portNumber
            case 1:
                try {
                    portNumber = Integer.parseInt(args[0]);
                } catch (Exception e) {
                    System.out.println("Invalid port number.");
                    System.out.println("Usage is: > java Client [portNumber] [serverAddress]");
                    return;
                }
            case 0:
                break;
            // invalid number of arguments
            default:
                System.out.println("Usage is: > java Client [username] [portNumber] {serverAddress]");
                return;
        }

        // create the Client object
        Node node = new Node(serverAddress, portNumber);
        // test if we can start the connection to the Server
        if (!node.start()) return;

        // wait for messages from user
        Scanner scan = new Scanner(System.in);

        // loop forever for message from the user
        while (true) {
            // read message from user
            System.out.print("> ");
            String msg = scan.nextLine();
            // logout if message is LOGOUT
            if (msg.equalsIgnoreCase("LOGOUT")) {
                //client.sendMessage(new ChatMessage(ChatMessage.LOGOUT, ""));
                // break to do the disconnect
                break;
            } else {
                System.out.println("Incorrect request, you can only logout by LOGOUT");
            }
        }
        // done disconnect
        node.disconnect();
    }

    /**
     * To start the dialog
     */
    public boolean start() {
        // try to connect to the server
        try {
            socket = new Socket(server, port);
        } catch (Exception ec) {
            display("Error connectiong to server:" + ec);
            return false;
        }

        String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
        display(msg);

        /* Creating both Data Stream */
        try {
            sInput = new ObjectInputStream(socket.getInputStream());
            sOutput = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException eIO) {
            display("Exception creating new Input/output Streams: " + eIO);
            return false;
        }

        // creates the Thread to listen from the server
        new ListenFromServer().start();

        try {
            sOutput.writeObject("NODE");
        } catch (IOException eIO) {
            display("Exception doing login : " + eIO);
            disconnect();
            return false;
        }
        // success we inform the caller that it worked
        return true;
    }

    private void display(String msg) {
        System.out.println(msg);      // println in console
    }

    /**
     * To send a message to the server
     */
    void sendToServer(ServerRequestGDF msg) {
        try {
            sOutput.writeObject(msg);
        } catch (IOException e) {
            display("Exception writing to server: " + e);
        }
    }

    /**
     * When something goes wrong => Close the Input/Output streams and disconnect not much to do in the catch clause
     */
    private void disconnect() {
        try {
            if (sInput != null) sInput.close();
        } catch (Exception e) {
        } // not much else I can do
        try {
            if (sOutput != null) sOutput.close();
        } catch (Exception e) {
        } // not much else I can do
        try {
            if (socket != null) socket.close();
        } catch (Exception e) {
        } // not much else I can do
    }

    class ListenFromServer extends Thread {
        public void run() {
            while (true) {
                try {
                    String msg = (String) sInput.readObject();
                    display(msg);
                    try {
                        sOutput.writeObject("Obliczyłem :D");
                    }
                    catch(Exception exc){}
                } catch (IOException e) {
                    display("Server has close the connection: " + e);
                    e.printStackTrace();
                    System.exit(0);
                } catch (ClassNotFoundException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
}