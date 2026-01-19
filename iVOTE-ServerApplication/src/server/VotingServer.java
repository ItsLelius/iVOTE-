/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class VotingServer {
    private int port;
    public static int activeConnections = 0;      // Currently connected clients
    public static int totalAttempts = 0;          // Total connection attempts (cumulative)
    
    public VotingServer(int port) {
        this.port = port;
    }
    
    public void startServer() {
        ServerSocket serverSocket = null;
        
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("===========================================");
            System.out.println("iVOTE Server Started Successfully!");
            System.out.println("Listening on port: " + port);
            System.out.println("Waiting for client connections...");
            System.out.println("===========================================");
            
            while (true) {
                try {
                    // Wait for client connection
                    Socket clientSocket = serverSocket.accept();
                    
                    // Increment counters
                    activeConnections++;
                    totalAttempts++;  // This never decreases - it's cumulative
                    
                    System.out.println("\n>>> New client connected!");
                    System.out.println("    Total Attempts: " + totalAttempts);
                    System.out.println("    Currently Active: " + activeConnections);
                    
                    // Force dashboard update
                    if (ServerApp.dashboardInstance != null) {
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            ServerApp.dashboardInstance.forceUpdate();
                        });
                    }
                    
                    // Handle the client in a new thread
                    Thread handlerThread = new Thread(new ClientHandler(clientSocket));
                    handlerThread.start();
                    
                } catch (IOException e) {
                    System.err.println("Error accepting client: " + e.getMessage());
                }
            }
            
        } catch (IOException e) {
            System.err.println("CRITICAL SERVER ERROR: " + e.getMessage());
            System.err.println("Port " + port + " may already be in use!");
            e.printStackTrace();
        } finally {
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                    System.out.println("Server socket closed.");
                } catch (IOException e) {
                    System.err.println("Error closing server socket: " + e.getMessage());
                }
            }
        }
    }
    
    // Method to reset counters (called by Reset button)
    public static void resetCounters() {
        activeConnections = 0;
        totalAttempts = 0;
    }
}