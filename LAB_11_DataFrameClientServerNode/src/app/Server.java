package app;

import files.DataFrame;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

// E:\Java\GIT\LAB_11_DataFrameClientServerNode\out\production\LAB_11_DataFrameClientServerNode

// java -cp E:\Java\GIT\LAB_9_DataFrameThread\out\artifacts\LAB_9_DataFrameThread_jar\LAB_9_DataFrameThread.jar; app.Server

public class Server {
    // a unique ID for each connection
    private static int uniqueId;
    HashMap<Integer, ClientThread> clients;
    HashMap<Integer, NodeThread> nodes;
    private int port;
    private boolean keepGoing;
    // to display time
    private SimpleDateFormat sdf;


    /**
     * server constructor that receive the port to listen to for connection as parameter
     */
    public Server(int port) {
        this.port = port;
        clients = new HashMap<>();
        nodes = new HashMap<>();
        sdf = new SimpleDateFormat("HH:mm:ss");
    }

    /**
     * To run as a console application just open a console window and:
     * > java Server
     * > java Server portNumber
     * If the port number is not specified 1500  is used
     */
    public static void main(String[] args) {
        // start server onException reading Streams port 1500 unless a PortNumber is specified
        int portNumber = 1500;
        switch (args.length) {
            case 1:
                try {
                    portNumber = Integer.parseInt(args[0]);
                } catch (Exception e) {
                    System.out.println("Invalid port number.");
                    System.out.println("Usage is: > java Server [portNumber]");
                    return;
                }
            case 0:
                break;
            default:
                System.out.println("Usage is: > java Server [portNumber]");
                return;
        }
        // create a server object and start it
        Server server = new Server(portNumber);
        server.start();
    }

