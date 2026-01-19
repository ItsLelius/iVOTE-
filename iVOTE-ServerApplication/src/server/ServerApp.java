/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */package server;

import model.CampusBallot;
import ui.ServerDashboard;

public class ServerApp {
    // Global instances for cross-class synchronization
    public static CampusBallot ballot = new CampusBallot("iVOTE 2025");
    public static ServerDashboard dashboardInstance = null;

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            dashboardInstance = new ServerDashboard();
            dashboardInstance.setVisible(true);
        });
    }
}