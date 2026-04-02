package phase3;
import phase3.AidRecord;
public class FairnessScorer
{
    public static final double D=0.4; //Damage weight
    public static final double N=0.4; //Neglect weight
    public static final double V=0.2; //Vulnerability weight

    //Computing priority score
    public static double computeScore(AidRecord record)
    {
        double damagePart = D * record.getDamageLevel();
        double neglectPart = N * (record.getNeglectMultiplier() / (record.getAidReceived() + 1))* record.getTimeIgnored();
        double vulnerabilityPart = V * record.getVulnerabilityBonus();
        return damagePart + neglectPart + vulnerabilityPart;
    }
    public static void printScore(String zoneName, double score) 
    {
        System.out.println(zoneName + ": priority score = " + score);
    }
}