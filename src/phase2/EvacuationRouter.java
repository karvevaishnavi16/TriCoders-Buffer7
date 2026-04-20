package phase2;

import java.util.*;
import city.CityGraph;

public class EvacuationRouter {

    // Safe zones — final destinations
    private static final Set<String> SAFE_ZONES =
            new HashSet<>(Arrays.asList("H", "I", "J"));

    // 🔥 Find shortest path to nearest safe zone
    public List<String> findRoute(String startZone, CityGraph graph) {

        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> parent = new HashMap<>();

        // Initialize distances
        for (String zone : graph.getAdjList().keySet()) {
            dist.put(zone, Integer.MAX_VALUE);
        }

        dist.put(startZone, 0);
        parent.put(startZone, null);

        // Min-heap (Dijkstra)
        PriorityQueue<String> pq = new PriorityQueue<>(
                (a, b) -> dist.get(a) - dist.get(b)
        );

        pq.add(startZone);

        String safeZoneReached = null;

        while (!pq.isEmpty()) {
            String current = pq.poll();

            // Stop when safe zone reached
            if (SAFE_ZONES.contains(current)) {
                safeZoneReached = current;
                break;
            }

            // Explore neighbors
            for (String[] neighbor : graph.getAdjList().getOrDefault(current, new ArrayList<>())) {

                String next = neighbor[0];
                int weight = Integer.parseInt(neighbor[1]);

                int newDist = dist.get(current) + weight;

                if (newDist < dist.get(next)) {
                    dist.put(next, newDist);
                    parent.put(next, current);
                    pq.add(next);
                }
            }
        }

        // No safe zone found
        if (safeZoneReached == null) {
            System.out.println("No safe zone reachable from " + startZone);
            return new ArrayList<>();
        }

        // 🔥 Reconstruct path
        List<String> path = new ArrayList<>();
        String curr = safeZoneReached;

        while (curr != null) {
            path.add(0, curr);
            curr = parent.get(curr);
        }

        return path;
    }

    // 🔥 Print route nicely
    public void printRoute(String startZone, List<String> path, CityGraph graph) {

        if (path.isEmpty()) {
            System.out.println("No evacuation route found.");
            return;
        }

        int totalTime = 0;

        for (int i = 0; i < path.size() - 1; i++) {
            totalTime += graph.getWeight(path.get(i), path.get(i + 1));
        }

        System.out.println("Best evacuation route: " + String.join(" -> ", path));
        System.out.println("Total travel time: " + totalTime + " minutes");
        System.out.println("Safe zone reached: " + path.get(path.size() - 1));
    }

    // 🔥 Simulate congestion and reroute
    public void simulateCongestion(String startZone, String z1, String z2, CityGraph graph) {

        int oldWeight = graph.getWeight(z1, z2);
        int newWeight = oldWeight * 2;

        System.out.println("\n!!! CONGESTION: Road " + z1 + "-" + z2 +
                " changed from " + oldWeight + " -> " + newWeight);

        graph.updateWeight(z1, z2, newWeight);

        System.out.println("Recomputing route...");

        List<String> newPath = findRoute(startZone, graph);
        printRoute(startZone, newPath, graph);
    }
}