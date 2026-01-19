/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CampusBallot extends Ballot {
    private final Map<String, List<Candidate>> candidatesByPosition = new LinkedHashMap<>();
    private final Map<String, Integer> votes = new ConcurrentHashMap<>();
    private final String FOLDER_PATH = "C:\\ElectionData\\";
    
    public CampusBallot(String title) {
        super(title);
        initializeCandidates();
        ensureFolderExists();
        syncVotesFromFile();
    }
    
    private void ensureFolderExists() {
        File folder = new File(FOLDER_PATH);
        if (!folder.exists()) folder.mkdirs();
    }
    
    private void initializeCandidates() {
        // --- 1. Governor ---
        candidatesByPosition.put("Governor", List.of(
            new Candidate("Reinhard", "Democrito", "C001", "M", "BSCS", 3, "Governor", "Party A"),
            new Candidate("Aaron", "Villanueva", "C002", "M", "BSIT", 3, "Governor", "Party B")
        ));
        
        // --- 2. Vice Governor ---
        candidatesByPosition.put("Vice Governor", List.of(
            new Candidate("Matt", "Navarez", "C003", "M", "BSCS", 3, "Vice Governor", "Party A"),
            new Candidate("Joshua", "Ramirez", "C004", "M", "BSIT", 3, "Vice Governor", "Party B")
        ));
        
        // --- 3. Executive Secretary ---
        candidatesByPosition.put("Executive Secretary", List.of(
            new Candidate("David", "Northrup", "C005", "M", "BSCS", 3, "Executive Secretary", "Party A"),
            new Candidate("Emily", "Torres", "C006", "F", "BSIT", 3, "Executive Secretary", "Party B")
        ));
        
        // --- 4. Sports Secretary ---
        candidatesByPosition.put("Sports Secretary", List.of(
            new Candidate("Lelius", "Lawas", "C007", "M", "BSCS", 3, "Sports Secretary", "Party A"),
            new Candidate("Mark", "Cruz", "C008", "M", "BSIT", 3, "Sports Secretary", "Party B")
        ));
        
        // --- 5. Sociocultural Secretary ---
        candidatesByPosition.put("Sociocultural Secretary", List.of(
            new Candidate("Ashley", "Bendijo", "C009", "F", "BSCS", 3, "Sociocultural Secretary", "Party A"),
            new Candidate("Sophia", "Reyes", "C010", "F", "BSIT", 3, "Sociocultural Secretary", "Party B")
        ));
        
        // --- 6. Technical Secretary ---
        candidatesByPosition.put("Technical Secretary", List.of(
            new Candidate("Macky", "Sipalay", "C011", "M", "BSCS", 3, "Technical Secretary", "Party A"),
            new Candidate("Marquis", "Montos", "C012", "M", "BSIT", 3, "Technical Secretary", "Party B")
        ));
        
        // --- 7. Defense Secretary ---
        candidatesByPosition.put("Defense Secretary", List.of(
            new Candidate("Lawrence", "Herda", "C013", "M", "BSCS", 3, "Defense Secretary", "Party A"),
            new Candidate("James", "Luna", "C014", "M", "BSIT", 3, "Defense Secretary", "Party B")
        ));
        
        // --- 8. Logistics Secretary ---
        candidatesByPosition.put("Logistics Secretary", List.of(
            new Candidate("Radzgie", "Custodio", "C015", "M", "BSCS", 3, "Logistics Secretary", "Party A"),
            new Candidate("Ivan", "Salazar", "C016", "M", "BSIT", 3, "Logistics Secretary", "Party B")
        ));
        
        // --- 9. External Affairs Secretary ---
        candidatesByPosition.put("External Affairs Secretary", List.of(
            new Candidate("Mel", "Ford Batucan", "C017", "M", "BSCS", 3, "External Affairs Secretary", "Party A"),
            new Candidate("Nathan", "Garcia", "C018", "M", "BSIT", 3, "External Affairs Secretary", "Party B")
        ));
        
        // --- 10. Internal Affairs Secretary ---
        candidatesByPosition.put("Internal Affairs Secretary", List.of(
            new Candidate("Pauleen", "Borlado", "C019", "F", "BSCS", 3, "Internal Affairs Secretary", "Party A"),
            new Candidate("Vanessa", "Delos Santos", "C020", "F", "BSIT", 3, "Internal Affairs Secretary", "Party B")
        ));
        
        // --- 11. Academic Secretary ---
        candidatesByPosition.put("Academic Secretary", List.of(
            new Candidate("Charlie", "de Lumen", "C021", "M", "BSCS", 3, "Academic Secretary", "Party A"),
            new Candidate("Brian", "Santos", "C022", "M", "BSIT", 3, "Academic Secretary", "Party B")
        ));
        
        // --- 12. Finance Secretary ---
        candidatesByPosition.put("Finance Secretary", List.of(
            new Candidate("Juliah", "Gealon", "C023", "F", "BSCS", 3, "Finance Secretary", "Party A"),
            new Candidate("Miriam", "Lopez", "C024", "F", "BSIT", 3, "Finance Secretary", "Party B")
        ));
        
        // Set all to 0 initially
        for (List<Candidate> list : candidatesByPosition.values()) {
            for (Candidate c : list) votes.put(c.getId(), 0);
        }
    }
    
    public synchronized void syncVotesFromFile() {
        File file = new File(FOLDER_PATH + "votes.txt");
        if (!file.exists()) return;
        
        votes.keySet().forEach(key -> votes.put(key, 0));
        
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String candidateId = sc.nextLine().trim();
                if (votes.containsKey(candidateId)) {
                    votes.put(candidateId, votes.get(candidateId) + 1);
                }
            }
        } catch (Exception e) {
            System.err.println("Sync error: " + e.getMessage());
        }
    }
    
    public synchronized String processNetworkVote(model.Voter voter, List<String> selectedCandidateIds) {
        // 1. Check for duplicate ID
        ArrayList<model.Voter> existingVoters = getVotersFromFile();
        for (model.Voter v : existingVoters) {
            if (v.getId().equals(voter.getId())) {
                return "ALREADY_VOTED";
            }
        }
        
        try (FileWriter vw = new FileWriter(FOLDER_PATH + "voters.txt", true);
             FileWriter cw = new FileWriter(FOLDER_PATH + "votes.txt", true)) {
            
            // 2. CRITICAL FIX: Save with timestamp
            // Format: ID,Name,Time,Status
            String timestamp = voter.getSubmissionTime();
            if (timestamp == null || timestamp.isEmpty()) {
                timestamp = java.time.LocalTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
                voter.setSubmissionTime(timestamp);
            }
            
            vw.write(voter.getId() + "," + voter.getName() + "," + timestamp + ",Voted\n");
            
            // 3. Save Candidate Votes
            for (String id : selectedCandidateIds) {
                cw.write(id + "\n");
                if (votes.containsKey(id)) {
                    votes.put(id, votes.get(id) + 1);
                }
            }
            
            return "SUCCESS";
            
        } catch (IOException e) {
            return "ERROR: " + e.getMessage();
        }
    }
    
    public synchronized void resetSystem() {
        // 1. Clear memory
        votes.keySet().forEach(key -> votes.put(key, 0));
        
        // 2. Delete files
        try {
            File votesFile = new File(FOLDER_PATH + "votes.txt");
            File votersFile = new File(FOLDER_PATH + "voters.txt");
            
            if (votesFile.exists()) {
                votesFile.delete();
                System.out.println("Deleted votes.txt");
            }
            
            if (votersFile.exists()) {
                votersFile.delete();
                System.out.println("Deleted voters.txt");
            }
            
            System.out.println("Election data has been wiped.");
        } catch (Exception e) {
            System.err.println("Reset failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public int getTotalVotesFromFiles() {
        int count = 0;
        try {
            File file = new File(FOLDER_PATH + "votes.txt");
            if (file.exists()) {
                Scanner sc = new Scanner(file);
                while (sc.hasNextLine()) { sc.nextLine(); count++; }
                sc.close();
            }
        } catch (Exception e) { }
        return count;
    }
    
    public ArrayList<model.Voter> getVotersFromFile() {
        ArrayList<model.Voter> list = new ArrayList<>();
        try {
            File file = new File(FOLDER_PATH + "voters.txt");
            if (file.exists()) {
                Scanner sc = new Scanner(file);
                while (sc.hasNextLine()) {
                    String line = sc.nextLine().trim();
                    if (line.isEmpty()) continue;
                    
                    String[] data = line.split(",");
                    
                    // CRITICAL FIX: Handle both old and new format
                    if (data.length >= 3) {
                        // New format: ID,Name,Time,Status
                        model.Voter v = new model.Voter(data[0], data[1]);
                        v.setSubmissionTime(data[2]); // Set the saved timestamp
                        if (line.contains("Voted")) v.setHasVoted(true);
                        list.add(v);
                    } else if (data.length >= 2) {
                        // Old format: ID,Name,Status (for backward compatibility)
                        model.Voter v = new model.Voter(data[0], data[1]);
                        v.setSubmissionTime("--:--:--"); // Default for old entries
                        if (line.contains("Voted")) v.setHasVoted(true);
                        list.add(v);
                    }
                }
                sc.close();
            }
        } catch (Exception e) {
            System.err.println("Error reading voters: " + e.getMessage());
        }
        return list;
    }
    
    @Override
    public synchronized boolean castVote(String candidateId) {
        if (votes.containsKey(candidateId)) {
            votes.put(candidateId, votes.get(candidateId) + 1);
            return true;
        }
        return false;
    }
    
    @Override
    public Map<String, Integer> getResults() { return votes; }
    
    public Map<String, List<Candidate>> getCandidates() { return candidatesByPosition; }
}