    public void start() {
        keepGoing = true;
        /** create socket server and wait for connection requests */
        try {
            // the socket used by the server
            ServerSocket serverSocket = new ServerSocket(port);

            // infinite loop to wait for connections
            while (keepGoing) {
                display("Server is waiting on port " + port + ".");
                Socket socket = serverSocket.accept();      // accept connection
                System.out.println("Thread trying to create Object Input/Output Streams");
                try {
                    ObjectOutputStream sOutput = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream sInput = new ObjectInputStream(socket.getInputStream());
                    String who = (String) sInput.readObject();
                    display(who + " just connected");

                    if (who.equalsIgnoreCase("CLIENT")) {
                        ClientThread ct = new ClientThread(socket, sOutput, sInput);
                        clients.put(ct.id, ct);
                        ct.start();
                    } else if (who.equalsIgnoreCase("NODE")) {
                        NodeThread nt = new NodeThread(socket, sOutput, sInput);
                        nodes.put(nt.id, nt);
                        nt.start();
                    }
                } catch (IOException e) {
                    display("Exception creating new Input/output Streams: " + e);
                    return;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if (!keepGoing) break;      // if I was asked to stop
            }

            // I was asked to stop
            try {
                serverSocket.close();
//                for (Map.Entry<Integer, ClientThread> pair : loggedClients.entrySet()) {
//                    ClientThread tc = pair.getValue();
//                    try {
//                        tc.sInput.close();
//                        tc.sOutput.close();
//                        tc.socket.close();
//                    } catch (IOException ioE) {
//                        // not much I can do
//                    }
//                }
            } catch (Exception e) {
                display("Exception closing the server and clients: " + e);
            }
        }
        // something went bad
        catch (IOException e) {
            String msg = sdf.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
            display(msg);
        }
    }

    protected void stop() {
        keepGoing = false;
        // connect to myself as Client to exit statement
        // Socket socket = serverSocket.accept();
        try {
            new Socket("localhost", port);
        } catch (Exception e) {
            // nothing I can really do
        }
    }

    /**
     * Display an event (not a message) to the console
     */
    private void display(String msg) {
        String time = sdf.format(new Date());
        System.out.println(time + " " + msg);
    }

    /**
     * to broadcast a message
     */
    private synchronized void broadcast(Integer id, String message) {
        // add HH:mm:ss and \n to the message
        String time = sdf.format(new Date());
    }

    synchronized void remove(int id) {
        // scan the maps until we found the Id (first clients, because we rather not remove nodes)
        for (Map.Entry<Integer, ClientThread> pair : clients.entrySet()) {
            ClientThread ct = pair.getValue();
            // found it
            if (ct.id == id) {
                clients.remove(pair.getKey());
                return;
            }
        }
        for (Map.Entry<Integer, NodeThread> pair : nodes.entrySet()) {
            NodeThread nt = pair.getValue();
            // found it
            if (nt.id == id) {
                clients.remove(pair.getKey());
                return;
            }
        }
    }

    synchronized void divideGDFbetweenNodes(ServerRequestGDF srGDF, Integer clientID){
        //rozczytaj ile masz nodeow i podzielić srGDF na tyle nrGDF jesli nie ma żadnego odesłać że ups ale nie nie ma zadnych nodow
        display("rozdzielam na: " + nodes.size() + " node'ow");

        for (Map.Entry<Integer, NodeThread> pair : nodes.entrySet()) {
            pair.getValue().writeMsg("hehe przydzielam ci zadanie od klienta o ID: " + clientID);
        }

    }

    /**
     * One instance of this thread will run for each client
     */
    class ClientThread extends Thread {
        // the socket where to listen/talk
        Socket socket;
        ObjectInputStream sInput;
        ObjectOutputStream sOutput;
        Integer id;     // unique id
        String date;    // the date connect
        DataFrame dfToReturn;
        ServerRequestGDF requestGDF;

//        // the only type of message a will receive
//        ChatMessage cm;


        ClientThread(Socket socket, ObjectOutputStream sOutput, ObjectInputStream sInput) {
            // a unique id
            id = ++uniqueId;
            this.socket = socket;
            /* Creating both Data Stream */
            this.sInput = sInput;
            this.sOutput = sOutput;

            date = new Date().toString() + "\n";
        }

        // what will run forever
        public void run() {
            // to loop until LOGOUT
            boolean keepGoing = true;
            while (keepGoing) {
                // read a GDF (which is an object)
                try {
                    Object obj = sInput.readObject();
                    if (obj instanceof String) {
                        display((String) obj);
                    } else if (obj instanceof ServerRequestGDF) {
                        display("Dostałem ServerRequest");
                        divideGDFbetweenNodes((ServerRequestGDF) obj, id );
                    } else {
                        display("WHAT IS THIS!!!");
                    }
                } catch (IOException e) {
                    display("Client id: " + id + " Exception reading Streams: " + e);
                    break;
                } catch (ClassNotFoundException e2) {
                    break;
                }
                display("Server has receive request from client: " + this.id);

//                // Switch on the type of message receive
//                switch (cm.getType()) {
//
//                    case ChatMessage.MESSAGE:
//                        broadcast(id, message);
//                        break;
//                    case ChatMessage.WRITE_TO:
//                        writeTo = message;
//                        writeMsg("Now you are writing to: " + writeTo + " \n\n> ");
//                        break;
//                    case ChatMessage.LOGOUT:
//                        display(username + " disconnected with a LOGOUT message.");
//                        keepGoing = false;
//                        break;
//                    case ChatMessage.WHOISIN:
//                        writeMsg("List of the users connected at " + sdf.format(new Date()) + "\n");
//                        // scan all the users connected
//                        Integer iter = 0;
//                        for (Map.Entry<Integer, ClientThread> pair : loggedClients.entrySet()) {
//                            ClientThread ct = pair.getValue();
//                            writeMsg((iter + 1) + ") " + ct.username + " since " + ct.date);
//                            iter++;
//                        }
//                        break;
//                }
            }
            // remove myself from the arrayList containing the list of the
            // connected Clients
            remove(id);
            close();
        }

        // try to close everything
        private void close() {
            // try to close the connection
            try {
                if (sOutput != null) sOutput.close();
            } catch (Exception e) {
            }
            try {
                if (sInput != null) sInput.close();
            } catch (Exception e) {
            }
            ;
            try {
                if (socket != null) socket.close();
            } catch (Exception e) {
            }
        }

        /**
         * Write to the Client output stream
         */ //narazie to tylko string a potem docelowo ma byc dataframe
        public boolean writeMsg(String msg) {
            // if Client is still connected send the message to it
            if (!socket.isConnected()) {
                close();
                return false;
            }
            // write the message to the stream
            try {
                sOutput.writeObject(msg);
            }
            // if an error occurs, do not abort just inform the user
            catch (IOException e) {
                display("Error sending message to " + id);
                display(e.toString());
            }
            return true;
        }
    }

    /**
     * One instance of this thread will run for each node
     */
    class NodeThread extends Thread {
        // the socket where to listen/talk
        Socket socket;
        ObjectInputStream sInput;
        ObjectOutputStream sOutput;
        Integer id;     // unique id
        String date;    // the date  connect
        ServerRequestGDF requestGDF;

//        // the only type of message a will receive
//        ChatMessage cm;


        NodeThread(Socket socket, ObjectOutputStream sOutput, ObjectInputStream sInput) {
            // a unique id
            id = ++uniqueId;
            this.socket = socket;
            /* Creating both Data Stream */
            this.sInput = sInput;
            this.sOutput = sOutput;

            date = new Date().toString() + "\n";
        }

        // what will run forever
        public void run() {
            // to loop until LOGOUT
            boolean keepGoing = true;
            while (keepGoing) {
                // read a GDF (which is an object)
                try {
                    Object obj = sInput.readObject();
                    if (obj instanceof String) {
                        display((String) obj);
                    } else if (obj instanceof ServerRequestGDF) {
                        display("Dostałem DataFrame'a");
                    } else {
                        display("WHAT IS THIS!!!");
                    }
                } catch (IOException e) {
                    display("Node id: " + id + " Exception reading Streams: " + e);
                    break;
                } catch (ClassNotFoundException e2) {
                    break;
                }
                display("Server has receive DataFrame from Node: " + this.id);
            }
            // remove myself from the arrayList containing the list of the
            // connected Clients
            remove(id);
            close();
        }

        // try to close everything
        private void close() {
            // try to close the connection
            try {
                if (sOutput != null) sOutput.close();
            } catch (Exception e) {
            }
            try {
                if (sInput != null) sInput.close();
            } catch (Exception e) {
            }
            ;
            try {
                if (socket != null) socket.close();
            } catch (Exception e) {
            }
        }

        /**
         * Write to the Client output stream
         */ //narazie to tylko string a potem docelowo ma byc GDF
        public boolean writeMsg(String msg) {
            // if Client is still connected send the message to it
            if (!socket.isConnected()) {
                close();
                return false;
            }
            // write the message to the stream
            try {
                sOutput.writeObject(msg);
            }
            // if an error occurs, do not abort just inform the user
            catch (IOException e) {
                display("Error sending message to " + id);
                display(e.toString());
            }
            return true;
        }
    }
}