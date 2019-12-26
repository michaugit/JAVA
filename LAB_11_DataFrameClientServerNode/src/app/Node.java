package app;

import files.*;

import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
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
    // to display time
    private SimpleDateFormat sdf;

    Node(String server, int port) {
        this.server = server;
        this.port = port;
        sdf = new SimpleDateFormat("HH:mm:ss");
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
            String msg = scan.nextLine();
            // logout if message is LOGOUT
            if (msg.equalsIgnoreCase("LOGOUT")) {
                node.sendMessageToServer("LOGOUT");
                // break to do the disconnect
                break;
            } else {
                node.display("Incorrect request, you can only logout by LOGOUT");
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
        String time = sdf.format(new Date());
        System.out.println(time + " " + msg);
    }

    /**
     * To send a message to the server
     */
    void sendMessageToServer(String message){
        try {
            sOutput.writeObject(message);
        } catch (IOException e) {
            display("Exception writing to server: " + e);
        }
    }

    void sendToServer(NodeResultDF ndfResult) {
        try {
            sOutput.writeObject(ndfResult);
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
                    Object obj = sInput.readObject();
                    if (obj instanceof String) {
                        display((String) obj);
                    } else if (obj instanceof NodeRequestGDF) {
                        display("I have received NodeRequest from client ID: " + ((NodeRequestGDF) obj).getClientID());
                        DataFrame returnToServer = (((NodeRequestGDF) obj).groupedDF.applyWithThreads(((NodeRequestGDF) obj).getFunction()));
                        System.out.println("NodeRequest size: " + ((NodeRequestGDF) obj).groupedDF.getSize());
                        System.out.println("returnToserver size: " + returnToServer.size());
                        NodeResultDF nodeResultDF = new NodeResultDF(returnToServer, ((NodeRequestGDF) obj).getClientID());
                        sendToServer(nodeResultDF);
                        display("I have returned result to Server to client ID: " + nodeResultDF.clientID);

                    } else {
                        display("I have received something i do not know what is this :(");
                    }
                } catch (IOException e) {
                    display("Server has close the connection: " + e);
                    System.exit(0);
                } catch (ClassNotFoundException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
}