package phase1;

import phase1.SpreadPredictor;
import city.CityGraph;
import phase1.RiskHeap;
import java.io.*;
import java.util.*;
import city.Zone;

public class SignalSimulator {

    public void runSimulation(String filePath, Map<String, Zone> zones, RiskHeap riskHeap, CityGraph graph,
            SpreadPredictor spreader) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            int lastTick = -1;
            boolean tickHadActivity = false;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {

                // Fix 1 — skip empty lines
                line = line.trim();
                if (line.isEmpty())
                    continue;

                String[] data = line.split(",");

                // Fix 2 — skip incomplete rows
                if (data.length < 7)
                    continue;

                int tick = Integer.parseInt(data[0].trim());
                String zoneId = data[1].trim();
                double environmentalSignal = Double.parseDouble(data[2].trim());
                int sos = Integer.parseInt(data[3].trim());
                double infra = Double.parseDouble(data[4].trim());
                String zoneType = data[5].trim();
                double vulnerability = Double.parseDouble(data[6].trim());

                // Print tick header when tick changes
                if (tick != lastTick) {
                    // if previous tick had no alerts or crisis — print ALL CLEAR
                    if (lastTick != -1 && !tickHadActivity) {
                        System.out.println("  [ALL CLEAR] All zones normal.");
                    }
                    System.out.println("\n========== TICK " + tick + " ==========");
                    lastTick = tick;
                    tickHadActivity = false; // reset for new tick
                    // update heap with latest risk scores
                    riskHeap.updateAll(zones);
                    // print top 3 risk zones this tick
                    riskHeap.printTopZones(3);
                }

                // Get zone
                Zone z = zones.get(zoneId);
                if (z == null) {
                    System.out.println("Zone not found: " + zoneId);
                    continue;
                }

                // Update values
                z.environmentalSignal = environmentalSignal;
                z.sosCount = sos;
                z.infraStress = infra;
                z.evaluateRisk();
                z.zoneType = zoneType;
                z.vulnerabilityBonus = vulnerability;

                // Print only if ALERT or CRITICAL
                if (z.isCritical) {
                    System.out.println("  !!! CRISIS DETECTED: Zone " + zoneId +
                            " | Env: " + environmentalSignal +
                            " | SOS: " + sos +
                            " | Infra: " + infra +
                            " | Risk Score: " + z.riskScore +
                            " | Type: " + zoneType);
                    tickHadActivity = true;
                } else if (environmentalSignal > 6.0 || sos > 15 || infra > 1.8) {
                    System.out.println("  [ALERT] Zone " + zoneId +
                            " | Env: " + environmentalSignal +
                            " | SOS: " + sos +
                            " | Infra: " + infra);
                    tickHadActivity = true;
                    // trigger BFS spread prediction from this critical zone
                    spreader.predict(zoneId, graph, zones);
                }
            }

            if (lastTick != -1 && !tickHadActivity) {
                System.out.println("  [ALL CLEAR] All zones normal.");
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
