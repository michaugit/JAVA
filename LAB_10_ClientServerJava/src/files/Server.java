package files;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;


public class Server {
    // a unique ID for each connection
    private static int uniqueId;
    // an HashMap to keep the list of the Client
    private HashMap<Integer, ClientThread> loggedClients;
    // and HashMap to keep messages for not logged Clients
    private HashMap<String, ArrayList<String>> messagesToSend;
    // to display time
    private SimpleDateFormat sdf;
    // the port number to listen for connection
    private int port;
    // the boolean that will be turned of to stop the server
    private boolean keepGoing;

    /**
     * server constructor that receive the port to listen to for connection as parameter
     */
    public Server(int port) {
        // the port
        this.port = port;
        // to display hh:mm:ss
        sdf = new SimpleDateFormat("HH:mm:ss");
        // HashMap for the Clients
        loggedClients = new HashMap<>();
        // HashMap for the messages to send
        messagesToSend = new HashMap<>();
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
                // format message saying we are waiting
                display("Server waiting for Clients on port " + port + ".");
                Socket socket = serverSocket.accept();      // accept connection
                // if I was asked to stop
                if (!keepGoing) break;
                ClientThread t = new ClientThread(socket);  // make a thread of it
                loggedClients.put(t.id, t);                  // save it in the HashMap
                t.start();
            }
            // I was asked to stop
            try {
                serverSocket.close();
                for (Map.Entry<Integer, ClientThread> pair : loggedClients.entrySet()) {
                    ClientThread tc = pair.getValue();
                    try {
                        tc.sInput.close();
                        tc.sOutput.close();
                        tc.socket.close();
                    } catch (IOException ioE) {
                        // not much I can do
                    }
                }
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
        String time = sdf.format(new Date()) + " " + msg;
        System.out.println(time);
    }

    // for a client who logoff using the LOGOUT message


    /**
     * to broadcast a message to Clients
     */
    private synchronized void broadcast(Integer id, String message) {
        // add HH:mm:ss and \n to the message
        String time = sdf.format(new Date());
        String messageLf = time + " " + loggedClients.get(id).username + " => " + message + "\n";
        // display message on console
        System.out.print(time + " TO: " + loggedClients.get(id).writeTo + " FROM: " + loggedClients.get(id).username + " => " + message + "\n");
        if(loggedClients.get(id).writeTo.equals("\\nobody")){
            loggedClients.get(id).writeMsg(time + " " +"You must select the receiver of your message!" + "\n\n> " );
        }
        else if(loggedClients.get(id).writeTo.equals("\\write_to_all_logged_in_users")) {
            for (Map.Entry<Integer, ClientThread> pair : loggedClients.entrySet()) {
                ClientThread ct = pair.getValue();
                // try to write to the Client if it fails remove it from the list
                if (!ct.writeMsg(messageLf + "> ")) {
                    loggedClients.remove(pair.getKey());
                    display("Disconnected Client " + ct.username + " removed from list.");
                }
            }
        } else {
            boolean found = false;
            for (Map.Entry<Integer, ClientThread> pair : loggedClients.entrySet()) {
                if (pair.getValue().username.equals(loggedClients.get(id).writeTo)) {
                    ClientThread ct = pair.getValue();
                    ct.writeMsg(messageLf + "> ");
                    if (!ct.writeTo.equals(loggedClients.get(id).username)) {
                        ct.writeTo = loggedClients.get(id).username;
                        ct.writeMsg("Now you are writing to: " + ct.writeTo + " \n\n> ");
                    }
                    found = true;
                }
            }
            if (!found) {
                //send after logging in
                if (messagesToSend.containsKey(loggedClients.get(id).writeTo)) {
                    messagesToSend.get(loggedClients.get(id).writeTo).add(messageLf);
                } else {
                    messagesToSend.put(loggedClients.get(id).writeTo, new ArrayList<String>());
                    messagesToSend.get(loggedClients.get(id).writeTo).add(messageLf);
                }
            }
        }

    }


    synchronized void remove(int id) {
        // scan the array list until we found the Id
        for (Map.Entry<Integer, ClientThread> pair : loggedClients.entrySet()) {
            ClientThread ct = pair.getValue();
            // found it
            if (ct.id == id) {
                loggedClients.remove(pair.getKey());
                return;
            }
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
        // my unique id (easier for deconnection)
        int id;
        // the Username of the Client
        String username;
        // the selected person whom you writing
        String writeTo;
        // the only type of message a will receive
        ChatMessage cm;
        // the date I connect
        String date;

        ClientThread(Socket socket) {
            // a unique id
            id = ++uniqueId;
            this.socket = socket;
            /* Creating both Data Stream */
            System.out.println("Thread trying to create Object Input/Output Streams");
            try {
                // create output first
                sOutput = new ObjectOutputStream(socket.getOutputStream());
                sInput = new ObjectInputStream(socket.getInputStream());
                // read the username
                username = (String) sInput.readObject();
                writeTo = new String("\\nobody");
                display(username + " just connected.");
                if (messagesToSend.containsKey(username)) {
                    this.writeMsg("\nMessages sent to you while you were not logged in:\n");
                    ArrayList<String> toDeleteFromHashMap = new ArrayList<>();
                    for (String str : messagesToSend.get(username)) {
                        this.writeMsg("> " + str);
                        toDeleteFromHashMap.add(str);
                    }
                    for (String str : toDeleteFromHashMap) {
                        messagesToSend.values().remove(str);
                    }
                }
                this.writeMsg("\nTo select/change person to whom woud you like to write, enter:\n"
                        + "WRITE TO #NameOfPerson#\n\n");

            } catch (IOException e) {
                display("Exception creating new Input/output Streams: " + e);
                return;
            }
            // have to catch ClassNotFoundException
            // but I read a String, I am sure it will work
            catch (ClassNotFoundException e) {
            }
            date = new Date().toString() + "\n";
        }

        // what will run forever
        public void run() {
            // to loop until LOGOUT
            boolean keepGoing = true;
            while (keepGoing) {
                // read a String (which is an object)
                try {
                    cm = (ChatMessage) sInput.readObject();
                } catch (IOException e) {
                    display(username + " Exception reading Streams: " + e);
                    break;
                } catch (ClassNotFoundException e2) {
                    break;
                }
                // the messaage part of the ChatMessage
                String message = cm.getMessage();

                // Switch on the type of message receive
                switch (cm.getType()) {

                    case ChatMessage.MESSAGE:
                        broadcast(id, message);
                        break;
                    case ChatMessage.WRITE_TO:
                        writeTo = message;
                        writeMsg("Now you are writing to: " + writeTo + " \n\n> ");
                        break;
                    case ChatMessage.LOGOUT:
                        display(username + " disconnected with a LOGOUT message.");
                        keepGoing = false;
                        break;
                    case ChatMessage.WHOISIN:
                        writeMsg("List of the users connected at " + sdf.format(new Date()) + "\n");
                        // scan all the users connected
                        Integer iter = 0;
                        for (Map.Entry<Integer, ClientThread> pair : loggedClients.entrySet()) {
                            ClientThread ct = pair.getValue();
                            writeMsg((iter + 1) + ") " + ct.username + " since " + ct.date);
                            iter++;
                        }
                        break;
                }
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

        /*
         * Write a String to the Client output stream
         */
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
                display("Error sending message to " + username);
                display(e.toString());
            }
            return true;
        }
    }
}