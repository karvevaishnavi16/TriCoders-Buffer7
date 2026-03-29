package phase3;

public class AidRecord
{
    int zoneId;
    double damageLevel;
    int aidReceived;
    int timeIgnored;
    double vulnerabilityBonus;

    public AidRecord(int zoneId, double damageLevel)
    {
        this.zoneId = zoneId;
        this.damageLevel = damageLevel;
        this.aidReceived = 0;
        this.timeIgnored = 0;
        this.vulnerabilityBonus = 0;
    }

    public void printRecord()
    {
        System.out.println(
            "Zone ID: " + zoneId +
            ", Damage Level: " + damageLevel +
            ", Aid Received: " + aidReceived +
            ", Time Ignored: " + timeIgnored +
            ", Vulnerability Bonus: " + vulnerabilityBonus
        );
    }
}