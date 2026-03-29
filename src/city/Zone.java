public class Zone {
    int zoneId;
    String zoneName;
    double waterLevel;
    int sosCount;
    double infraStress;
    double riskScore;
    boolean isCritical;

    Zone(int zoneId, String zoneName) {
        this.zoneId = zoneId;
        this.zoneName = zoneName;
        this.waterLevel = 0.0;
        this.sosCount = 0;
        this.infraStress = 1.0;
        this.riskScore = 0.0;
        this.isCritical = false;
    }

    void printStatus() {
        System.out.println("-----------------------------");
        System.out.println("Zone        : " + zoneName);
        System.out.println("Water Level : " + waterLevel + " m");
        System.out.println("SOS Count   : " + sosCount + " reports");
        System.out.println("Infra Stress: " + infraStress + "x normal");
        System.out.println("Risk Score  : " + riskScore);
        System.out.println("Status      : " + (isCritical ? "CRITICAL" : "NORMAL"));
        System.out.println("-----------------------------");
    }
}