package phase2;

import java.util.PriorityQueue;

public class AmbulanceDispatcher {
    private static class EmergencyCall {
        private final int zoneId;
        private final int severity;

        EmergencyCall(int zoneId, int severity) {
            this.zoneId = zoneId;
            this.severity = severity;
        }
    }

    private final PriorityQueue<EmergencyCall> calls;

    public AmbulanceDispatcher() {
        calls = new PriorityQueue<>((a, b) -> {
            int bySeverity = Integer.compare(b.severity, a.severity);
            if (bySeverity != 0) {
                return bySeverity;
            }

            return Integer.compare(a.zoneId, b.zoneId);
        });
    }

    public void addCall(int zoneId, int severity) {
        calls.offer(new EmergencyCall(zoneId, severity));
    }

    public void dispatchNext() {
        if (calls.isEmpty()) {
            System.out.println("No emergency calls waiting.");
            return;
        }

        EmergencyCall nextCall = calls.poll();
        System.out.println(
            "Dispatching ambulance to Zone " + nextCall.zoneId +
            " with severity " + nextCall.severity
        );
    }
}
