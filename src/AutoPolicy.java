public class AutoPolicy extends Policy {

    public AutoPolicy(String policyId, double deductible, double coverageLimit, boolean active) {
        super(policyId, deductible, coverageLimit, active);
    }

    @Override
    public boolean covers(ClaimType type) {
        return type == ClaimType.AUTO;
    }
}
