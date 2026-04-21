package phase3;
import phase3.AidRecord;
public class FairnessScorer
{
    public static final double D=0.4; //Damage weight
    public static final double N=0.4; //Neglect weight
    public static final double V=0.2; //Vulnerability weight
    //Fairness formula: Priority=(D * damagelevel) + (N * neglectMultiplier / (aidReceived + 1)) * timeIgnored + (V * vulnerabilitBonus)
    //Computing priority score
    public static double computeScore(AidRecord record)
    {
        double damagePart = D * record.getDamageLevel();
        double neglectPart = N * (record.getNeglectMultiplier() / (record.getAidReceived() + 1))* record.getTimeIgnored();
        double vulnerabilityPart = V * record.getVulnerabilityBonus();
        return damagePart + neglectPart + vulnerabilityPart;
    }
    // How each part works:
    // Part 1 — damage: more damaged zone = higher score
    // Part 2 — neglect: ignored many times + low aid received = higher score
    //          neglectMultiplier increases if zone keeps getting skipped
    //          aidReceived+1 in denominator = more aid received = lower score
    //          timeIgnored = multiplied directly — longer ignored = higher score
    // Part 3 — vulnerability: SLUM/HOSPITAL zones score higher than RESIDENTIAL

    public static void printScore(String zoneName, double score) 
    {
        System.out.println(zoneName + ": priority score = " + score);
    }
    // Compare two zones and return which should get aid first
    // Returns zone name of winner
    public static String getHigherPriority(String zone1, AidRecord rec1, String zone2, AidRecord rec2) {
        double score1 = computeScore(rec1);
        double score2 = computeScore(rec2);
        return score1 >= score2 ? zone1 : zone2;
    }
}
