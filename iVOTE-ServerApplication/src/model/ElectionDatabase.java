/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
    package model;



    import java.io.*;

    import java.util.*;



    public class ElectionDatabase {

        // [OOP: ENCAPSULATION] - Private paths for security

        private final String ROOT = "C:\\iVOTE";

        private final String LOGS = ROOT + "\\voters_logs.txt";

        private final String TALLY = ROOT + "\\tallies.txt";



        // [OOP: JAVA COLLECTIONS] - Maps for data organization

        public Map<String, Integer> voteTotals = new HashMap<>();



        public ElectionDatabase() {

            initFileSystem();

        }



        private void initFileSystem() {

            File folder = new File(ROOT);

            if (!folder.exists()) folder.mkdirs();

        }



        // [RUBRIC: FILE HANDLING] - Segregated storage logic

        public synchronized void recordBallot(Voter voter, List<String> choices) {

            try {

                // Write to logs: Time | Name | ID | Choices

                BufferedWriter writer = new BufferedWriter(new FileWriter(LOGS, true));

                writer.write(System.currentTimeMillis() + " | " + voter.getName() + " | " + voter.getId() + " | " + choices);

                writer.newLine();

                writer.close();



                // Update memory tally for the Chart

                for (String candidate : choices) {

                    voteTotals.put(candidate, voteTotals.getOrDefault(candidate, 0) + 1);

                }

                saveTally();

            } catch (IOException e) { e.printStackTrace(); }

        }



        private void saveTally() throws IOException {

            PrintWriter pw = new PrintWriter(new FileWriter(TALLY));

            voteTotals.forEach((name, count) -> pw.println(name + ":" + count));

            pw.close();

        }



        // RESET FEATURE: Wipes the database and files

        public void resetSystem() {

            voteTotals.clear();

            new File(LOGS).delete();

            new File(TALLY).delete();

        }



        // FIX FOR 12-INCREMENT: Counts lines in log file (1 line = 1 ballot)

        public int getTotalBallotsCount() {

            int count = 0;

            try (BufferedReader br = new BufferedReader(new FileReader(LOGS))) {

                while (br.readLine() != null) count++;

            } catch (Exception e) { return 0; }

            return count;

        }

    }