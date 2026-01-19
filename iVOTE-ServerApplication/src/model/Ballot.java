/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
    package model;
    import java.util.Map;

    // [RUBRIC] Abstraction: Hides the complexity of different election types
    public abstract class Ballot {
        protected String title;

        public Ballot(String title) { this.title = title; }

        // Children MUST implement these
        public abstract boolean castVote(String candidate);
        public abstract Map<String, Integer> getResults();
    }
