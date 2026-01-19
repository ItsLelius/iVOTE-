/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import model.Voter;
import client.VotingClient;

public class ClientApp extends JFrame {
    private CardLayout cl = new CardLayout();
    private JPanel mainContainer = new JPanel(cl);
    
    // Theme Colors
    private final Color PRIMARY_DARK = new Color(33, 33, 33);
    private final Color ACCENT_GREEN = new Color(114, 189, 53);
    private final Color BG_LIGHT = new Color(245, 245, 245);
    
    private JTextField txtName, txtID, txtIP;
    private JPasswordField txtPass;
    private Map<String, JComboBox<String>> dropdowns = new LinkedHashMap<>();
    
    public ClientApp() {
        setTitle("iVOTE | Secure Student Voting");
        setSize(500, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        setupLogin();
        setupBallot();
        
        add(mainContainer);
        setLocationRelativeTo(null);
    }
    
    private void setupLogin() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(BG_LIGHT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Header
        JLabel lblLogo = new JLabel("iVOTE LOGIN", SwingConstants.CENTER);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        loginPanel.add(lblLogo, gbc);
        
        // Fields
        gbc.gridwidth = 1;
        gbc.gridy++; loginPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1; txtName = createStyledField(); loginPanel.add(txtName, gbc);
        
        gbc.gridx = 0; gbc.gridy++; loginPanel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1; txtID = createStyledField(); loginPanel.add(txtID, gbc);
        
        gbc.gridx = 0; gbc.gridy++; loginPanel.add(new JLabel("Server IP:"), gbc);
        gbc.gridx = 1; txtIP = createStyledField(); txtIP.setText("127.0.0.1"); loginPanel.add(txtIP, gbc);
        
        gbc.gridx = 0; gbc.gridy++; loginPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; txtPass = new JPasswordField(15); loginPanel.add(txtPass, gbc);
        
        // Button
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        JButton btnEnter = createStyledButton("ENTER VOTING BOOTH", ACCENT_GREEN);
        btnEnter.addActionListener(e -> {
            String name = txtName.getText().trim();
            String id = txtID.getText().trim();
            String password = new String(txtPass.getPassword());
            
            // Validation
            if (name.isEmpty() || id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields!");
                return;
            }
            
            if (!password.equals("Voter123")) {
                JOptionPane.showMessageDialog(this, "Invalid Password!");
                return;
            }
            
            cl.show(mainContainer, "BALLOT");
        });
        loginPanel.add(btnEnter, gbc);
        
        mainContainer.add(loginPanel, "LOGIN");
    }
    
    private void setupBallot() {
        JPanel ballotPanel = new JPanel(new BorderLayout());
        ballotPanel.setBackground(BG_LIGHT);
        
        // Header Card
        JPanel header = new JPanel();
        header.setBackground(PRIMARY_DARK);
        header.setPreferredSize(new Dimension(0, 70));
        JLabel lblTitle = new JLabel("OFFICIAL DIGITAL BALLOT");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.add(lblTitle);
        ballotPanel.add(header, BorderLayout.NORTH);
        
        // Ballot Content
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(BG_LIGHT);
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Add all 12 positions
        addModernDropdown(form, "Governor", new String[]{"Reinhard Democrito (C001)", "Aaron Villanueva (C002)"});
        addModernDropdown(form, "Vice Governor", new String[]{"Matt Navarez (C003)", "Joshua Ramirez (C004)"});
        addModernDropdown(form, "Executive Secretary", new String[]{"David Northrup (C005)", "Emily Torres (C006)"});
        addModernDropdown(form, "Sports Secretary", new String[]{"Lelius Lawas (C007)", "Mark Cruz (C008)"});
        addModernDropdown(form, "Sociocultural Secretary", new String[]{"Ashley Bendijo (C009)", "Sophia Reyes (C010)"});
        addModernDropdown(form, "Technical Secretary", new String[]{"Macky Sipalay (C011)", "Marquis Montos (C012)"});
        addModernDropdown(form, "Defense Secretary", new String[]{"Lawrence Herda (C013)", "James Luna (C014)"});
        addModernDropdown(form, "Logistics Secretary", new String[]{"Radzgie Custodio (C015)", "Ivan Salazar (C016)"});
        addModernDropdown(form, "External Affairs Secretary", new String[]{"Mel Ford Batucan (C017)", "Nathan Garcia (C018)"});
        addModernDropdown(form, "Internal Affairs Secretary", new String[]{"Pauleen Borlado (C019)", "Vanessa Delos Santos (C020)"});
        addModernDropdown(form, "Academic Secretary", new String[]{"Charlie de Lumen (C021)", "Brian Santos (C022)"});
        addModernDropdown(form, "Finance Secretary", new String[]{"Juliah Gealon (C023)", "Miriam Lopez (C024)"});
        
        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        ballotPanel.add(scroll, BorderLayout.CENTER);
        
        // Submit Button
        JButton btnSubmit = createStyledButton("CAST VOTE", ACCENT_GREEN);
        btnSubmit.setPreferredSize(new Dimension(0, 60));
        btnSubmit.addActionListener(e -> processVote());
        ballotPanel.add(btnSubmit, BorderLayout.SOUTH);
        
        mainContainer.add(ballotPanel, "BALLOT");
    }
    
    private void addModernDropdown(JPanel p, String title, String[] items) {
        JLabel lbl = new JLabel(title.toUpperCase());
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(100, 100, 100));
        p.add(lbl);
        
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cb.setBackground(Color.WHITE);
        dropdowns.put(title, cb);
        p.add(cb);
        p.add(Box.createVerticalStrut(20));
    }
    
