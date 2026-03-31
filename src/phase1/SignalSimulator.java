package phase1;

import java.io.*;
import java.util.*;
import city.Zone;

public class SignalSimulator {

    public void runSimulation(String filePath, Map<String, Zone> zones) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;

            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");
                String zoneId = data[1].trim();
                int tick = Integer.parseInt(data[0]);
                double environmentalSignal = Double.parseDouble(data[2]);
                int sos = Integer.parseInt(data[3]);
                double infra = Double.parseDouble(data[4]);
                String zoneType = data[5];
                double vulnerability = Double.parseDouble(data[6]);
                // Get zone
                Zone z = zones.get(zoneId);

                //(prevents crash)
                if (z == null) {
                    System.out.println("Zone not found: " + zoneId);
                    continue;
                }

                // Update values
                z.environmentalSignal = environmentalSignal;
                z.sosCount = sos;
                z.infraStress = infra;
                z.evaluateRisk();
                System.out.println("Tick " + tick + " updated Zone " + zoneId);
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}