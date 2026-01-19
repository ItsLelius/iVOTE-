/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Candidate extends Person {
    
    private String gender;
    private String course;
    private int yearLevel;
    private String position;
    private String party;

    public Candidate(String fName, String lName, String id, String gender, 
                     String course, int year, String position, String party) {
        
        // [OOP] SUPER: Passes common data up to the Person parent
        super(id, fName, lName); 
        
        this.gender = gender;
        this.course = course;
        this.yearLevel = year;
        this.position = position;
        this.party = party;
    }

    public String getPosition() { return position; }
    public String getParty() { return party; }

    // [OOP] Polymorphism: Returns "Candidate" instead of generic Person
    @Override
    public String getRole() {
        return "Election Candidate (" + position + ")";
    }
}