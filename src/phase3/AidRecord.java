package phase3;

public class AidRecord
{
    public int zoneId;//Zone identity
    public double damageLevel;//comes from zones risk score after simulation
    public int aidReceived;//increases when every time zone receives aid
    public int timeIgnored;//how many cycles this zone was skipped without receiving aid,increases every cycle zone isnot served,decreases back to zero when zone finally receives the aid
    public double vulnerabilityBonus;//bonus for vulnerable areas,comes from csv
    public double neglectMultiplier;// Multiplier that increases when zone is repeatedly ignored
    // Starts at 1.0
    // AidDistributor increases this by 0.5 every 3 cycles zone is skipped
    // Makes repeatedly ignored zones rise in priority automatically


    public AidRecord(int zoneId, double damageLevel) {
        this.zoneId = zoneId;
        this.damageLevel = damageLevel;
        this.aidReceived = 0;
        this.timeIgnored = 0;
        this.vulnerabilityBonus = 0;
        this.neglectMultiplier = 1.0;
    }

public double getDamageLevel() {
    return damageLevel;
}

    public int getAidReceived() {
        return aidReceived;
    }

    public int getTimeIgnored() {
        return timeIgnored;
    }

    public double getVulnerabilityBonus() {
        return vulnerabilityBonus;
    }

    public double getNeglectMultiplier() {
        return neglectMultiplier;
    }

    public void setAidReceived(int aidReceived) {
        this.aidReceived = aidReceived;
    }

public void setVulnerabilityBonus(double vulnerabilityBonus) {
    this.vulnerabilityBonus = vulnerabilityBonus;
}
public void setNeglectMultiplier(double neglectMultiplier) {
        this.neglectMultiplier = neglectMultiplier;
}
    // Called by AidDistributor when this zone receives aid
    // Resets ignore counter, increases aid count
    public void markServed() {
        this.aidReceived++;
        this.timeIgnored = 0;
        // neglectMultiplier stays — history is remembered
    }
        // Called by AidDistributor when this zone is skipped in a cycle
    // Increases ignore counter
    // Every 3 skips — neglectMultiplier increases by 0.5
    public void markSkipped() {
        this.timeIgnored++;
        if (this.timeIgnored % 3 == 0) {
            this.neglectMultiplier += 0.5;
        }
    }


    public void printRecord()
    {
        System.out.println(
                "Zone ID: " + zoneId +
                        ", Damage Level: " + damageLevel +
                        ", Aid Received: " + aidReceived +
                        ", Time Ignored: " + timeIgnored +
                        ", Vulnerability Bonus: " + vulnerabilityBonus +
                        ", Priority of ignored zone:" + neglectMultiplier);
    }
}