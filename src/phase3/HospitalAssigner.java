package phase3;

import java.util.PriorityQueue;

public class HospitalAssigner {
    private static class Hospital {
        private final String name;
        private final int capacity;
        private int occupancy;

        Hospital(String name, int capacity) {
            this.name = name;
            this.capacity = capacity;
            this.occupancy = 0;
        }

        boolean hasCapacity() {
            return occupancy < capacity;
        }

        double loadFactor() {
            return capacity == 0 ? Double.MAX_VALUE : (double) occupancy / capacity;
        }
    }

    private final PriorityQueue<Hospital> hospitals;

    public HospitalAssigner() {
        hospitals = new PriorityQueue<>((a, b) -> {
            int byLoad = Double.compare(a.loadFactor(), b.loadFactor());//Compare how full each hospital is
            if (byLoad != 0) {
                return byLoad;
            }

            int byOccupancy = Integer.compare(a.occupancy, b.occupancy);//If load is equal compare by number of patients
            if (byOccupancy != 0) {
                return byOccupancy;
            }

            return a.name.compareTo(b.name);//If still tied sort it by alphabetical order
        });
    }

    public void addHospital(String name, int capacity) {
        hospitals.offer(new Hospital(name, capacity));
    }

    public void assignPatient() {
        if (hospitals.isEmpty()) {
            System.out.println("No hospitals available for assignment.");
            return;
        }

        Hospital leastLoaded = hospitals.poll();//Gives the least loaded hospital

        if (!leastLoaded.hasCapacity()) {
            System.out.println("All hospitals are full.");
            hospitals.offer(leastLoaded);
            return;
        }

        leastLoaded.occupancy++;
        System.out.println(
            "Patient assigned to " + leastLoaded.name +
            " (" + leastLoaded.occupancy + "/" + leastLoaded.capacity + " occupied)"
        );

        hospitals.offer(leastLoaded);
    }
}
