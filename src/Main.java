
import java.util.*;

import city.CityGraph;
import city.Zone;

import phase1.SignalSimulator;
import phase1.SignalMonitor;
import phase1.SpreadPredictor;

import phase2.EvacuationRouter;

import phase3.AidRecord;
import phase3.FairnessScorer;

public class Main {
    public static void main(String[] args) {

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

        System.out.println("======================================================================");
        System.out.println("SMART CITY CRISIS ENGINE");
        System.out.println("======================================================================");

        // ── PHASE 1: SIGNAL SIMULATION ──
        SignalSimulator sim = new SignalSimulator();
        sim.runSimulation("data/mock_data.csv", zones);

        // ── FINAL STATUS ──
        System.out.println("\n===== FINAL ZONE STATUS =====");
        for (Zone z : zones.values()) {
            z.printStatus();
        }

        // ── CRITICAL ZONES ──
        System.out.println("\n===== CRITICAL ZONES =====");
        boolean anyCritical = false;

        for (Zone z : zones.values()) {
            if (z.isCritical) {
                System.out.println("!!! Zone " + z.zoneName +
                        " [" + z.zoneType + "] is CRITICAL !!!");
                anyCritical = true;
            }
        }

        if (!anyCritical) {
            System.out.println("⚠ No critical zones detected → forcing test case...");
            zones.get("A").isCritical = true;   // 🔥 ensures rest of program runs
        }

        // ── SIGNAL MONITOR TEST ──
        System.out.println("\n===== SIGNAL MONITOR TEST =====");

        SignalMonitor monitor = new SignalMonitor();
        monitor.initZone("A");

        double[] testValues = {4, 5, 6, 7, 8, 8.5, 9};

        for (double val : testValues) {
            monitor.addReading("A", val, 5, 1.2);
            System.out.println("Env=" + val +
                    " | Size=" + monitor.getWindowSize("A") +
                    " | Latest=" + monitor.getLatestEnv("A"));
        }

        // ── PHASE 2: EVACUATION ──
        System.out.println("\n===== EVACUATION ROUTING =====");

        CityGraph graph = new CityGraph();

        for (String z : zones.keySet()) {
            graph.addZone(z);
        }

        // connections
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

        EvacuationRouter router = new EvacuationRouter();

        // run evacuation
        for (Zone z : zones.values()) {
            if (z.isCritical) {
                System.out.println("\n🚨 Crisis in Zone " + z.zoneName);

                List<String> route = router.findRoute(z.zoneName, graph);
                router.printRoute(z.zoneName, route, graph);

                router.simulateCongestion(z.zoneName, "D", "H", graph);
                break;
            }
        }

        // ── PHASE 3: FAIRNESS ──
        System.out.println("\n===== FAIRNESS SCORES =====");

        for (Zone z : zones.values()) {

            AidRecord record = new AidRecord(z.zoneId, z.riskScore);

            record.setVulnerabilityBonus(z.vulnerabilityBonus);
            record.setAidReceived((int)(Math.random() * 5));
            record.setTimeIgnored((int)(Math.random() * 6));

            double neglect = 1.0 + z.riskScore * 0.3;
            if (z.isCritical) {
                neglect += 0.5;
            }

            record.setNeglectMultiplier(neglect);

            double score = FairnessScorer.computeScore(record);

            System.out.printf("%s: priority score = %.2f\n",
                    z.zoneName, score);
        }

        // ── SPREAD PREDICTION ──
        System.out.println("\n===== SPREAD PREDICTION =====");

        SpreadPredictor predictor = new SpreadPredictor();
        boolean predicted = false;

        for (Zone z : zones.values()) {
            if (z.isCritical) {
                System.out.println("Predicting spread from Zone " + z.zoneName);
                predictor.predict(z.zoneName, graph, zones);
                predicted = true;
                break;
            }
        }

        if (!predicted) {
            System.out.println("⚠ No prediction executed.");
        }
    }
}


