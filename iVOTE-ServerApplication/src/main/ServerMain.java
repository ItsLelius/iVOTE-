/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import ui.ServerDashboard;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class ServerMain {

    public static void main(String[] args) {
        
        // OPTIONAL: Makes the UI look like Windows/Mac instead of old Java
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // If it fails, just use default look
        }

        // Launch the Dashboard safely on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            ServerDashboard dashboard = new ServerDashboard();
            dashboard.setVisible(true);
            
            // Center the window on the screen
            dashboard.setLocationRelativeTo(null);
        });
    }
}