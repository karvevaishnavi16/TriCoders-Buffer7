package phase1;

import java.util.*;
import city.Zone;

public class RiskHeap {

    // Max-heap — zone with highest riskScore always at top
    // Comparator: compare b to a (reversed) = max-heap
    private PriorityQueue<Zone> heap;

    public RiskHeap() {
        // Custom comparator — highest riskScore at top
        heap = new PriorityQueue<>(
            (a, b) -> Double.compare(b.riskScore, a.riskScore)
        );
    }

    // Add a zone to the heap
    // O(log n)
    public void addZone(Zone z) {   
        heap.add(z);
    }

    // See highest risk zone without removing it
    // O(1)
    public Zone getHighestRisk() {
        return heap.peek();
    }

    // Remove and return highest risk zone
    // O(log n)
    public Zone pollHighestRisk() {
        return heap.poll();
    }

    // Update heap after all zone risk scores change
    // Called after every tick — clears heap and re-adds all zones
    // This ensures heap reflects latest risk scores
    public void updateAll(Map<String, Zone> zones) {
        heap.clear();
        for (Zone z : zones.values()) {
            heap.add(z);
        }
    }

    // How many zones are in heap
    public int size() {
        return heap.size();
    }

    // Print top N highest risk zones
    // Makes a copy of heap so original is not disturbed
    public void printTopZones(int n) {
        System.out.println("\n--- Top " + n + " Risk Zones ---");

        // Copy heap so we don't destroy the original
        PriorityQueue<Zone> copy = new PriorityQueue<>(heap);

        int count = 0;
        while (!copy.isEmpty() && count < n) {
            Zone z = copy.poll();
            System.out.println("  " + (count + 1) + ". Zone " + z.zoneName +
                " | Risk Score: " + z.riskScore +
                " | Status: " + (z.isCritical ? "CRITICAL" : "NORMAL") +
                " | Type: " + z.zoneType);
            count++;
        }
    }
}