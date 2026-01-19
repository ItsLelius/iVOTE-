/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public abstract class Person {
    // Protected allows children (Voter/Candidate) to access these directly
    protected String id;
    protected String firstName;
    protected String lastName;

    public Person(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getId() { return id; }
    
    public String getFullName() { 
        if (lastName == null || lastName.isEmpty()) return firstName;
        return firstName + " " + lastName; 
    }

    // Abstract method: Forces children to define their role
    public abstract String getRole();
}