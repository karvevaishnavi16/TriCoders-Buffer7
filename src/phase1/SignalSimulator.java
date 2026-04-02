package phase1;

import java.io.*;
import java.util.*;

import city.Zone;
import city.CityGraph;
import phase2.EvacuationRouter;

public class SignalSimulator {

    public void runSimulation(String filePath, Map<String, Zone> zones, CityGraph graph) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            int lastTick = -1;

            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {

                // skip empty lines
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");

                // skip incomplete rows
                if (data.length < 5) continue;

                int tick = Integer.parseInt(data[0].trim());
                String zoneId = data[1].trim();
                double environmentalSignal = Double.parseDouble(data[2].trim());
                int sos = Integer.parseInt(data[3].trim());
                double infra = Double.parseDouble(data[4].trim());

                // print tick header
                if (tick != lastTick) {
                    System.out.println("\n========== TICK " + tick + " ==========");
                    lastTick = tick;
                }

                // get zone
                Zone z = zones.get(zoneId);
                if (z == null) {
                    System.out.println("Zone not found: " + zoneId);
                    continue;
                }

                // update values
                z.environmentalSignal = environmentalSignal;
                z.sosCount = sos;
                z.infraStress = infra;

                // evaluate risk
                z.evaluateRisk();

                // 🚨 CRITICAL
                if (z.isCritical) {
                    System.out.println("  !!! CRISIS DETECTED: Zone " + zoneId +
                            " | Env: " + environmentalSignal +
                            " | SOS: " + sos +
                            " | Infra: " + infra +
                            " | Risk Score: " + z.riskScore);

                    // 🔥 EVACUATION LOGIC
                    EvacuationRouter router = new EvacuationRouter();
                    List<String> path = router.findRoute(zoneId, graph);

                    System.out.println("  🚑 Evacuation Path: " + path);
                }

                // ⚠ ALERT (optional)
                else if (environmentalSignal > 6.0 || sos > 15 || infra > 1.8) {
                    System.out.println("  [ALERT] Zone " + zoneId +
                            " | Env: " + environmentalSignal +
                            " | SOS: " + sos +
                            " | Infra: " + infra);
                }
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}