    private JTextField createStyledField() {
        JTextField f = new JTextField(15);
        f.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return f;
    }
    
    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return btn;
    }
    
    private void processVote() {
        // Extract candidate IDs from selections
        List<String> selectedIds = new ArrayList<>();
        
        for (Map.Entry<String, JComboBox<String>> entry : dropdowns.entrySet()) {
            String selected = (String) entry.getValue().getSelectedItem();
            if (selected != null && !selected.isEmpty()) {
                // Extract ID from format "Name (C001)"
                int start = selected.lastIndexOf("(C");
                int end = selected.lastIndexOf(")");
                if (start != -1 && end != -1) {
                    String id = selected.substring(start + 1, end);
                    selectedIds.add(id);
                }
            }
        }
        
        // Validate selections
        if (selectedIds.size() != 12) {
            JOptionPane.showMessageDialog(this, 
                "Please select a candidate for ALL positions!\nSelected: " + selectedIds.size() + "/12");
            return;
        }
        
        // Create voter object
        String voterName = txtName.getText().trim();
        String voterId = txtID.getText().trim();
        Voter voter = new Voter(voterId, voterName);
        
        // Get server IP (port is hardcoded to 6000)
        String serverIP = txtIP.getText().trim();
        
        // Submit vote to server
        VotingClient client = new VotingClient(serverIP, 6000);
        
        // Show loading dialog
        JDialog loadingDialog = new JDialog(this, "Submitting Vote...", true);
        JLabel loadingLabel = new JLabel("Please wait...", SwingConstants.CENTER);
        loadingDialog.add(loadingLabel);
        loadingDialog.setSize(250, 100);
        loadingDialog.setLocationRelativeTo(this);
        
        // Submit in background thread
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() {
                return client.submitVote(voter, selectedIds);
            }
            
            @Override
            protected void done() {
                loadingDialog.dispose();
                try {
                    String response = get();
                    
                    if (response.equals("SUCCESS")) {
                        JOptionPane.showMessageDialog(ClientApp.this, 
                            "Thank you, " + voterName + "!\nYour vote has been recorded successfully.",
                            "Vote Submitted", JOptionPane.INFORMATION_MESSAGE);
                        
                        // Reset form and go back to login
                        resetForm();
                        cl.show(mainContainer, "LOGIN");
                        
                    } else if (response.equals("ALREADY_VOTED")) {
                        JOptionPane.showMessageDialog(ClientApp.this, 
                            "This Student ID has already voted!\nDuplicate voting is not allowed.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(ClientApp.this, 
                            "Error: " + response,
                            "Submission Failed", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(ClientApp.this, 
                        "Connection Error: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        
        worker.execute();
        loadingDialog.setVisible(true);
    }
    
    private void resetForm() {
        // Clear login fields
        txtName.setText("");
        txtID.setText("");
        txtPass.setText("");
        
        // Reset all dropdowns to first option
        for (JComboBox<String> cb : dropdowns.values()) {
            cb.setSelectedIndex(0);
        }
    }
    
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } 
        catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new ClientApp().setVisible(true));
    }
}