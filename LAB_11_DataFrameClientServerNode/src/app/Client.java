package app;

import GroupFunctions.*;
import files.*;

import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

// E:\Java\GIT\LAB_11_DataFrameClientServerNode\out\production\LAB_11_DataFrameClientServerNode

// java -cp E:\Java\GIT\LAB_9_DataFrameThread\out\artifacts\LAB_9_DataFrameThread_jar\LAB_9_DataFrameThread.jar; app.Client
public class Client {
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

    Client(String server, int port) {
        this.server = server;
        this.port = port;
        sdf = new SimpleDateFormat("HH:mm:ss");
        display("Loading dataframe from csv, please wait!");
        dataframe= new DataFrame("C:\\Users\\resta\\Desktop\\group2.csv", new Class[]{StringObject.class, DateObject.class, DoubleObject.class, DoubleObject.class});
        display("Dataframe loaded!");
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
        Client client = new Client(serverAddress, portNumber);
        // test if we can start the connection to the Server
        if (!client.start()) return;

        // wait for messages from user
        Scanner scan = new Scanner(System.in);

        // loop forever for message from the user
        while (true) {
            // read message from user
            String msg = scan.nextLine();
            // logout if message is LOGOUT
            if (msg.equalsIgnoreCase("LOGOUT")) {
                client.sendMessageToServer("LOGOUT");
                // break to do the disconnect
                break;
            } else {
                client.display("MESSAGE TO SENT TO THE SERVER: " + msg);
                if (msg.matches("^groupby\\((\"[^\"]+\")(,\"[^\"]+\")*\\)\\..+\\(\\)$")) {
                    String[] tmp = msg.split("\\.");
                    String keysTmp = (tmp[0].substring(8, tmp[0].length() - 1)).replace("\"", "");
                    String[] keys = keysTmp.split(",");
                    Applyable fun= null;
                    switch(tmp[1]) {
                        case "max()":
                            fun = new Max();
                            break;
                        case "min()":
                            fun = new Min();
                            break;
                        case "mean()":
                            fun = new Mean();
                            break;
                        case "mediana()":
                            fun = new Mediana();
                            break;
                        case "std()":
                            fun = new Std();
                            break;
                        case "sum()":
                            fun = new Sum();
                            break;
                        case "var()":
                            fun = new Var();
                            break;
                        default:
                            System.out.println("Function not found");
                            break;
                    }

                    GroupDataFrame GDF = client.dataframe.groupBy(keys);
                    client.sendToServer(new ServerRequestGDF(fun, GDF));
                    client.display("Request was sent to server!");
                } else {
                    System.out.println("Invalid request");
                    System.out.println("Usage is : > groupby(\"[something]\").[function]()");
                }
            }
        }

        // done disconnect
        client.disconnect();
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
            sOutput.writeObject("CLIENT");
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

    class ListenFromServer extends Thread { //trzeba nasłuchiwać stringow albo dfa
        public void run() {
            while (true) {
                try {
                    Object obj = sInput.readObject();
                    if( obj instanceof String){
                        display((String) obj);
                    }
                    if( obj instanceof  DataFrame){
                        display("I have received DataFrame!!! :)");
                        ((DataFrame) obj).print();
                    }
                    else{
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