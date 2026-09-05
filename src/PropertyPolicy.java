public class PropertyPolicy extends Policy {

    public PropertyPolicy(String policyId, double deductible, double coverageLimit, boolean active) {
        super(policyId, deductible, coverageLimit, active);
    }

    @Override
    public boolean covers(ClaimType type) {
        return type == ClaimType.PROPERTY;
    }
}
