
class AidRecord
{
    int zoneId;
    double damageLevel;
    int aidReceived;
    int timeIgnored;
    double vulnerabilityBonus;
    AidRecord(int zoneId,double damageLevel)
    {
        this.zoneId=zoneId;
        this.damageLevel=damageLevel;
        this.aidReceived=0;
        this.timeIgnored=0;
        this.vulnerabilityBonus=0;
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

public class Main
{
   public static void main(String[] args)
   {
       // Create objects
        AidRecord r1 = new AidRecord(1, 75.5);
        AidRecord r2 = new AidRecord(2, 50.0);
        AidRecord r3 = new AidRecord(3, 90.2);

        // Print initial values
        System.out.println("Initial Records:");
        r1.printRecord();
        r2.printRecord();
        r3.printRecord();

        // Update values
        r1.aidReceived = 5;
        r1.timeIgnored = 2;

        r2.aidReceived = 3;
        r2.timeIgnored = 1;

        r3.aidReceived = 7;
        r3.timeIgnored = 4;

        // Print updated values
        System.out.println("\nUpdated Records:");
        r1.printRecord();
        r2.printRecord();
        r3.printRecord();
    }
}
