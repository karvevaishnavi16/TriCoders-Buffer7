package city;

public class Zone {
    public int zoneId;
    public String zoneName;
    public double environmentalSignal;
    public int sosCount;
    public double infraStress;
    public double riskScore;
    public boolean isCritical;
    public String zoneType;
    public double vulnerabilityBonus;

    public Zone(int zoneId, String zoneName) {
        this.zoneId = zoneId;
        this.zoneName = zoneName;
        this.environmentalSignal = 0.0;
        this.sosCount = 0;
        this.infraStress = 1.0;
        this.riskScore = 0.0;
        this.isCritical = false;
        this.zoneType = "RESIDENTIAL";
        this.vulnerabilityBonus = 1.0;
    }

    public int getZoneId() {
        return zoneId;
    }
    public String getZoneName() {
        return zoneName;
    }
    public double getEnvironmentalSignal() {
        return environmentalSignal;
    }
    public int getSosCount() {
        return sosCount;
    }
    public double getInfraStress() {
        return infraStress;
    }
    public double getRiskScore() {
        return riskScore;
    }
    public double getVulnerabilityBonus() {
        return vulnerabilityBonus;
    }
    public boolean isCritical() {
        return isCritical;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }
    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }
    public void setEnvironmentalSignal(double environmentalSignal) {
        this.environmentalSignal = environmentalSignal;
    }
    public void setSosCount(int sosCount) {
        this.sosCount = sosCount;
    }
    public void setInfraStress(double infraStress) {
        this.infraStress = infraStress;
    }
    public void setRiskScore(double riskScore) {
        this.riskScore = riskScore;
    }
    public void setCritical(boolean critical) {
        this.isCritical = critical;
    }
    public void evaluateRisk() {
        int flagCount = 0;

        if (environmentalSignal > 7.0) {
            flagCount++;
        }
        if (sosCount > 30) {
            flagCount++;
        }
        if (infraStress > 2.0) {
            flagCount++;
        }

        riskScore = flagCount;
        isCritical = (flagCount >= 2);
    }

    public void printStatus() {
        System.out.println("-----------------------------");
        System.out.println("Zone        : " + zoneName);
        System.out.println("Type        : " + zoneType);
        System.out.println("Env Signal  : " + environmentalSignal);
        System.out.println("SOS Count   : " + sosCount + " reports");
        System.out.println("Infra Stress: " + infraStress + "x normal");
        System.out.println("Vulnerability: " + vulnerabilityBonus);
        System.out.println("Risk Score  : " + riskScore);
        System.out.println("Status      : " + (isCritical ? "!!! CRITICAL !!!" : "NORMAL"));
        System.out.println("-----------------------------");
    }
}
