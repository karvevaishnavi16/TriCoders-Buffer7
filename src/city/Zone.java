package city;

public class Zone {
    public int zoneId;
    public String zoneName;
    public double environmentalLevel;
    public int sosCount;
    public double infraStress;
    public double riskScore;
    public boolean isCritical;

    public Zone(int zoneId, String zoneName) {
        this.zoneId = zoneId;
        this.zoneName = zoneName;
        this.environmentalLevel = 0.0;
        this.sosCount = 0;
        this.infraStress = 1.0;
        this.riskScore = 0.0;
        this.isCritical = false;
    }

    public void printStatus() {
        System.out.println("-----------------------------");
        System.out.println("Zone        : " + zoneName);
        System.out.println("Environmental Level : " + environmentalLevel + " m");
        System.out.println("SOS Count   : " + sosCount + " reports");
        System.out.println("Infra Stress: " + infraStress + "x normal");
        System.out.println("Risk Score  : " + riskScore);
        System.out.println("Status      : " + (isCritical ? "CRITICAL" : "NORMAL"));
        System.out.println("-----------------------------");
    }
}