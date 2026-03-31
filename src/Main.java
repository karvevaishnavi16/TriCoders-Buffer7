import java.util.*;
import phase3.AidRecord;
import city.CityGraph;
import city.Zone;
import phase1.SignalSimulator;
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
            "C:\\Users\\Karve\\DSAprj-Buffer\\TriCoders-Buffer7\\data\\mock_data.csv",
            zones
        );
        System.out.println("\nFinal Zone Status:");

        for (Zone z : zones.values()) {
            z.printStatus();
        }

        System.out.println("\nCritical Zones:");

        for (Map.Entry<String, Zone> entry : zones.entrySet()) {
        if (entry.getValue().isCritical) 
        {
            System.out.println(entry.getKey());
        }
        }

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
   }
}
