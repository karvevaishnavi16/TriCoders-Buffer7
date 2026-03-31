package city;

public class Zone {
    public int zoneId;
    public String zoneName;
    public double environmentalSignal;
    public int sosCount;
    public double infraStress;
    public double riskScore;
    public boolean isCritical;

    public Zone(int zoneId, String zoneName) {
        this.zoneId = zoneId;
        this.zoneName = zoneName;
        this.environmentalSignal = 0.0;
        this.sosCount = 0;
        this.infraStress = 1.0;
        this.riskScore = 0.0;
        this.isCritical = false;
    }

    public void evaluateRisk() {
        int flagCount = 0;

        if (environmentalSignal > 7.0) flagCount++;
        if (sosCount > 30) flagCount++;
        if (infraStress > 2.0) flagCount++;

        riskScore = flagCount;
        isCritical = (flagCount >= 2);
    }

    public void printStatus() {
        System.out.println("-----------------------------");
        System.out.println("Zone        : " + zoneName);
        System.out.println("Env Signal  : " + environmentalSignal);
        System.out.println("SOS Count   : " + sosCount + " reports");
        System.out.println("Infra Stress: " + infraStress + "x normal");
        System.out.println("Risk Score  : " + riskScore);
        System.out.println("Status      : " + (isCritical ? "!!! CRITICAL !!!" : "NORMAL"));
        System.out.println("-----------------------------");
    }
}