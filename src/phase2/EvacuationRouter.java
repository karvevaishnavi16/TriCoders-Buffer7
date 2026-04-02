package phase2;

import java.util.*;
import city.CityGraph;

public class EvacuationRouter {

    public List<String> findRoute(String startZone, CityGraph graph) {

        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> parent = new HashMap<>();

        // Initialize
        // for (String zone : graph.adjList.keySet()) {
        //     dist.put(zone, Integer.MAX_VALUE);
        //     parent.put(zone, null);
        // }
        for (String zone : graph.getAllZones()) {
    dist.put(zone, Integer.MAX_VALUE);
    parent.put(zone, null);
}

        dist.put(startZone, 0);

        PriorityQueue<String> pq = new PriorityQueue<>(
            Comparator.comparingInt(dist::get)
        );

        pq.add(startZone);

        // Safe zones
        Set<String> safeZones = new HashSet<>(Arrays.asList("H", "I", "J"));

        while (!pq.isEmpty()) {

            String current = pq.poll();

            // STOP when safe zone reached
            if (safeZones.contains(current)) {
                return buildPath(current, parent);
            }

            for (String neighbor : graph.getNeighbors(current)) {

                int weight = graph.getWeight(current, neighbor);
                int newDist = dist.get(current) + weight;

                if (newDist < dist.get(neighbor)) {
                    dist.put(neighbor, newDist);
                    parent.put(neighbor, current);
                    pq.add(neighbor);
                }
            }
        }

        return new ArrayList<>();
    }

    private List<String> buildPath(String end, Map<String, String> parent) {

        List<String> path = new ArrayList<>();
        String curr = end;

        while (curr != null) {
            path.add(0, curr);
            curr = parent.get(curr);
        }

        return path;
    }

    public void printRoute(List<String> path, int totalTime) {
        System.out.println("Best evacuation route: "
                + String.join(" -> ", path)
                + " (travel time: " + totalTime + " min)");
    }

    public void simulateCongestion(CityGraph graph, String z1, String z2) {

        int currentWeight = graph.getWeight(z1, z2);
        int newWeight = currentWeight * 2;

        graph.updateWeight(z1, z2, newWeight);

        System.out.println("Congestion between " + z1 + " and " + z2);
    }
}