/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package server;

import java.io.*;
import java.net.*;
import java.util.List;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ClientHandler implements Runnable {
    private Socket socket;
    
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }
    
    @Override
    public void run() {
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        
        try {
            System.out.println("New client connected: " + socket.getInetAddress());
            
            // Create OUTPUT stream FIRST
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            
            in = new ObjectInputStream(socket.getInputStream());
            
            System.out.println("Streams established, waiting for data...");
            
            // 1. Receive Voter and Selections
            model.Voter voter = (model.Voter) in.readObject();
            List<String> selections = (List<String>) in.readObject();
            
            // 2. CRITICAL: Set timestamp when vote is received
            String timestamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            voter.setSubmissionTime(timestamp);
            
            System.out.println("Received vote from: " + voter.getName() + " (ID: " + voter.getId() + ") at " + timestamp);
            System.out.println("Selected candidates: " + selections);
            
            // 3. Process the vote (timestamp is now part of voter object)
            String response = server.ServerApp.ballot.processNetworkVote(voter, selections);
            
            System.out.println("Vote processing result: " + response);
            
            // 4. Send response to client
            out.writeObject(response);
            out.flush();
            
            // Wait for client to receive
            Thread.sleep(100);
            
            System.out.println("Response sent to client");
            
            // 5. Update dashboard immediately
            if (response.equals("SUCCESS") && ServerApp.dashboardInstance != null) {
                javax.swing.SwingUtilities.invokeLater(() -> {
                    ServerApp.dashboardInstance.forceUpdate();
                });
                System.out.println("Dashboard update triggered");
            }
            
        } catch (EOFException e) {
            System.err.println("Client disconnected unexpectedly");
        } catch (SocketException e) {
            System.err.println("Socket error: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Thread interrupted: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Handler Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Decrement active connections
            server.VotingServer.activeConnections--;
            
            // Update dashboard to show decreased connection count
            if (ServerApp.dashboardInstance != null) {
                javax.swing.SwingUtilities.invokeLater(() -> {
                    ServerApp.dashboardInstance.forceUpdate();
                });
            }
            
            System.out.println("Active connections: " + server.VotingServer.activeConnections);
            
            // Clean up
            try {
                if (out != null) out.close();
                if (in != null) in.close();
                if (socket != null && !socket.isClosed()) socket.close();
            } catch (IOException e) {
                // Ignore
            }
            
            System.out.println("Client handler finished");
        }
    }
}