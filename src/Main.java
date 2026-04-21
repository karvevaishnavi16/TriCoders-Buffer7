import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import city.CityGraph;
import city.Zone;
import phase1.RiskHeap;
import phase1.SignalMonitor;
import phase1.SignalSimulator;
import phase1.SpreadPredictor;
import phase2.AmbulanceDispatcher;
import phase2.EvacuationRouter;
import phase3.AidDistributor;
import phase3.AidRecord;
import phase3.FairnessScorer;
import phase3.HospitalAssigner;

public class Main {
    private static final String DATA_FILE = "data/mock_data.csv";

    private static class AppContext {
        Map<String, Zone> zones = createZones();
        CityGraph graph = createGraph();
        RiskHeap riskHeap = new RiskHeap();
        SpreadPredictor spreadPredictor = new SpreadPredictor();
        Map<String, Double> peakRiskScores = createPeakRiskMap();
        boolean simulationRun = false;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AppContext context = new AppContext();

        printBanner();
        printJudgeHighlights();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt(scanner, "Choose an option: ");
            System.out.println();

            switch (choice) {
                case 1:
                    runFullDemo(context);
                    break;
                case 2:
                    runCrisisDetection(context);
                    break;
                case 3:
                    showZoneSummary(context);
                    break;
                case 4:
                    runSlidingWindowDemo();
                    break;
                case 5:
                    runEvacuationDemo(context);
                    break;
                case 6:
                    runSpreadPrediction(context);
                    break;
                case 7:
                    runFairnessScoring(context);
                    break;
                case 8:
                    runAidDistribution(context);
                    break;
                case 9:
                    runHospitalAssignment();
                    break;
                case 10:
                    runAmbulanceDispatch();
                    break;
                case 11:
                    context = new AppContext();
                    System.out.println("System reset complete. Fresh demo state loaded.\n");
                    break;
                case 0:
                    running = false;
                    System.out.println("Exiting Smart City Crisis Engine. Demo session closed.");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a menu number.\n");
            }
        }

