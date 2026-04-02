package city;

import java.util.*;

public class CityGraph {

    Map<String, List<String[]>> adjList;

    public CityGraph() {
        adjList = new HashMap<>();
    }
    public Set<String> getAllZones() {
    return adjList.keySet();
}
    public Map<String, List<String[]>> getAdjList() {
    return adjList;
}

    public void addZone(String zone) {
        adjList.putIfAbsent(zone, new ArrayList<>());
    }

    public void addConnection(String z1, String z2, int weight) {
        adjList.get(z1).add(new String[]{z2, String.valueOf(weight)});
        adjList.get(z2).add(new String[]{z1, String.valueOf(weight)});
    }

    public List<String> getNeighbors(String zone) {
        List<String> neighbors = new ArrayList<>();
        for (String[] entry : adjList.getOrDefault(zone, new ArrayList<>())) {
            neighbors.add(entry[0]);
        }
        return neighbors;
    }

    public int getWeight(String z1, String z2) {
        for (String[] entry : adjList.getOrDefault(z1, new ArrayList<>())) {
            if (entry[0].equals(z2)) return Integer.parseInt(entry[1]);
        }
        return Integer.MAX_VALUE;
    }

    public void updateWeight(String z1, String z2, int newWeight) {
        for (String[] entry : adjList.getOrDefault(z1, new ArrayList<>())) {
            if (entry[0].equals(z2)) { entry[1] = String.valueOf(newWeight); break; }
        }
        for (String[] entry : adjList.getOrDefault(z2, new ArrayList<>())) {
            if (entry[0].equals(z1)) { entry[1] = String.valueOf(newWeight); break; }
        }
    }

    public void dijkstra(String start) {
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> parent = new HashMap<>();

        for (String zone : adjList.keySet()) {
            dist.put(zone, Integer.MAX_VALUE);
            parent.put(zone, null);
        }
        dist.put(start, 0);

        // int[] = {distance, zoneHashCode} — correct Dijkstra approach
        PriorityQueue<String> pq = new PriorityQueue<>(
            (a, b) -> dist.get(a) - dist.get(b)
        );
        pq.add(start);

        while (!pq.isEmpty()) {
            String current = pq.poll();

            for (String[] neighbor : adjList.getOrDefault(current, new ArrayList<>())) {
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

        System.out.println("\nShortest distances from " + start + ":");
        for (String zone : dist.keySet()) {
            System.out.print("  " + start + " -> " + zone + " = " + dist.get(zone));

            // Print path
            List<String> path = new ArrayList<>();
            String curr = zone;
            while (curr != null) {
                path.add(0, curr);
                curr = parent.get(curr);
            }
            System.out.println(" | Path: " + String.join(" -> ", path));
        }
    }

    public void printGraph() {
        for (String zone : adjList.keySet()) {
            System.out.print(zone + " -> ");
            for (String[] neighbor : adjList.get(zone)) {
                System.out.print(neighbor[0] + "(time:" + neighbor[1] + ") ");
            }
            System.out.println();
        }
    }
}