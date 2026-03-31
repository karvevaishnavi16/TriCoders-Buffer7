package phase3;

public class AidRecord
{
    public int zoneId;
    public double damageLevel;
    public int aidReceived;
    public int timeIgnored;
    public double vulnerabilityBonus;
    public double neglectMultiplier;

    public AidRecord(int zoneId, double damageLevel)
    {
        this.zoneId = zoneId;
        this.damageLevel = damageLevel;
        this.aidReceived = 0;
        this.timeIgnored = 0;
        this.vulnerabilityBonus = 0;
        this.neglectMultiplier=1.0;
    }

    public void printRecord()
    {
        System.out.println(
            "Zone ID: " + zoneId +
            ", Damage Level: " + damageLevel +
            ", Aid Received: " + aidReceived +
            ", Time Ignored: " + timeIgnored +
            ", Vulnerability Bonus: " + vulnerabilityBonus +
            ", Priority of ignored zone:" + neglectMultiplier
        );
    }
}