package city;

public class Zone {
    int zoneId;
    double waterLevel;
    int sosCount;
    double infraStress;

    public Zone(int zoneId, double waterLevel, int sosCount, double infraStress) {
        this.zoneId = zoneId;
        this.waterLevel = waterLevel;
        this.sosCount = sosCount;
        this.infraStress = infraStress;
    }
}