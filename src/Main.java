import java.util.*;
import phase2.AmbulanceDispatcher;
import phase1.RiskHeap;
import phase3.AidRecord;
import city.CityGraph;
import city.Zone;
import phase1.SignalSimulator;
import phase3.FairnessScorer;
import phase1.SignalMonitor;
import phase1.SpreadPredictor;
import phase2.EvacuationRouter;
import phase3.AidDistributor;
import phase3.HospitalAssigner;

public class Main {

    static Map<String, Zone> zones = new HashMap<>();
    static CityGraph graph = new CityGraph();
    static RiskHeap riskHeap = new RiskHeap();
    static SpreadPredictor spreader = new SpreadPredictor();
    static AidDistributor distributor = new AidDistributor();
    static EvacuationRouter router = new EvacuationRouter();
    static boolean simulationRun = false;

    public static void main(String[] args) {

        setup();

        Scanner sc = new Scanner(System.in);
        int choice = 0;

        System.out.println("======================================================================");
        System.out.println("           SMART CITY CRISIS & FAIR RESPONSE ENGINE                  ");
        System.out.println("                     TriCoders - Buffer 7.0                          ");
        System.out.println("======================================================================");

        while (choice != 8) {

            System.out.println("\n==================== MAIN MENU ====================");
            System.out.println("  1. Run Phase 1 : Crisis Detection");
            System.out.println("  2. View Final Zone Status");
            System.out.println("  3. View Risk Heap :  Zone Rankings");
            System.out.println("  4. Run Phase 2 : Evacuation Routing");
            System.out.println("  5. Run Phase 3 : Fair Aid Distribution");
            System.out.println("  6. Demo : Ambulance Dispatcher");
            System.out.println("  7. Demo : Hospital Assigner");
            System.out.println("  8. Exit");
            System.out.println("===================================================");
            System.out.print("Enter choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Invalid input. Enter a number 1-8.");
                continue;
            }

            switch (choice) {
                case 1: runPhase1(); break;
                case 2: viewZoneStatus(); break;
                case 3: viewRiskHeap(); break;
                case 4: runPhase2(sc); break;
                case 5: runPhase3(sc); break;
                case 6: runAmbulance(); break;
                case 7: runHospital(); break;
                case 8:
                    System.out.println("\nExiting Smart City Crisis Engine. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1-8.");
            }
        }

