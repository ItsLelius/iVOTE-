/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.io.Serializable;

public class Voter implements Serializable {
    // Requirements: Set fixed serialVersionUID for persistence
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private boolean hasVoted;
    private String submissionTime; // Requirement: Persistent field for UI

    public Voter(String id, String name) {
        this.id = id;
        this.name = name;
        this.hasVoted = false;
        this.submissionTime = "";
    }

    // Methods to handle the injection of the fixed timestamp
    public void setSubmissionTime(String time) { this.submissionTime = time; }
    public String getSubmissionTime() { return submissionTime; }
    
    public String getName() { return name; }
    public String getId() { return id; }
    
    public void setHasVoted(boolean hasVoted) { this.hasVoted = hasVoted; }
    public boolean hasVoted() { return hasVoted; }
}