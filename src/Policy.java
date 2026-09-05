public abstract class Policy {
    private String policyId;
    private double deductible;
    private double coverageLimit;
    private boolean active;

    public Policy(String policyId, double deductible, double coverageLimit, boolean active) {
        this.policyId = policyId;
        this.deductible = deductible;
        this.coverageLimit = coverageLimit;
        this.active = active;
    }

    public abstract boolean covers(ClaimType type);

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public double getDeductible() {
        return deductible;
    }

    public void setDeductible(double deductible) {
        this.deductible = deductible;
    }

    public double getCoverageLimit() {
        return coverageLimit;
    }

    public void setCoverageLimit(double coverageLimit) {
        this.coverageLimit = coverageLimit;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
