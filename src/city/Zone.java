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

    public void evaluateRisk()
    {
        int abnormalCount=0;
        // Check environmental condition
        if(environmentalSignal > 7.0)
        {
            abnormalCount++;
        }
        // Check SOS level
        if(sosCount> 20)
        {
            abnormalCount++;
        }
        if(infraStress > 1.5)
        {
            abnormalCount++;
        }
        //assign risk score
        riskScore = abnormalCount;

         if (abnormalCount >= 2) {
        isCritical = true;
    } else {
        isCritical = false;
    }
    }
    public void printStatus() {
        System.out.println("-----------------------------");
        System.out.println("Zone        : " + zoneName);
        System.out.println("Environmental Level : " + environmentalSignal + " m");
        System.out.println("SOS Count   : " + sosCount + " reports");
        System.out.println("Infra Stress: " + infraStress + "x normal");
        System.out.println("Risk Score  : " + riskScore);
        System.out.println("Status      : " + (isCritical ? "CRITICAL" : "NORMAL"));
        System.out.println("-----------------------------");
    }
    //print
}