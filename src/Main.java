import java.util.*;
import phase3.AidRecord;
import city.CityGraph;
import city.Zone;
import phase1.SignalSimulator;
import phase3.FairnessScorer;
public class Main {
    public static void main(String[] args) {

      //zone
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

        SignalSimulator sim = new SignalSimulator();

        sim.runSimulation(
           "data/mock_data.csv",
            zones
        );
        System.out.println("\nFinal Zone Status:");

        for (Zone z : zones.values()) {
            z.printStatus();
        }

        System.out.println("\nCritical Zones:");

        for (Map.Entry<String, Zone> entry : zones.entrySet()) {
        if (entry.getValue().isCritical()) 
        {
            System.out.println(entry.getKey());
        }
        }

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


        CityGraph graph = new CityGraph();

        // Add zones (nodes)
        graph.addZone("A");
        graph.addZone("B");
        graph.addZone("C");
        graph.addZone("D");

        // Add connections (edges with weights)
        graph.addConnection("A", "B", 5);
        graph.addConnection("A", "C", 10);
        graph.addConnection("B", "C", 3);
        graph.addConnection("C", "D", 2);

        // Print graph
        System.out.println("Graph:");
        graph.printGraph();

        // Run Dijkstra
        System.out.println("\nShortest Paths:");
        graph.shortestPath("A");

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
            record.setAidReceived(2);     // you can make this dynamic later
            record.setTimeIgnored(3);     // simulate delay

            // 🔹 Neglect multiplier (IMPORTANT 🔥)
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
