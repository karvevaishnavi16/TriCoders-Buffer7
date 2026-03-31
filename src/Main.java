import phase3.AidRecord;
import city.CityGraph;
import city.Zone;

public class Main
{
   public static void main(String[] args)
   {
        // AidRecord test
        AidRecord r1 = new AidRecord(1, 75.5);
        AidRecord r2 = new AidRecord(2, 50.0);
        AidRecord r3 = new AidRecord(3, 90.2);

        System.out.println("Initial Records:");
        r1.printRecord();
        r2.printRecord();
        r3.printRecord();

        r1.aidReceived = 5;
        r1.timeIgnored = 2;

        r2.aidReceived = 3;
        r2.timeIgnored = 1;

        r3.aidReceived = 7;
        r3.timeIgnored = 4;

        System.out.println("\nUpdated Records:");
        r1.printRecord();
        r2.printRecord();
        r3.printRecord();

        // Zone test 
        Zone z = new Zone(1, "Zone A");
        z.waterLevel = 8.4;
        z.sosCount = 38;
        z.infraStress = 1.3;
        z.printStatus();

         // Create graph
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