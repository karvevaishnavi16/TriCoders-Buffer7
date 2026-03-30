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

        //Citygraph test
        CityGraph graph = new CityGraph();

         graph.addZone(1);
         graph.addZone(2);
         graph.addZone(3);

         graph.addConnection(1, 2);
         graph.addConnection(2, 3);

         graph.printGraph();

        
   }
}              