        sc.close();
    }

    // ── SETUP — runs once at start ──
    static void setup() {
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

        for (String z : zones.keySet()) graph.addZone(z);

        graph.addConnection("A", "B", 5);
        graph.addConnection("A", "C", 10);
        graph.addConnection("B", "C", 3);
        graph.addConnection("B", "D", 7);
        graph.addConnection("C", "D", 2);
        graph.addConnection("D", "E", 4);
        graph.addConnection("D", "H", 6);
        graph.addConnection("E", "I", 3);
        graph.addConnection("C", "J", 8);
        graph.addConnection("F", "G", 5);
        graph.addConnection("G", "H", 4);
    }

    // ── MENU 1 — Phase 1 Crisis Detection ──
    static void runPhase1() {
        System.out.println("\n--- PHASE 1: Crisis Detection ---");
        System.out.println("Reading sensor data from CSV...\n");

        SignalSimulator sim = new SignalSimulator();
        sim.runSimulation("../data/mock_data.csv", zones, riskHeap, graph, spreader);
        simulationRun = true;

        // register zones for aid after simulation
        distributor = new AidDistributor();
        for (Zone z : zones.values()) {
            distributor.registerZone(z.zoneName, z.zoneId,
                z.getRiskScore(), z.getVulnerabilityBonus());
        }

        System.out.println("\nPhase 1 complete. Press 2 to see zone status.");
    }

    // ── MENU 2 — Zone Status ──
    static void viewZoneStatus() {
        if (!simulationRun) {
            System.out.println("Run Phase 1 first (option 1).");
            return;
        }
        System.out.println("\n===== FINAL ZONE STATUS =====");
        for (Zone z : zones.values()) z.printStatus();

        System.out.println("\n===== CRITICAL ZONES =====");
        boolean any = false;
        for (Zone z : zones.values()) {
            if (z.isCritical) {
                System.out.println("!!! Zone " + z.zoneName +
                    " [" + z.zoneType + "] is CRITICAL !!!");
                any = true;
            }
        }
        if (!any) System.out.println("No critical zones.");
    }

    // ── MENU 3 — Risk Heap ──
    static void viewRiskHeap() {
        if (!simulationRun) {
            System.out.println("Run Phase 1 first (option 1).");
            return;
        }
        System.out.println("\n===== RISK HEAP - TOP 5 ZONES =====");
        riskHeap.updateAll(zones);
        riskHeap.printTopZones(5);
    }

    // ── MENU 4 — Phase 2 Evacuation ──
    static void runPhase2(Scanner sc) {
        if (!simulationRun) {
            System.out.println("Run Phase 1 first (option 1).");
            return;
        }

        System.out.println("\n--- PHASE 2: Evacuation Routing ---");
        System.out.println("\nCity Road Network:");
        graph.printGraph();

        System.out.print("\nEnter zone name to evacuate from (e.g. A): ");
        String zone = sc.nextLine().trim().toUpperCase();

        if (!zones.containsKey(zone)) {
            System.out.println("Zone not found.");
            return;
        }

        System.out.println("\nFinding evacuation route from Zone " + zone + "...");
        List<String> route = router.findRoute(zone, graph);
        router.printRoute(zone, route, graph);

        System.out.print("\nSimulate road congestion? (y/n): ");
        String ans = sc.nextLine().trim().toLowerCase();
        if (ans.equals("y")) {
            System.out.print("Enter road to congest (e.g. D H): ");
            String[] roads = sc.nextLine().trim().toUpperCase().split(" ");
            if (roads.length == 2) {
                router.simulateCongestion(zone, roads[0], roads[1], graph);
            } else {
                System.out.println("Invalid input. Enter two zone names separated by space.");
            }
        }
    }

    // ── MENU 5 — Phase 3 Aid Distribution ──
    static void runPhase3(Scanner sc) {
        if (!simulationRun) {
            System.out.println("Run Phase 1 first (option 1).");
            return;
        }

        System.out.println("\n--- PHASE 3: Fair Aid Distribution ---");
        System.out.print("How many aid cycles to run? (1-5): ");

        int cycles = 3;
        try {
            cycles = Integer.parseInt(sc.nextLine().trim());
            if (cycles < 1 || cycles > 5) cycles = 3;
        } catch (Exception e) {
            cycles = 3;
        }

        System.out.print("How many aid units per cycle? (1-5): ");
        int units = 2;
        try {
            units = Integer.parseInt(sc.nextLine().trim());
            if (units < 1 || units > 5) units = 2;
        } catch (Exception e) {
            units = 2;
        }

        for (int i = 1; i <= cycles; i++) {
            distributor.runCycle(i, units);
        }

        distributor.printAllRecords();
    }

    // ── MENU 6 — Ambulance Dispatcher ──
    static void runAmbulance() {
        System.out.println("\n--- AMBULANCE DISPATCHER (Future Scope Demo) ---");
        System.out.println("Dispatching by severity score — highest severity first.\n");

        AmbulanceDispatcher dispatcher = new AmbulanceDispatcher();
        dispatcher.addCall(1, 5);  // Zone 1, severity 5
        dispatcher.addCall(3, 9);  // Zone 3, severity 9 — highest
        dispatcher.addCall(2, 7);  // Zone 2, severity 7

        System.out.println("3 emergency calls received:");
        System.out.println("  Zone 1 — severity 5");
        System.out.println("  Zone 3 — severity 9");
        System.out.println("  Zone 2 — severity 7");
        System.out.println("\nDispatching in order:");

        dispatcher.dispatchNext();
        dispatcher.dispatchNext();
        dispatcher.dispatchNext();

        System.out.println("\nNote: Full integration with live graph routing is the next development phase.");
    }

    // ── MENU 7 — Hospital Assigner ──
    static void runHospital() {
        System.out.println("\n--- HOSPITAL ASSIGNER (Future Scope Demo) ---");
        System.out.println("Assigning patients to least loaded hospital first.\n");

        HospitalAssigner assigner = new HospitalAssigner();
        assigner.addHospital("City Hospital", 3);
        assigner.addHospital("General Hospital", 5);
        assigner.addHospital("Metro Care", 2);

        System.out.println("3 hospitals registered:");
        System.out.println("  City Hospital   — capacity 3");
        System.out.println("  General Hospital — capacity 5");
        System.out.println("  Metro Care       — capacity 2");
        System.out.println("\nAssigning 5 patients:");

        assigner.assignPatient();
        assigner.assignPatient();
        assigner.assignPatient();
        assigner.assignPatient();
        assigner.assignPatient();

        System.out.println("\nNote: Full integration with ambulance routing is the next development phase.");
    }
}