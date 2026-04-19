import java.util.*;
import phase3.AidRecord;
import city.CityGraph;
import city.Zone;
import phase1.SignalSimulator;
import phase3.FairnessScorer;
import phase3.AidDistributor;
public class Main {
    public static void main(String[] args) {

      //zone
      // ── ZONES ──
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
        SignalSimulator sim = new SignalSimulator();
        
        sim.runSimulation(
            "data/mock_data.csv",
            zones
        );
        System.out.println("\n===== FINAL ZONE STATUS =====");
        

        for (Zone z : zones.values()) {
            z.printStatus();
        }

        System.out.println("\n===== CRITICAL ZONES =====");
        boolean anyCritical = false;
        for (Map.Entry<String, Zone> entry : zones.entrySet()) {
            if (entry.getValue().isCritical) {
                System.out.println("!!! Zone " + entry.getKey() + " is CRITICAL !!!");
                anyCritical = true;
            }
        }
        if (!anyCritical) System.out.println("No critical zones detected.");

       //aidRecord
       /* AidRecord zoneA = new AidRecord(1, 75.5);
        AidRecord zoneB = new AidRecord(2, 50.0);
        AidRecord zoneC = new AidRecord(3, 90.2);

        System.out.println("\nInitial Aid Records:");
        zoneA.printRecord();
        zoneB.printRecord();
        zoneC.printRecord();

        zoneA.setAidReceived(5);
        zoneA.setTimeIgnored(2);

        zoneB.setAidReceived(3);
        zoneB.setTimeIgnored(1);

        zoneC.setAidReceived(7);
        zoneC.setTimeIgnored(4);

        System.out.println("\nUpdated Aid Records:");
        zoneA.printRecord();
        zoneB.printRecord();
        zoneC.printRecord();
         */


         // Create graph
        CityGraph graph = new CityGraph();

        // Add zones
        graph.addZone("A");
        graph.addZone("B");
        graph.addZone("C");
        graph.addZone("D");
        graph.addZone("E");

        // Add connections (with weights)
        graph.addConnection("A", "B", 5);
        graph.addConnection("A", "C", 10);
        graph.addConnection("B", "C", 3);
        graph.addConnection("B", "D", 7);
        graph.addConnection("C", "D", 2);
        graph.addConnection("D", "E", 4);

        // Print graph
        System.out.println("Graph:");
        graph.printGraph();

        // Run Dijkstra from A
        System.out.println("\nRunning Dijkstra from A...");
        graph.dijkstra("A");

        // Optional: test weight update
        System.out.println("\nUpdating weight B -> D to 2...");
        graph.updateWeight("B", "D", 2);

        // Run again after update
        System.out.println("\nRunning Dijkstra again after update...");
        graph.dijkstra("A");
       

         // Compute scores
        /*double scoreA = FairnessScorer.computeScore(zoneA);
        double scoreB = FairnessScorer.computeScore(zoneB);
        double scoreC = FairnessScorer.computeScore(zoneC);

        // Print scores
        FairnessScorer.printScore("Zone A", scoreA);
        FairnessScorer.printScore("Zone B", scoreB);
        FairnessScorer.printScore("Zone C", scoreC);

        // Optional: find highest priority
        if (scoreA >= scoreB && scoreA >= scoreC) {
            System.out.println("Highest priority: Zone A");
        } else if (scoreB >= scoreA && scoreB >= scoreC) {
            System.out.println("Highest priority: Zone B");
        } else {
            System.out.println("Highest priority: Zone C");
        }*/
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
          // ── AID DISTRIBUTOR TEST ──
System.out.println("\n===== AID DISTRIBUTOR — 3 CYCLES =====");

AidDistributor distributor = new AidDistributor();

// Register zones with damage level from actual simulation
// vulnerabilityBonus comes from CSV
distributor.registerZone("A", 1, zones.get("A").getRiskScore(),
    zones.get("A").getVulnerabilityBonus());
distributor.registerZone("B", 2, zones.get("B").getRiskScore(),
    zones.get("B").getVulnerabilityBonus());
distributor.registerZone("C", 3, zones.get("C").getRiskScore(),
    zones.get("C").getVulnerabilityBonus());
distributor.registerZone("D", 4, zones.get("D").getRiskScore(),
    zones.get("D").getVulnerabilityBonus());
distributor.registerZone("E", 5, zones.get("E").getRiskScore(),
    zones.get("E").getVulnerabilityBonus());
// Run 3 aid cycles — 2 units per cycle
distributor.runCycle(1, 2);
distributor.runCycle(2, 2);
distributor.runCycle(3, 2);
distributor.printAllRecords();


   }
 


}
