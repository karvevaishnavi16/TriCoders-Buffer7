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
            Set<String> criticalZonesAlerted = new HashSet<>();

            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {

                line = line.trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length < 7) continue;

                int tick = Integer.parseInt(data[0].trim());
                String zoneId = data[1].trim();
                double environmentalSignal = Double.parseDouble(data[2].trim());
                int sos = Integer.parseInt(data[3].trim());
                double infra = Double.parseDouble(data[4].trim());
                String zoneType = data[5].trim();
                double vulnerability = Double.parseDouble(data[6].trim());

                if (tick != lastTick) {
                    if (lastTick != -1 && !tickHadActivity) {
                        System.out.println("  [ALL CLEAR] All zones normal.");
                    }
                    System.out.println("\n========== TICK " + tick + " ==========");
                    lastTick = tick;
                    tickHadActivity = false;
                    riskHeap.updateAll(zones);
                    riskHeap.printTopZones(3);
                }

                Zone z = zones.get(zoneId);
                if (z == null) {
                    System.out.println("  Zone not found: " + zoneId);
                    continue;
                }

                z.environmentalSignal = environmentalSignal;
                z.sosCount = sos;
                z.infraStress = infra;
                z.zoneType = zoneType;
                z.vulnerabilityBonus = vulnerability;
                z.evaluateRisk();

                if (z.isCritical) {
                    System.out.println("  !!! CRISIS DETECTED: Zone " + zoneId +
                            " | Env: " + environmentalSignal +
                            " | SOS: " + sos +
                            " | Infra: " + infra +
                            " | Risk Score: " + z.riskScore +
                            " | Type: " + zoneType);
                    tickHadActivity = true;

                    // BFS spread — only trigger once per zone
                    if (!criticalZonesAlerted.contains(zoneId)) {
                        criticalZonesAlerted.add(zoneId);
                        spreader.predict(zoneId, graph, zones);
                    }

                } else if (environmentalSignal > 6.0 || sos > 15 || infra > 1.8) {
                    System.out.println("  [ALERT] Zone " + zoneId +
                            " | Env: " + environmentalSignal +
                            " | SOS: " + sos +
                            " | Infra: " + infra);
                    tickHadActivity = true;
                }
            }

            if (!tickHadActivity) {
                System.out.println("  [ALL CLEAR] All zones normal.");
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
