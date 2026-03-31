import java.util.*;
import phase3.AidRecord;
import city.CityGraph;
import city.Zone;
import phase1.SignalSimulator;
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
        sim.runSimulation("data/mock_data.csv", zones);

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
        AidRecord r1 = new AidRecord(1, 75.5);
        AidRecord r2 = new AidRecord(2, 50.0);
        AidRecord r3 = new AidRecord(3, 90.2);

        System.out.println("\nInitial Aid Records:");
        r1.printRecord();
        r2.printRecord();
        r3.printRecord();

        r1.aidReceived = 5;
        r1.timeIgnored = 2;

        r2.aidReceived = 3;
        r2.timeIgnored = 1;

        r3.aidReceived = 7;
        r3.timeIgnored = 4;

        System.out.println("\nUpdated Aid Records:");
        r1.printRecord();
        r2.printRecord();
        r3.printRecord();



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
   }
}
