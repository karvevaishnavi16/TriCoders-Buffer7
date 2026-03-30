package city;

import java.util.*;

public class CityGraph {
    Map<Integer, List<Integer>> adjList;
    public CityGraph() {
    adjList = new HashMap<>();
}
    public void addZone(int zoneId) {
    adjList.putIfAbsent(zoneId, new ArrayList<>());
    }
    public void addConnection(int z1, int z2) {
    adjList.get(z1).add(z2);
    adjList.get(z2).add(z1); // because roads are bidirectional
    }
    public void printGraph() {
    for (int zone : adjList.keySet()) {
        System.out.println(zone + " -> " + adjList.get(zone));
    }
}
}