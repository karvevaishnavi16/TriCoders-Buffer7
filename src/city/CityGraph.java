package city;

import java.util.*;

public class CityGraph {

    // Adjacency list: zone -> list of {neighbor, weight}
    Map<String, List<String[]>> adjList;

    public CityGraph() {
        adjList = new HashMap<>();
    }

    // Add zone
    public void addZone(String zone) {
        adjList.putIfAbsent(zone, new ArrayList<>());
    }

    // Add connection with weight (bidirectional)
    public void addConnection(String z1, String z2, int weight) {
        adjList.get(z1).add(new String[]{z2, String.valueOf(weight)});
        adjList.get(z2).add(new String[]{z1, String.valueOf(weight)});
    }

    // Print graph
    public void printGraph() {
        for (String zone : adjList.keySet()) {
            System.out.print(zone + " -> ");
            for (String[] neighbor : adjList.get(zone)) {
                System.out.print(neighbor[0] + "(time: " + neighbor[1] + ") ");
            }
            System.out.println();
        }
    }

    // Dijkstra Algorithm
    public void shortestPath(String start) {

        Map<String, Integer> dist = new HashMap<>();

        // Initialize distances
        for (String zone : adjList.keySet()) {
            dist.put(zone, Integer.MAX_VALUE);
        }
        dist.put(start, 0);

        PriorityQueue<String> pq = new PriorityQueue<>(
                Comparator.comparingInt(dist::get)
        );

        pq.add(start);

        while (!pq.isEmpty()) {

            String current = pq.poll();

            for (String[] neighbor : adjList.get(current)) {

                String next = neighbor[0];
                int weight = Integer.parseInt(neighbor[1]);

                int newDist = dist.get(current) + weight;

                if (newDist < dist.get(next)) {
                    dist.put(next, newDist);
                    pq.add(next);
                }
            }
        }

        // Print result
        System.out.println("\nShortest distances from " + start + ":");
        for (String zone : dist.keySet()) {
            System.out.println(start + " -> " + zone + " = " + dist.get(zone));
        }
    }
}