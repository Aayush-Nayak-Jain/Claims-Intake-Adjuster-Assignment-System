public class FiledState implements ClaimState {

    @Override
    public void approve(Claim claim, String adjusterId) {
        throw new IllegalStateException("Cannot approve claim directly from FILED state. Claim must be UNDER_REVIEW.");
    }

    @Override
    public void reject(Claim claim, String adjusterId) {
        throw new IllegalStateException("Cannot reject claim directly from FILED state. Claim must be UNDER_REVIEW.");
    }

    @Override
    public void settle(Claim claim, String adjusterId) {
        throw new IllegalStateException("Cannot settle claim from FILED state. Claim must be APPROVED first.");
    }
}
