public class FiledState implements ClaimState {

    @Override
    public void approve(Claim claim, String adjusterId) {
        throw new IllegalStateException("Cannot approve claim " + (claim != null ? claim.getClaimId() : "") + " from FILED state. Claim must be UNDER_REVIEW.");
    }

    @Override
    public void reject(Claim claim, String adjusterId) {
        throw new IllegalStateException("Cannot reject claim " + (claim != null ? claim.getClaimId() : "") + " from FILED state. Claim must be UNDER_REVIEW.");
    }

    @Override
    public void settle(Claim claim, String adjusterId) {
        throw new IllegalStateException("Cannot settle claim " + (claim != null ? claim.getClaimId() : "") + " from FILED state. Claim must be APPROVED first.");
    }
}
