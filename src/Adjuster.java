import java.util.HashSet;
import java.util.Set;

public class Adjuster {
    private String adjusterId;
    private Set<ClaimType> specializations;
    private int maxCaseload;
    private int currentCaseload;

    public Adjuster(String adjusterId, int maxCaseload) {
        this.adjusterId = adjusterId;
        this.maxCaseload = maxCaseload;
        this.currentCaseload = 0;
        this.specializations = new HashSet<>();
    }

    public String getAdjusterId() {
        return adjusterId;
    }

    public Set<ClaimType> getSpecializations() {
        return specializations;
    }

    public int getMaxCaseload() {
        return maxCaseload;
    }

    public int getCurrentCaseload() {
        return currentCaseload;
    }

    public void addSpecialization(ClaimType type) {
        if (type != null) {
            this.specializations.add(type);
        }
    }

    public boolean specializes(ClaimType type) {
        return this.specializations.contains(type);
    }

    public boolean hasCapacity() {
        return this.currentCaseload < this.maxCaseload;
    }

    public void incrementCaseload() {
        this.currentCaseload++;
    }

    public void decrementCaseload() {
        if (this.currentCaseload > 0) {
            this.currentCaseload--;
        }
    }
}
