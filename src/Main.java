import java.util.*;
import phase3.AidRecord;
import city.CityGraph;
import city.Zone;
import phase1.SignalSimulator;
import phase3.FairnessScorer;
import phase1.SignalMonitor;

public class Main {
    public static void main(String[] args) {

        // zones
        Map<String, Zone> zones = new HashMap<>();
        zones.put("A", new Zone(1, "A"));
        zones.put("B", new Zone(2, "B"));
        zones.put("C", new Zone(3, "C"));
        zones.put("D", new Zone(4, "D"));
        zones.put("E", new Zone(5, "E"));
        zones.put("F", new Zone(6, "F"));
        zones.put("G", new Zone(7, "G"));
        zones.put("H", new Zone(8, "H"));
        zones.put("I", new Zone(9, "I"));
        zones.put("J", new Zone(10, "J"));

        // ── SIGNAL SIMULATION ──
        System.out.println("======================================================================");
        System.out.println("                     SMART CITY CRISIS ENGINE                         ");
        System.out.println("======================================================================");
        System.out.println("\n--- PHASE 1: Crisis Detection Started ---\n");
        SignalSimulator sim = new SignalSimulator();

        sim.runSimulation(
                "../data/mock_data.csv",
                zones);

        System.out.println("\n===== FINAL ZONE STATUS =====");
        for (Map.Entry<String, Zone> entry : zones.entrySet()) {
            entry.getValue().printStatus();
        }

        System.out.println("\n===== CRITICAL ZONES =====");
        boolean anyCritical = false;
        for (Map.Entry<String, Zone> entry : zones.entrySet()) {
            if (entry.getValue().isCritical) {
                System.out.println("!!! Zone " + entry.getKey() +
                        " [" + entry.getValue().zoneType + "] is CRITICAL !!!");
                anyCritical = true;
            }
        }
        if (!anyCritical)
            System.out.println("No critical zones detected.");

        // ── SIGNAL MONITOR TEST ──
        System.out.println("\n===== SIGNAL MONITOR — SLIDING WINDOW TEST =====");
        SignalMonitor monitor = new SignalMonitor();
        monitor.initZone("A");
        double[] testValues = { 4.0, 5.0, 6.0, 7.0, 8.0, 8.5, 9.0 };
        for (double val : testValues) {
            monitor.addReading("A", val, 5, 1.2);
            System.out.println("Added env=" + val +
                    " | Window size: " + monitor.getWindowSize("A") +
                    " | Latest: " + monitor.getLatestEnv("A"));
        }
        monitor.printWindow("A");

        // aidRecord
        /*
         * AidRecord zoneA = new AidRecord(1, 75.5);
         * AidRecord zoneB = new AidRecord(2, 50.0);
         * AidRecord zoneC = new AidRecord(3, 90.2);
         * 
         * System.out.println("\nInitial Aid Records:");
         * zoneA.printRecord();
         * zoneB.printRecord();
         * zoneC.printRecord();
         * 
         * zoneA.setAidReceived(5);
         * zoneA.setTimeIgnored(2);
         * 
         * zoneB.setAidReceived(3);
         * zoneB.setTimeIgnored(1);
         * 
         * zoneC.setAidReceived(7);
         * zoneC.setTimeIgnored(4);
         * 
         * System.out.println("\nUpdated Aid Records:");
         * zoneA.printRecord();
         * zoneB.printRecord();
         * zoneC.printRecord();
         */

        // CITY GRAPH + DIJKSTRA
        System.out.println("\n--- PHASE 2: Evacuation Routing ---\n");
        CityGraph graph = new CityGraph();

        // Add zones
        graph.addZone("A");
        graph.addZone("B");
        graph.addZone("C");
        graph.addZone("D");
        graph.addZone("E");
        graph.addZone("F");
        graph.addZone("G");
        graph.addZone("H");
        graph.addZone("I");
        graph.addZone("J");

        // // Add roads with travel times as weights
        graph.addConnection("A", "B", 5);
        graph.addConnection("A", "C", 10);
        graph.addConnection("B", "C", 3);
        graph.addConnection("B", "D", 7);
        graph.addConnection("C", "D", 2);
        graph.addConnection("D", "E", 4);
        graph.addConnection("D", "H", 6); // H = safe zone
        graph.addConnection("E", "I", 3); // I = safe zone
        graph.addConnection("C", "J", 8); // J = safe zone
        graph.addConnection("F", "G", 5);
        graph.addConnection("G", "H", 4);

        System.out.println("City Road Network:");
        graph.printGraph();

        // Run Dijkstra from A
        System.out.println("\nFinding evacuation route from Zone A (crisis zone)...");
        graph.dijkstra("A");

        // Simulate road congestion
        System.out.println("\n--- Road B-D congested! Weight doubled ---");
        graph.updateWeight("B", "D", 14);
        System.out.println("Re-computing evacuation route after congestion...");
        graph.dijkstra("A");

        // ── PHASE 3 — FAIR AID DISTRIBUTION ──
        // Compute scores
        /*
         * double scoreA = FairnessScorer.computeScore(zoneA);
         * double scoreB = FairnessScorer.computeScore(zoneB);
         * double scoreC = FairnessScorer.computeScore(zoneC);
         * 
         * // Print scores
         * FairnessScorer.printScore("Zone A", scoreA);
         * FairnessScorer.printScore("Zone B", scoreB);
         * FairnessScorer.printScore("Zone C", scoreC);
         * 
         * // Optional: find highest priority
         * if (scoreA >= scoreB && scoreA >= scoreC) {
         * System.out.println("Highest priority: Zone A");
         * } else if (scoreB >= scoreA && scoreB >= scoreC) {
         * System.out.println("Highest priority: Zone B");
         * } else {
         * System.out.println("Highest priority: Zone C");
         * }
         */
        System.out.println("\n--- PHASE 3: Fair Aid Distribution ---\n");

        // Create AidRecords using actual zone data from simulation
        // damageLevel comes from zone's riskScore (set by evaluateRisk)
        // vulnerabilityBonus comes from zone's vulnerability (read from CSV)
        // neglectMultiplier = 1.0 for now — AidDistributor will update this

        System.out.println("\nPriority Scores:");

        for (Zone z : zones.values()) {

            // Convert Zone → AidRecord
            AidRecord record = new AidRecord(z.getZoneId(), z.getRiskScore());

            // 🔹 Set vulnerability
            record.setVulnerabilityBonus(z.getVulnerabilityBonus());

            // 🔹 Simulate fairness factors
            record.setAidReceived((int) (Math.random() * 5));
            record.setTimeIgnored((int) (Math.random() * 6));

            // 🔹 Neglect multiplier
            double neglect = 1.0 + z.getRiskScore() * 0.3;
            if (z.isCritical()) {
                neglect += 0.5;
            }

            record.setNeglectMultiplier(neglect);

            // 🔹 Compute score
            double score = FairnessScorer.computeScore(record);

            // 🔹 Print
            FairnessScorer.printScore(z.getZoneName(), score);
        }
    }
}
