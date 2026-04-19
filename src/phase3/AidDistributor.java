package phase3;
import java.util.*;
public class AidDistributor
{
    //Stores AidRecord for every zone
    //Key = zone name
    //Value = AidRecord with full aid history
    private HashMap<String,AidRecord> aidRecords;
    public AidDistributor()
    {
        aidRecords= new HashMap<>();
    }
    //Register a zone with its damage level and vulnerability 
    //Called once for each zone before simulation starts
    public  void registerZone(String zoneName, int zoneId, double damageLevel, double vulnerabilityBonus)
    {
        AidRecord record = new AidRecord(zoneId, damageLevel);
        record.setVulnerabilityBonus(vulnerabilityBonus);
        aidRecords.put(zoneName, record);
    }
        // Run one complete aid distribution cycle
    // aidUnits = how many zones get aid this cycle
    public void runCycle(int cycleNumber, int aidUnits) {

        System.out.println("\n  --- Aid Cycle " + cycleNumber +
            " (" + aidUnits + " aid units available) ---");

        // Step 1 — Score all zones and build max-heap
        PriorityQueue<Map.Entry<String, AidRecord>> heap =
            new PriorityQueue<>((a, b) -> {
                double scoreA = FairnessScorer.computeScore(a.getValue());
                double scoreB = FairnessScorer.computeScore(b.getValue());
                return Double.compare(scoreB, scoreA); // max-heap
            });

        // Add all zones to heap
        for (Map.Entry<String, AidRecord> entry : aidRecords.entrySet()) {
            heap.add(entry);
        }

        // Step 2 — Track which zones were served this cycle
        Set<String> servedThisCycle = new HashSet<>();

        // Step 3 — Deliver aid to top N zones
        int unitsLeft = aidUnits;
        while (!heap.isEmpty() && unitsLeft > 0) {
            Map.Entry<String, AidRecord> top = heap.poll();
            String zoneName = top.getKey();
            AidRecord record = top.getValue();

            double score = FairnessScorer.computeScore(record);

            System.out.println("  Zone " + zoneName +
                " [" + String.format("%.2f", score) + "] -> AID DELIVERED" +
                " | Damage: " + record.damageLevel +
                " | Ignored: " + record.timeIgnored + " cycles" +
                " | Neglect x" + record.neglectMultiplier);

            // Mark this zone as served
            record.markServed();
            servedThisCycle.add(zoneName);
            unitsLeft--;
        }

        // Step 4 — Mark all unserved zones as skipped
        // Their timeIgnored increases
        // Every 3 skips — neglectMultiplier increases by 0.5
        for (Map.Entry<String, AidRecord> entry : aidRecords.entrySet()) {
            if (!servedThisCycle.contains(entry.getKey())) {
                entry.getValue().markSkipped();
                System.out.println("  Zone " + entry.getKey() +
                    " -> SKIPPED" +
                    " | Time ignored: " + entry.getValue().timeIgnored +
                    " | Neglect multiplier: " + entry.getValue().neglectMultiplier);
            }
        }
    }

    // Print current state of all AidRecords
    public void printAllRecords() {
        System.out.println("\n  Current Aid Records:");
        for (Map.Entry<String, AidRecord> entry : aidRecords.entrySet()) {
            System.out.print("  Zone " + entry.getKey() + ": ");
            entry.getValue().printRecord();
        }
    }

}