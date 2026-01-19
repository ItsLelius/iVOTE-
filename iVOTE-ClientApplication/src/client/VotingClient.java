/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import java.io.*;
import java.net.*;
import java.util.List;
import model.Voter;

public class VotingClient {
    private String serverIP;
    private int port;
    
    public VotingClient(String serverIP, int port) {
        this.serverIP = serverIP;
        this.port = port;
    }
    
    public String submitVote(Voter voter, List<String> selectedIds) {
        Socket socket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        
        try {
            System.out.println("Attempting to connect to " + serverIP + ":" + port);
            
            // Create socket with timeout
            socket = new Socket();
            socket.connect(new InetSocketAddress(serverIP, port), 5000);
            socket.setSoTimeout(10000);
            
            // Disable Nagle's algorithm for immediate sending
            socket.setTcpNoDelay(true);
            
            System.out.println("Connected successfully!");
            
            // Create OUTPUT stream FIRST
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            
            // Then create INPUT stream
            in = new ObjectInputStream(socket.getInputStream());
            
            System.out.println("Sending voter: " + voter.getName() + " (" + voter.getId() + ")");
            System.out.println("Sending selections: " + selectedIds);
            
            // Send voter object
            out.writeObject(voter);
            out.flush();
            
            // Send selections
            out.writeObject(selectedIds);
            out.flush();
            
            System.out.println("Data sent, waiting for response...");
            
            // Read response - this is where it was failing before
            Object responseObj = in.readObject();
            String response = (String) responseObj;
            
            System.out.println("Received response: " + response);
            
            return response;
            
        } catch (SocketTimeoutException e) {
            System.err.println("Connection timeout: " + e.getMessage());
            e.printStackTrace();
            return "Connection Error: Server did not respond in time.";
            
        } catch (ConnectException e) {
            System.err.println("Connection refused: " + e.getMessage());
            e.printStackTrace();
            return "Connection Error: Cannot reach server at " + serverIP + ":" + port;
            
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + e.getMessage());
            e.printStackTrace();
            return "Connection Error: Invalid IP address: " + serverIP;
            
        } catch (EOFException e) {
            System.err.println("EOF Exception - server closed connection: " + e.getMessage());
            e.printStackTrace();
            return "Connection Error: Server closed connection unexpectedly";
            
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
            e.printStackTrace();
            return "Connection Error: " + e.getMessage();
            
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + e.getMessage());
            e.printStackTrace();
            return "Error: Invalid response from server";
            
        } finally {
            // Clean up resources in reverse order
            try {
                if (in != null) in.close();
            } catch (IOException e) {
                System.err.println("Error closing input stream: " + e.getMessage());
            }
            
            try {
                if (out != null) out.close();
            } catch (IOException e) {
                System.err.println("Error closing output stream: " + e.getMessage());
            }
            
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
            
            System.out.println("Client connection cleanup complete");
        }
    }
}