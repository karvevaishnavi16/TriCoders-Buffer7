package phase1;

import java.util.*;
import city.CityGraph;
import city.Zone;

public class SpreadPredictor {

    public void predict(String criticalZone,
                        CityGraph graph,
                        Map<String, Zone> zones) {

        System.out.println("\n===== SPREAD PREDICTION (BFS) =====");
        System.out.println("Starting from CRITICAL zone: " + criticalZone);

        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, Integer> level = new HashMap<>();

        queue.add(criticalZone);
        visited.add(criticalZone);
        level.put(criticalZone, 0);

        while (!queue.isEmpty()) {

            String current = queue.poll();
            int currLevel = level.get(current);

            // stop after 2 hops
            if (currLevel >= 2) continue;

            // 🔥 IMPORTANT: traverse neighbors
            for (String[] neighbor : graph.getAdjList()
                    .getOrDefault(current, new ArrayList<>())) {

                String next = neighbor[0];

                if (!visited.contains(next)) {

                    visited.add(next);
                    queue.add(next);

                    int nextLevel = currLevel + 1;
                    level.put(next, nextLevel);

                    System.out.println(
                        "⚠️ Warning: Zone " + next +
                        " may be affected (distance " + nextLevel + " hop)"
                    );
                }
            }
        }
    }
}