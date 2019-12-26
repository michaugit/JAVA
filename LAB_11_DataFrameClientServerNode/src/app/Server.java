package app;

import files.DataFrame;
import files.DataFrameThread;
import files.GroupDataFrame;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// E:\Java\GIT\LAB_11_DataFrameClientServerNode\out\production\LAB_11_DataFrameClientServerNode

// java -cp E:\Java\GIT\LAB_9_DataFrameThread\out\artifacts\LAB_9_DataFrameThread_jar\LAB_9_DataFrameThread.jar; app.Server

public class Server {
    static final int MAX_clients=10;
    static final int MAX_nodes=10;

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
            ExecutorService clientsThreadPool = Executors.newFixedThreadPool(MAX_clients);
            ExecutorService nodesThreadPool = Executors.newFixedThreadPool(MAX_nodes);

            // infinite loop to wait for connections
            while (keepGoing) {
                display("Server is waiting on port " + port + ".");
                Socket socket = serverSocket.accept();      // accept connection
                display("Thread trying to create Object Input/Output Streams");
                try {
                    ObjectOutputStream sOutput = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream sInput = new ObjectInputStream(socket.getInputStream());
                    String who = (String) sInput.readObject();
                    display(who + " just connected, assigned ID: " + (uniqueId + 1));

                    if (who.equalsIgnoreCase("CLIENT")) {
                        ClientThread ct = new ClientThread(socket, sOutput, sInput);
                        clientsThreadPool.execute(ct);
//                        clients.put(ct.id, ct);
//                        ct.start();
                    } else if (who.equalsIgnoreCase("NODE")) {
                        NodeThread nt = new NodeThread(socket, sOutput, sInput);
                        nodesThreadPool.execute(nt);
//                        nodes.put(nt.id, nt);
//                        nt.start();
                    }
                } catch (IOException e) {
                    display("Exception creating new Input/output Streams: " + e);
                    return;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (!keepGoing)  break;      // if I was asked to stop

            }

            // I was asked to stop
            try {
                serverSocket.close();
                for (Map.Entry<Integer, ClientThread> pair : clients.entrySet()) {
                    ClientThread tc = pair.getValue();
                    try {
                        tc.sInput.close();
                        tc.sOutput.close();
                        tc.socket.close();
                    } catch (IOException ioE) {
                        // not much I can do
                    }
                }
                for (Map.Entry<Integer, NodeThread> pair : nodes.entrySet()) {
                    NodeThread tc = pair.getValue();
                    try {
                        tc.sInput.close();
                        tc.sOutput.close();
                        tc.socket.close();
                    } catch (IOException ioE) {
                        // not much I can do
                    }
                }
                clientsThreadPool.shutdown();
                nodesThreadPool.shutdown();
                while(!clientsThreadPool.isTerminated()) {}
                while(!nodesThreadPool.isTerminated()) {}

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

    synchronized void remove(int id) {
        // scan the maps until we found the Id (first clients, because we rather not remove nodes)
        if(clients.containsKey(id)){
            clients.remove(id);
        }
        else if(nodes.containsKey(id)){
            nodes.remove(id);
        }
    }

    synchronized void divideGDFbetweenNodes(ServerRequestGDF srGDF, Integer clientID){
        if(nodes.size()==0){
            clients.get(clientID).writeMsg("There are not any nodes to do your request :(");
        }
        else {
            display("Spliting the task for: " + nodes.size() + " nodes");
            ArrayList<NodeRequestGDF> nodeRequestGDFArrayList = new ArrayList<>();
            if (srGDF.getGroupedDF().getSize() > nodes.size()) { //check if there aren't more nodes than groups
                for (int i = 0; i < nodes.size(); ++i) {
                    nodeRequestGDFArrayList.add(new NodeRequestGDF(srGDF.getFunction(), new GroupDataFrame(), clientID));
                }
            } else {
                for (int i = 0; i < srGDF.getGroupedDF().getSize(); ++i) {
                    nodeRequestGDFArrayList.add(new NodeRequestGDF(srGDF.getFunction(), new GroupDataFrame(), clientID));
                }
            }

            Integer iter = 0;
            for (DataFrame df : srGDF.getGroupedDF().getData()) {
                nodeRequestGDFArrayList.get((iter % nodeRequestGDFArrayList.size())).getGroupedDF().getData().add(df);
                iter++;
            }

            Integer iter2 = 0;
            for (Map.Entry<Integer, NodeThread> pair : nodes.entrySet()){
                pair.getValue().sendNRGDFtoNode(nodeRequestGDFArrayList.get(iter2));
                iter2++;
                if (iter2 >= nodeRequestGDFArrayList.size()) break;
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
            dfToReturn=new DataFrame();
        }

        // what will run forever
        public void run() {
            clients.put(this.id, this);
            // to loop until LOGOUT
            boolean keepGoing = true;
            while (keepGoing) {
                // read a GDF (which is an object)
                try {
                    Object obj = sInput.readObject();
                    if (obj instanceof String) {
                        String msg= (String) obj;
                        if(msg.equalsIgnoreCase("LOGOUT")){
                            display("LOGOUT Client ID: " +  this.id);break;
                        }
                        else {
                            display(msg);
                        }
                    } else if (obj instanceof ServerRequestGDF) {
                        display("Server has received ServerRequest from client ID: " + this.id);
                        requestGDF= (ServerRequestGDF) obj;
                        divideGDFbetweenNodes(requestGDF, id );
                    } else {
                        display("Server has received something i do not know what is this :(");
                    }
                } catch (IOException e) {
                    display("Client ID: " + id + " Exception reading Streams: " + e);
                    break;
                } catch (ClassNotFoundException e2) {
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
                display("Error sending message to client ID: " + id);
                display(e.toString());
            }
            return true;
        }

        public boolean sendDFtoClient(DataFrame dataFrame) {
            // if Client is still connected send the message to it
            if (!socket.isConnected()) {
                close();
                return false;
            }
            try {
                sOutput.writeObject(dataFrame);
            }
            // if an error occurs, do not abort just inform the user
            catch (IOException e) {
                display("Error sending message to client ID: " + id);
                display(e.toString());
            }
            return true;
        }

        synchronized public void addResultFromNode(DataFrame dataFrame){
            if(dfToReturn==null) dfToReturn = new DataFrame();
            dfToReturn.addAnotherDF(dataFrame);
            if(dfToReturn.size()==requestGDF.getGroupedDF().getSize()){
                display("Server has completed all the NodeResults for the ServerRequest and returns DataFrame to client ID: " + this.id);
                sendDFtoClient(dfToReturn);
                dfToReturn = null;
            }
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
            nodes.put(this.id, this);
            // to loop until LOGOUT
            boolean keepGoing = true;
            while (keepGoing) {
                // read a GDF (which is an object)
                try {
                    Object obj = sInput.readObject();
                    if (obj instanceof String) {
                        String msg= (String) obj;
                        if(msg.equalsIgnoreCase("LOGOUT")){
                            display("LOGOUT Node ID: " +  this.id);break;
                        }
                        else {
                            display(msg);
                        }
                    } else if (obj instanceof NodeResultDF) {
                        display("Server has received NodeResultDF from Node ID: "+ this.id + " to Client ID: " + ((NodeResultDF) obj).clientID);
                        clients.get(((NodeResultDF) obj).clientID).addResultFromNode(((NodeResultDF) obj).dataFrame);
                    } else {
                        display("Server has received something i do not know what is this :(");
                    }
                } catch (IOException e) {
                    display("Node id: " + id + " Exception reading Streams: " + e);
                    break;
                } catch (ClassNotFoundException e2) {
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
                display("Error sending message to node ID: " + id);
                display(e.toString());
            }
            return true;
        }

         public boolean sendNRGDFtoNode(NodeRequestGDF nrGDF){
            // if Client is still connected send the message to it
            if (!socket.isConnected()) {
                close();
                return false;
            }
            try {
                //NodeRequestGDF lala= new NodeRequestGDF("lala", new DataFrame().groupBy(new String[]{""}),23 );
                sOutput.writeObject((NodeRequestGDF) nrGDF);
            }
            // if an error occurs, do not abort just inform the user
            catch (IOException e) {
                display("Error sending NodeRequest to node ID: " + id);
                display("Separation of tasks into working nodes");
                remove(id);

                ServerRequestGDF  srGDF = new ServerRequestGDF(nrGDF.getFunction(),nrGDF.getGroupedDF());
                divideGDFbetweenNodes(srGDF, nrGDF.getClientID());
                display(e.toString());
            }
            return true;
        }
    }
}