        scanner.close();
    }

    private static void printBanner() {
        System.out.println("======================================================================");
        System.out.println("              SMART CITY CRISIS & FAIR RESPONSE ENGINE");
        System.out.println("======================================================================");
    }

    private static void printJudgeHighlights() {
        System.out.println("What this demo shows:");
        System.out.println("1. Multi-signal crisis detection with risk ranking");
        System.out.println("2. Sliding-window signal monitoring");
        System.out.println("3. Evacuation routing with congestion-based rerouting");
        System.out.println("4. Spread prediction for nearby zones");
        System.out.println("5. Fair aid allocation, hospital balancing, and ambulance dispatch");
        System.out.println();
    }

    private static void printMenu() {
        System.out.println("Menu");
        System.out.println("1. Run complete judge demo");
        System.out.println("2. Run crisis detection simulation");
        System.out.println("3. Show final zone summary");
        System.out.println("4. Show sliding-window monitor demo");
        System.out.println("5. Show evacuation routing demo");
        System.out.println("6. Show spread prediction demo");
        System.out.println("7. Show fairness scoring");
        System.out.println("8. Show aid distribution cycles");
        System.out.println("9. Show hospital assignment demo");
        System.out.println("10. Show ambulance dispatch demo");
        System.out.println("11. Reset demo state");
        System.out.println("0. Exit");
    }

    private static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static void runFullDemo(AppContext context) {
        System.out.println("===== COMPLETE JUDGE DEMO =====");
        runCrisisDetection(context);
        showZoneSummary(context);
        runSlidingWindowDemo();
        runEvacuationDemo(context);
        runSpreadPrediction(context);
        runFairnessScoring(context);
        runAidDistribution(context);
        runHospitalAssignment();
        runAmbulanceDispatch();
        System.out.println("===== END OF DEMO =====\n");
    }

    private static void ensureSimulation(AppContext context) {
        if (context.simulationRun) {
            return;
        }

        SignalSimulator simulator = new SignalSimulator();
        simulator.runSimulation(
            DATA_FILE,
            context.zones,
            context.riskHeap,
            context.graph,
            context.spreadPredictor
        );
        loadPeakRiskScores(context);
        context.riskHeap.updateAll(context.zones);
        context.simulationRun = true;
    }

    private static void runCrisisDetection(AppContext context) {
        System.out.println("===== PHASE 1: CRISIS DETECTION =====");
        ensureSimulation(context);
        printCriticalZones(context.zones);
        System.out.println();
    }

    private static void showZoneSummary(AppContext context) {
        System.out.println("===== FINAL ZONE STATUS =====");
        ensureSimulation(context);
        for (Zone zone : context.zones.values()) {
            zone.printStatus();
        }
        System.out.println();
    }

    private static void printCriticalZones(Map<String, Zone> zones) {
        boolean anyCritical = false;
        System.out.println("Critical zones detected:");
        for (Zone zone : zones.values()) {
            if (zone.isCritical()) {
                System.out.println(
                    "  Zone " + zone.zoneName +
                    " [" + zone.zoneType + "] with risk score " + zone.riskScore
                );
                anyCritical = true;
            }
        }

        if (!anyCritical) {
            System.out.println("  No zones are critical in the latest simulation state.");
        }
    }

    private static void runSlidingWindowDemo() {
        System.out.println("===== SIGNAL MONITOR: SLIDING WINDOW =====");
        SignalMonitor monitor = new SignalMonitor();
        String zoneName = "A";
        monitor.initZone(zoneName);

        double[] envReadings = {4.0, 5.1, 6.3, 7.4, 8.1, 8.5, 9.0};
        int[] sosReadings = {2, 3, 6, 10, 22, 38, 45};
        double[] infraReadings = {1.0, 1.1, 1.2, 1.2, 1.3, 1.4, 1.6};

        for (int i = 0; i < envReadings.length; i++) {
            monitor.addReading(zoneName, envReadings[i], sosReadings[i], infraReadings[i]);
            System.out.printf(
                "Reading %d -> Env: %.1f, SOS: %d, Infra: %.1f | Window size: %d%n",
                i + 1,
                envReadings[i],
                sosReadings[i],
                infraReadings[i],
                monitor.getWindowSize(zoneName)
            );
        }

        monitor.printWindow(zoneName);
        System.out.println();
    }

    private static void runEvacuationDemo(AppContext context) {
        System.out.println("===== PHASE 2: EVACUATION ROUTING =====");
        ensureSimulation(context);

        Zone crisisZone = getResponseFocusZone(context);

        EvacuationRouter router = new EvacuationRouter();
        System.out.println("Response source selected: Zone " + crisisZone.zoneName);
        List<String> route = router.findRoute(crisisZone.zoneName, context.graph);
        router.printRoute(crisisZone.zoneName, route, context.graph);
        router.simulateCongestion(crisisZone.zoneName, "D", "H", context.graph);
        System.out.println();
    }

    private static void runSpreadPrediction(AppContext context) {
        System.out.println("===== SPREAD PREDICTION =====");
        ensureSimulation(context);

        Zone crisisZone = getResponseFocusZone(context);

        context.spreadPredictor.predict(crisisZone.zoneName, context.graph, context.zones);
        System.out.println();
    }

    private static void runFairnessScoring(AppContext context) {
        System.out.println("===== PHASE 3: FAIRNESS SCORES =====");
        ensureSimulation(context);

        for (Zone zone : context.zones.values()) {
            AidRecord record = buildAidRecord(zone, context);
            double score = FairnessScorer.computeScore(record);
            System.out.printf(
                "Zone %s [%s] -> priority score %.2f%n",
                zone.zoneName,
                zone.zoneType,
                score
            );
        }
        System.out.println();
    }

    private static void runAidDistribution(AppContext context) {
        System.out.println("===== AID DISTRIBUTION DEMO =====");
        ensureSimulation(context);

        AidDistributor distributor = new AidDistributor();
        for (Zone zone : context.zones.values()) {
            distributor.registerZone(
                zone.zoneName,
                zone.zoneId,
                getResponseDamage(zone, context),
                zone.getVulnerabilityBonus()
            );
        }

        distributor.runCycle(1, 3);
        distributor.runCycle(2, 3);
        distributor.runCycle(3, 3);
        distributor.printAllRecords();
        System.out.println();
    }

    private static void runHospitalAssignment() {
        System.out.println("===== HOSPITAL ASSIGNER =====");
        HospitalAssigner assigner = new HospitalAssigner();
        assigner.addHospital("City Hospital", 3);
        assigner.addHospital("General Hospital", 5);
        assigner.addHospital("Metro Care", 2);

        for (int i = 1; i <= 9; i++) {
            System.out.print("Patient " + i + ": ");
            assigner.assignPatient();
        }
        System.out.println();
    }

    private static void runAmbulanceDispatch() {
        System.out.println("===== AMBULANCE DISPATCHER =====");
        AmbulanceDispatcher dispatcher = new AmbulanceDispatcher();
        dispatcher.addCall(2, 5);
        dispatcher.addCall(7, 9);
        dispatcher.addCall(4, 7);
        dispatcher.addCall(1, 10);

        for (int i = 0; i < 5; i++) {
            dispatcher.dispatchNext();
        }
        System.out.println();
    }

    private static AidRecord buildAidRecord(Zone zone, AppContext context) {
        double damageLevel = getResponseDamage(zone, context);
        AidRecord record = new AidRecord(zone.zoneId, damageLevel);
        record.setVulnerabilityBonus(zone.vulnerabilityBonus);
        record.setAidReceived((int) Math.round(damageLevel) % 3);
        record.setTimeIgnored(damageLevel >= 2 ? 3 : 1);
        record.setNeglectMultiplier(damageLevel >= 2 ? 1.8 : 1.1);
        return record;
    }

    private static Zone getFirstCriticalZone(Map<String, Zone> zones) {
        for (Zone zone : zones.values()) {
            if (zone.isCritical()) {
                return zone;
            }
        }
        return null;
    }

    private static Zone getResponseFocusZone(AppContext context) {
        Zone criticalZone = getFirstCriticalZone(context.zones);
        if (criticalZone != null) {
            return criticalZone;
        }

        Zone bestZone = null;
        double bestScore = -1.0;
        for (Zone zone : context.zones.values()) {
            double peakScore = context.peakRiskScores.getOrDefault(zone.zoneName, 0.0);
            if (peakScore > bestScore) {
                bestScore = peakScore;
                bestZone = zone;
            }
        }

        if (bestZone != null) {
            System.out.println(
                "Latest tick is stable, so response demos use peak-risk hotspot Zone " +
                bestZone.zoneName + "."
            );
            return bestZone;
        }

        return context.zones.get("A");
    }

    private static double getResponseDamage(Zone zone, AppContext context) {
        return Math.max(zone.getRiskScore(), context.peakRiskScores.getOrDefault(zone.zoneName, 0.0));
    }

    private static void loadPeakRiskScores(AppContext context) {
        for (String zoneName : context.peakRiskScores.keySet()) {
            context.peakRiskScores.put(zoneName, 0.0);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }

                String[] data = trimmed.split(",");
                if (data.length < 7) {
                    continue;
                }

                String zoneName = data[1].trim();
                double env = Double.parseDouble(data[2].trim());
                int sos = Integer.parseInt(data[3].trim());
                double infra = Double.parseDouble(data[4].trim());

                int risk = 0;
                if (env > 7.0) {
                    risk++;
                }
                if (sos > 30) {
                    risk++;
                }
                if (infra > 2.0) {
                    risk++;
                }

                double currentPeak = context.peakRiskScores.getOrDefault(zoneName, 0.0);
                context.peakRiskScores.put(zoneName, Math.max(currentPeak, risk));
            }
        } catch (IOException ex) {
            System.out.println("Could not load peak-risk summary: " + ex.getMessage());
        }
    }

    private static Map<String, Double> createPeakRiskMap() {
        Map<String, Double> peakRiskMap = new LinkedHashMap<>();
        for (char zone = 'A'; zone <= 'J'; zone++) {
            peakRiskMap.put(String.valueOf(zone), 0.0);
        }
        return peakRiskMap;
    }

    private static Map<String, Zone> createZones() {
        Map<String, Zone> zones = new LinkedHashMap<>();
        for (int i = 0; i < 10; i++) {
            String name = String.valueOf((char) ('A' + i));
            zones.put(name, new Zone(i + 1, name));
        }
        return zones;
    }

    private static CityGraph createGraph() {
        CityGraph graph = new CityGraph();
        List<String> zoneNames = new ArrayList<>();
        for (char zone = 'A'; zone <= 'J'; zone++) {
            zoneNames.add(String.valueOf(zone));
        }

        for (String zoneName : zoneNames) {
            graph.addZone(zoneName);
        }

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

        return graph;
    }
}
